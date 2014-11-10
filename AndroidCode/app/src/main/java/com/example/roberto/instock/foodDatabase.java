package com.example.roberto.instock;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Roberto on 11/3/14.
 */
public class foodDatabase {

    Map<String,foodTag> database;

    public foodDatabase(){
        database = new HashMap<String,foodTag>();
    }

    public void putFoodTag(foodTag foodTag) {
        database.put(foodTag.getID(),foodTag);
    }

    public foodTag getTag(String key){
        return database.get(key);
    }

    public int getDatabaseSize(){
        return database.size();
    }

    public Set getDatabaseKeys(){
        return database.keySet();
    }

}
