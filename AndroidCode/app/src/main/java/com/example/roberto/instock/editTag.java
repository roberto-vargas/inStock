package com.example.roberto.instock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Arrays;


public class editTag extends Activity {
Intent startIntent = null;
    // Create class variable foodDatabase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tag);
        startIntent = getIntent();
        // get intent and unpack from previous activity

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_tag, menu);
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

    public void saveButtonClicked (View v){
        EditText tagName = (EditText) findViewById(R.id.newName);
        String newName = tagName.getText().toString();
        Button newDateButton = (Button) findViewById(R.id.newDate);
        String dateString = newDateButton.getText().toString();
        int newFoodDate = Integer.parseInt(dateString);
        Intent intent = new Intent(this,mainActivity.class);
        String oldExtra = startIntent.getStringExtra("name");
        intent.putExtra("name", oldExtra);
        intent.putExtra("newName",newName);
        intent.putExtra("newDate",newFoodDate);
        startActivity(intent);
    }

    // TO DO:
    // finish putting in Spinnable (dropdown) for item name
    // allow date selection (do calendar view later)
    // make function to commit changes to foodDatabase and restart mainActivity -> save button

}
