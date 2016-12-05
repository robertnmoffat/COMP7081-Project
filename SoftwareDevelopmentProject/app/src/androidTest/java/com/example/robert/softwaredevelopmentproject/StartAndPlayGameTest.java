package com.example.robert.softwaredevelopmentproject;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getContext;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by dingus on 11/28/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class StartAndPlayGameTest {

    //All valid strings in this activity can contain spaces
    public static final String TEST_FILENAME = "TestFilename";

    @Rule
    public ActivityTestRule<MainActivity> sUActivityRule = new ActivityTestRule<>(MainActivity.class, false, false);

    @Rule
    public ActivityTestRule<GameActivity> gameActivityRule = new ActivityTestRule<>(GameActivity.class, false, false);

    /**
     * Sets up the intent and starts activity
     */
    public void setUpIntent(){
        Intent intent = new Intent();

        sUActivityRule.launchActivity(intent);
    }

    public GameScreen startUpNewGame(){
        setUpIntent();

        onView(withId(R.id.newGameButton)).perform(click()).check(doesNotExist());//press new game

        onView(withId(R.id.fileNameId)).perform(typeText(TEST_FILENAME), closeSoftKeyboard());//type filename for test game
        onView(withId(R.id.newGameContinueButton)).perform(click()).check(doesNotExist());//press continue button, check that it has moved to new screen

        onView(withId(999)).perform(click());
        onView(withId(999)).perform(click());
        onView(withId(999)).perform(click());

        onView(withId(R.id.startGameButton)).perform(click()).check(doesNotExist());//begin game, check that it doesnt exist anymore

        Intent intent = new Intent();
        gameActivityRule.launchActivity(intent);

        GameScreen gs = (GameScreen)gameActivityRule.getActivity().findViewById(R.id.gameScreenView);//get a copy of the game's screen
        return gs;
    }


    @Test
    public void unitMoveTest(){
        GameScreen gs = startUpNewGame();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //onView(withId(R.id.next_button)).perform(click());
        gs.selectNextShip();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        gs.fakeTouch(200,200);//fake a user touch on the screen
        assert !gs.shipIsAtTargetAngle();

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assert gs.shipIsAtTargetAngle();
    }

}
