package com.example.robert.softwaredevelopmentproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RewardActivity extends AppCompatActivity {
    private int[] reward = {2,3,1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);

        LinearLayout rewardView = (LinearLayout)findViewById(R.id.reward_linearLayout);

        for(int i=0; i<reward.length; i++){
            PlayerData.addShip(i,reward[i]);

            for(int j=0; j<reward[i]; j++) {

                LinearLayout ll = new LinearLayout(this);
                ll.setOrientation(LinearLayout.HORIZONTAL);

                TextView tv = new TextView(this);
                tv.setText(ShipDictionary.getShipName(i));

                ll.addView(tv);

                View view = new View(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                params.weight = 1.0f;
                view.setLayoutParams(params);

                ll.addView(view);

                Button button2 = new Button(this);
                button2.setText("?");
                button2.setLayoutParams(new LinearLayout.LayoutParams(120, 120));

                ll.addView(button2);

                rewardView.addView(ll);
            }
        }

        GameFunctions.save(this, PlayerData.getSaveName());
    }

    public void gotoLayoutMenu(View view){
        Intent intent = new Intent(this, LoadOutMenu.class);
        startActivity(intent);
    }
}
