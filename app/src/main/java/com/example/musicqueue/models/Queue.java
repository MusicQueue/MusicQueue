package com.example.musicqueue.models;


import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.PropertyName;

import java.util.Map;

@IgnoreExtraProperties
public class Queue {

    private String name;
    private GeoPoint location;
    @DocumentId
    private String docId;
    private Timestamp created;
    private Long songCount;
    private boolean favorite;
    private Map<String, Boolean> favoritesMap;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    private String creator;

    public Queue(){}

    public Queue(String name, GeoPoint location, Timestamp created, Long songCount,
                 Map<String, Boolean> favoritesMap, String creator) {
        this.name = name;
        this.location = location;
        this.created = created;
        this.songCount = songCount;
        this.favoritesMap = favoritesMap;
        this.creator = creator;
    }

    public Timestamp getCreated() {
        return this.created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public GeoPoint getLocation() {
        return this.location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public String getDocId() {
        return this.docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSongCount() { return this.songCount;}

    public void setFavorite(boolean b) { this.favorite = b; }

    public boolean getFavorite() { return this.favorite; }

    @PropertyName("favorites")
    public void setFavoritesMap(Map<String, Boolean> favoritesMap) {
        this.favoritesMap = favoritesMap;
    }

    @PropertyName("favorites")
    public Map<String, Boolean> getFavoritesMap() {
        return this.favoritesMap;
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("DocID :").append(this.docId).append("; ");
        builder.append("Name :").append(this.name).append("; ");
        builder.append("Location :").append(this.location.toString()).append("; ");
        builder.append("created :").append(this.created.toString()).append("; ");
        builder.append("Favorites :").append(this.favoritesMap != null ? this.favoritesMap.toString() : "").append("; ");
        return builder.toString();
    }


}
