package com.example.robert.softwaredevelopmentproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.robert.softwaredevelopmentproject.Game.Explosion;
import com.example.robert.softwaredevelopmentproject.Game.GameObject;
import com.example.robert.softwaredevelopmentproject.Game.GameShip;
import com.example.robert.softwaredevelopmentproject.Game.Laser;

import android.graphics.Matrix;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dingus on 11/7/2016.
 */
public class GameScreen extends View{

    int touchTimer = 0;
    private Paint p;

    boolean gameIsOver = false;

    String gameText = "";


    Context context;



    float dragPosx=-1,dragPosy=-1;

    Bitmap background;
    Bitmap backgroundPlanet;
    Bitmap pauseButton;
    Bitmap playButton;

    public static float mapSizex, mapSizey;

    //public static GameObject[][] shipGrid = new GameShip[(int)mapSizex][(int)mapSizey];


    float displacementx=0, displacementy=0;
    float touchStartx,touchStarty;

    public GameController gameController;

    public GameScreen(Context context, AttributeSet aSet){
        super(context, aSet);
        p = new Paint();
        this.context = context;

        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.star_background);
        backgroundPlanet = BitmapFactory.decodeResource(context.getResources(), R.drawable.blue_planet);
        pauseButton = BitmapFactory.decodeResource(context.getResources(), R.drawable.pause_button);
        playButton = BitmapFactory.decodeResource(context.getResources(), R.drawable.play_button);

        mapSizex = background.getWidth()*2;
        mapSizey = background.getHeight()*2;

