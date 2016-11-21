package com.example.robert.softwaredevelopmentproject;

import android.app.Activity;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.StringTokenizer;

/**
 * Created by dingus on 11/8/2016.
 */
public class GameFunctions {

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
}
