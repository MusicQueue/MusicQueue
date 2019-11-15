package com.example.musicqueue.ui.queue;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.musicqueue.R;
import com.example.musicqueue.models.AbstractQueue;
import com.example.musicqueue.models.Queue;
import com.google.android.material.chip.Chip;

import javax.annotation.Nonnull;

public class QueueHolder extends RecyclerView.ViewHolder {

    private String docid;
    private final TextView queueNameTV, queueLocationTV, songSizeTV;
    private final Chip favoriteChip;

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
        setSongSize(queue.getDocId());
        setFavorite(true);
    }

    private void setDocId(@Nonnull String docId) { this.docid = docId;}

    private void setName(@Nonnull String name) {this.queueNameTV.setText(name);}

    private void setLocation(@Nonnull String loc) {this.queueLocationTV.setText(loc);}

    private void setSongSize(@Nonnull String docid) {
        //TODO: actually get a count of songs, here or in the model
        this.songSizeTV.setText("0");
    }

    private void setFavorite(@Nonnull boolean fave) {
        favoriteChip.setChecked(fave);
        
    }
}
