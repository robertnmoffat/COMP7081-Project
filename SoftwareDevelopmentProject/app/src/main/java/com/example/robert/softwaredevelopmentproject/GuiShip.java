package com.example.robert.softwaredevelopmentproject;

/**
 * Created by dingus on 11/7/2016.
 */
public class GuiShip {
    private String name;
    private int image;
    private int value;

    public GuiShip(String name, int image, int value){
        this.name = name;
        this.image = image;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
