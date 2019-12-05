package com.example.musicqueue.utilities;

import android.text.TextUtils;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class FormUtils {

    // Email pattern Regex
    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
    );

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
    public static boolean validateEmail(String email) {
        if (email == null) {
            return false;
        }

        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
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

    /**
     * validates the email and password form and sets the appropiate erros if the exist
     * @param email user's email
     * @param pass user's passowrd
     * @param emailTIL email text input layout
     * @param passTIL password text input layout
     * @return boolean
     */
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

    /**
     * Same logic as one above, but used for testing until I can figure out how to
     * test wil TextInputLayout
     *
     * @param email email
     * @param pass password
     * @return boolean
     */
    public static boolean validateEmailPassForm(String email, String pass) {
        boolean result = true;

        if (inputIsEmpty(email)) {
            result = false;
        }
        else if (!validateEmail(email)) {
            result = false;
        }

        if (inputIsEmpty(pass)) {
            result = false;
        }
        else if (!validatePassword(pass)) {
            result = false;
        }

        return result;
    }

    /**
     * validateEmail determines whether or not the given email is valid
     *
     * @return boolean
     */
    public static boolean validateEmailForm(String email, TextInputLayout emailTIL) {
        boolean valid = true;

        // email is required
        if (inputIsEmpty(email)) {
            emailTIL.setError("Required");
            valid = false;
        }
        // must be a valid email address
        else if (!validateEmail(email)) {
            emailTIL.setError("Please enter a valid email");
            valid = false;
        }
        else {
            emailTIL.setError(null);
        }

        return valid;
    }

}
