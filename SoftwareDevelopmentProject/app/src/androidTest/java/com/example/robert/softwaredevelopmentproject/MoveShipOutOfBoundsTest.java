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
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Robert on 12/5/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MoveShipOutOfBoundsTest {
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

    //Start up new game and get a copy of the game screen for game tests
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

    public void waitFor(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shipMoveOutOfBounds(){
        int moveDist=500;
        int touchStartx = 200, touchStarty = 200;
        GameScreen gs = startUpNewGame();

        gs.touch_start(200, 200);

        waitFor(500);

        for (int i=0; i<=moveDist; i++){
            gs.touch_move(touchStartx-i,touchStarty+i);
            waitFor(10);
        }

        gs.touch_up(touchStartx + moveDist, touchStarty + moveDist);

        gs.moveShipToEdgeOfBoundsTest();

        waitFor(10000);

        if(!(gs.playerShips.get(0).getX()<GameScreen.mapSizex))
            throw new AssertionError();

    }
}
