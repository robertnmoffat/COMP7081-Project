package com.example.robert.softwaredevelopmentproject;

/**
 * Created by dingus on 11/8/2016.
 */
public class PlayerData {
    private static int[] shipOwnedArray = new int[3];
    private static String saveName;

    public static void addShip(int ship, int amount){
        shipOwnedArray[ship] +=amount;
    }

    public static int[] getShipArray(){
        return shipOwnedArray;
    }

    public static void setShipOwnedArray(int[] ships){
        if(ships.length==shipOwnedArray.length) {
            shipOwnedArray = ships;
        }
    }

    public static void setSaveName(String name){
        saveName = name;
    }


    public static String getSaveName(){
        return saveName;
    }
}
