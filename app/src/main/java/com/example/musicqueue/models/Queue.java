package com.example.musicqueue.models;


import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class Queue {

    private String name;
    private String location;
    @DocumentId
    private String docId;
    private Timestamp created;
    private Long songCount;
    private boolean favorite;
    private Map<String, Boolean> favoritesMap;

    public Queue(String name, String location, String docId, Timestamp created, Long songCount,
                 Map<String, Boolean> favoritesMap) {
        this.name = name;
        this.location = location;
        this.docId = docId;
        this.created = created;
        this.songCount = songCount;
        this.favoritesMap = favoritesMap;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSongCount() { return songCount;}

    public void setFavorite(boolean b) { this.favorite = b; }

    public boolean getFavorite() { return this.favorite; }

    public void setFavoritesMap(Map<String, Boolean> favoritesMap) {
        this.favoritesMap = favoritesMap;
    }

    public Map<String, Boolean> getFavoritesMap() {
        return this.favoritesMap;
    }
}
