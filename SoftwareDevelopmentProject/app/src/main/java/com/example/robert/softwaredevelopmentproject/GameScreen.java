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
    private Paint p;

    boolean gameIsOver = false;

    String gameText = "";

    GameShip selectedShip = null;
    int selectedShipIndex;
    Context context;

    boolean isPaused = false;
    boolean hasBeenSetup = false;

    float dragPosx=-1,dragPosy=-1;

    Bitmap background;
    Bitmap backgroundPlanet;
    Bitmap pauseButton;
    Bitmap playButton;

    public static float mapSizex, mapSizey;
    public static int mapGridSquareSize = 60;
    //public static GameObject[][] shipGrid = new GameShip[(int)mapSizex][(int)mapSizey];
    public static ArrayList<GameObject>[][] shipGrid;

    float displacementx=0, displacementy=0;
    float touchStartx,touchStarty;

    ArrayList<GameObject> playerShips;
    ArrayList<GameObject> enemyShips;
    ArrayList<GameObject> gameObjects;

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

        int gridCountHorizontal = (int)mapSizex/mapGridSquareSize;
        int gridCountVertical = (int)mapSizey/mapGridSquareSize;

        shipGrid = new ArrayList[gridCountHorizontal][gridCountVertical];

        for(int i=0; i<shipGrid.length; i++){
            for(int j=0; j<shipGrid[0].length; j++){
                shipGrid[i][j]=new ArrayList<>();
            }
        }
    }

    public GameShip getRandomShip(int team){
        if(team==1){
            if(playerShips.size()!=0)
                return (GameShip)playerShips.get(GameFunctions.getRandInt(playerShips.size()));
            else
                return null;
        }
        if(team==2){
            if(enemyShips.size()!=0)
                return (GameShip)enemyShips.get(GameFunctions.getRandInt(enemyShips.size()));
            else
                return null;
        }
        return null;
    }


    public void addObjectToScene(GameObject toAdd, int team){
        switch (team){
            case 0:
                gameObjects.add(toAdd);
                break;
            case 1:
                playerShips.add((GameShip)toAdd);
                break;
            case 2:
                playerShips.add((GameShip)toAdd);
                break;
        }
    }

    public void addLaser(float x, float y, float angle){
        Laser laser = new Laser(x,y,context);
        laser.setVelocity(1);
        laser.setAngle(angle);
        gameObjects.add(laser);
    }

    public void addExplosion(float x, float y){
        Explosion exp = new Explosion(x,y,0,context);
        gameObjects.add(exp);
    }

    public void setupShips(){
        playerShips = new ArrayList<>();
        enemyShips = new ArrayList<>();
        gameObjects = new ArrayList<>();

        ArrayList<GuiShip> loadoutShipList = GameFunctions.getLoadoutShipList();
        GameShip testShip;

        float startOfScreenx = mapSizex/2-getWidth()/2;
        float startOfScreeny = mapSizey/2-getHeight()/2;
        float endOfScreenx = mapSizex/2+getWidth()/2;
        float endOfScreeny = mapSizey/2+getHeight()/2;

        displacementx = -startOfScreenx;
        displacementy = -startOfScreeny;

        int position = (int)startOfScreeny;

        for(GuiShip guiShip: loadoutShipList){
            //System.out.println("Creating Ship");
            position+=75;
            int shipType = ShipDictionary.nameToArrayPos.get(guiShip.getName());
            testShip = new GameShip(startOfScreenx+50,position,1,shipType,context);
            testShip.setVelocity(ShipDictionary.shipSpeeds[shipType]);
            playerShips.add(testShip);
        }

        position = (int)startOfScreeny;

        for(int i=0; i<3; i++){
            position+=75;
            int shipType = 0;
            testShip = new GameShip(endOfScreenx-50, position,2,shipType,context);
            testShip.setVelocity(0.5f);
            testShip.setAngle(180);
            testShip.setEnemyTarget((GameShip)playerShips.get(GameFunctions.getRandInt(playerShips.size())));
            enemyShips.add(testShip);
        }
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

    public void touch_up(float x, float y) {
        if(selectedShip!=null){
            selectedShip.setTargetx(x-displacementx);
            selectedShip.setTargety(y-displacementy);
            selectedShip.setHasTargetPosition(true);
            return;
        }
    }

    public void touch_move(float x, float y) {
        if(selectedShip!=null){
            dragPosx = x;
            dragPosy = y;
            return;
        }

        if(x-touchStartx<0&&x-touchStartx>-(mapSizex-getWidth())) {
            displacementx = x - touchStartx;
        }
        if(y-touchStarty<0&&y-touchStarty>-(mapSizey-getHeight())) {
            displacementy = y - touchStarty;
        }

    }

    public void touch_start(float x, float y) {
        if(isPressOnPauseButton(x,y)){
            togglePauseGame();
            return;
        }

        if(isPressOnOwnedShip(x,y)){

        }

        if(selectedShip!=null){
            dragPosx = x;
            dragPosy = y;
            return;
        }

        touchStartx = x-displacementx;
        touchStarty = y-displacementy;
    }

    private boolean isPressOnOwnedShip(float x, float y){
        //Todo: properly test whether pressing ship.
        return false;
    }

    private boolean isPressOnPauseButton(float x, float y){
        return x>getWidth()-pauseButton.getWidth()-10&&y< 10+pauseButton.getHeight();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (!hasBeenSetup) {
            setupShips();
            hasBeenSetup = true;
        }

        //System.out.println("selectedPosition: "+playerShips.get(0).getX());

        super.onDraw(canvas);


        p.setColor(Color.BLACK);

        p.setAlpha(255);
        p.setStyle(Paint.Style.FILL);

        p.setStrokeWidth(1);

        canvas.drawRect(0, 0, getWidth(), getHeight(), p);

        canvas.drawBitmap(background, displacementx, displacementy, p);
        canvas.drawBitmap(background, background.getWidth() + displacementx, displacementy, p);
        canvas.drawBitmap(background, displacementx, +background.getHeight() + displacementy, p);
        canvas.drawBitmap(background, background.getWidth() + displacementx, +background.getHeight() + displacementy, p);

        canvas.drawBitmap(backgroundPlanet, mapSizex / 4 + displacementx, mapSizey / 4 + displacementy, p);

        p.setColor(Color.BLUE);
        canvas.drawLine(mapSizex + displacementx, 0 + displacementy, mapSizex + displacementx, mapSizey + displacementy, p);

        updateAndDrawObjectList(playerShips, canvas);
        updateAndDrawObjectList(enemyShips, canvas);
        updateAndDrawObjectList(gameObjects, canvas);


        if(selectedShip!=null) {
            float x = selectedShip.getX();
            float y = selectedShip.getY();
            float width = selectedShip.getGraphic().getWidth();
            float height = selectedShip.getGraphic().getHeight();
            float radius;

            if (dragPosx >= 0 && dragPosy >= 0) {
                p.setStyle(Paint.Style.STROKE);

                if(selectedShip.isHasEnemyTarget()){
                    if(GameFunctions.isUnderRange(dragPosx,dragPosy,selectedShip.getEnemyTarget().getX()+displacementx,selectedShip.getEnemyTarget().getY()+displacementy, 100)) {
                        dragPosx = selectedShip.getEnemyTarget().getX() + displacementx;
                        dragPosy = selectedShip.getEnemyTarget().getY() + displacementy;
                    }
                }

                GameObject selected = GameFunctions.checkForCollision(dragPosx-displacementx,dragPosy-displacementy,100,100);
                if(selected!=null) {
                    selectedShip.setEnemyTarget((GameShip)selected);
                    selectedShip.setHasEnemyTarget(true);
                    p.setColor(Color.RED);
                    dragPosx=selected.getX()+displacementx;
                    dragPosy=selected.getY()+displacementy;
                }
                else {
                    selectedShip.setEnemyTarget(null);
                    selectedShip.setHasEnemyTarget(false);
                    p.setColor(Color.BLUE);
                }

                canvas.drawLine(x + displacementx, y + displacementy, dragPosx, dragPosy, p);
                canvas.drawCircle(dragPosx, dragPosy, 20, p);
            }

            if (width > height)
                radius = width;
            else
                radius = height;

            p.setStyle(Paint.Style.STROKE);
            p.setColor(Color.BLUE);
            canvas.drawCircle(x + displacementx, y + displacementy, radius, p);
        }

        if(playerShips.size()==0){
            gameText="Battle Lost";
            gameIsOver=true;
        }
        if(enemyShips.size()==0){
            gameText="Battle Won";
            gameIsOver=true;
        }

        if(isPaused){
            canvas.drawBitmap(playButton, getWidth()-pauseButton.getWidth()-10, 10, p);
        }else{
            canvas.drawBitmap(pauseButton, getWidth()-pauseButton.getWidth()-10, 10, p);
        }

        p.setColor(Color.GREEN);
        p.setTextSize(100);
        canvas.drawText(gameText, getWidth()/4,getHeight()/4,p);
    }

    public void updateAndDrawObjectList(ArrayList<GameObject> list, Canvas canvas){
        ArrayList<GameObject> listToRemove = new ArrayList<>();

        //update and draw all objects
        for(GameObject currentObject: list){
            if(!isPaused)
                currentObject.updatePosition();

            canvas.drawBitmap(currentObject.getGraphic(), rotateByMatrix(currentObject), null);
            if(!currentObject.isAlive())listToRemove.add(currentObject);
        }

        for(GameObject currentObject: listToRemove){
            list.remove(currentObject);
        }
        listToRemove.clear();
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

    public void selectNextShip(){
        if(selectedShip==null){
            selectedShip = (GameShip)playerShips.get(0);
            selectedShipIndex = 0;
        }else if(selectedShipIndex+1<playerShips.size())
            selectedShip = (GameShip)playerShips.get(++selectedShipIndex);
        else{
            selectedShip = (GameShip)playerShips.get(0);
            selectedShipIndex = 0;
        }
        //displacementx = -selectedShip.getX()+getWidth()/2;
        //displacementy = -selectedShip.getY()+getHeight()/2;
    }

    public void selectPreviousShip(){
        if(selectedShip==null){
            selectedShip = (GameShip)playerShips.get(playerShips.size()-1);
            selectedShipIndex = playerShips.size()-1;
        }else if(selectedShipIndex-1<0) {
            selectedShip = (GameShip)playerShips.get(playerShips.size()-1);
            selectedShipIndex = playerShips.size()-1;
        }
        else{
            selectedShip = (GameShip)playerShips.get(--selectedShipIndex);
        }
    }

    public void selectNone(){
        dragPosx=-1;
        dragPosy=-1;
        selectedShip = null;
    }

    public void togglePauseGame(){
        if(isPaused)
            isPaused=false;
        else
            isPaused = true;
    }

    public void fakeUnitMoveTouchTest(float x, float y) {

        touch_start(x, y);
        touch_move(x + 50, y + 50);
        touch_up(x + 50, x + 50);

    }

    public boolean shipIsAtTargetAngleTest(){
        int shipAngle = Math.round(selectedShip.getAngle());
        int targetAngle = Math.round(selectedShip.getTargetAngle());

        return shipAngle==targetAngle;
    }

    public float getDisplacementx() {
        return displacementx;
    }

    public float getDisplacementy() {
        return displacementy;
    }

    public void moveShipToEdgeOfBoundsTest(){
        GameShip ship = (GameShip)playerShips.get(0);

        ship.setX(mapSizex-50);
    }
}
