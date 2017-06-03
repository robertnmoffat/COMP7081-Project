package com.example.robert.softwaredevelopmentproject;

import android.content.Context;

import com.example.robert.softwaredevelopmentproject.Game.Explosion;
import com.example.robert.softwaredevelopmentproject.Game.GameObject;
import com.example.robert.softwaredevelopmentproject.Game.GameShip;
import com.example.robert.softwaredevelopmentproject.Game.Laser;

import java.util.ArrayList;

/**
 * Created by dingus on 6/2/2017.
 */

enum ObjectList{PLAYERSHIPS, ENEMYSHIPS, GAMEOBJECTS};

public class GameController {
    public static ArrayList<GameObject>[][] shipGrid;
    public static int mapGridSquareSize = 60;
    public static float mapSizex, mapSizey;
    public float backgroundSizeX, backgroundSizeY;

    ArrayList<GameObject> playerShips;
    ArrayList<GameObject> enemyShips;
    ArrayList<GameObject> gameObjects;

    private Context context;

    GameShip selectedShip = null;
    int selectedShipIndex;

    float displacementx=0, displacementy=0;

    private boolean isPaused;
    boolean hasBeenSetup = false;

    /**
     *
     * @param context
     * @param backgroundSizeX Screen background size horizontally
     * @param backgroundSizeY Screen background size vertically
     */
    public GameController(Context context, float backgroundSizeX, float backgroundSizeY){
        this.context = context;
        this.backgroundSizeX = backgroundSizeX;
        this.backgroundSizeY = backgroundSizeY;
        mapSizex = backgroundSizeX*2;
        mapSizey = backgroundSizeY*2;

        int gridCountHorizontal = (int)mapSizex/mapGridSquareSize;
        int gridCountVertical = (int)mapSizey/mapGridSquareSize;

        shipGrid = new ArrayList[gridCountHorizontal][gridCountVertical];

        for(int i=0; i<shipGrid.length; i++){
            for(int j=0; j<shipGrid[0].length; j++){
                shipGrid[i][j]=new ArrayList<>();
            }
        }

    }

    /**
     * Gets the currently selected ship
     * @return the currently selected ship
     */
    public GameShip getSelectedShip(){
        return selectedShip;
    }

    /**
     * Sets selectedShip to the newly selected ship
     * @param newSelectedShip the newly selected ship
     */
    public void setSelectedShip(GameShip newSelectedShip){
        selectedShip = newSelectedShip;
    }

    public boolean isPaused(){
        return isPaused;
    }

    /**
     * Set the screen displacement from dragging the view around
     * @param x x screen displacement
     * @param y y screen displacement
     */
    public void setDisplacement(float x, float y){
        displacementx = x;
        displacementy = y;
    }


    /**
     * Get a random ship from the list of current ships in game
     * @param team Team to randomly select a ship from
     * @return The randomly selected ship
     */
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


    /**
     * Adds an object to the game scene
     * @param toAdd GameObject to add to the current scene
     * @param team Team of the GameObject being added to the scene
     */
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

    /**
     * Add a laser to the game at the given position and angle
     * @param x position
     * @param y position
     * @param angle Angle which the laser is facing and moving
     */
    public void addLaser(float x, float y, float angle){
        Laser laser = new Laser(x,y,context);
        laser.setVelocity(1);
        laser.setAngle(angle);
        gameObjects.add(laser);
    }

    /**
     * Add an explosion to the game at the given location
     * @param x position
     * @param y position
     */
    public void addExplosion(float x, float y){
        Explosion exp = new Explosion(x,y,0,context);
        gameObjects.add(exp);
    }

    /**
     * Set up the ship arrays for the game based upon which ships have been chosen to use
     */
    public void setupShips(){
        playerShips = new ArrayList<>();
        enemyShips = new ArrayList<>();
        gameObjects = new ArrayList<>();

        ArrayList<GuiShip> loadoutShipList = GameFunctions.getLoadoutShipList();
        GameShip testShip;

        float startOfScreenx = mapSizex/2-backgroundSizeX/2;
        float startOfScreeny = mapSizey/2-backgroundSizeY/2;
        float endOfScreenx = mapSizex/2+backgroundSizeX/2;
        float endOfScreeny = mapSizey/2+backgroundSizeY/2;

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

    public boolean isPressOnOwnedShip(float x, float y, float displacementx, float displacementy){
        //GameObject collided=GameFunctions.checkForCollision(x-displacementx,y-displacementy,50,50);

        GameObject collided = GameFunctions.checkForCollisionsAccurate(x-displacementx, y-displacementy, 50, playerShips);
        if(collided==null)
            collided = GameFunctions.checkForCollisionsAccurate(x-displacementx, y-displacementy, 50, enemyShips);

        if(collided==null)
            return false;

        if(collided.getTeam()!=1)
            return false;

        selectedShip = (GameShip) collided;
        return true;
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

    public void togglePauseGame(){
        if(isPaused)
            isPaused=false;
        else
            isPaused = true;
    }

    public boolean shipIsAtTargetAngleTest(){
        int shipAngle = Math.round(selectedShip.getAngle());
        int targetAngle = Math.round(selectedShip.getTargetAngle());

        return shipAngle==targetAngle;
    }

    public void moveShipToEdgeOfBoundsTest(){
        GameShip ship = (GameShip)playerShips.get(0);

        ship.setX(mapSizex-50);
    }

    public void updateObjectList(ArrayList<GameObject> list){
        ArrayList<GameObject> listToRemove = new ArrayList<>();

        //update and draw all objects
        for(GameObject currentObject: list){
            if(!isPaused)
                currentObject.updatePosition();

            if(!currentObject.isAlive())listToRemove.add(currentObject);
        }

        for(GameObject currentObject: listToRemove){
            list.remove(currentObject);
        }
        listToRemove.clear();
    }

    /**
     * Get a desired gameObject list from the GameController
     * @param listToGet Desired object list
     * @return desired list
     */
    public ArrayList<GameObject> getObjectList(ObjectList listToGet){
        switch (listToGet){
            case PLAYERSHIPS:
                return playerShips;
            case ENEMYSHIPS:
                return enemyShips;
            case GAMEOBJECTS:
                return gameObjects;
            default:
                return null;
        }

    }

    public void runGameIteration(){
        if (!hasBeenSetup) {
            setupShips();
            hasBeenSetup = true;
        }

        updateObjectList(playerShips);
        updateObjectList(enemyShips);
        updateObjectList(gameObjects);
    }
}
