package com.example.musicqueue.models;

import com.google.android.material.textfield.TextInputLayout;

public class RegisterModel {

    String displayName;
    String email;
    String password;
    String passwordConfirm;
    TextInputLayout nameTIL;
    TextInputLayout emailTIL;
    TextInputLayout passwordTIL;
    TextInputLayout confirmPasswordTIL;

    public RegisterModel() {}

    public void setDisplayName(String s) { this.displayName = s; }

    public void setEmail(String s) { this.email = s; }

    public void setPassword(String s) { this.password = s; }

    public void setPasswordConfirm(String s) { this.password = s; }


}
