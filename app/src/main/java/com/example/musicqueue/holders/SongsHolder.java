package com.example.musicqueue.holders;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicqueue.Constants;
import com.example.musicqueue.R;
import com.example.musicqueue.abstracts.AbstractSongs;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class SongsHolder extends RecyclerView.ViewHolder {

    private final TextView songNameTV, artistNameTV, songRankTV;
    public final TextView ownerTV;
    private final RadioButton upVote, downVote;
    private String docid, queueId, ownerUid;
    public CardView songCV;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private String uid = FirebaseAuth.getInstance().getUid().toString();
    private CollectionReference songCollection;
    private Map<String, Boolean> votersMap;

    public SongsHolder(@NonNull final View itemView){
        super(itemView);

        songCV = itemView.findViewById(R.id.song_card_view);
        ownerTV = itemView.findViewById(R.id.owner_text_view);
        songNameTV = itemView.findViewById(R.id.song_title_text_view);
        artistNameTV = itemView.findViewById(R.id.artist_text_view);
        songRankTV = itemView.findViewById(R.id.vote_count_text_view);

        upVote = itemView.findViewById(R.id.radio_up);
        downVote = itemView.findViewById(R.id.radio_down);

        // TODO: Fix voting system
        RadioGroup radioGroup = itemView.findViewById(R.id.vote_radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (i == R.id.radio_up) {
                    upVoteSong();
                }
                else if (i == R.id.radio_down) {
                    downVoteSong();
                }

            }
        });

    }

    private void upVoteSong() {
        songCollection = firestore.collection(Constants.FIRESTORE_QUEUE_COLLECTION)
                .document(queueId)
                .collection(Constants.FIRESTORE_SONG_COLLECTION);

        long v = Long.parseLong(songRankTV.getText().toString());
        v = v + 1;
        songCollection.document(docid).update("votes", v);
        //songCollection.document(docid).update("voters." + uid, true);
    }

    private void downVoteSong() {
        songCollection = firestore.collection(Constants.FIRESTORE_QUEUE_COLLECTION)
                .document(queueId)
                .collection(Constants.FIRESTORE_SONG_COLLECTION);

        long v = Long.parseLong(songRankTV.getText().toString());
        v = v - 1;
        songCollection.document(docid).update("votes", v);
        //songCollection.document(docid).update("voters." + uid, false);
    }

    public void bind(@NonNull AbstractSongs song) {
        setName(song.getName());
        setArtist(song.getArtist());
        setVotes(song.getVotes());
        setDocId(song.getDocId());
        setQueueId(song.getQueueId());
        setOwnerUid(song.getOwnerUid());
        setVotersMap(song.getVotersMap());
    }

    public void setDocId(@Nullable String docid) { this.docid = docid; }

    public void setQueueId(@Nullable String queueId) { this.queueId = queueId; }

    public void setOwnerUid(@Nullable String ownerUid) { this.ownerUid = ownerUid; }

    public void setName(@Nullable String name) { this.songNameTV.setText(name); }

    public void setArtist(@Nullable String artist) { this.artistNameTV.setText(artist); }

    public void setVotes(long votes) { this.songRankTV.setText(Long.toString(votes)); }

    public void setVotersMap(Map<String, Boolean> map) { this.votersMap = map; }

    public void setVoteArrows(boolean b) {
        /*if (b) {
            upVote.setChecked(true);
        }
        else {
            downVote.setChecked(true);
        }*/
    }

}
