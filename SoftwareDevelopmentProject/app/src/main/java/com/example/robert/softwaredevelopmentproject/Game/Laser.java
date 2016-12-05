package com.example.robert.softwaredevelopmentproject.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.robert.softwaredevelopmentproject.GameFunctions;
import com.example.robert.softwaredevelopmentproject.GameScreen;
import com.example.robert.softwaredevelopmentproject.R;

/**
 * Created by dingus on 11/27/2016.
 */
public class Laser extends GameObject{
    int age = 2000;

    public Laser(float x, float y, Context context){
        super(x,y,0);
        Bitmap laserGraphic = BitmapFactory.decodeResource(context.getResources(), R.drawable.laser);
        setGraphic(laserGraphic);
    }

    public void updatePosition(){
        super.updatePosition();

        age-=1;

        if(age==0)setAlive(false);

        GameObject collided=GameFunctions.checkForCollision(x,y,graphic.getWidth(),graphic.getHeight());
        if(collided!=null){
            if(collided.team>0)collided.doDamage(1);
            removeObject();
        }
    }
}
