package com.example.musicqueue.ui.favorites;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicqueue.Constants;
import com.example.musicqueue.MainActivity;
import com.example.musicqueue.R;
import com.example.musicqueue.models.Queue;
import com.example.musicqueue.holders.QueueHolder;
import com.example.musicqueue.utilities.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Map;

public class FavoritesFragment extends Fragment {

    private static final String TAG = "FavoritesFragment";

    private FavoritesViewModel favoritesViewModel;

    private RecyclerView recyclerView;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference queueCollection;
    private FirestoreRecyclerAdapter<Queue, QueueHolder> adapter;

    private LinearLayoutManager linearLayoutManager;

    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        favoritesViewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);
        root = inflater.inflate(R.layout.fragment_favorites, container, false);

        setColors();

        queueCollection = firestore.collection(Constants.FIRESTORE_QUEUE_COLLECTION);

        recyclerView = root.findViewById(R.id.favorite_recycler);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        setUpAdapter();

        return root;
    }

    private void setUpAdapter() {
        final String uid = FirebaseAuth.getInstance().getUid();
        Query baseQuery = queueCollection.whereEqualTo("favorites." + uid, true);

        FirestoreRecyclerOptions<Queue> options =
                new FirestoreRecyclerOptions.Builder<Queue>()
                        .setQuery(baseQuery, new SnapshotParser<Queue>() {
                            @NonNull
                            @Override
                            public Queue parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                                Log.v(TAG, snapshot.toString());
                                return snapshot.toObject(Queue.class);
                            }
                        }).build();

        adapter = new FirestoreRecyclerAdapter<Queue, QueueHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull QueueHolder holder, int position, @NonNull Queue model) {
                holder.setDocId(model.getDocId());
                holder.setName(model.getName());
                holder.setLocation(model.getLocation());
                holder.setSongSize(model.getSongCount());
                holder.initCardClickListener(model.getDocId(), model.getOwnerId());

                Map<String, Boolean> favMap = model.getFavoritesMap();
                holder.setFavoritesMap(favMap);

                if (favMap.containsKey(uid)) {
                    holder.setFavorite(favMap.get(uid));
                }
                else {
                    holder.setFavorite(false);
                }

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
}