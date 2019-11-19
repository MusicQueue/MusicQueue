package com.example.musicqueue;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.musicqueue.authentication.EmailPasswordActivity;
import com.example.musicqueue.utilities.FormUtils;
import com.google.android.material.textfield.TextInputLayout;

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

        TextInputLayout emailTIL = activityRule.getActivity().emailTIL;
        TextInputLayout passTIL = activityRule.getActivity().passwordTIL;

        //Espresso.onView(ViewMatchers.withId(R.id.email_text_input)).perform(ViewActions.typeText(email));


        assertTrue(FormUtils.validateEmailPassForm(email, password, emailTIL, passTIL));
    }
}
