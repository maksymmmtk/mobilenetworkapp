package com.example.mobilenetworkapp.ui.activities;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import android.app.Activity;

import androidx.test.espresso.matcher.RootMatchers;
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
public class TariffsActivityTest {

    @Rule
    public ActivityScenarioRule<TariffsActivity> activityRule =
            new ActivityScenarioRule<>(TariffsActivity.class);

    @Test
    public void checkRecyclerViewIsDisplayed() {
        onView(withId(R.id.rvTariffs)).check(matches(isDisplayed()));
    }

    @Test
    public void clickPrepaidButton_filtersList() {
        onView(withId(R.id.prepaidButton)).perform(click());
    }

    @Test
    public void clickContractButton_filtersList() {
        onView(withId(R.id.contractButton)).perform(click());
    }

    @Test
    public void clickExportButton_withEmptyList_showsToast() {
        onView(withId(R.id.btnExportTariffs)).perform(click());
        onView(withText("Список тарифів порожній"))
                .inRoot(RootMatchers.withDecorView(not(is(getActivityInstance().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
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
