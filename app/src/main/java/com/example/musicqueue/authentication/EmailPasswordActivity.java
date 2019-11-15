package com.example.musicqueue.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicqueue.MainActivity;
import com.example.musicqueue.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class EmailPasswordActivity extends AppCompatActivity {

    private TextInputEditText emailText, passwordText;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_password);

        firebaseAuth = FirebaseAuth.getInstance();

        // if the user is already signed in, navigate to Main Activity
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(EmailPasswordActivity.this, MainActivity.class));
            finish();
        }

        emailText = findViewById(R.id.email_text_input);
        passwordText = findViewById(R.id.password_text_input);

        emailText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    emailText.clearFocus();
                }
            }
        });

        passwordText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    emailText.clearFocus();
                }
            }
        });


        Button signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserAccount();
                hideKeyboard(v);
            }
        });

        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmailPasswordActivity.this, RegisterActivity.class));
                finish();
            }
        });

        TextView forgotPasswordTV = findViewById(R.id.forgot_password_text_view);
        forgotPasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassword();
            }
        });
    }

    /**
     * loginUserAccount authenticates the user's credentials with Firebase
     */
    private void loginUserAccount() {

        String email, password;
        email = emailText.getText().toString();
        password = passwordText.getText().toString();

        // validate user input
        if (!validateForm()) {
            return;
        }

        // authenticate with Firebase
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            showToast("Login successful!");
                            Intent intent = new Intent(EmailPasswordActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            showToast("Login failed! Incorrect username or password.");
                        }
                    }
                });
    }

    /**
     * forgotPassword starts the ForgotPassword Activity
     */
    private void forgotPassword() {
        startActivity(new Intent(EmailPasswordActivity.this, ForgotPasswordActivity.class));
    }

    /**
     * validateForm determines wheter or not user input is valid for
     * authentication
     *
     * @return boolean
     */
    private boolean validateForm() {
        boolean valid = true;

        TextInputLayout emailTIL = findViewById(R.id.email_text_layout);
        TextInputLayout passwordTIL = findViewById(R.id.password_text_layout);

        String email, password;
        email = emailText.getText().toString();
        password = passwordText.getText().toString();

        // email field is required
        if (TextUtils.isEmpty(email)) {
            emailTIL.setError("Required");
            valid = false;
        }
        // checks validation of the email
        else if (!isValidEmail(email)) {
            emailTIL.setError("Please enter a valid email");
            valid = false;
        }
        else {
            emailTIL.setError(null);
        }

        // password is required
        if (TextUtils.isEmpty(password)) {
            passwordTIL.setError("Required");
            valid = false;
        }
        // user password needs to be at least 8 characters in length
        else if (password.length() <= 8) {
            passwordTIL.setError("Password invalid, must be at least 8 characters");
            valid = false;
        }
        else {
            passwordTIL.setError(null);
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

    /**
     * hideKeyboard hides the keyboard; it is called on button presses
     *
     * @param view the activity view
     */
    public void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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
