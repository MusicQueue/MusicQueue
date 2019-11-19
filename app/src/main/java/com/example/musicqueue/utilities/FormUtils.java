package com.example.musicqueue.utilities;

import android.text.TextUtils;

import com.google.android.material.textfield.TextInputLayout;

public class FormUtils {

    /**
     * inputIsEmpty determines wheter or not the input is empty
     *
     * @param input given input
     * @return boolean
     */
    public static boolean inputIsEmpty(String input) {
        return TextUtils.isEmpty(input);
    }

    /**
     * validateEmail determines whether or not the given email is a valid one
     * (i.e. has characters before @ symbol, has @ symbol, has a domain)
     *
     * @param email given email to be validated
     * @return  String
     */
    public static boolean validateEmail(CharSequence email) {
        if (email == null) {
            return false;
        }

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * validatePassword determines whether or not the password is valid
     * by determining if its length is at least 8 characters in length
     *
     * @param password given password
     * @return boolean
     */
    public static boolean validatePassword(String password) {
        boolean result = true;

        if (password.length() <= 8) {
            result = false;
        }

        return result;
    }

    public static boolean validateEmailPassForm(String email, String pass,
                                                TextInputLayout emailTIL, TextInputLayout passTIL)
    {
        boolean result = true;

        if (inputIsEmpty(email)) {
            emailTIL.setError("Required");
            result = false;
        }
        else if (!validateEmail(email)) {
            emailTIL.setError("Please enter a valid email address.");
            result = false;
        }
        else {
            emailTIL.setError(null);
        }

        if (inputIsEmpty(pass)) {
            passTIL.setError("Required");
            result = false;
        }
        else if (!validatePassword(pass)) {
            passTIL.setError("Invalid password, must be at least 8 characters");
            result = false;
        }
        else {
            passTIL.setError(null);
        }

        return result;
    }

}
