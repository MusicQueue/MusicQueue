package com.example.musicqueue.ui.songs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.example.musicqueue.Constants;
import com.example.musicqueue.R;
import com.example.musicqueue.models.Queue;
import com.example.musicqueue.utilities.CommonUtils;
import com.example.musicqueue.utilities.FormUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.protobuf.Any;

import java.util.HashMap;
import java.util.Map;

public class AddSongActivity extends AppCompatActivity {

    private Button addSongButton;

    private TextInputLayout songNameTIL, artistTIL;
    private TextInputEditText songNameTIET, artistTIET;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference songsCollection, libraryCollection;

    String queueDocid;
    String songCount;
    String uid;
    Long songCountLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song);

        uid = FirebaseAuth.getInstance().getUid().toString();

        queueDocid = getIntent().getStringExtra("DOCUMENT_ID");
        songCount = getIntent().getStringExtra("SONG_COUNT");
        songCountLong = Long.parseLong(songCount);

        songsCollection = firestore.collection(Constants.FIRESTORE_QUEUE_COLLECTION)
                .document(queueDocid)
                .collection(Constants.FIRESTORE_SONG_COLLECTION);

        libraryCollection = firestore.collection("users")
                .document(uid)
                .collection("library");

        songNameTIL = findViewById(R.id.song_name_text_input_layout);
        artistTIL = findViewById(R.id.artist_text_input_layout);

        songNameTIET = findViewById(R.id.song_name_text_input_edit_text);
        artistTIET = findViewById(R.id.artist_text_input_edit_text);

        songNameTIET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    songNameTIET.clearFocus();
                }
            }
        });

        artistTIET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    artistTIET.clearFocus();
                }
            }
        });

        findViewById(R.id.add_song_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v) {
                addSong();
            }


        });

        initActionbar();
    }

    public void addSong() {
        if (FormUtils.inputIsEmpty(songNameTIET.getText().toString())) {
            songNameTIL.setError("Reuired");
            return;
        }
        if (FormUtils.inputIsEmpty(artistTIET.getText().toString())) {
            artistTIL.setError("Required");
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("name", songNameTIET.getText().toString());
        data.put("artist", artistTIET.getText().toString());
        data.put("votes", Integer.toUnsignedLong(0));
        data.put("queueId", queueDocid);
        data.put("ownerUid", uid);

        songsCollection.add(data)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        songCountLong = songCountLong + 1;
                        CollectionReference queueref = firestore.collection(Constants.FIRESTORE_QUEUE_COLLECTION);
                        queueref.document(queueDocid).update("songCount", songCountLong);

                        CommonUtils.showToast(getApplicationContext(), "Added song!");
                        task.getResult().collection(Constants.FIRESTORE_SONG_COLLECTION);
                        finish();
                    }
                });


    }

    /**
     * initActionbar initializes the action bar with settings title and button back to
     * Account fragment
     */
    private void initActionbar() {
        getSupportActionBar().setElevation(0);  // remove actionbar shadow
        getSupportActionBar().setTitle("Back");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    /**
     * dispatchTouchEvent is used to remove focus from text fields when touch is
     * triggered outside the text field
     *
     * @param event touch event
     * @return boolean
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof TextInputEditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    Log.d("focus", "touchevent");
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
