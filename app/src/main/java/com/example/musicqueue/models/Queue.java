package com.example.musicqueue.models;


import com.google.firebase.Timestamp;
import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Queue {

    private String name;
    private String location;
    private String docId;
    private Timestamp created;
    private Long songCount;

    public Queue(String name, String location, String docId, Timestamp created, Long songCount) {
        this.name = name;
        this.location = location;
        this.docId = docId;
        this.created = created;
        this.songCount = songCount;
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
}
