package com.example.musicqueue.ui.song;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.musicqueue.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class SongFragment extends Fragment {

    private SongViewModel songViewModel;

    private static final String TAG = "SongFragment";

    RecyclerView mRecycler;
    SwipeRefreshLayout mSwipeRefreshLayout;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference songsCollection;
    private FirestoreRecyclerAdapter<SongModel, SongsHolder> adapter;

    private LinearLayoutManager linearLayoutManager;

    //TODO Make sure all the fields for firebase are correct
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        songViewModel = ViewModelProviders.of(this).get(SongViewModel.class);
        View root = inflater.inflate(R.layout.song_fragment,container,false);

        setHasOptionsMenu(true);

        songsCollection = firestore.collection("users")
                .document(firebaseUser.getUid())
                .collection("songs");

        mRecycler = root.findViewById(R.id.songs_recycler);
        linearLayoutManager = new LinearLayoutManager(getContext());
        mRecycler.setLayoutManager(linearLayoutManager);

        setUpAdapter();

        return root;
    }

    private void setUpAdapter() {
        Query baseQuery = songsCollection.orderBy("songRank", Query.Direction.ASCENDING);

        //TODO Make sure the document names are correct
        FirestoreRecyclerOptions<SongModel> options =
                new FirestoreRecyclerOptions.Builder<SongModel>()
                    .setQuery(baseQuery, new SnapshotParser<SongModel>() {
                        @NonNull
                        @Override
                        public SongModel parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                            return new SongModel(
                                    snapshot.get("songName").toString(),
                                    snapshot.get("artistName").toString(),
                                    (int) snapshot.get("company"),
                                    snapshot.get("docid").toString());
                        }
                    })
                    .build();

        adapter = new FirestoreRecyclerAdapter<SongModel, SongsHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SongsHolder holder, int position, @NonNull SongModel model) {
                holder.setDocId(model.getDocId());
                holder.setSongName(model.getSongName());
                holder.setArtistName(model.getArtistName());
                holder.setSongRank(model.getSongRank());
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

    private void showToast(@NonNull String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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

}
