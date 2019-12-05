package com.example.musicqueue.ui.library;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.musicqueue.Constants;
import com.example.musicqueue.R;
import com.example.musicqueue.holders.QueueHolder;
import com.example.musicqueue.models.Queue;
import com.example.musicqueue.models.Song;
import com.example.musicqueue.ui.queue.QueueAdapter;
import com.example.musicqueue.utilities.CommonUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddToQueueAdapter extends QueueAdapter {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;
    private CollectionReference queueCollection;

    private String songName, songArtist;

    public AddToQueueAdapter(List<Queue> qList, String uid, GeoPoint location, String songName, String songArtist, Context context){
        super(qList, uid, location);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        this.songArtist = songArtist;
        this.songName = songName;


        queueCollection = firestore.collection(Constants.FIRESTORE_QUEUE_COLLECTION);
    }

    @Override
    public void onBindViewHolder(@NonNull QueueHolder holder, int position) {
        Queue model = this.queueList.get(position);
        holder.setDocId(model.getDocId());
        holder.setName(model.getName());
        holder.setLocation(model.getLocation());
        holder.setSongSize(model.getSongCount());


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkIfSongInQueue(holder.getQueueId(), holder.getSongCount(), view.getContext());

            }
        });
    }

        /**
         * checkIfSongInQueue determines whether a song already exists the queue or not
         *
         * @param queueId the queue id
         * @param songCount the queue's song count
         */
        private void checkIfSongInQueue(final String queueId, final long songCount, Context context) {

            CollectionReference songsCollection = queueCollection.document(queueId)
                    .collection(Constants.FIRESTORE_SONG_COLLECTION);

            songsCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        boolean exists = false;
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            if (doc.get("name").toString().equals(songName) && doc.get("artist").toString().equals(songArtist)) {
                                exists = true;
                            }
                        }
                        if (exists) {
                            songExistsDialog(context);
                        }
                        else {
                            addSongToQueue(queueId, songCount, context);
                        }
                    }
                }
            });
        }

        /**
         * addSongToQueue adds the song from the library to the choosen queue
         * @param queueId the queue id
         * @param songCount the queue's song count
         */
        private void addSongToQueue(final String queueId, final long songCount, Context context) {
            CollectionReference songsCollection = queueCollection.document(queueId)
                    .collection(Constants.FIRESTORE_SONG_COLLECTION);

            Map<String, Object> data = new HashMap<>();
            Map<String, Boolean> votersMap = new HashMap<>();
            votersMap.put(firebaseUser.getUid(), true);
            data.put("voters", votersMap);
            data.put("name", this.songName);
            data.put("artist", this.songArtist);
            data.put("votes", Integer.toUnsignedLong(0));
            data.put("queueId", queueId);
            data.put("ownerUid", firebaseUser.getUid());

            songsCollection.add(data)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            long songCountLong = songCount + 1;
                            CollectionReference queueref = firestore.collection(Constants.FIRESTORE_QUEUE_COLLECTION);
                            queueref.document(queueId).update("songCount", songCountLong);

                            CommonUtils.showToast(context.getApplicationContext(), "Added song!");
                            task.getResult().collection(Constants.FIRESTORE_SONG_COLLECTION);
                            ((Activity) context).finish();
                        }
                    });
        }

        /**
         * displays the song exists dialog
         */
        private void songExistsDialog(Context context) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context , R.style.AppTheme_AlertDialogTheme);
            builder.setTitle("Song in Queue");

            final View v = LayoutInflater.from(context).inflate(R.layout.dialog_song_exists, null);
            builder.setView(v);

            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }

}