        gameController = new GameController(context, background.getWidth(), background.getHeight());
    }


    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up(x, y);
                invalidate();
                break;
        }
        return true;
    }

    public void touch_start(float x, float y) {
        touchTimer = 0;
        GameShip selectedShip = gameController.getSelectedShip();

        touchStartx = x-displacementx;
        touchStarty = y-displacementy;
    }

    public void touch_move(float x, float y) {
        GameShip selectedShip = gameController.getSelectedShip();

//        if(selectedShip!=null&&touchTimer<=15){
//            dragPosx = x;
//            dragPosy = y;
//            return;
//        }

        if(x-touchStartx<0&&x-touchStartx>-(mapSizex-getWidth())) {
            displacementx = x - touchStartx;
        }
        if(y-touchStarty<0&&y-touchStarty>-(mapSizey-getHeight())) {
            displacementy = y - touchStarty;
        }

    }

    public void touch_up(float x, float y) {
        if(isPressOnPauseButton(x,y)){
            gameController.togglePauseGame();
            return;
        }

        if(touchTimer<=15) {
            System.out.println("Tapped");

            if(gameController.isPressOnOwnedShip(x,y, displacementx, displacementy)){
                System.out.println("SHIP PRESSED");
                GameShip selectedShip = gameController.getSelectedShip();
                dragPosx = selectedShip.getTargetx();
                dragPosy = selectedShip.getTargety();
                return;
            }

            GameShip selectedShip = gameController.getSelectedShip();

            if (selectedShip != null) {
                dragPosx = x- displacementx;
                dragPosy = y- displacementy;
                //return;

                selectedShip.setTargetx(x - displacementx);
                selectedShip.setTargety(y - displacementy);
                selectedShip.setHasTargetPosition(true);
                return;
            }
        }
    }

    private boolean isPressOnPauseButton(float x, float y){
        return x>getWidth()-pauseButton.getWidth()-10&&y< 10+pauseButton.getHeight();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        touchTimer++;

        super.onDraw(canvas);

        //Run game iteration through game logic
        gameController.runGameIteration();

        clearScreen(canvas);

        drawBackground(canvas);

        drawGameObjects(canvas);

        drawShipSelectionGraphics(canvas);

        checkEndGameConditions();

        updateInterfaceGraphics(canvas);

        drawGameText(canvas);
    }

    public void clearScreen(Canvas canvas){
        p.setColor(Color.BLACK);

        p.setAlpha(255);
        p.setStyle(Paint.Style.FILL);

        p.setStrokeWidth(1);

        canvas.drawRect(0, 0, getWidth(), getHeight(), p);
    }

    public void drawBackground(Canvas canvas){
        //Repeat image four times to cover whole game
        canvas.drawBitmap(background, displacementx, displacementy, p);
        canvas.drawBitmap(background, background.getWidth() + displacementx, displacementy, p);
        canvas.drawBitmap(background, displacementx, +background.getHeight() + displacementy, p);
        canvas.drawBitmap(background, background.getWidth() + displacementx, +background.getHeight() + displacementy, p);

        canvas.drawBitmap(backgroundPlanet, mapSizex / 4 + displacementx, mapSizey / 4 + displacementy, p);

        //Testing crap I think
        p.setColor(Color.BLUE);
        canvas.drawLine(mapSizex + displacementx, 0 + displacementy, mapSizex + displacementx, mapSizey + displacementy, p);
    }

    public void drawGameObjects(Canvas canvas){
        drawObjectList(gameController.getObjectList(ObjectList.PLAYERSHIPS), canvas);
        drawObjectList(gameController.getObjectList(ObjectList.ENEMYSHIPS), canvas);
        drawObjectList(gameController.getObjectList(ObjectList.GAMEOBJECTS), canvas);
    }

    public void drawShipSelectionGraphics(Canvas canvas){
        //Get currently selected ship
        GameShip selectedShip = gameController.getSelectedShip();

        if(selectedShip!=null&&selectedShip.isAlive()) {
            float x = selectedShip.getX();
            float y = selectedShip.getY();
            float width = selectedShip.getGraphic().getWidth();
            float height = selectedShip.getGraphic().getHeight();
            float radius;

            if (dragPosx >= 0 && dragPosy >= 0 &&selectedShip.hasTargetPosition()) {
                p.setStyle(Paint.Style.STROKE);

                if(selectedShip.isHasEnemyTarget()){
                    if(GameFunctions.isUnderRange(dragPosx,dragPosy,selectedShip.getEnemyTarget().getX()+displacementx,selectedShip.getEnemyTarget().getY()+displacementy, 100)) {
                        dragPosx = selectedShip.getEnemyTarget().getX() + displacementx;
                        dragPosy = selectedShip.getEnemyTarget().getY() + displacementy;
                    }
                }

                //GameObject selected = GameFunctions.checkForCollision(dragPosx-displacementx,dragPosy-displacementy,100,100);
                GameObject selected = GameFunctions.checkForCollisionsAccurate(dragPosx,dragPosy,50, gameController.enemyShips);
                if(selected!=null) {
                    selectedShip.setEnemyTarget((GameShip)selected);
                    selectedShip.setHasEnemyTarget(true);
                    p.setColor(Color.RED);
                    dragPosx=selected.getX();
                    dragPosy=selected.getY();
                }
                else {
                    selectedShip.setEnemyTarget(null);
                    selectedShip.setHasEnemyTarget(false);
                    p.setColor(Color.BLUE);
                }


                canvas.drawLine(x + displacementx, y + displacementy, dragPosx+ displacementx, dragPosy+ displacementy, p);
                canvas.drawCircle(dragPosx+ displacementx, dragPosy+ displacementy, 20, p);
            }

            if (width > height)
                radius = width;
            else
                radius = height;

            p.setStyle(Paint.Style.STROKE);
            p.setColor(Color.BLUE);
            canvas.drawCircle(x + displacementx, y + displacementy, radius, p);
        }
    }

    public void updateInterfaceGraphics(Canvas canvas){
        if(gameController.isPaused()){
            canvas.drawBitmap(playButton, getWidth()-pauseButton.getWidth()-10, 10, p);
        }else{
            canvas.drawBitmap(pauseButton, getWidth()-pauseButton.getWidth()-10, 10, p);
        }
    }

    public void drawGameText(Canvas canvas){
        p.setColor(Color.GREEN);
        p.setTextSize(100);
        canvas.drawText(gameText, getWidth()/4,getHeight()/4,p);
    }

    public void checkEndGameConditions(){
        //If list of player ships is empty, you have lost the battle and the game is over.
        if(gameController.getObjectList(ObjectList.PLAYERSHIPS).size()==0){
            gameText="Battle Lost";
            gameIsOver=true;
        }
        //If the list of enemy ships is empty, you have won the battle and the game is over.
        if(gameController.getObjectList(ObjectList.ENEMYSHIPS).size()==0){
            gameText="Battle Won";
            gameIsOver=true;
        }
    }

    public void drawObjectList(ArrayList<GameObject> list, Canvas canvas){
        //draw all objects
        for(GameObject currentObject: list){
            canvas.drawBitmap(currentObject.getGraphic(), rotateByMatrix(currentObject), null);
        }
    }

    public Matrix rotateByMatrix(GameObject object) {
        Bitmap source = object.getGraphic();
        float angle = object.getAngle()+90;
        int x=(int) object.getX();
        int y = (int) object.getY();

        Matrix matrix = new Matrix();
        matrix.postTranslate(-source.getWidth() / 2, -source.getHeight() / 2);
        matrix.postRotate(angle);
        matrix.postTranslate(source.getWidth() / 2 + x + displacementx - object.getCenterx(), source.getHeight() / 2 + y + displacementy - object.getCentery());
        return matrix;
    }

    public void selectNone(){
        dragPosx=-1;
        dragPosy=-1;
        gameController.setSelectedShip(null);
    }

    public void fakeUnitMoveTouchTest(float x, float y) {

        touch_start(x, y);
        touch_move(x + 50, y + 50);
        touch_up(x + 50, x + 50);

    }



    public float getDisplacementx() {
        return displacementx;
    }

    public float getDisplacementy() {
        return displacementy;
    }
}
