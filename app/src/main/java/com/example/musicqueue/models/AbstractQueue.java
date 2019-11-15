package com.example.musicqueue.models;

import com.google.firebase.Timestamp;

public interface AbstractQueue {

     Timestamp getCreated();

     void setCreated(Timestamp created);

     String getLocation();

     void setLocation(String location);

     String getDocId();

     void setDocId(String docId);

     String getName();

     void setName(String name);
}
