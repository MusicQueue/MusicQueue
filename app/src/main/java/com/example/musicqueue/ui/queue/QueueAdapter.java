package com.example.musicqueue.ui.queue;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicqueue.Constants;
import com.example.musicqueue.R;
import com.example.musicqueue.holders.QueueHolder;
import com.example.musicqueue.models.Queue;
import com.example.musicqueue.utilities.CommonUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class QueueAdapter extends RecyclerView.Adapter<QueueHolder> {

    private List<Queue> queueList;
    private String uid;

    public QueueAdapter(List<Queue> qList, String uid, GeoPoint location){

        Collections.sort(qList,(Queue q1, Queue q2) ->  q1.getLocation().compareTo(location) > q2.getLocation().compareTo(location) ? -1 : 1);

        this.queueList = qList;
        this.uid = uid;
    }

    @NonNull
    @Override
    public QueueHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.queue_card_layout, parent, false);

        return new QueueHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QueueHolder holder, int position) {
        Queue model = this.queueList.get(position);
        holder.setDocId(model.getDocId());
        holder.setName(model.getName());
        holder.setLocation(model.getLocation());
        holder.setSongSize(model.getSongCount());
        holder.initCardClickListener(model.getDocId(), model.getCreator());

        Map<String, Boolean> favMap = model.getFavoritesMap();
        holder.setFavoritesMap(favMap);

        if (favMap.containsKey(this.uid)) {
            holder.setFavorite(favMap.get(this.uid));
        }
        else {
            holder.setFavorite(false);
        }

        final String uid = FirebaseAuth.getInstance().getUid().toString();
        final String queueDocid = model.getDocId();
        final String creator = model.getCreator();

        if (uid.equals(creator)) {
            holder.ownerTV.setVisibility(View.VISIBLE);
            holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    openDeleteDialog(v, queueDocid);

                    return false;
                }
            });
        }

    }

    private void openDeleteDialog(final View view, final String queueDocid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(), R.style.AppTheme_AlertDialogTheme);
        builder.setTitle("Delete Queue");

        final View v = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_delete_queue, null);
        builder.setView(v);

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                CollectionReference queueCollection = FirebaseFirestore.getInstance().collection(Constants.FIRESTORE_QUEUE_COLLECTION);

                queueCollection.document(queueDocid).delete();

                CommonUtils.showToast(view.getContext(), "Queue deleted");
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    public int getItemCount() {
        return queueList.size();
    }

    public void addQueue(Queue q){
        this.queueList.add(q);
    }

    public void removeQueue(Queue q){
        this.queueList.remove(q);
    }
}
