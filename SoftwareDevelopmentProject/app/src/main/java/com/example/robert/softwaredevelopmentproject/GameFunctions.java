package com.example.robert.softwaredevelopmentproject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.text.format.Time;

import com.example.robert.softwaredevelopmentproject.Game.GameObject;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * Created by dingus on 11/8/2016.
 */
public class GameFunctions {
    static ArrayList<GuiShip> loadoutShipList;
    static Random rand;
    static float screenWidth;
    static float screenHeight;

    //if colliding with object, returns that, else returns null
    static public GameObject checkForCollision(float x, float y, float width, float height){
        Point gridPoint = convertPositionToGridCoord(x,y);

        float firstLeft = x-width/2;
        float firstRight = x+width/2;
        float firstTop = y-height/2;
        float firstBottom = y+height/2;

        for(GameObject object: GameScreen.shipGrid[gridPoint.x][gridPoint.y]){
            float objectWidth = object.getGraphic().getWidth();
            float objectHeight = object.getGraphic().getHeight();

            float secondLeft = object.getX()-objectWidth/2;
            float secondRight = object.getX()+objectWidth/2;
            float secondTop = object.getY()-objectHeight/2;
            float secondBottom = object.getY()+objectHeight/2;

            if(firstLeft<secondRight&&firstRight>secondLeft){
                if(firstBottom>secondTop&&firstTop<secondBottom){
                    return object;
                }
            }
        }
        return null;
    }


    //convert map position to a point on the grid for collision detection
    static public Point convertPositionToGridCoord(float x, float y){
        int mapGridSquareSize = GameScreen.mapGridSquareSize;
        Point point = new Point();
        point.x = (int)x/mapGridSquareSize;
        point.y = (int)y/mapGridSquareSize;
        return point;
    }

    static public boolean isMoveInBounds(float x, float y){
        Point gridPoint = convertPositionToGridCoord(x,y);
        if(gridPoint.x>=GameScreen.shipGrid.length||gridPoint.x<0)return false;
        if(gridPoint.y>=GameScreen.shipGrid[0].length||gridPoint.y<0)return false;
        return true;
    }

    static public boolean hasGridPositionChanged(float x, float y, float newx, float newy){
        Point oldPoint = convertPositionToGridCoord(x, y);
        Point newPoint = convertPositionToGridCoord(newx, newy);

        if(oldPoint.x==newPoint.x&&oldPoint.y==newPoint.y)return false;
        return true;
    }

    //return whether two points are under a given range from each other
    public static boolean isUnderRange(float firstx, float firsty, float secondx, float secondy, float range){
        float xdist = firstx - secondx;
        float ydist = firsty - secondy;

        float realDist = (float) Math.sqrt(xdist*xdist+ydist*ydist);

        return realDist<range;
    }

    public static int getRandInt(int range){
        if(rand==null){
            Time t = new Time();
            t.setToNow();
            rand = new Random(t.toMillis(false));
        }
        return rand.nextInt(range);
    }

    public static void save(Activity context, String fileName){
        File path = context.getFilesDir();
        File file = new File(path, fileName+".sav");

        PlayerData.setSaveName(fileName);


        int[] ships = PlayerData.getShipArray();
        StringBuilder str = new StringBuilder();
        for(int i=0; i<ships.length; i++){
            str.append(ships[i]).append(",");
        }

        try {
            FileOutputStream stream = new FileOutputStream(file);
            String saveString = str.toString();
            byte[] saveBytes = saveString.getBytes();
            stream.write(saveBytes);
            stream.close();
        }catch (Exception e){

        }
    }

    public static float getAngleToPoint(float x, float y){
        float angle = (float)Math.atan(y / x);
        return angle;
    }

    public static float UnwrapAngle(float angle) {
        if (angle >= 0) {
            float tempAngle = angle % 360;
            return tempAngle == 360 ? 0 : tempAngle;
        }
        else
            return 360 - (-1 * angle) % 360;
    }

    public static void setLoadoutShipList(ArrayList<GuiShip> list){
        loadoutShipList = list;
    }

    public static ArrayList<GuiShip> getLoadoutShipList() {
        return loadoutShipList;
    }

    public static boolean load(Activity context, String fileName){
        if(fileName==null)return false;

        File path = context.getFilesDir();
        File file = new File(path, fileName+".sav");

        int length = (int) file.length();

        byte[] bytes = new byte[length];

        try {
            FileInputStream in = new FileInputStream(file);
            in.read(bytes);
            in.close();
        } catch(Exception e) {

        }

        String saveDataString = new String(bytes);

        int arrayLength = PlayerData.getShipArray().length;

        StringTokenizer st = new StringTokenizer(saveDataString, ",");
        int[] saveData = new int[arrayLength];
        for (int i = 0; i < arrayLength; i++) {
            saveData[i] = Integer.parseInt(st.nextToken());
        }

        PlayerData.setShipOwnedArray(saveData);
        PlayerData.setSaveName(fileName);

        return true;
    }

    public static boolean delete(Activity context, String fileName){
        File path = context.getFilesDir();
        File file = new File(path, fileName+".sav");
        boolean result = file.delete();
        fileName = null;

        return result;
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postTranslate(-source.getWidth() / 2, -source.getHeight() / 2);
        matrix.postRotate(angle);
        matrix.postTranslate(source.getWidth()/2, source.getHeight()/2);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);


        //Matrix matrix = new Matrix();
       // matrix.postRotate(angle, source.getWidth() / 2, source.getHeight() / 2);
        //return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


}
