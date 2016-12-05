package com.example.robert.softwaredevelopmentproject.Game;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.example.robert.softwaredevelopmentproject.GameFunctions;
import com.example.robert.softwaredevelopmentproject.GameScreen;

/**
 * Created by dingus on 11/26/2016.
 */
public abstract class GameObject {
    int health = 10;
    int identifier;
    float x,y;
    float velocity;
    float angle=0;
    int team;//0=neutral, 1=player, 2=enemy
    Bitmap graphic;
    public boolean alive = true;

    Point gridPosition=new Point(-1,-1);

    public  GameObject(float x, float y, int team){
        this.x = x;
        this.y = y;
        this.team = team;

        int squareLength = GameScreen.mapGridSquareSize;

    }

    public void removeObject(){
        alive = false;
    }

    public void doDamage(int amount){
        health-=amount;
    }

    public float getCenterx(){
        return graphic.getWidth()/2;
    }

    public float getCentery(){
        return graphic.getHeight()/2;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public Bitmap getGraphic() {
        return graphic;
    }

    public void setGraphic(Bitmap graphic) {
        this.graphic = graphic;
    }

    public void updatePosition(){
        float radAngle = (float)Math.toRadians(angle);

        float movedx = x;
        float movedy = y;
        movedx += velocity*Math.cos(radAngle);
        movedy += velocity*Math.sin(radAngle);

        if(GameFunctions.isMoveInBounds(movedx, movedy)){
            x=movedx;
            y=movedy;
        }else{
            alive = false;
        }

    }
}
