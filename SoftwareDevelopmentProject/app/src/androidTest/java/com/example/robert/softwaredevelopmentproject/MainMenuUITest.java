package com.example.robert.softwaredevelopmentproject;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Robert on 12/1/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainMenuUITest {

    @Rule
    public ActivityTestRule<MainActivity> sUActivityRule = new ActivityTestRule<>(MainActivity.class, false, false);

    /**
     * Sets up the intent and starts activity
     */
    public void setUpIntent(){
        Intent intent = new Intent();

        sUActivityRule.launchActivity(intent);
    }

    @Test
    public void newGameButtonTest(){
        setUpIntent();

        onView(withId(R.id.newGameButton)).perform(click()).check(doesNotExist());
        onView(withId(R.id.newGameCancelButton)).perform(click()).check(doesNotExist());
        onView(withId(R.id.newGameButton)).check(matches(isDisplayed()));
    }

    @Test
    public void loadGameButtonTest(){
        setUpIntent();

        onView(withId(R.id.loadGameButton)).perform(click()).check(doesNotExist());
        onView(withId(R.id.loadGameCancelButton)).perform(click()).check(doesNotExist());
        onView(withId(R.id.loadGameButton)).check(matches(isDisplayed()));
    }

    @Test
    public void optionsGameButtonTest(){
        setUpIntent();

        onView(withId(R.id.optionsButton)).perform(click()).check(doesNotExist());
    }
}
