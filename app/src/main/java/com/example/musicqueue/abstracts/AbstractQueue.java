package com.example.musicqueue.abstracts;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

public interface AbstractQueue {

     Timestamp getCreated();

     void setCreated(Timestamp created);

     String getLocation();

     void setLocation(String location);

     String getDocId();

     void setDocId(String docId);

     String getName();

     void setName(String name);

     Long getSongCount();

     void setFavoritesArray(String[] favoritesArray);

     String[] getFavoritesArray();

     void setGeoPoint(GeoPoint g);

     GeoPoint getGeoPoint();
}
