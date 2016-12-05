package com.example.robert.softwaredevelopmentproject;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LoadOutMenu extends AppCompatActivity{
    private ArrayList<GuiShip> shipsChosen;
    private ArrayList<GuiShip> shipsOwned;

    private int totalPoints = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_load_out_menu);

        shipsOwned = new ArrayList<GuiShip>();
        shipsChosen = new ArrayList<GuiShip>();

        int[] shipOwnedArray = PlayerData.getShipArray();

        for(int i=0;i<shipOwnedArray.length; i++){
            for(int j=0; j<shipOwnedArray[i]; j++){
                String name = ShipDictionary.getShipName(i);
                int img = ShipDictionary.getShipImage(i);
                int value = ShipDictionary.getShipValue(i);

                shipsOwned.add(new GuiShip(name,img,value));
            }
        }
    }

    protected void onStart(){
        super.onStart();

        refreshLists();
    }

    public void refreshLists(){
        totalPoints = 0;
        int currentPoints;

        LinearLayout sv = (LinearLayout)findViewById(R.id.svLinearLayout);
        sv.removeAllViews();

        TextView title = new TextView(this);
        title.setText("Ready");
        title.setGravity(Gravity.CENTER);
        sv.addView(title);

        int buttonCount =0;

        for(final GuiShip ship: shipsChosen){
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.HORIZONTAL);

            TextView tv = new TextView(this);
            tv.setText(ship.getName());

            ll.addView(tv);

            View view = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.weight = 1.0f;
            view.setLayoutParams(params);

            ll.addView(view);

            TextView valueText = new TextView(this);
            currentPoints = ship.getValue();
            valueText.setText(""+currentPoints);
            totalPoints+=currentPoints;

            ll.addView(valueText);

            Button button = new Button(this);
            button.setText("-");
            int id = 888+buttonCount++;
            button.setId(id);
            button.setLayoutParams(new LinearLayout.LayoutParams(120, 120));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shipsOwned.add(ship);
                    shipsChosen.remove(ship);
                    refreshLists();
                }
            });

            ll.addView(button);

            Button button2 = new Button(this);
            button2.setText("?");
            button2.setLayoutParams(new LinearLayout.LayoutParams(120, 120));

            ll.addView(button2);

            sv.addView(ll);
        }

        LinearLayout sv2 = (LinearLayout)findViewById(R.id.ownedShipsLinearLayout);
        sv2.removeAllViews();

        TextView title2 = new TextView(this);
        title2.setText("Owned");
        title2.setGravity(Gravity.CENTER);
        sv2.addView(title2);

        buttonCount=0;

        for(final GuiShip ship: shipsOwned) {
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.HORIZONTAL);

            TextView tv = new TextView(this);
            tv.setText(ship.getName());

            ll.addView(tv);

            View view = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.weight = 1.0f;
            view.setLayoutParams(params);

            ll.addView(view);

            TextView valueText = new TextView(this);
            valueText.setText("" + ship.getValue());
            ll.addView(valueText);

            Button button = new Button(this);
            button.setText("+");
            int id = 999+buttonCount++;
            button.setId(id);
            button.setLayoutParams(new LinearLayout.LayoutParams(120, 120));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    shipsChosen.add(ship);
                    shipsOwned.remove(ship);
                    refreshLists();
                }
            });

            ll.addView(button);

            Button button2 = new Button(this);
            button2.setText("?");
            button2.setLayoutParams(new LinearLayout.LayoutParams(120, 120));

            ll.addView(button2);

            sv2.addView(ll);
        }

        TextView pointsTextView = (TextView)findViewById(R.id.pointsTextView);
        if(totalPoints>100)pointsTextView.setTextColor(Color.RED);
        else pointsTextView.setTextColor(Color.BLACK);
        pointsTextView.setText(""+totalPoints+"/100");

    }

    public void startGameActivity(View view){
        if(totalPoints<=100) {
            GameFunctions.setLoadoutShipList(shipsChosen);
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        }
    }
}
