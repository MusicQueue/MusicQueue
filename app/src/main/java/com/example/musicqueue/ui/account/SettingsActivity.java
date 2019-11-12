package com.example.musicqueue.ui.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicqueue.R;
import com.example.musicqueue.authentication.SignInActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initActionbar();

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

        TextView updateEmailTV = findViewById(R.id.update_email_text_view);
        updateEmailTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmailDialog();
            }
        });

        TextView resetPasswordTV = findViewById(R.id.reset_password_text_view);
        resetPasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPasswordDialog();
            }
        });
    }

    /**
     * initActionbar initializes the action bar with settings title and button back to
     * Account fragment
     */
    private void initActionbar() {
        getSupportActionBar().setTitle(R.string.title_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    /**
     * hideEmailPassword ides email update and password reset options if the
     * user is signed in via Google sign in (email and passwords are not used
     * for Google account authentication)
     */
    private void hideEmailPassword() {
        for (UserInfo user: FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
            if (user.getProviderId().equals("google.com")) {
                TextView emailTV = findViewById(R.id.update_email_text_view);
                emailTV.setVisibility(TextView.INVISIBLE);
                TextView passwordTV = findViewById(R.id.reset_password_text_view);
                passwordTV.setVisibility(TextView.INVISIBLE);
            }
            if (user.getProviderId().equals("password")) {
                TextView emailTV = findViewById(R.id.update_email_text_view);
                emailTV.setVisibility(TextView.VISIBLE);
                TextView passwordTV = findViewById(R.id.reset_password_text_view);
                passwordTV.setVisibility(TextView.VISIBLE);
            }
        }
    }

    /**
     * updateDisplayNameDialog creates and opens a dialog for the user to
     * update their display name
     */
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

    /**
     * updateDisplayName takes a new string value and updates the user's
     * display name in Firebase to that value
     *
     * @param name new display name
     */
    private void updateDisplayNameFirebase(String name) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        firebaseUser.updateProfile(profileUpdates);
    }

    /**
     * deleteAccountDialog creates and opens a dialog with the option for the
     * user to delete their account
     */
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

    /**
     * deleteAccount deletes the user's account from Firebase and from the database
     */
    private void deleteAccount() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(firebaseUser.getUid()).delete();

        // sign the user out of Firebase
        firebaseAuth.signOut();

        // sign the user out of their Google account
        GoogleSignInClient mGoogleSignInClient;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(SettingsActivity.this, gso);
        mGoogleSignInClient.signOut();

        // delete the user from Firebase
        firebaseUser.delete();

        // navigate the user back to the SignIn Activity
        startActivity(new Intent(SettingsActivity.this, SignInActivity.class));
        finish();
    }

    /**
     * updateEmailDialog creates and opens a dialog to allow the user to update
     * the email address for their account and authentication
     */
    private void updateEmailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this, R.style.AppTheme_AlertDialogTheme);
        builder.setTitle(R.string.title_update_email);

        final View v = getLayoutInflater().inflate(R.layout.dialog_update_email, null);
        builder.setView(v);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TextInputEditText emailText = v.findViewById(R.id.email_text_input);
                String email = emailText.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    showToast("Must enter a email");
                }
                else {
                    updateEmailFirebase(email);
                    showToast("Email updated!");
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

    /**
     * updateEmailFirebase updates the user's email in Firebase authentication
     * and in the database
     *
     * @param email new email
     */
    private void updateEmailFirebase(String email) {
        final String TAG = "UpdateEmailDialog";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updateEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            Map<String, Object> data = new HashMap<>();
                            data.put("email", firebaseUser.getEmail());
                            db.collection("users").document(firebaseUser.getUid())
                                    .set(data, SetOptions.merge());
                            Log.d(TAG, "User email address updated.");
                        }
                    }
                });
    }

    /**
     * resetPasswordDialog creates and opens a dialog allowing the user to reset
     * their password
     */
    private void resetPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this, R.style.AppTheme_AlertDialogTheme);
        builder.setTitle(R.string.title_reset_password);

        final View v = getLayoutInflater().inflate(R.layout.dialog_reset_password, null);
        builder.setView(v);

        builder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TextInputEditText passwordText = v.findViewById(R.id.password_text_input);
                String password = passwordText.getText().toString();

                TextInputEditText passwordConfirmText = v.findViewById(R.id.password_confirm_text_input);
                String passwordConfirm = passwordConfirmText.getText().toString();

                if (TextUtils.isEmpty(password)) {
                    showToast("Must enter a password");
                }
                else if (password.length() < 8) {
                    showToast("Password must be longer than 8 characters");
                }
                else if (TextUtils.isEmpty(passwordConfirm)) {
                    showToast("Must confirm password");
                }
                else if (!password.equals(passwordConfirm)) {
                    showToast("Passwords must match");
                }
                else {
                    resetPasswordFirebase(password);
                    showToast("Password updated!");
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

    /**
     * resetPasswordFirebase updates the user's password in Firebase authentication
     * using the new given password
     *
     * @param password new password
     */
    private void resetPasswordFirebase(String password) {
        final String TAG = "resetPasswordDialog";

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updatePassword(password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User password updated.");
                        }
                    }
                });
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
