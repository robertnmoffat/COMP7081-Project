package com.example.robert.softwaredevelopmentproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

    }

    public void startGame(View view){
        Intent intent = new Intent(this, NewGameActivity.class);
        startActivity(intent);
    }

    public void loadGame(View view){
        Intent intent = new Intent(this, LoadGameActivity.class);
        startActivity(intent);
    }
}
