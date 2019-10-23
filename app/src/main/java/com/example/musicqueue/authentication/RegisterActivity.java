package com.example.musicqueue.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.musicqueue.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText nameText, emailText, passwordText, confirmPasswordText;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        nameText = findViewById(R.id.name_text_input);
        nameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    nameText.clearFocus();
                }
            }
        });

        emailText = findViewById(R.id.email_text_input);
        emailText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    emailText.clearFocus();
                }
            }
        });

        passwordText = findViewById(R.id.password_text_input);
        passwordText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    passwordText.clearFocus();
                }
            }
        });

        confirmPasswordText = findViewById(R.id.password_confirm_text_input);
        confirmPasswordText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    confirmPasswordText.clearFocus();
                }
            }
        });

        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
                hideKeyboard(v);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, EmailPasswordActivity.class));
                finish();
            }
        });

    }

    private void registerNewUser() {

        final String name, email, password;
        name = nameText.getText().toString();
        email = emailText.getText().toString();
        password = passwordText.getText().toString();

        if (!validateForm()) {
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            showToast("Registration successful!");

                            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name).build();
                            firebaseUser.updateProfile(profile);

                            Map<String, Object> data = new HashMap<>();
                            data.put("displayName", firebaseUser.getDisplayName());
                            data.put("email", email);
                            db.collection("users").document(firebaseUser.getUid())
                                    .set(data, SetOptions.merge());

                            FirebaseAuth.getInstance().signOut();

                            Intent intent = new Intent(RegisterActivity.this, EmailPasswordActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            showToast("Registration failed!");
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        TextInputLayout nameTIL = findViewById(R.id.name_text_layout);
        TextInputLayout emailTIL = findViewById(R.id.email_text_layout);
        TextInputLayout passwordTIL = findViewById(R.id.password_text_layout);
        TextInputLayout confirmPasswordTIL = findViewById(R.id.password_confirm_text_layout);

        String displayName, email, password, passwordConfirm;
        displayName = nameText.getText().toString();
        email = emailText.getText().toString();
        password = passwordText.getText().toString();
        passwordConfirm = confirmPasswordText.getText().toString();

        if (TextUtils.isEmpty(displayName)) {
            nameTIL.setError("Required");
            valid = false;
        }
        else {
            nameTIL.setError(null);
        }

        if (TextUtils.isEmpty(email)) {
            emailTIL.setError("Required");
            valid = false;
        }
        else if (!isValidEmail(email)) {
            emailTIL.setError("Please enter a valid email");
            valid = false;
        }
        else {
            emailTIL.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            passwordTIL.setError("Required");
            valid = false;
        }
        else if (password.length() < 8) {
            passwordTIL.setError("Must be at least 8 characetrs in length");
        }
        else {
            passwordTIL.setError(null);
        }

        if (TextUtils.isEmpty(passwordConfirm)) {
            confirmPasswordTIL.setError("Required");
            valid = false;
        }
        else {
            confirmPasswordTIL.setError(null);
        }

        if (!passwordConfirm.equals(password)) {
            confirmPasswordTIL.setError("Passwords do not match");
            valid = false;
        }
        else {
            passwordTIL.setError(null);
            confirmPasswordTIL.setError(null);
        }

        return valid;
    }

    public final boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        }

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    public void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

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
}
