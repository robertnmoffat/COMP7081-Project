package com.example.robert.softwaredevelopmentproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dingus on 11/7/2016.
 */
public class GameScreen extends View{
    private Paint p;
    int rotation =0;

    public GameScreen(Context context, AttributeSet aSet){
        super(context, aSet);
        p = new Paint();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        rotation+=10;

        p.setColor(Color.BLACK);

        p.setAlpha(255);

        p.setStrokeWidth(1);

        canvas.drawRect(0, 0, getWidth(), getHeight(), p);

        p.setColor(Color.BLUE);
        p.setTextSize(100);
        canvas.rotate(rotation,getWidth()/2,getHeight()/2);

        canvas.drawText("Game Canvas", getWidth()/2,getHeight()/2,p);
    }

}
