package com.example.robert.softwaredevelopmentproject.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.text.format.Time;

import com.example.robert.softwaredevelopmentproject.GameActivity;
import com.example.robert.softwaredevelopmentproject.GameController;
import com.example.robert.softwaredevelopmentproject.GameFunctions;
import com.example.robert.softwaredevelopmentproject.GameScreen;
import com.example.robert.softwaredevelopmentproject.ShipDictionary;

import java.util.Random;

/**
 * Created by dingus on 11/26/2016.
 */
public class GameShip extends GameObject {
    GameController gameController;

    int type;

    int fullReloadTime = 120;
    int currentReloadTime = fullReloadTime;

    Bitmap rotatedGraphic;
    float turnSpeed = 0.5f;
    float targetx,targety;
    boolean hasTargetPosition = false;
    GameShip enemyTarget;
    boolean hasEnemyTarget = false;

    float targetAngle;

    float stopRange = 200;
    float shotBeginDistance = 40;

    public GameShip(float x, float y, int team, int type, Context context) {
        super(x, y, team);
        this.type = type;
        graphic = ShipDictionary.getGraphic(type, context);
        rotatedGraphic = graphic;

        gameController = GameActivity.getGameController();
    }

    public boolean hasTargetPosition() {
        return hasTargetPosition;
    }

    public void setHasTargetPosition(boolean hasTargetPosition) {
        this.hasTargetPosition = hasTargetPosition;
    }

    public float getTargetAngle() {
        return targetAngle;
    }

    public float getTargetx() {
        return targetx;
    }

    public void setTargetx(float targetx) {
        this.targetx = targetx;
    }

    public float getTargety() {
        return targety;
    }

    public void setTargety(float targety) {
        this.targety = targety;
    }

    public GameShip(float x, float y, int team, String type) {
        super(x, y, team);

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setAngle(float angle) {
        this.angle = angle;
        rotatedGraphic = GameFunctions.rotateBitmap(graphic,angle);
    }

    public Bitmap getRotatedGraphic() {
        return rotatedGraphic;
    }

    public void setRotatedGraphic(Bitmap rotatedGraphic) {
        this.rotatedGraphic = rotatedGraphic;
    }

    public GameShip getEnemyTarget() {
        return enemyTarget;
    }

    public void setEnemyTarget(GameShip enemyTarget) {
        hasEnemyTarget=true;
        this.enemyTarget = enemyTarget;
    }

    public boolean isHasEnemyTarget() {
        return hasEnemyTarget;
    }

    public void setHasEnemyTarget(boolean hasEnemyTarget) {
        this.hasEnemyTarget = hasEnemyTarget;
    }

    public void updatePosition(){
        float radAngle = (float)Math.toRadians(angle);

        if(health<=0){
            gameController.addExplosion(x,y-10);
            gameController.addExplosion(x-10,y+10);
            gameController.addExplosion(x+10,y+10);
            gameController.shipGrid[gridPosition.x][gridPosition.y].remove(this);
            alive=false;
        }

        if(hasTargetPosition||hasEnemyTarget) {
            if(hasEnemyTarget){
                //if(enemyTarget.team==2)System.out.println(enemyTarget.health);

                targetx = enemyTarget.getX();
                targety = enemyTarget.getY();

                if(currentReloadTime!=0){
                    if(GameFunctions.getRandInt(3)==1)
                    currentReloadTime--;
                }else{
                    currentReloadTime = fullReloadTime;
                    float shotAngle = angle + GameFunctions.getRandInt(11)-5;
                    float shotPositionx = x;
                    float shotPositiony = y;
                    shotPositionx += shotBeginDistance * Math.cos(radAngle);
                    shotPositiony += shotBeginDistance * Math.sin(radAngle);
                    gameController.addLaser(shotPositionx,shotPositiony,shotAngle);
                }

                if(enemyTarget.isAlive()==false){
                    enemyTarget = null;
                    hasEnemyTarget = false;
                    int otherTeam;
                    if(team==1)
                        otherTeam=2;
                    else
                        otherTeam = 1;

                    GameShip newShipTarget = gameController.getRandomShip(otherTeam);

                    if(newShipTarget!=null){
                        enemyTarget = newShipTarget;
                        hasEnemyTarget = true;
                    }
                }
            }

            angle = GameFunctions.UnwrapAngle(angle);

            float reverseAngle = GameFunctions.UnwrapAngle(angle + 90);//only plus 90 instead of 180, because the angles are off by 90
            float realAngle = angle;
            float xLength = targetx - x, yLength = targety - y;
            targetAngle = GameFunctions.getAngleToPoint(xLength, yLength);
            float degreeAngle = (float) Math.toDegrees(targetAngle);
            if(xLength<0)degreeAngle = 180+degreeAngle;
            if (degreeAngle < 0) degreeAngle = 360 + degreeAngle;

            //System.out.println("Target Angle: " + degreeAngle + " Current Angle: " + realAngle + " Reverse Angle: " + reverseAngle);

            float counterClockwiseDist;
            float clockwiseDist;

            if (realAngle > degreeAngle) {
                counterClockwiseDist = (Math.abs(360 - realAngle) + Math.abs(0 - degreeAngle));
                clockwiseDist = realAngle - degreeAngle;
            } else {
                clockwiseDist = (Math.abs(360 - degreeAngle) + Math.abs(0 - realAngle));
                counterClockwiseDist = degreeAngle - realAngle;
            }

            if (clockwiseDist > counterClockwiseDist) {
                angle += turnSpeed;
                if(angle>360)GameFunctions.UnwrapAngle(angle);
            } else {
                angle -= turnSpeed;
                if(angle<0)GameFunctions.UnwrapAngle(angle);
            }

        }

        //if they either do not have a target, or they are outside the stop range of their target
        if(!hasEnemyTarget||(hasEnemyTarget&&!GameFunctions.isUnderRange(x,y,enemyTarget.getX(),enemyTarget.getY(), stopRange))) {
            float xMove = velocity * (float) Math.cos(radAngle);
            float yMove = velocity * (float) Math.sin(radAngle);
            int squareLength = gameController.mapGridSquareSize;

            if(GameFunctions.hasGridPositionChanged(x,y,x+xMove,y+yMove)){
                //System.out.println("GRIDPOSITION: "+gridPosition.x+" "+gridPosition.y);
                Point potentialGridPos = GameFunctions.convertPositionToGridCoord(x+xMove,y+yMove);
                if(potentialGridPos.x>=0&&potentialGridPos.y>=0
                        &&potentialGridPos.x<gameController.shipGrid.length&&potentialGridPos.y<gameController.shipGrid[0].length) {
                    if(gridPosition.x>=0&&gridPosition.y>=0)
                        gameController.shipGrid[gridPosition.x][gridPosition.y].remove(this);
                    gridPosition = potentialGridPos;
                    gameController.shipGrid[gridPosition.x][gridPosition.y].add(this);
                }else{
                    targetx=GameScreen.mapSizex/2;
                    targety=GameScreen.mapSizey/2;
                    hasTargetPosition=true;
                }
            }

            super.updatePosition();

        }
    }

}
