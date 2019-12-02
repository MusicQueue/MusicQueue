package com.example.musicqueue.holders;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicqueue.R;
import com.example.musicqueue.models.AbstractLibrary;

public class LibrarySongsHolder extends RecyclerView.ViewHolder {

    private final TextView songNameTV, artistNameTV;
    private final Button addToQueueButton;
    public CardView songCV;
    private String ownerUid, docid;

    public LibrarySongsHolder(@NonNull final View itemView){
        super(itemView);

        songCV = itemView.findViewById(R.id.song_card_view);
        songNameTV = itemView.findViewById(R.id.song_title_text_view);
        artistNameTV = itemView.findViewById(R.id.artist_text_view);

        addToQueueButton = itemView.findViewById(R.id.add_to_queue_button);
        addToQueueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToQueue();
            }
        });
    }

    public void bind(@NonNull AbstractLibrary library) {
        setName(library.getName());
        setArtist(library.getArtist());
        setOwnerUid(library.getOwnerUid());
        setDocid(library.getDocid());
    }

    public void setOwnerUid(@Nullable String ownerUid) { this.ownerUid = ownerUid; }

    public void setName(@Nullable String name) { this.songNameTV.setText(name); }

    public void setArtist(@Nullable String artist) { this.artistNameTV.setText(artist); }

    public void setDocid(String docid) { this.docid = docid; }

    private void addToQueue() {

    }

}
