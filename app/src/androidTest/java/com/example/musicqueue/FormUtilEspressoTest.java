package com.example.musicqueue;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.musicqueue.authentication.EmailPasswordActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class FormUtilEspressoTest {

    @Rule
    public ActivityTestRule<EmailPasswordActivity> activityRule =
            new ActivityTestRule<>(EmailPasswordActivity.class);

    @Test
    public void testValidateEmailPassForm() {

        String email = "hbugajski@gmail.com";
        String password = "password123";

        //Espresso.onView(ViewMatchers.withId(R.id.email_text_input)).perform(ViewActions.typeText(email));
        //Espresso.onView(ViewMatchers.withId(R.id.password_text_input)).perform(ViewActions.typeText(password));

    }
}
