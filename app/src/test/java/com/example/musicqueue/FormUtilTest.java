package com.example.musicqueue;


import com.example.musicqueue.utilities.FormUtils;

import org.junit.Test;

import static org.junit.Assert.*;

public class FormUtilTest {

    @Test
    public void testValidatePasswordTrue() {
        String password = "password123";

        assertTrue(FormUtils.validatePassword(password));
    }

    @Test
    public void testValidatePasswordFalse() {
        String password = "pass";

        assertFalse(FormUtils.validatePassword(password));
    }
}
