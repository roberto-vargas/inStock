package com.example.roberto.instock;

/**
 * Created by Roberto on 11/3/14.
 */
public class foodTag extends Object {

    private int date;
    private String ID;
    private String daysSince;
    private int dateViewID;
    private int buttonViewID;

    public foodTag(int date, String ID) {
        this.ID = ID;
        this.date = date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getDate(){
        return date;
    }

    public void setID(String ID){
        this.ID = ID;
    }

    public String getID(){
        return ID;
    }

    public void setDaysSince(String daysSince){
        this.daysSince = daysSince;
    }
    public String getDaysSince(){
        return daysSince;
    }

    public void setDateVewID(int ID){
        dateViewID = ID;
    }

    public void setbuttonViewID(int ID){
        buttonViewID = ID;
    }
}
