package com.example.musicqueue.ui.queue;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicqueue.R;
import com.example.musicqueue.holders.QueueHolder;
import com.example.musicqueue.models.Queue;
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
        holder.initCardClickListener(model.getDocId());

        Map<String, Boolean> favMap = model.getFavoritesMap();
        holder.setFavoritesMap(favMap);

        if (favMap.containsKey(this.uid)) {
            holder.setFavorite(favMap.get(this.uid));
        }
        else {
            holder.setFavorite(false);
        }

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
