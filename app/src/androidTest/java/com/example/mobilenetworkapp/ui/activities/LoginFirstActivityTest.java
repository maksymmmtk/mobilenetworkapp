package com.example.mobilenetworkapp.ui.activities;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import com.example.mobilenetworkapp.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginFirstActivityTest {

    @Rule
    public GrantPermissionRule permissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.INTERNET);

    private ActivityScenario<LoginFirstActivity> scenario;

    @Before
    public void setUp() {
        scenario = ActivityScenario.launch(LoginFirstActivity.class);
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
        scenario.close();
    }

    @Test
    public void activityLaunchesCorrectly() {
        onView(withId(R.id.editPhoneNumber)).check(matches(isDisplayed()));
        onView(withId(R.id.exitButton)).check(matches(isDisplayed()));
    }

    @Test
    public void whenPhoneFieldIsEmpty_thenShowToast() {
        onView(withId(R.id.editPhoneNumber)).perform(replaceText(""), closeSoftKeyboard());
        onView(withId(R.id.exitButton)).perform(click());
    }

    @Test
    public void whenPhoneIsEntered_thenStartVerification() {
        onView(withId(R.id.editPhoneNumber)).perform(replaceText("+380687729776"), closeSoftKeyboard());
        onView(withId(R.id.exitButton)).perform(click());
    }
}