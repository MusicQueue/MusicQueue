package com.example.musicqueue.ui.songs;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicqueue.R;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SongsHolder extends RecyclerView.ViewHolder {

    private final TextView songNameTV,  artistNameTV, songRankTV;
    private final Chip upChip, downChip;
    private String docid;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference songCollection = firestore.collection("testSongs");

    public SongsHolder(@NonNull final View itemView){
        super(itemView);

        songNameTV = itemView.findViewById(R.id.song_title_text_view);
        artistNameTV = itemView.findViewById(R.id.artist_text_view);
        songRankTV = itemView.findViewById(R.id.vote_count_text_view);

        upChip = itemView.findViewById(R.id.chip_up);
        downChip = itemView.findViewById(R.id.chip_down);
    }

    public void bind(@NonNull AbstractSongs song) {
        setName(song.getName());
        setArtist(song.getArtist());
        setVotes(song.getVotes());
        setDocId(song.getDocId());
    }

    public void setDocId(@Nullable String docid) { this.docid = docid; }

    public void setName(@Nullable String name) { this.songNameTV.setText(name); }

    public void setArtist(@Nullable String artist) { this.artistNameTV.setText(artist); }

    public void setVotes(long votes) { this.songRankTV.setText(Long.toString(votes));}

    /*
    * TODO Add in methods for up and down on clicks
    */
}
