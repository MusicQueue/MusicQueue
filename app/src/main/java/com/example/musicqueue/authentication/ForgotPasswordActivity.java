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
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputEditText emailText;
    private FirebaseAuth firebaseAuth;
    private final String TAG = "ForgotPasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        firebaseAuth = FirebaseAuth.getInstance();

        emailText = findViewById(R.id.email_text_input);
        emailText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                emailText.clearFocus();
            }
            }
        });

        Button registerButton = findViewById(R.id.rest_password_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPasswordResetEmail();
                hideKeyboard(v);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * sendPasswordResetEmail sends a password reset link in an email
     * to the given email
     */
    private void sendPasswordResetEmail() {

        // valide the given email
        if (!validateEmail()) {
            return;
        }

        // send the reset password email
        firebaseAuth.sendPasswordResetEmail(emailText.getText().toString())
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        showToast("Password reset email sent!");
                        Log.d(TAG, "Email sent.");
                        startActivity(new Intent(ForgotPasswordActivity.this, EmailPasswordActivity.class));
                        finish();
                    }
                    else {
                        showToast("Password reset email failed!");
                        Log.d(TAG, "Email failed.");
                    }
                }
            });
    }

    /**
     * validateEmail determines whether or not the given email is valid
     *
     * @return boolean
     */
    private boolean validateEmail() {

        boolean valid = true;

        TextInputLayout emailTIL = findViewById(R.id.email_text_layout);
        String email = emailText.getText().toString();

        // email is required
        if (TextUtils.isEmpty(email)) {
            emailTIL.setError("Required");
            valid = false;
        }
        // must be a valid email address
        else if (!isValidEmail(email)) {
            emailTIL.setError("Please enter a valid email");
            valid = false;
        }
        else {
            emailTIL.setError(null);
        }

        return valid;
    }

    /**
     * isValidEmail determines whether or not the given email is a valid one
     * (i.e. has characters before @ symbol, has @ symbol, has a domain)
     *
     * @param target given email to be validated
     * @return  boolean
     */
    public final boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        }

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    /**
     * hideKeyboard hides the keyboard; it is called on reset password button press
     *
     * @param view the activity view
     */
    public void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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

}
