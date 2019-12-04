package com.example.musicqueue.abstracts;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

public interface AbstractQueue {

     Timestamp getCreated();

     void setCreated(Timestamp created);

     GeoPoint getLocation();

     void setLocation(GeoPoint location);

     String getDocId();

     void setDocId(String docId);

     String getName();

     void setName(String name);

     Long getSongCount();

     void setFavoritesArray(String[] favoritesArray);

     String[] getFavoritesArray();
}
