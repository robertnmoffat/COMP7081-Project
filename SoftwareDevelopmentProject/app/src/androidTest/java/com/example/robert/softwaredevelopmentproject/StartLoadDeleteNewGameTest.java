package com.example.robert.softwaredevelopmentproject;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.support.test.espresso.ViewAssertion;
/**
 * Created by dingus on 25/05/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class StartLoadDeleteNewGameTest {

    public static final String TEST_FILENAME = "TestFilename";

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
    public void a_StartNewGame(){
        setUpIntent();

        onView(withId(R.id.newGameButton)).perform(click()).check(doesNotExist());//press new game

        onView(withId(R.id.fileNameId)).perform(typeText(TEST_FILENAME), closeSoftKeyboard());//type filename for test game
        onView(withId(R.id.newGameContinueButton)).perform(click()).check(doesNotExist());//press continue button, check that it has moved to new screen
    }

    @Test
    public void b_LoadTestGame(){
        setUpIntent();

        onView(withId(R.id.loadGameButton)).perform(click()).check(doesNotExist());
        onView(withId(999)).perform(click()).check(matches(isDisplayed()));
        onView(withId(R.id.loadAndStartButton)).perform(click()).check(doesNotExist());
        onView(withId(R.id.startGameButton)).check(matches(isDisplayed()));
    }

    @Test
    public void c_DeleteTestGame(){
        setUpIntent();

        onView(withId(R.id.loadGameButton)).perform(click()).check(doesNotExist());//press load game button and check that activity has moved

        onView(withText(TEST_FILENAME+".sav")).perform(click());
        onView(withId(R.id.deleteButton)).perform(click());
        onView(withText(TEST_FILENAME+".sav")).check(doesNotExist());
    }
}