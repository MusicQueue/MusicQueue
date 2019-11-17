package com.example.musicqueue.ui.song;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicqueue.R;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SongsHolder extends RecyclerView.ViewHolder {

    private static final String FONT_LIGHT = "#FAFAFA";
    private static final String FONT_DARK = "#212121";

    private final TextView songNameTV,  artistNameTV, songRankTV;
    private final Chip upChip, downChip;
    private String docid;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference songCollection = firestore.collection("users")
            .document(firebaseUser.getUid())
            .collection("songs");

    public SongsHolder(@NonNull final View itemView){
        super(itemView);

        songNameTV = itemView.findViewById(R.id.song_title_text_view);
        artistNameTV = itemView.findViewById(R.id.artist_text_view);
        songRankTV = itemView.findViewById(R.id.vote_count_text_view);

        upChip = itemView.findViewById(R.id.chip_up);
        downChip = itemView.findViewById(R.id.chip_down);
    }

    public void bind(@NonNull AbstractSong song) {
        setSongName(song.getSongName());
        setArtistName(song.getArtistName());
        setSongRank(song.getSongRank());
        setDocId(song.getDocId());

    }

    public void setDocId(@Nullable String docid) { this.docid = docid; }

    public void setSongName(@Nullable String songName) { this.songNameTV.setText(songName); }

    public void setArtistName(@Nullable String artistName) { this.artistNameTV.setText(artistName); }

    public void setSongRank(@Nullable int songRank) { this.songRankTV.setText(songRank);}

    /*
    * TODO Add in methods for up and down on clicks
    */
}
