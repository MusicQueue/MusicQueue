package com.example.musicqueue.ui.library;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
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
import com.example.musicqueue.ui.queue.QueueAdapter;
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
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.imperiumlabs.geofirestore.GeoFirestore;
import org.imperiumlabs.geofirestore.GeoQuery;
import org.imperiumlabs.geofirestore.listeners.GeoQueryDataEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddToQueueActivity extends AppCompatActivity {
    private static final GeoPoint testPoint = new GeoPoint(33.2107754,-87.5547761);

    private final static String TAG = "AddToQueueActivity";

    RecyclerView recyclerView;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference queueCollection;

    private GeoFirestore geoFirestore;
    private GeoQuery geoQuery;
    private List<Queue> listOfQueues;
    private QueueAdapter adapter;

    private String songName, songArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_queue);
        listOfQueues = new ArrayList<>();

        queueCollection = firestore.collection(Constants.FIRESTORE_QUEUE_COLLECTION);
        geoFirestore = new GeoFirestore(queueCollection);

        songName = getIntent().getStringExtra("SONG_NAME");
        songArtist = getIntent().getStringExtra("SONG_ARTIST");

        recyclerView = findViewById(R.id.queue_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        initActionbar();
        setupGeoquery();
        setUpAdapter(listOfQueues);
    }

    /**
     * setUpAdapter pulls the documents from the database so that each one is
     * display as a card in the recycler view
     */
    private void setUpAdapter(List<Queue> queueList) {
        adapter = new AddToQueueAdapter(queueList, FirebaseAuth.getInstance().getUid(), testPoint, songName, songArtist, getApplicationContext());

        recyclerView.setAdapter(adapter);
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
    }

    @Override
    public void onStop() {
        geoQuery.removeAllListeners();
        super.onStop();
    }


    private void setupGeoquery(){
        geoQuery = geoFirestore.queryAtLocation(testPoint, 5);
        geoQuery.addGeoQueryDataEventListener(new GeoQueryDataEventListener() {
            @Override
            public void onDocumentEntered(DocumentSnapshot documentSnapshot, GeoPoint geoPoint) {
                listOfQueues.add(documentSnapshot.toObject(Queue.class));
                setUpAdapter(listOfQueues);
                Log.v(TAG, listOfQueues.toString());
            }

            @Override
            public void onDocumentExited(DocumentSnapshot documentSnapshot) {
                listOfQueues.remove(documentSnapshot.toObject(Queue.class));
                Log.v(TAG, listOfQueues.toString());
                setUpAdapter(listOfQueues);
            }

            @Override
            public void onDocumentMoved(DocumentSnapshot documentSnapshot, GeoPoint geoPoint) {
            }

            @Override
            public void onDocumentChanged(DocumentSnapshot documentSnapshot, GeoPoint geoPoint) {
            }

            @Override
            public void onGeoQueryReady() {
            }

            @Override
            public void onGeoQueryError(Exception e) {
                Log.e(TAG, e.toString());
            }
        });
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
