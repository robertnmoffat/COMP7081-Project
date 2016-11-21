package com.example.robert.softwaredevelopmentproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class NewGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
    }

    public void continueToLoadout(View view){
        setupInitialShips();

        createInitialSaveFile();

        Intent intent = new Intent(this, LoadOutMenu.class);
        startActivity(intent);
    }

    public void cancelToMain(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void setupInitialShips(){
        int[] ships = ShipDictionary.getStartingShipList();
        for(int i=0; i<ships.length; i++){
            PlayerData.addShip(i,ships[i]);
        }
    }

    public void createInitialSaveFile(){
        String fileName = ((EditText)findViewById(R.id.fileNameId)).getText().toString();

        GameFunctions.save(this, fileName);
    }
}
