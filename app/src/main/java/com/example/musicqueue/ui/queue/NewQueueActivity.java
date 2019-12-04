package com.example.musicqueue.ui.queue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
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
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.internal.ui.AutocompleteImplFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class NewQueueActivity extends AppCompatActivity {

    private final static String TAG = "NewQueueActivity";

    private Button queueCreateButton;

    private TextInputLayout queueNameTIL, locationTIL;
    private TextInputEditText queuenameTIET, locationTIET;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference queueCollection;

    private PlacesClient placesClient;
    private double latitude = 0.0, longitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_queue);

        // Initialize the SDK
        Places.initialize(getApplicationContext(), getString(R.string.places_api));
        // Create a new Places client instance
        placesClient = Places.createClient(this);

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

        initAutoComplete();

    }

    private void initAutoComplete() {
        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,
                Place.Field.ADDRESS_COMPONENTS, Place.Field.ADDRESS, Place.Field.LAT_LNG));

        autocompleteFragment.setHint("Add Address");

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                setLatLng(place.getLatLng().latitude, place.getLatLng().longitude);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    private void setLatLng(double lat, double lng) {
        latitude = lat;
        longitude = lng;
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
        if (latitude == 0.0 && longitude == 0.0) {
            CommonUtils.showToast(getApplicationContext(), "Adress Required");
            return;
        }

        Map<String, Boolean> fav = new HashMap<>();
        fav.put(FirebaseAuth.getInstance().getUid().toString(), true);

        Map<String, Object> data = new HashMap<>();
        data.put("name", queuenameTIET.getText().toString());
        data.put("location", locationTIET.getText().toString());
        data.put("geoPoint", new GeoPoint(latitude, longitude));
        data.put("created", Timestamp.now());
        data.put("songCount", Integer.toUnsignedLong(0));
        data.put("favorites", fav);
        data.put("ownerUid", FirebaseAuth.getInstance().getUid().toString());

        queueCollection.add(data)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        CommonUtils.showToast(getApplicationContext(), "Added queue!");
                        task.getResult().collection(Constants.FIRESTORE_QUEUE_COLLECTION);
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
