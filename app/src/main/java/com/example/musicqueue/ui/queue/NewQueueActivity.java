package com.example.musicqueue.ui.queue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.musicqueue.Constants;
import com.example.musicqueue.R;
import com.example.musicqueue.models.Queue;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewQueueActivity extends AppCompatActivity {

    private Button queueCreateButton;

    private EditText queueNameET, queueLocationET;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference queueCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_queue);

        queueCollection = firestore.collection(Constants.FIRESTORE_QUEUE_COLLECTION);

        queueNameET = findViewById(R.id.queueNameET);
        queueLocationET = findViewById(R.id.queueLocationET);

        findViewById(R.id.queueCreateButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v) {

                if (queueNameET.getText().length() == 0) {
                    //TODO: Queue Name is required
                    return;
                }
                if (queueLocationET.getText().length() == 0) {
                    //TODO: Queue Name is required
                    return;
                }
                Queue newQueue = new Queue(
                        queueNameET.getText().toString(),
                        queueLocationET.getText().toString(),
                        queueCollection.document().getId(),
                        Timestamp.now(),
                        Integer.toUnsignedLong(0)
                );

                queueCollection.add(newQueue)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    task.getResult()
                                            .collection(Constants.FIRESTORE_SONG_COLLECTION);
                                    Context context = v.getContext();
                                    Intent intent = new Intent(context, QueueFragment.class);
                                    context.startActivity(intent);
                                }
                            });
            }


        });


    }
}
