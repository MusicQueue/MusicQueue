package com.example.musicqueue;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.musicqueue.authentication.EmailPasswordActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class EmaillPasswordInstrumentedTest {

    @Rule
    public ActivityTestRule<EmailPasswordActivity> activityRule =
            new ActivityTestRule<>(EmailPasswordActivity.class);

    /*@Test
    public void testEmailPassSignIn() {
        Espresso.onView(withId(R.id.email_text_layout))
                .perform(typeText("hjbugajski@crimson.ua.edu"));
        Espresso.onView(withId(R.id.password_text_layout))
                .perform(typeText("musicQUEUE"));
        Espresso.onView(withId(R.id.sign_in_button)).perform(click());

        assertTrue(activityRule.getActivity().isFinishing());
    }*/

    @Test
    public void testStartForgotPassAct() {
        Espresso.onView(withId(R.id.forgot_password_text_view)).perform(click());


    }
}
