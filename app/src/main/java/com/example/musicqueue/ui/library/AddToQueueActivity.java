package com.example.musicqueue.ui.library;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicqueue.Constants;
import com.example.musicqueue.R;
import com.example.musicqueue.holders.QueueHolder;
import com.example.musicqueue.models.Queue;
import com.example.musicqueue.utilities.CommonUtils;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AddToQueueActivity extends AppCompatActivity {

    private final static String TAG = "AddToQueueActivity";

    RecyclerView recyclerView;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference queueCollection;
    private FirestoreRecyclerAdapter<Queue, QueueHolder> adapter;

    private String songName, songArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_queue);

        queueCollection = firestore.collection(Constants.FIRESTORE_QUEUE_COLLECTION);

        songName = getIntent().getStringExtra("SONG_NAME");
        songArtist = getIntent().getStringExtra("SONG_ARTIST");

        recyclerView = findViewById(R.id.queue_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        initActionbar();

        setUpAdapter();
    }

    private void setUpAdapter() {
        Query baseQuery = queueCollection;

        FirestoreRecyclerOptions<Queue> options =
                new FirestoreRecyclerOptions.Builder<Queue>()
                        .setQuery(baseQuery, new SnapshotParser<Queue>() {
                            @NonNull
                            @Override
                            public Queue parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                                return snapshot.toObject(Queue.class);
                            }
                        }).build();

        adapter = new FirestoreRecyclerAdapter<Queue, QueueHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final QueueHolder holder, int position, @NonNull Queue model) {
                holder.setDocId(model.getDocId());
                holder.setName(model.getName());
                holder.setLocation(model.getLocation());
                holder.setSongSize(model.getSongCount());
                holder.setCreator(model.getCreator());

                holder.favoriteChip.setVisibility(View.GONE);

                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkIfSongInQueue(holder.getQueueId(), holder.getSongCount());
                    }
                });
            }

            @NonNull
            @Override
            public QueueHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.queue_card_layout, parent, false);

                return new QueueHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);

    }

    private void checkIfSongInQueue(final String queueId, final long songCount) {

        CollectionReference songsCollection = queueCollection.document(queueId)
                .collection(Constants.FIRESTORE_SONG_COLLECTION);

        songsCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    boolean exists = false;
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        if (doc.get("name").toString().equals(songName) && doc.get("artist").toString().equals(songArtist)) {
                            exists = true;
                        }
                    }
                    if (exists) {
                        songExistsDialog();
                    }
                    else {
                        addSongToQueue(queueId, songCount);
                    }
                }
            }
        });
    }

    private void addSongToQueue(final String queueId, final long songCount) {
        CollectionReference songsCollection = queueCollection.document(queueId)
                .collection(Constants.FIRESTORE_SONG_COLLECTION);

        Map<String, Object> data = new HashMap<>();
        Map<String, Boolean> votersMap = new HashMap<>();
        votersMap.put(firebaseUser.getUid(), true);
        data.put("voters", votersMap);
        data.put("name", songName);
        data.put("artist", songArtist);
        data.put("votes", Integer.toUnsignedLong(0));
        data.put("queueId", queueId);
        data.put("ownerUid", firebaseUser.getUid());

        songsCollection.add(data)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        long songCountLong = songCount + 1;
                        CollectionReference queueref = firestore.collection(Constants.FIRESTORE_QUEUE_COLLECTION);
                        queueref.document(queueId).update("songCount", songCountLong);

                        CommonUtils.showToast(getApplicationContext(), "Added song!");
                        task.getResult().collection(Constants.FIRESTORE_SONG_COLLECTION);
                        finish();
                    }
                });
    }

    private void songExistsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddToQueueActivity.this, R.style.AppTheme_AlertDialogTheme);
        builder.setTitle("Song in Queue");

        final View v = getLayoutInflater().inflate(R.layout.dialog_song_exists, null);
        builder.setView(v);

        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    /**
     * initActionbar initializes the action bar with settings title and button back to
     * Account fragment
     */
    private void initActionbar() {
        getSupportActionBar().setElevation(0);  // remove actionbar shadow
        getSupportActionBar().setTitle("Queues");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        if (adapter != null) {
            adapter.stopListening();
        }
        super.onStop();
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
