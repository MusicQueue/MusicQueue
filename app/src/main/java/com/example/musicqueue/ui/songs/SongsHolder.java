package com.example.musicqueue.ui.songs;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicqueue.Constants;
import com.example.musicqueue.R;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SongsHolder extends RecyclerView.ViewHolder {

    private final TextView songNameTV,  artistNameTV, songRankTV;
    private final Chip upChip, downChip;
    private String docid;
    private String queueId;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference songCollection;

    public SongsHolder(@NonNull final View itemView){
        super(itemView);

        songNameTV = itemView.findViewById(R.id.song_title_text_view);
        artistNameTV = itemView.findViewById(R.id.artist_text_view);
        songRankTV = itemView.findViewById(R.id.vote_count_text_view);

        upChip = itemView.findViewById(R.id.chip_up);
        downChip = itemView.findViewById(R.id.chip_down);

        upChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //upChip.setChecked(true);

                songCollection = firestore.collection(Constants.FIRESTORE_QUEUE_COLLECTION)
                        .document(queueId)
                        .collection(Constants.FIRESTORE_SONG_COLLECTION);

                Long v = Long.parseLong(songRankTV.getText().toString());
                v = v + 1;
                songCollection.document(docid).update("votes", v);

                downChip.setChecked(false);
            }
        });

        downChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //downChip.setChecked(true);
                songCollection = firestore.collection(Constants.FIRESTORE_QUEUE_COLLECTION)
                        .document(queueId)
                        .collection(Constants.FIRESTORE_SONG_COLLECTION);

                Long v = Long.parseLong(songRankTV.getText().toString());
                v = v - 1;
                songCollection.document(docid).update("votes", v);

                upChip.setChecked(false);
            }
        });
    }

    public void bind(@NonNull AbstractSongs song) {
        setName(song.getName());
        setArtist(song.getArtist());
        setVotes(song.getVotes());
        setDocId(song.getDocId());
        setQueueId(song.getQueueId());
    }

    public void setUpChip(Chip chip) {
        songCollection = firestore.collection(Constants.FIRESTORE_QUEUE_COLLECTION)
                .document(queueId)
                .collection(Constants.FIRESTORE_SONG_COLLECTION);
        //chip.setChecked(true);
        //if (chip.isChecked()) {
        downChip.setChecked(false);
        upChip.setChecked(true);

        if (upChip.isChecked()) {
            upChip.setChecked(true);
        }

        Long v = Long.parseLong(this.songRankTV.getText().toString());
        v = v + 1;
        songCollection.document(docid).update("votes", v);
        //}
    }

    public void setDownChip(Chip chip) {
        songCollection = firestore.collection(Constants.FIRESTORE_QUEUE_COLLECTION)
                .document(queueId)
                .collection(Constants.FIRESTORE_SONG_COLLECTION);

        upChip.setChecked(false);
        downChip.setChecked(true);
        Long v = Long.parseLong(this.songRankTV.getText().toString());
        v = v - 1;
        songCollection.document(docid).update("votes", v);
    }

    public void setDocId(@Nullable String docid) { this.docid = docid; }

    public void setQueueId(@Nullable String queueId) { this.queueId = queueId; }

    public void setName(@Nullable String name) { this.songNameTV.setText(name); }

    public void setArtist(@Nullable String artist) { this.artistNameTV.setText(artist); }

    public void setVotes(long votes) { this.songRankTV.setText(Long.toString(votes)); }

    /*
    * TODO Add in methods for up and down on clicks
    */
}
