package com.example.musicqueue.holders;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicqueue.Constants;
import com.example.musicqueue.R;
import com.example.musicqueue.models.AbstractSongs;
import com.example.musicqueue.ui.account.SettingsActivity;
import com.example.musicqueue.ui.songs.SongsActivity;
import com.example.musicqueue.utilities.CommonUtils;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SongsHolder extends RecyclerView.ViewHolder {

    private final TextView songNameTV, artistNameTV, songRankTV;
    public final TextView ownerTV;
    private final Chip upChip, downChip;
    private String docid, queueId, ownerUid;
    public CardView songCV;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference songCollection;

    public SongsHolder(@NonNull final View itemView){
        super(itemView);

        songCV = itemView.findViewById(R.id.song_card_view);
        ownerTV = itemView.findViewById(R.id.owner_text_view);
        songNameTV = itemView.findViewById(R.id.song_title_text_view);
        artistNameTV = itemView.findViewById(R.id.artist_text_view);
        songRankTV = itemView.findViewById(R.id.vote_count_text_view);

        upChip = itemView.findViewById(R.id.chip_up);
        downChip = itemView.findViewById(R.id.chip_down);

        // TODO: Fix voting system
        upChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                songCollection = firestore.collection(Constants.FIRESTORE_QUEUE_COLLECTION)
                        .document(queueId)
                        .collection(Constants.FIRESTORE_SONG_COLLECTION);

                Long v = Long.parseLong(songRankTV.getText().toString());
                v = v + 1;
                songCollection.document(docid).update("votes", v);

                downChip.setChecked(false);
            }
        });

        // TODO: Fix voting system
        downChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        setOwnerUid(song.getOwnerUid());
    }

    public void setDocId(@Nullable String docid) { this.docid = docid; }

    public void setQueueId(@Nullable String queueId) { this.queueId = queueId; }

    public void setOwnerUid(@Nullable String ownerUid) { this.ownerUid = ownerUid; }

    public void setName(@Nullable String name) { this.songNameTV.setText(name); }

    public void setArtist(@Nullable String artist) { this.artistNameTV.setText(artist); }

    public void setVotes(long votes) { this.songRankTV.setText(Long.toString(votes)); }

}
