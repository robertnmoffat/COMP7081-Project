package com.example.robert.softwaredevelopmentproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {
    Handler h = new Handler();

    static GameScreen gs;

    boolean exitButtonIsSetup = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game);

        FullScreencall();

        gs = (GameScreen)findViewById(R.id.gameScreenView);

        h.post(timedTask);
    }

    public void FullScreencall() {
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public Runnable timedTask = new Runnable()
    {

        @Override
        public void run()
        {
            if(gs.gameIsOver&&!exitButtonIsSetup){
                exitButtonIsSetup = true;
                setupExitButton();
            }
            initGfx();

            //delay for the runnable
            h.postDelayed(timedTask, 16);
        }
    };

    public void setupExitButton(){
        Button theButton = new Button(this);
        theButton.setVisibility(View.VISIBLE);
        theButton.setBackgroundColor(Color.TRANSPARENT);
        theButton.setWidth(gs.getWidth());
        theButton.setHeight(gs.getHeight());

        theButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRewards(v);
            }
        });
        LinearLayout ll = (LinearLayout)findViewById(R.id.gameLayout);
        ll.addView(theButton);

    }

    public void initGfx(){
        GameScreen gs = (GameScreen)findViewById(R.id.gameScreenView);
        gs.invalidate();
        TextView shipNameTextView = (TextView)findViewById(R.id.shipName_text);
        if(gs.gameController.getSelectedShip()!=null) {
            shipNameTextView.setText(ShipDictionary.getShipName(gs.gameController.getSelectedShip().getType()));
        }else{
            shipNameTextView.setText("None Selected");
        }
    }

    public void goToRewards(View view){
        Intent intent = new Intent(this, RewardActivity.class);
        startActivity(intent);
    }

    public void nextShipButton(View view){
        gs = (GameScreen)findViewById(R.id.gameScreenView);
        gs.gameController.selectNextShip();
    }

    public void previousShipButton(View view){
        gs = (GameScreen)findViewById(R.id.gameScreenView);
        gs.gameController.selectPreviousShip();
    }

    public void unselectShipButton(View view){
        gs = (GameScreen)findViewById(R.id.gameScreenView);
        gs.selectNone();
    }

    public static GameScreen getGameScreen(){
        return gs;
    }

    public static GameController getGameController(){
        return gs.gameController;
    }
}
