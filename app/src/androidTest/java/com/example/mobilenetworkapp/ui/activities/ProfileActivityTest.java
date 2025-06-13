package com.example.mobilenetworkapp.ui.activities;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.app.Activity;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import com.example.mobilenetworkapp.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;

@RunWith(AndroidJUnit4.class)
public class ProfileActivityTest {

    @Rule
    public ActivityScenarioRule<ProfileActivity> activityRule =
            new ActivityScenarioRule<>(ProfileActivity.class);

    @Test
    public void checkProfileFieldsAreDisplayed() {
        onView(withId(R.id.name)).check(matches(isDisplayed()));
        onView(withId(R.id.surname)).check(matches(isDisplayed()));
        onView(withId(R.id.patronymic)).check(matches(isDisplayed()));
        onView(withId(R.id.userPhoneNumber)).check(matches(isDisplayed()));
        onView(withId(R.id.userTariff)).check(matches(isDisplayed()));
        onView(withId(R.id.tariffConnectionDate)).check(matches(isDisplayed()));
    }

    @Test
    public void clickStopTariffButton_updatesUI() {
        onView(withId(R.id.stopTariffButton)).perform(click());
    }

    @Test
    public void clickExitButton_startsLoginActivity() {
        onView(withId(R.id.exitButton)).perform(click());
    }

    @Test
    public void checkBottomNavigationInteraction() {
        onView(withId(R.id.nav_main)).perform(click());
        onView(withId(R.id.nav_tariffs)).perform(click());
        onView(withId(R.id.nav_profile)).perform(click());
    }

    private Activity getActivityInstance() {
        final Activity[] currentActivity = new Activity[1];
        getInstrumentation().runOnMainSync(() -> {
            Collection<Activity> resumedActivities = ActivityLifecycleMonitorRegistry.getInstance()
                    .getActivitiesInStage(Stage.RESUMED);
            if (!resumedActivities.isEmpty()) {
                currentActivity[0] = resumedActivities.iterator().next();
            }
        });
        return currentActivity[0];
    }
}