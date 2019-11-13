package com.example.musicqueue.ui.queue;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.musicqueue.R;
import com.google.android.material.chip.Chip;

import javax.annotation.Nonnull;

public class QueueHolder extends RecyclerView.ViewHolder {

    private final TextView queueNameTV, queueLocationTV, songSizeTV;
    private final Chip favoriteChip;

    public QueueHolder(@Nonnull final View itemView){
        super(itemView);

        queueNameTV = itemView.findViewById(R.id.queue_name_text_view);
        queueLocationTV = itemView.findViewById(R.id.queue_location_text_view);
        songSizeTV = itemView.findViewById(R.id.song_size_text_view);

        favoriteChip = itemView.findViewById(R.id.fave_chip);
    }
}
