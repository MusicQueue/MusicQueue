package com.example.musicqueue.ui.queue;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicqueue.Constants;
import com.example.musicqueue.MainActivity;
import com.example.musicqueue.R;
import com.example.musicqueue.holders.QueueHolder;
import com.example.musicqueue.models.Queue;
import com.example.musicqueue.utilities.CommonUtils;
import com.example.musicqueue.utilities.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;

import org.imperiumlabs.geofirestore.GeoFirestore;
import org.imperiumlabs.geofirestore.GeoQuery;
import org.imperiumlabs.geofirestore.listeners.GeoQueryDataEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueueFragment extends Fragment {

    private static final GeoPoint testPoint = new GeoPoint(33.2107754,-87.5547761);
    private static final String TAG = "QueueFragment";

    RecyclerView recyclerView;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference queueCollection;

    private GeoFirestore geoFirestore;
    private GeoQuery geoQuery;
    private List<Queue> idList;
    private QueueAdapter adapter;

    private LinearLayoutManager linearLayoutManager;

    private String uid;


    public View onCreateView(@NonNull LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        QueueViewModel queueViewModel = ViewModelProviders.of(this).get(QueueViewModel.class);
        View root = inflater.inflate(R.layout.fragment_queue, container, false);
        idList = new ArrayList<>();

        setColors();

        uid = firebaseUser.getUid();

        queueCollection = firestore.collection(Constants.FIRESTORE_QUEUE_COLLECTION);
        geoFirestore = new GeoFirestore(queueCollection);

        recyclerView = root.findViewById(R.id.queue_recycler);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        setUpAdapter(idList);
        setupGeoquery();

        root.findViewById(R.id.new_queue_button).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view){
               Context context = view.getContext();
               Intent intent = new Intent(context, NewQueueActivity.class);
               context.startActivity(intent);
           }
        });

        return root;
    }

    private void setUpAdapter(@Nullable List<Queue>listOfIds) {
        adapter = new QueueAdapter(listOfIds, uid, testPoint);

        recyclerView.setAdapter(adapter);
    }

    private void setColors() {
        String PRIMARY_COLOR = "#192125";
        ((AppCompatActivity) getActivity()).getSupportActionBar()
                .setBackgroundDrawable(new ColorDrawable(Color.parseColor(PRIMARY_COLOR)));
        ((MainActivity)getActivity()).updateStatusBarColor(PRIMARY_COLOR);
        ((MainActivity)getActivity()).updateStatusBarIconColor(false);
        Toolbar toolbar = getActivity().findViewById(R.id.action_bar);
        if (toolbar!= null){
            String COLOR_FONT_LIGHT = "#F5F5F5";
            toolbar.setTitleTextColor(Color.parseColor(COLOR_FONT_LIGHT));
        }
    }

    private void setupGeoquery(){
        geoQuery = geoFirestore.queryAtLocation(testPoint, 5);
        geoQuery.addGeoQueryDataEventListener(new GeoQueryDataEventListener() {
            @Override
            public void onDocumentEntered(DocumentSnapshot documentSnapshot, GeoPoint geoPoint) {
                idList.add(documentSnapshot.toObject(Queue.class));
                setUpAdapter(idList);
                Log.v(TAG, idList.toString());
            }

            @Override
            public void onDocumentExited(DocumentSnapshot documentSnapshot) {
                idList.remove(documentSnapshot.toObject(Queue.class));
                Log.v(TAG, idList.toString());
                setUpAdapter(idList);
            }

            @Override
            public void onDocumentMoved(DocumentSnapshot documentSnapshot, GeoPoint geoPoint) {
            }

            @Override
            public void onDocumentChanged(DocumentSnapshot documentSnapshot, GeoPoint geoPoint) {
            }

            @Override
            public void onGeoQueryReady() {
                Log.v(TAG, idList.toString());
            }

            @Override
            public void onGeoQueryError(Exception e) {
                Log.e(TAG, e.toString());
            }
        });
    }

    @Override
    public void onStop() {
        geoQuery.removeAllListeners();
        super.onStop();
    }

}