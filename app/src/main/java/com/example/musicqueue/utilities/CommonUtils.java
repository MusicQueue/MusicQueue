package com.example.musicqueue.utilities;

import android.content.Context;
import android.widget.Toast;

public class CommonUtils {

    /**
     * shows a toast for a given message
     * @param context the application context
     * @param msg the message to be toasted
     */
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
