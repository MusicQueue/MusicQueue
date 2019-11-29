package com.example.musicqueue.ui.library;

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

import com.example.musicqueue.MainActivity;
import com.example.musicqueue.R;
import com.example.musicqueue.holders.LibrarySongsHolder;
import com.example.musicqueue.models.LibrarySongs;
import com.example.musicqueue.utilities.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class LibraryFragment extends Fragment {

    private static final String TAG = "LibraryFragment";

    private LibraryViewModel libraryViewModel;

    RecyclerView recyclerViewSongs;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference librarySongCollection;
    private FirestoreRecyclerAdapter<LibrarySongs, LibrarySongsHolder> adapter;

    private LinearLayoutManager linearLayoutManager;

    private String uid;

    public View onCreateView(@NonNull LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        libraryViewModel = ViewModelProviders.of(this).get(LibraryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_library, container, false);

        uid = firebaseUser.getUid().toString();

        librarySongCollection = firestore.collection("users")
            .document(uid)
            .collection("librarySongs");

        recyclerViewSongs = root.findViewById(R.id.library_recycler);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewSongs.setLayoutManager(linearLayoutManager);

        setUpSongsAdapter();

        setColors();

        return root;
    }

    private void setUpSongsAdapter() {
        Query baseQuery = librarySongCollection.orderBy("name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<LibrarySongs> options =
                new FirestoreRecyclerOptions.Builder<LibrarySongs>()
                        .setQuery(baseQuery, new SnapshotParser<LibrarySongs>() {
                            @NonNull
                            @Override
                            public LibrarySongs parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                                Log.v(TAG, snapshot.toString());
                                return new LibrarySongs(
                                        FirebaseUtils.getStringOrEmpty(snapshot, "name"),
                                        FirebaseUtils.getStringOrEmpty(snapshot, "artist"),
                                        FirebaseUtils.getStringOrEmpty(snapshot, "ownerUid"));
                            }
                        }).build();

        adapter = new FirestoreRecyclerAdapter<LibrarySongs, LibrarySongsHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull LibrarySongsHolder holder, int position, @NonNull LibrarySongs model) {
                holder.setName(model.getName());
                holder.setArtist(model.getArtist());
                holder.setOwnerUid(model.getOwnerUid());
            }

            @NonNull
            @Override
            public LibrarySongsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.library_song_card_layout, parent, false);

                return new LibrarySongsHolder(view);
            }
        };

        recyclerViewSongs.setAdapter(adapter);

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