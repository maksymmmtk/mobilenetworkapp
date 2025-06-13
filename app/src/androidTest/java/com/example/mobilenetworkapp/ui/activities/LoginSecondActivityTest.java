package com.example.mobilenetworkapp.ui.activities;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.mobilenetworkapp.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginSecondActivityTest {

    private ActivityScenario<LoginSecondActivity> scenario;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        SharedPreferences prefs = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("phone_number", "+380687729776");
        editor.putString("verification_id", "test_verification_id");
        editor.apply();

        Intents.init();
        Intent intent = new Intent(context, LoginSecondActivity.class);
        scenario = ActivityScenario.launch(intent);
    }

    @After
    public void tearDown() {
        Intents.release();
        scenario.close();
    }

    @Test
    public void activityLaunchesCorrectly() {
        onView(withId(R.id.smsCodeField)).check(matches(isDisplayed()));
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
    }

    @Test
    public void loginWithCorrectCode() {
        onView(withId(R.id.smsCodeField)).perform(replaceText("123456"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
    }

    @Test
    public void loginWithEmptyCode() {
        onView(withId(R.id.smsCodeField)).perform(replaceText(""), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
    }
}