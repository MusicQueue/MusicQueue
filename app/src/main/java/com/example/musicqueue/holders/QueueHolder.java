package com.example.musicqueue.holders;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicqueue.Constants;
import com.example.musicqueue.R;
import com.example.musicqueue.abstracts.AbstractQueue;
import com.example.musicqueue.ui.songs.SongsActivity;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.Map;

import javax.annotation.Nonnull;

public class QueueHolder extends RecyclerView.ViewHolder {

    private String docid, ownerId;
    private final TextView queueNameTV, songSizeTV, songTV;
    public final Chip favoriteChip;
    public final CardView cardView;
    private long songCount;

    private Map<String, Boolean> favoritesMap;
    private GeoPoint location;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference queueCollection = firestore.collection(Constants.FIRESTORE_QUEUE_COLLECTION);

    public QueueHolder(@Nonnull final View itemView){
        super(itemView);

        queueNameTV = itemView.findViewById(R.id.queue_name_text_view);
        songSizeTV = itemView.findViewById(R.id.song_size_text_view);
        songTV = itemView.findViewById(R.id.songs_title_text_view);

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

    public void setLocation(@Nonnull GeoPoint loc) { this.location = loc; }

    public void setOwnerId(String s) { this.ownerId = s; }

    public void setSongSize(@Nonnull Long songCount) {
        this.songCount = songCount;

        if (songCount == 1) {
            this.songTV.setText("Song");
        }
        else {
            this.songTV.setText("Songs");
        }

        this.songSizeTV.setText(songCount.toString());
    }

    public void setFavoritesMap(Map<String, Boolean> favoritesMap) {
        this.favoritesMap = favoritesMap;
    }

    public void setFavorite(final boolean fave) {
        favoriteChip.setChecked(fave);
        favoriteChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favoriteChip.setChecked(!fave);
                favoritesMap.put(firebaseUser.getUid().toString(), !fave);
                queueCollection.document(docid).update("favorites", favoritesMap);
            }
        });
    }

    public void initCardClickListener(final String docid, final String ownerId) {
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = itemView.getContext();
                Intent intent = new Intent(context, SongsActivity.class);
                intent.putExtra("DOCUMENT_ID", docid);
                intent.putExtra("OWNER_ID", ownerId);
                intent.putExtra("DOCUMENT_NAME", queueNameTV.getText().toString());
                intent.putExtra("SONG_COUNT", songSizeTV.getText().toString());
                context.startActivity(intent);
            }
        });
    }

    public String getQueueId() {
        return this.docid;
    }

    public long getSongCount() {
        return this.songCount;
    }

    public GeoPoint getLocation() {
        return this.location;
    }

    public String getOwnerId() {
        return this.ownerId;
    }
}
