package com.example.musicqueue.ui.queue;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicqueue.Constants;
import com.example.musicqueue.R;
import com.example.musicqueue.models.AbstractQueue;
import com.example.musicqueue.ui.songs.SongsActivity;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.annotation.Nonnull;

public class QueueHolder extends RecyclerView.ViewHolder {


    private String docid;
    private final TextView queueNameTV, queueLocationTV, songSizeTV;
    private final Chip favoriteChip;
    private final CardView cardView;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference queueCollection = firestore.collection(Constants.FIRESTORE_QUEUE_COLLECTION);

    public QueueHolder(@Nonnull final View itemView){
        super(itemView);

        queueNameTV = itemView.findViewById(R.id.queue_name_text_view);
        queueLocationTV = itemView.findViewById(R.id.queue_location_text_view);
        songSizeTV = itemView.findViewById(R.id.song_size_text_view);

        favoriteChip = itemView.findViewById(R.id.fave_chip);

        cardView = itemView.findViewById(R.id.card_view);
    }

    public void bind(@Nonnull AbstractQueue queue) {
        setDocId(queue.getDocId());
        setName(queue.getName());
        setLocation(queue.getLocation());
        setSongSize(queue.getSongCount());
        setFavorite(false);
    }

    public void setDocId(@Nonnull String docId) { this.docid = docId; }

    public void setName(@Nonnull String name) { this.queueNameTV.setText(name); }

    public void setLocation(@Nonnull String loc) { this.queueLocationTV.setText(loc); }

    public void setSongSize(@Nonnull Long songCount) {
        this.songSizeTV.setText(songCount.toString());
    }

    public void setFavorite(@Nonnull final boolean fave) {
        favoriteChip.setChecked(fave);
        favoriteChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favoriteChip.setChecked(!fave);
                queueCollection.document(docid).update("favorite", !fave);
            }
        });
    }

    public void initCardClickListener(final String docid) {
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = itemView.getContext();
                Intent intent = new Intent(context, SongsActivity.class);
                intent.putExtra("DOCUMENT_ID", docid);
                intent.putExtra("DOCUMENT_NAME", queueNameTV.getText().toString());
                intent.putExtra("SONG_COUNT", songSizeTV.getText().toString());
                context.startActivity(intent);
            }
        });
    }
}
