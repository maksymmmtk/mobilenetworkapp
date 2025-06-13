package com.example.mobilenetworkapp.ui.activities;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isSelected;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.mobilenetworkapp.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Before
    public void setUp() {
        ActivityScenario.launch(MainActivity.class);
    }

    @Test
    public void testBottomNavigation_MainSelected() {
        onView(withId(R.id.nav_main)).check(matches(isSelected()));
    }

    @Test
    public void testTariffButtonExists() {
        onView(withId(R.id.buttonTariffs)).check(matches(isDisplayed()));
    }

    @Test
    public void testUIElementsDisplayed() {
        onView(withId(R.id.userNameTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.balance)).check(matches(isDisplayed()));
        onView(withId(R.id.tariff)).check(matches(isDisplayed()));
        onView(withId(R.id.price)).check(matches(isDisplayed()));
        onView(withId(R.id.internetDescription)).check(matches(isDisplayed()));
        onView(withId(R.id.phoneCallsDescription)).check(matches(isDisplayed()));
        onView(withId(R.id.smsDescription)).check(matches(isDisplayed()));
    }
}