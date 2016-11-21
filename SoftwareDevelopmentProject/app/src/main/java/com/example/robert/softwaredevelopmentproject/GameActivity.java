package com.example.robert.softwaredevelopmentproject;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class GameActivity extends AppCompatActivity {
    Handler h = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game);

        h.post(timedTask);

    }

    public Runnable timedTask = new Runnable()
    {

        @Override
        public void run()
        {
            initGfx();

            //delay for the runnable
            h.postDelayed(timedTask, 250);
        }
    };

    public void initGfx(){
        GameScreen gs = (GameScreen)findViewById(R.id.gameScreenView);
        gs.invalidate();
    }

    public void goToRewards(View view){
        Intent intent = new Intent(this, RewardActivity.class);
        startActivity(intent);
    }
}
