package com.example.musicqueue;

import com.example.musicqueue.utilities.FormUtils;

import org.junit.Test;

import static org.junit.Assert.*;

public class FormUtilTest {

    String emailTrue = "hbugajski@gmail.com";
    String emailFalse = "hbugajskigmail.com";

    String passTrue = "password123";
    String passFalse = "pass";

    /* ---------- OLD TEST CASES FROM SPRINT 2 ---------- */

    /**
     * OLD TEST CASE FROM SPRINT 2
     *
     * tests that the validate email functions works properly
     */
    @Test
    public void testValidateEmail() {
        assertTrue(FormUtils.validateEmail(emailTrue));
        assertFalse(FormUtils.validateEmail(emailFalse));
    }

    /**
     * OLD TEST CASE FROM SPRINT 2
     *
     * tests that the validate password function works properly
     */
    @Test
    public void testValidatePassword() {
        assertTrue(FormUtils.validatePassword(passTrue));
        assertFalse(FormUtils.validatePassword(passFalse));
    }

    /**
     * OLD TEST CASE FROM SPRINT 2
     *
     * tests that the validate form fucntion works properly
     */
    @Test
    public void testValidateForm() {
        assertTrue(FormUtils.validateEmailPassForm(emailTrue, passTrue));
        assertFalse(FormUtils.validateEmailPassForm(emailTrue, passFalse));
        assertFalse(FormUtils.validateEmailPassForm(emailFalse, passTrue));
        assertFalse(FormUtils.validateEmailPassForm(emailFalse, passFalse));
    }
}
