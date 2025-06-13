package com.example.mobilenetworkapp.ui.adapters;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.os.IBinder;
import android.view.WindowManager;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Root;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.mobilenetworkapp.R;
import com.example.mobilenetworkapp.ui.activities.TariffsActivity;

import org.hamcrest.Description;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TariffAdapterTest {


    @Test
    public void testTariffAdapter_showsTariffs_andHandlesClick() {
        ActivityScenario<TariffsActivity> scenario = ActivityScenario.launch(TariffsActivity.class);

        // Перевіряємо, що RecyclerView показується
        onView(withId(R.id.rvTariffs)).check(matches(isDisplayed()));

        // Очікуємо, що є кнопка "Вибрати" та натискаємо її (перша кнопка в списку)
        onView(allOf(withId(R.id.chooseButton), isDisplayed())).perform(click());

        // Перевірка, що показався Toast про обраний тариф
        onView(withText(Matchers.containsString("Тариф")))
                .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    public static class ToastMatcher extends TypeSafeMatcher<Root> {
        @Override
        public void describeTo(Description description) {
            description.appendText("is toast");
        }

        @Override
        public boolean matchesSafely(Root root) {
            int type = root.getWindowLayoutParams().get().type;
            if (type == WindowManager.LayoutParams.TYPE_TOAST) {
                IBinder windowToken = root.getDecorView().getWindowToken();
                IBinder appToken = root.getDecorView().getApplicationWindowToken();
                return windowToken == appToken;
            }
            return false;
        }
    }
}