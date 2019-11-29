package com.example.musicqueue.utilities;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

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

    public static Long getLongOrZero(DocumentSnapshot snapshot, String field) {
        return snapshot.contains(field) ?
                (Long) snapshot.get(field) :
                0;
    }

    public static Map<String, Boolean> getMapOrInit(DocumentSnapshot snapshot, String field) {
        return snapshot.contains(field) ?
                (Map<String, Boolean>) snapshot.get(field) :
                new HashMap<String, Boolean>();
    }
}
