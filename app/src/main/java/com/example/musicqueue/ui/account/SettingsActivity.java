package com.example.musicqueue.ui.account;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicqueue.R;
import com.example.musicqueue.authentication.SignInActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        /* Initialize action bar */
        initActionbar();

        /* Hide email update and password reset options if signed in via Google */
        hideEmailPassword();

        TextView updateDisplayNameTV = findViewById(R.id.update_diaplay_name_text_view);
        updateDisplayNameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDisplayNameDialog();
            }
        });

        TextView deleteAccountTV = findViewById(R.id.delete_account_text_view);
        deleteAccountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccountDialog();
            }
        });
    }

    /**
     * Initializes the action bar with settings title and button back to
     * Account fragment
     */
    private void initActionbar() {
        getSupportActionBar().setTitle(R.string.title_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    /**
     * Hides email update and password reset options if the user is signed in via
     * Google sign in (email and passwords are not used for authentication)
     */
    private void hideEmailPassword() {
        for (UserInfo user: FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
            if (user.getProviderId().equals("google.com")) {
                TextView emailTV = findViewById(R.id.update_email_text_view);
                emailTV.setVisibility(TextView.INVISIBLE);
                TextView passwordTV = findViewById(R.id.reset_password_text_view);
                passwordTV.setVisibility(TextView.INVISIBLE);
            }
        }
    }

    private void updateDisplayNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this, R.style.AppTheme_AlertDialogTheme);
        builder.setTitle(R.string.title_update_display_name);

        final View v = getLayoutInflater().inflate(R.layout.dialog_update_display_name, null);
        builder.setView(v);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TextInputEditText nameText = v.findViewById(R.id.name_text_input);
                String name = nameText.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    showToast("Must enter a name");
                }
                else {
                    updateDisplayNameFirebase(name);
                    showToast("Display name updated!");
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void updateDisplayNameFirebase(String name) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        firebaseUser.updateProfile(profileUpdates);
    }

    private void deleteAccountDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this, R.style.AppTheme_AlertDialogTheme);
        builder.setTitle(R.string.delete_account);

        final View v = getLayoutInflater().inflate(R.layout.dialog_delete_account, null);
        builder.setView(v);

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAccount();
                showToast("Account deleted.");
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void deleteAccount() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(firebaseUser.getUid()).delete();

        GoogleSignInClient mGoogleSignInClient;
        firebaseAuth.signOut();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(SettingsActivity.this, gso);
        mGoogleSignInClient.signOut();
        firebaseUser.delete();

        startActivity(new Intent(SettingsActivity.this, SignInActivity.class));
        finish();
    }

    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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
