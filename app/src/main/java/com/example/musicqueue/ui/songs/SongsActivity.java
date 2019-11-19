package com.example.musicqueue.ui.songs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicqueue.Constants;
import com.example.musicqueue.R;
import com.example.musicqueue.utilities.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class SongsActivity extends AppCompatActivity {

    private static final String TAG = "SongsActivity";

    RecyclerView mRecycler;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference songsCollection;
    private FirestoreRecyclerAdapter<SongsModel, SongsHolder> adapter;

    private LinearLayoutManager linearLayoutManager;

    /**
     * TODO: this is the docid for the queue that the songs are under, use this to update
     * TODO: the sountCount in the queue so that we properly track the amount of songs
     */
    String queueDocid;
    String queueName;
    String soungCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);

        queueDocid = getIntent().getStringExtra("DOCUMENT_ID");
        queueName = getIntent().getStringExtra("DOCUMENT_NAME");
        soungCount = getIntent().getStringExtra("SONG_COUNT");

        songsCollection = firestore.collection(Constants.FIRESTORE_QUEUE_COLLECTION)
            .document(queueDocid)
            .collection(Constants.FIRESTORE_SONG_COLLECTION);

        mRecycler = findViewById(R.id.song_recycler);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
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
        Query baseQuery = songsCollection.orderBy("votes", Query.Direction.ASCENDING);

        //TODO Make sure the document names are correct
        FirestoreRecyclerOptions<SongsModel> options =
                new FirestoreRecyclerOptions.Builder<SongsModel>()
                        .setQuery(baseQuery, new SnapshotParser<SongsModel>() {
                            @NonNull
                            @Override
                            public SongsModel parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                                return new SongsModel(
                                        FirebaseUtils.getStringOrEmpty(snapshot, "name"),
                                        FirebaseUtils.getStringOrEmpty(snapshot, "artist"),
                                        FirebaseUtils.getLongOrZero(snapshot,"votes"),
                                        snapshot.getId().toString(),
                                        FirebaseUtils.getStringOrEmpty(snapshot, "queueId"));
                            }
                        })
                        .build();

        adapter = new FirestoreRecyclerAdapter<SongsModel, SongsHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SongsHolder holder, int position, @NonNull SongsModel model) {
                holder.setDocId(model.getDocId());
                holder.setName(model.getName());
                holder.setArtist(model.getArtist());
                holder.setVotes(model.getVotes());
                holder.setQueueId(model.getQueueId());
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
