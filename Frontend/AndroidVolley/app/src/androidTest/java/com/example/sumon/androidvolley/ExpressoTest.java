package com.example.sumon.androidvolley;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.example.sumon.androidvolley.app.AppController;
import com.example.sumon.androidvolley.utils.Const;
import com.example.sumon.androidvolley.utils.UserInfo;
import com.example.sumon.androidvolley.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExpressoTest {

    String expected_user = "bdm1024";
    String expected_pass = "pass";

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Test
    public void mainPage()
    {
        onView(withId(R.id.editTextUsername)).check(matches(isDisplayed()));
        onView(withId(R.id.editTextUsername)).perform(click());
        onView(withId(R.id.editTextUsername)).perform(typeText(expected_user));
        onView(withId(R.id.editTextPassword)).check(matches(isDisplayed()));
        onView(withId(R.id.editTextPassword)).perform(click());
        onView(withId(R.id.editTextPassword)).perform(typeText(expected_pass));

        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());

        onView(withId(R.id.loginButton)).perform(click());

        ActivityScenario.launch(HomeActivity.class);

        onView(withId(R.id.home)).check(matches(isDisplayed()));

        onView(withId(R.id.Profile)).check(matches(isDisplayed()));
        onView(withId(R.id.Profile)).perform(click());

        onView(withId(R.id.list_Container)).check(matches(isDisplayed()));
        


    }

}
