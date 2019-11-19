package com.example.musicqueue.ui.queue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewQueueActivity extends AppCompatActivity {

    private Button queueCreateButton;

    private TextInputLayout queueNameTIL, locationTIL;
    private TextInputEditText queuenameTIET, locationTIET;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference queueCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_queue);

        queueCollection = firestore.collection(Constants.FIRESTORE_QUEUE_COLLECTION);

        queueNameTIL = findViewById(R.id.queue_name_text_input_layout);
        locationTIL = findViewById(R.id.location_text_input_layout);

        queuenameTIET = findViewById(R.id.queue_name_text_input_edit_text);
        locationTIET = findViewById(R.id.location_text_input_edit_text);

        queuenameTIET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    queuenameTIET.clearFocus();
                }
            }
        });

        locationTIET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    locationTIET.clearFocus();
                }
            }
        });

        findViewById(R.id.queueCreateButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v) {
                createQueue();

            }


        });

        initActionbar();

    }

    public void createQueue() {
        if (FormUtils.inputIsEmpty(queuenameTIET.getText().toString())) {
            queueNameTIL.setError("Reuired");
            return;
        }
        if (FormUtils.inputIsEmpty(locationTIET.getText().toString())) {
            locationTIL.setError("Required");
            return;
        }
        Queue newQueue = new Queue(
                queuenameTIET.getText().toString(),
                locationTIET.getText().toString(),
                queueCollection.document().getId(),
                Timestamp.now(),
                Integer.toUnsignedLong(0)
        );

        queueCollection.add(newQueue)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        CommonUtils.showToast(getApplicationContext(), "Added queue!");
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
