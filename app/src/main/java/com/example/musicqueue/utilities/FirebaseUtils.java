package com.example.musicqueue.utilities;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

public class FirebaseUtils {

    public static String getStringOrEmpty(DocumentSnapshot snapshot, String field){
        return snapshot.contains(field) ?
                snapshot.get(field).toString() :
                "Does Not Exist";

    }

    public static Timestamp getTimestampOrNow(DocumentSnapshot snapshot, String field) {
        return snapshot.contains(field) ?
                (Timestamp) snapshot.get(field) :
                Timestamp.now();
    }

    public static Long getLongOrZero( DocumentSnapshot snapshot, String field) {
        return snapshot.contains(field) ?
                (Long) snapshot.get(field) :
                0;
    }
}
