package com.example.robert.softwaredevelopmentproject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dingus on 11/7/2016.
 */
public class ShipDictionary {

    static HashMap<String, GuiShip> shipMap = new HashMap<>();
    static boolean initialized = false;
    static String[] names = {"Deimos","Io","Thebe"};

    static int[] startingShipList = {1,1,1};

    public ShipDictionary() {

    }

    public static void init(){
        shipMap.put("Deimos", new GuiShip("Deimos", 0, 10));
        shipMap.put("Io", new GuiShip("Io",0,20));
        shipMap.put("Thebe", new GuiShip("Thebe",0,15));
        initialized = true;
    }

    public static String getShipName(int ship){
        return names[ship];
    }

    public static int getShipImage(String name){
        if(!initialized)init();
        return shipMap.get(name).getImage();
    }

    public static int getShipImage(int ship){
        if(!initialized)init();
        String name = names[ship];
        return shipMap.get(name).getImage();
    }

    public static int getShipValue(String name){
        if(!initialized)init();
        return shipMap.get(name).getValue();
    }

    public static int getShipValue(int ship){
        if(!initialized)init();
        String name = names[ship];
        return shipMap.get(name).getValue();
    }

    public static int[] getStartingShipList(){
        return startingShipList;
    }

}
