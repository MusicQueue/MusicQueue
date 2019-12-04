package com.example.musicqueue.ui.songs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicqueue.Constants;
import com.example.musicqueue.R;
import com.example.musicqueue.holders.SongsHolder;
import com.example.musicqueue.models.Song;
import com.example.musicqueue.utilities.CommonUtils;
import com.example.musicqueue.utilities.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Map;

public class SongsActivity extends AppCompatActivity {

    private static final String TAG = "SongsActivity";

    RecyclerView mRecycler;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference songsCollection;
    private FirestoreRecyclerAdapter<Song, SongsHolder> adapter;

    String queueDocid;
    String queueName;
    String soungCount;
    String queueCreator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);

        queueDocid = getIntent().getStringExtra("DOCUMENT_ID");
        queueName = getIntent().getStringExtra("DOCUMENT_NAME");
        soungCount = getIntent().getStringExtra("SONG_COUNT");
        queueCreator = getIntent().getStringExtra("OWNER_ID");

        songsCollection = firestore.collection(Constants.FIRESTORE_QUEUE_COLLECTION)
            .document(queueDocid)
            .collection(Constants.FIRESTORE_SONG_COLLECTION);

        mRecycler = findViewById(R.id.song_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecycler.setLayoutManager(linearLayoutManager);

        setUpAdapter();

        initActionbar();

        findViewById(R.id.add_song_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, AddSongActivity.class);
                intent.putExtra("DOCUMENT_ID", queueDocid);
                intent.putExtra("SONG_COUNT", soungCount);
                context.startActivity(intent);
            }
        });
    }

    private void initActionbar() {
        getSupportActionBar().setElevation(0);  // remove actionbar shadow
        getSupportActionBar().setTitle(queueName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setUpAdapter() {
        Query baseQuery = songsCollection.orderBy("votes", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Song> options =
                new FirestoreRecyclerOptions.Builder<Song>()
                        .setQuery(baseQuery, new SnapshotParser<Song>() {
                            @NonNull
                            @Override
                            public Song parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                                return new Song(
                                        FirebaseUtils.getStringOrEmpty(snapshot, "name"),
                                        FirebaseUtils.getStringOrEmpty(snapshot, "artist"),
                                        FirebaseUtils.getLongOrZero(snapshot,"votes"),
                                        snapshot.getId().toString(),
                                        FirebaseUtils.getStringOrEmpty(snapshot, "queueId"),
                                        FirebaseUtils.getStringOrEmpty(snapshot, "ownerUid"),
                                        FirebaseUtils.getMapOrInit(snapshot, "voters"));
                            }
                        })
                        .build();

        adapter = new FirestoreRecyclerAdapter<Song, SongsHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SongsHolder holder, int position, @NonNull final Song model) {
                holder.setDocId(model.getDocId());
                holder.setName(model.getName());
                holder.setArtist(model.getArtist());
                holder.setVotes(model.getVotes());
                holder.setQueueId(model.getQueueId());
                holder.setOwnerUid(model.getOwnerUid());
                holder.setVotersMap(model.getVotersMap());

                String uid = FirebaseAuth.getInstance().getUid().toString();
                String ownerUid = model.getOwnerUid();


                if (uid.equals(ownerUid) || uid.equals(queueCreator)) {
                    holder.ownerTV.setVisibility(View.VISIBLE);
                    holder.songCV.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {

                            deleteSongDialog(view, model.getDocId());

                            return true;
                        }
                    });
                }

                Map<String, Boolean> map = model.getVotersMap();

                if (map.containsKey(FirebaseAuth.getInstance().getUid().toString())) {
                    holder.setVoteArrows(map.get(uid));
                }
            }

            @NonNull
            @Override
            public SongsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.song_card_layout, parent, false);
                return new SongsHolder(view);
            }
        };

        mRecycler.setAdapter(adapter);
    }

    private void deleteSongDialog(final View view, final String docid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(), R.style.AppTheme_AlertDialogTheme);
        builder.setTitle("Delete Song");

        final View v = getLayoutInflater().inflate(R.layout.dialog_delete_song, null);
        builder.setView(v);

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                CollectionReference queueCollection = firestore.collection(Constants.FIRESTORE_QUEUE_COLLECTION);

                DocumentReference queueDocRef = queueCollection.document(queueDocid);
                queueDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Long songCount = FirebaseUtils.getLongOrZero(documentSnapshot, "songCount");
                        songCount -= 1;

                        updateSongCount(songCount);
                    }
                });

                songsCollection.document(docid).delete();


                CommonUtils.showToast(getApplicationContext(), "Song deleted");
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void updateSongCount(Long count) {
        CollectionReference queue = firestore.collection(Constants.FIRESTORE_QUEUE_COLLECTION);
        queue.document(queueDocid).update("songCount", count);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
