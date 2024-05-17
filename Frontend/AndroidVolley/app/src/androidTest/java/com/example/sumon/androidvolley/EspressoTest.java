package com.example.sumon.androidvolley;
import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.StringEndsWith.endsWith;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.runner.AndroidJUnitRunner;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class EspressoTest {
    @Rule
    public ActivityScenarioRule<MainActivity> login = new
            ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void login()throws Exception {

        onView(withId(R.id.editTextUsername)).perform(typeText("tc"),closeSoftKeyboard());
        onView(withId(R.id.editTextPassword)).perform(typeText("test"),closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        //wait(500);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.Profile)).perform(click());
    }
    @Test
    public void profileTest() throws Exception{

        onView(withId(R.id.usernameText)).check(matches(withText("Tom")));
        onView(withId(R.id.username)).check(matches(withText("@tc")));
        onView(withId(R.id.firstnameText)).check(matches(withText("Cruise")));
    }
    @Test
    public void profileLogs() throws Exception{
        onView(withId(R.id.horizontalScrollView)).perform(swipeLeft());

    }
    @Test
    public void editProfile(){
        onView(withId(R.id.editUsername)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.editUserBar)).perform(typeText("tc1"),closeSoftKeyboard());
        onView(withId(R.id.userEnter)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.username)).check(matches(withText("@tc1")));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.editUsername)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.editUserBar)).perform(typeText("tc"),closeSoftKeyboard());
        onView(withId(R.id.userEnter)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.username)).check(matches(withText("@tc")));
    }
    @Test
    public void search()throws Exception{
        onView(withId(R.id.search)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.editSearchBar)).perform(typeText("Top Gun Maverick"),closeSoftKeyboard());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.searchEnter)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }

        onView(withId(R.id.image_container1)).perform(click());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }onView(withId(R.id.TitleText)).check(matches(withText("Top Gun: Maverick")));

    }
    @Test
    public void announcementTest(){
        onView(withId(R.id.notifications)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.textViewAnn)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.titleText)).perform(typeText("test announcement"));
        onView(withId(R.id.bodyText)).perform(typeText("testing testing 123"));
        onView(withId(R.id.postButton)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.ann_container)).perform(click());
    }
}
