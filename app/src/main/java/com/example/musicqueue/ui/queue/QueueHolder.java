package com.example.musicqueue.ui.queue;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.musicqueue.Constants;
import com.example.musicqueue.R;
import com.example.musicqueue.models.AbstractQueue;
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

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference queueCollection = firestore.collection(Constants.FIRESTORE_QUEUE_COLLECTION);

    public QueueHolder(@Nonnull final View itemView){
        super(itemView);

        queueNameTV = itemView.findViewById(R.id.queue_name_text_view);
        queueLocationTV = itemView.findViewById(R.id.queue_location_text_view);
        songSizeTV = itemView.findViewById(R.id.song_size_text_view);

        favoriteChip = itemView.findViewById(R.id.fave_chip);
    }

    public void bind(@Nonnull AbstractQueue queue) {
        setDocId(queue.getDocId());
        setName(queue.getName());
        setLocation(queue.getLocation());
        setSongSize(queue.getSongCount());
        setFavorite(true);
    }

    private void setDocId(@Nonnull String docId) { this.docid = docId;}

    private void setName(@Nonnull String name) {this.queueNameTV.setText(name);}

    private void setLocation(@Nonnull String loc) {this.queueLocationTV.setText(loc);}

    private void setSongSize(@Nonnull Integer songCount) {
        this.songSizeTV.setText(songCount.toString());
    }

    private void setFavorite(@Nonnull boolean fave) {
        favoriteChip.setChecked(fave);
        favoriteChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((Chip)v).isChecked()) {
                    ((Chip) v).setTextColor(Color.parseColor(Constants.CHECKED_COLOR));
                }
                else {
                    ((Chip) v).setTextColor(Color.parseColor(Constants.UNCHECKED_COLOR));
                }
            }
        });
    }
}
