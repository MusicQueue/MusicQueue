package com.example.musicqueue;


import com.example.musicqueue.utilities.FormUtils;

import org.junit.Test;

import static org.junit.Assert.*;

public class FormUtilTest {

    String emailTrue = "hbugajski@gmail.com";
    String emailFalse = "hbugajskigmail.com";

    String passTrue = "password123";
    String passFalse = "pass";

    @Test
    public void testValidatePassword() {
        assertTrue(FormUtils.validatePassword(passTrue));
        assertFalse(FormUtils.validatePassword(passFalse));
    }

    @Test
    public void testValidateForm() {
        assertTrue(FormUtils.validateEmailPassForm(emailTrue, passTrue));
        assertFalse(FormUtils.validateEmailPassForm(emailTrue, passFalse));
        assertFalse(FormUtils.validateEmailPassForm(emailFalse, passTrue));
        assertFalse(FormUtils.validateEmailPassForm(emailFalse, passFalse));
    }
}
