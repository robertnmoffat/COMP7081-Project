package com.example.robert.softwaredevelopmentproject.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.robert.softwaredevelopmentproject.GameFunctions;
import com.example.robert.softwaredevelopmentproject.R;

/**
 * Created by dingus on 11/28/2016.
 */
public class Explosion extends GameObject{
    float totalAge = 100;
    float age = totalAge;

    Bitmap[] animation = new Bitmap[5];

    public Explosion(float x, float y, int team, Context context) {
        super(x, y, team);
        animation[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion1);
        animation[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion2);
        animation[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion3);
        animation[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion4);
        animation[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion5);

        setGraphic(animation[0]);
    }

    public void updatePosition(){
        super.updatePosition();

        age-=1;

        float firstAge = (float)age*0.8f;

        if(age>totalAge*0.8f)
            setGraphic(animation[0]);
        else if(age>totalAge*0.6f)
            setGraphic(animation[1]);
        else if(age>totalAge*0.4f)
            setGraphic(animation[2]);
        else if(age>totalAge*0.2f)
            setGraphic(animation[1]);
        else if(age>0f)
            setGraphic(animation[0]);


        if(age==0)setAlive(false);
    }
}
