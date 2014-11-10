package com.example.roberto.instock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.prefs.Preferences;


public class mainActivity extends Activity {

public foodDatabase foodDatabase;

    private static final int MIN_NUMBER_OF_TAGS = 3;
    private static final int MAX_LOOP_SIZE = 100;

    private static final String[] NUMBERS = {"zero","one","two","three","four","five","six","seven","eight","nine"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        foodDatabase = new foodDatabase();

        // Parse SharedPreferences for existing tags and create foodTag instances
        //parsePrefs(foodDatabase);

        // Create remaining foodTag instances
        if (foodDatabase.getDatabaseSize()<MIN_NUMBER_OF_TAGS){
            while (foodDatabase.getDatabaseSize() < MIN_NUMBER_OF_TAGS){
                String countString = NUMBERS[foodDatabase.getDatabaseSize()];
                foodTag foodTag = new foodTag(0,countString);
                foodDatabase.putFoodTag(foodTag);
            }
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        // Rebuild foodDatabase

        // Parse SharedPreferences for existing tags and create foodTag instances
        parsePrefs(foodDatabase);

        // Create remaining foodTag instances
        if (foodDatabase.getDatabaseSize()<MIN_NUMBER_OF_TAGS){
            while (foodDatabase.getDatabaseSize() < MIN_NUMBER_OF_TAGS){
                String countString = NUMBERS[foodDatabase.getDatabaseSize()];
                foodTag foodTag = new foodTag(0,countString);
                foodDatabase.putFoodTag(foodTag);
            }
        }

        // Update foodDatabase with changes from previous activity
        Intent intent = getIntent();
        if (intent.hasExtra("name")){
            String tagName = intent.getStringExtra("name");
            foodTag changeTag = foodDatabase.getTag(tagName);
            String newName = intent.getStringExtra("newName");
            int newDate = intent.getIntExtra("newDate",0);//getIntArrayExtra("newDate");
            changeTag.setID(newName);
            changeTag.setDate(newDate);
            Context context = getApplicationContext();
            String toastString = "Name changed to: " + changeTag.getID();
            CharSequence text = toastString;
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            String toastDateString = "Date changed to: " + Integer.toString(changeTag.getDate());
            CharSequence dateText = toastDateString;
            Toast dateToast = Toast.makeText(context, dateText, duration);
            dateToast.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void tag1Click(View v) {
        // For whatever click, get the tagName and get the matching foodTag object
        Button button = (Button) v;
        String name = getResources().getResourceEntryName(v.getId());
        if (foodDatabase.getDatabaseKeys().contains(name)) {
            foodTag tag = foodDatabase.getTag(name);
        }

        Intent activityPassIntent = new Intent(this, editTag.class);
        String intentKey = "name";
        activityPassIntent.putExtra(intentKey,name);
        startActivity(activityPassIntent);
        // Start new activity and pass in the tag database
    }

    public void parsePrefs(foodDatabase foodDatabase){
        Context context = getApplicationContext();
        SharedPreferences sharedPrefPrimary = context.getSharedPreferences(getString(R.string.primary_file_key),Context.MODE_PRIVATE);
        SharedPreferences sharedPrefSecondary = context.getSharedPreferences(getString(R.string.secondary_file_key),Context.MODE_PRIVATE);
        for (int i = 0; i<MAX_LOOP_SIZE;i++){
            String intString = Integer.toString(i);
            String itemName = sharedPrefPrimary.getString(intString,"NULL");
            if (itemName == null) {
                return;
            }
            int date = sharedPrefSecondary.getInt(itemName,0);
            if (date == 0) {
                // add catch code
            }
            foodTag foodTag = new foodTag(date,itemName);
            foodDatabase.putFoodTag(foodTag);
            }
        }

        // Read from SharedPrefs until the default value is returned
        //

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

        Context context = getApplicationContext();
        SharedPreferences sharedPrefPrimary = context.getSharedPreferences(getString(R.string.primary_file_key), Context.MODE_PRIVATE);
        SharedPreferences sharedPrefSecondary = context.getSharedPreferences(getString(R.string.secondary_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefPrimary.edit();
        SharedPreferences.Editor editor1 = sharedPrefSecondary.edit();

        // get a keySet, loop through it and populate both sharedPreferences files
        int count = 0;
        String intString = Integer.toString(count);
        Set keySet = foodDatabase.getDatabaseKeys();
        Iterator iter = keySet.iterator();
        while (iter.hasNext()){
            String iterKey = (String) iter.next();
            editor.putString(intString,iterKey);
            editor1.putInt(iterKey,foodDatabase.getTag(iterKey).getDate());
            editor.commit();
            editor1.commit();
            count++;
            intString = Integer.toString(count);
        }
    }

    public void onStop() {
        super.onStop();

        Context context = getApplicationContext();
        SharedPreferences sharedPrefPrimary = context.getSharedPreferences(getString(R.string.primary_file_key), Context.MODE_PRIVATE);
        SharedPreferences sharedPrefSecondary = context.getSharedPreferences(getString(R.string.secondary_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefPrimary.edit();
        SharedPreferences.Editor editor1 = sharedPrefSecondary.edit();

        // get a keySet, loop through it and populate both sharedPreferences files
            int count = 0;
            String intString = Integer.toString(count);
            Set keySet = foodDatabase.getDatabaseKeys();
            Iterator iter = keySet.iterator();
            while (iter.hasNext()){
                String iterKey = (String) iter.next();
                editor.putString(intString,iterKey);
                editor1.putInt(iterKey, foodDatabase.getTag(iterKey).getDate());
                editor.commit();
                editor1.commit();
                count++;
                intString = Integer.toString(count);
            }
            // get keySet and map intStrings to tagNames (order doesn't matter)


    }

}
