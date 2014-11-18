package com.example.roberto.instock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.prefs.Preferences;


public class mainActivity extends Activity {

    private static final String TAG = "WifiConnect";
    private static final int BUFFER_SIZE = 1024;
    private static final int SERVER_PORT = 8888;
    private static final String CONNECTED = "Connected";
    private static final String DISCONNECTED = "Disconnected";
    private String IPAddress = "192.168.1.127";
    //private TextView IPAddress = new TextView(this.getApplicationContext());

    //private ServerSocket serverSocket = null;
    private Socket clientSocket;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.v(TAG, "Handling something!");
            Bundle receivedBundle = msg.getData();
            String recString = receivedBundle.getString("readMessage");
            Log.v(TAG, "recString is: " + recString);
            if(recString != ""){
                parseWifiString(recString);
                updateView();
                Context context = getApplicationContext();
                String toastString = recString;
                CharSequence text = toastString;
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }


        }
    };

    /**
     * Runnable to constantly wait for data input to read.
     */
    Runnable readRunnable = new Runnable() {
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                if(clientSocket.isConnected()){
                    readData();
                }
                else{
                }
            }
        }
    };

public foodDatabase foodDatabase;

    private static final int MIN_NUMBER_OF_TAGS = 6;
    private static final int MAX_LOOP_SIZE = 100;

    private static final String[] NUMBERS = {"zero","one","two","three","four","five"};
    private static final String[] DATE_NUMBERS = {"date_zero","date_one","date_two","date_three","date_four","date_five"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Context context = getApplicationContext();
        String string = getString(R.string.primary_file_key);
        String string2 = getString(R.string.secondary_file_key);
        context.getSharedPreferences(string, 0).edit().clear().commit(); //remove app preferences
        context.getSharedPreferences(string2, 0).edit().clear().commit(); //remove app preferences
        */

        setContentView(R.layout.activity_main);

        // WiFi stuff
        clientSocket = new Socket();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Thread readThread = new Thread(readRunnable);
        readThread.start();

        // App stuff
        foodDatabase = new foodDatabase();

        // Parse SharedPreferences for existing tags and create foodTag instances
        parsePrefs(foodDatabase);

        // Create remaining foodTag instances
        if (foodDatabase.getDatabaseSize()<MIN_NUMBER_OF_TAGS){
            while (foodDatabase.getDatabaseSize() < MIN_NUMBER_OF_TAGS){
                String countString = NUMBERS[foodDatabase.getDatabaseSize()];
                foodTag foodTag = new foodTag(0,countString);
                foodDatabase.putFoodTag(foodTag,countString);
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
                foodDatabase.putFoodTag(foodTag,countString);
            }
        }

        // Update foodDatabase with changes from previous activity
        Intent intent = getIntent();
        if (intent.hasExtra("name")){
            String tagName = intent.getStringExtra("name");
            foodTag changeTag = foodDatabase.getTag(tagName);
            String newName = intent.getStringExtra("newName");
            int newDate = intent.getIntExtra("newDate",0);
            changeTag.setID(newName);
            changeTag.setDate(newDate);
            foodDatabase.changeTag(changeTag,newName);
            //Code for debugging
            /*Button currentButton = (Button) findViewById(intent.getIntExtra("id",0));
            currentButton.setText(newName);
            Context context = getApplicationContext();
            String toastString = "Name changed to: " + changeTag.getID();
            CharSequence text = toastString;
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            String toastDateString = "Date changed to: " + Integer.toString(changeTag.getDate());
            CharSequence dateText = toastDateString;
            Toast dateToast = Toast.makeText(context, dateText, duration);
            dateToast.show();*/
        }
        updateView();
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
        // First, save current foodDatabase to sharedPrefs

        Context context = getApplicationContext();
        SharedPreferences sharedPrefPrimary = context.getSharedPreferences(getString(R.string.primary_file_key), Context.MODE_PRIVATE);
        SharedPreferences sharedPrefSecondary = context.getSharedPreferences(getString(R.string.secondary_file_key), Context.MODE_PRIVATE);
        SharedPreferences sharedPrefTernary = context.getSharedPreferences(getString(R.string.secondary_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefPrimary.edit();
        SharedPreferences.Editor editor1 = sharedPrefSecondary.edit();
        SharedPreferences.Editor editor2 = sharedPrefTernary.edit();


        // get a keySet, loop through it and populate both sharedPreferences files
        int count = 0;
        String intString = Integer.toString(count);
        Set keySet = foodDatabase.getDatabaseKeys();
        Iterator iter = keySet.iterator();
        while (iter.hasNext()){
            String iterKey = (String) iter.next();
            editor.putString(intString,iterKey);
            editor1.putInt(iterKey,foodDatabase.getTag(iterKey).getDate());
            editor2.putString(iterKey,foodDatabase.getTag(iterKey).getID());
            editor.apply();
            editor1.apply();
            editor2.apply();
            count++;
            intString = Integer.toString(count);
        }

        // For whatever click, get the tagName and get the matching foodTag object
        Button button = (Button) v;
        int id;
        foodTag tag = null;
        id = v.getId();
        String name = getResources().getResourceEntryName(v.getId());
        if (foodDatabase.getDatabaseKeys().contains(name)) {
            tag = foodDatabase.getTag(name);
        }

        Intent activityPassIntent = new Intent(this, editTag.class);
        String intentKey = "name";
        String idKey = "id";
        activityPassIntent.putExtra(idKey, id);
        activityPassIntent.putExtra(intentKey,name);
        activityPassIntent.putExtra("tagName",tag.getID());
        activityPassIntent.putExtra("oldDate",tag.getDate());
        startActivity(activityPassIntent);
        // Start new activity and pass in the tag database
    }

    public void parsePrefs(foodDatabase foodDatabase){
        Context context = getApplicationContext();
        SharedPreferences sharedPrefPrimary = context.getSharedPreferences(getString(R.string.primary_file_key),Context.MODE_PRIVATE);
        SharedPreferences sharedPrefSecondary = context.getSharedPreferences(getString(R.string.secondary_file_key),Context.MODE_PRIVATE);
        SharedPreferences sharedPrefTernary = context.getSharedPreferences(getString(R.string.ternary_file_key),Context.MODE_PRIVATE);

        for (int i = 0; i<MAX_LOOP_SIZE;i++){
            String intString = Integer.toString(i);
            String itemKey = sharedPrefPrimary.getString(intString, "NULL");
            if (itemKey == "NULL") {
                return;
            }
            int date = sharedPrefSecondary.getInt(itemKey,0);
            if (date == 0) {
                // add catch code
            }
            String itemName = sharedPrefTernary.getString(itemKey,"NULL");
            foodTag foodTag = new foodTag(date,itemName);
            foodDatabase.putFoodTag(foodTag,itemKey);
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
        SharedPreferences sharedPrefTernary = context.getSharedPreferences(getString(R.string.ternary_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefPrimary.edit();
        SharedPreferences.Editor editor1 = sharedPrefSecondary.edit();
        SharedPreferences.Editor editor2 = sharedPrefTernary.edit();

        // get a keySet, loop through it and populate both sharedPreferences files
        int count = 0;
        String intString = Integer.toString(count);
        Set keySet = foodDatabase.getDatabaseKeys();
        Iterator iter = keySet.iterator();
        while (iter.hasNext()){
            String iterKey = (String) iter.next();
            editor.putString(intString,iterKey);
            editor1.putInt(iterKey,foodDatabase.getTag(iterKey).getDate());
            editor2.putString(iterKey,foodDatabase.getTag(iterKey).getID());
            editor.commit();
            editor1.commit();
            editor2.commit();
            count++;
            intString = Integer.toString(count);
        }
    }

    public void onStop() {
        super.onStop();

        Context context = getApplicationContext();
        SharedPreferences sharedPrefPrimary = context.getSharedPreferences(getString(R.string.primary_file_key), Context.MODE_PRIVATE);
        SharedPreferences sharedPrefSecondary = context.getSharedPreferences(getString(R.string.secondary_file_key), Context.MODE_PRIVATE);
        SharedPreferences sharedPrefTernary = context.getSharedPreferences(getString(R.string.ternary_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefPrimary.edit();
        SharedPreferences.Editor editor1 = sharedPrefSecondary.edit();
        SharedPreferences.Editor editor2 = sharedPrefTernary.edit();


        // get a keySet, loop through it and populate both sharedPreferences files
            int count = 0;
            String intString = Integer.toString(count);
            Set keySet = foodDatabase.getDatabaseKeys();
            Iterator iter = keySet.iterator();
            while (iter.hasNext()){
                String iterKey = (String) iter.next();
                editor.putString(intString,iterKey);
                editor1.putInt(iterKey, foodDatabase.getTag(iterKey).getDate());
                editor2.putString(iterKey,foodDatabase.getTag(iterKey).getID());
                editor.commit();
                editor1.commit();
                editor2.commit();
                count++;
                intString = Integer.toString(count);
            }
            // get keySet and map intStrings to tagNames (order doesn't matter)


    }
    private foodDatabase readFromWeb(){
        // This takes information from the web
        return foodDatabase;
    }

    private void readData() {
        try {
            InputStream inputStream = clientSocket.getInputStream();

            byte[] buffer = new byte[BUFFER_SIZE];
            String readMessage;
            int byteCount = inputStream.read(buffer);
                if (byteCount < 0 ){
                    readMessage = "null";
                } else {
                    readMessage = new String(buffer, 0, byteCount);
                    Log.v(TAG, "Server received: " + readMessage);

                    Message msg = handler.obtainMessage();
                    Bundle readBundle = new Bundle();
                    readBundle.putString("readMessage", readMessage);
                    msg.setData(readBundle);
                    handler.sendMessage(msg);
                }

        }
        catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * Handles onClick from the send button
     * //@param v
     */
    public void wifiSend(View v) {
        if(clientSocket.isConnected()){
            String outgoingMessage = "a";
            clientSendActions(outgoingMessage);
            //EditText outgoingMessage = (EditText) findViewById(R.id.MsgOutput);
            //clientSendActions(outgoingMessage.getText().toString());
        }
    }

    public void clientConnect(View v){
        clientConnection(IPAddress, SERVER_PORT);
    }


    private void clientSendActions(String outMsg) {
        try {
            Log.v(TAG, "socket.getOutputStream");
            OutputStream outputStream = clientSocket.getOutputStream();
            Log.v(TAG, "gotOutputStream");
            String outMsgWithCr = outMsg.concat("\n");    //add CR to facilitate handling for server and client
            byte[] buffer = new byte[BUFFER_SIZE];
            buffer = outMsgWithCr.getBytes();
            Log.v(TAG, "writing Buffer");
            outputStream.write(buffer);
            Log.v(TAG, "Sent " + outMsg);
        } catch (IOException e) {
            //Catch exception
        }
    }

    /**
     *  Creates the client connection.  Currently is called when the send button is hit.
     * @param host
     * @param port
     */

    private void clientConnection(String host, int port) {
        if(!clientSocket.isConnected()) {
            Log.v(TAG, "Attempting to connect as client");
            try {
                // Create a client socket with the host, port, and timeout information.
                Log.v(TAG, "socket.bind");
                clientSocket.bind(null);
                Log.v(TAG, "INetSocketAddress = " + host + ":" + port);
                InetSocketAddress address = new InetSocketAddress(host, port);
                Log.v(TAG, "socket.connect");
                try {
                    clientSocket.connect(address);
                    Log.v(TAG, "Successfully connected!");
                    View v = null;
                    wifiSend(v);
                } catch (IOException io) {
                    Log.v(TAG, "IO");
                } catch (IllegalArgumentException ia) {
                    Log.v(TAG, "IA");
                }
            } catch (IOException e) {
                // catch logic
            }
        }
    }

    /**
     * Currently deprecated and updating.
     */
    public static class FileServerAsyncTask extends AsyncTask<Void, Void, String> {

        private Context context;
        private TextView statusText;

        public FileServerAsyncTask(Context context, View statusText) {
            this.context = context;
            this.statusText = (TextView) statusText;
        }

        @Override
        protected String doInBackground(Void... params) {
            Log.v(TAG, "doInBackground Started!");
            try {

                // Create a server socket and wait for client connections. This
                //  call blocks until a connection is accepted from a client
                Log.v(TAG, "doInBackground Started!");
                ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
                Log.v(TAG, "Server socket opened");
                Socket client = serverSocket.accept();
                Log.v(TAG, "Server socket connection accepted");

                // If this code is reached, a client has connected and
                //  transferred data.
                InputStream inputStream = client.getInputStream();
                OutputStream outputStream = client.getOutputStream();

                byte[] buffer = new byte[BUFFER_SIZE];
                byte[] bufferOut = new byte[BUFFER_SIZE];

                int byteCount = inputStream.read(buffer);
                String readMessage = new String(buffer, 0, byteCount);
                Log.v(TAG, "Server received: " + readMessage);
                String outMessage = "ME202 - Hey back!";
                bufferOut = outMessage.getBytes();
                outputStream.write(bufferOut);
                Log.v(TAG, "Output stream written.");
                return readMessage;
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                statusText.setText(result);
            }
        }
    }
    private void updateView(){
        Context context = getApplicationContext();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        // Go through the buttons and update their text fields using the foodDatabase

        LinearLayout layout = (LinearLayout)findViewById(R.id.buttonLayout);

        for (int i = 0; i<layout.getChildCount();i++){
            Button v = (Button) layout.getChildAt(i);
            if (v instanceof Button){
                String name = getResources().getResourceEntryName(v.getId());
                TextView dateView = (TextView) findViewById(R.id.date_zero);
                TextView dateTextView = (TextView) findViewById(R.id.textView7);
                int padding = 10;
                int buttonWidth = width-(dateView.getWidth() + dateTextView.getWidth() - padding);
                v.setWidth(buttonWidth);
               // v.getBackground().setColorFilter(0xE040FB, PorterDuff.Mode.MULTIPLY);
                if (foodDatabase.getDatabaseKeys().contains(name)) {
                    foodTag tag = foodDatabase.getTag(name);
                    CharSequence nameUpdate = tag.getID();
                    v.setText(nameUpdate);
                }

            }
        }

        LinearLayout dateLayout = (LinearLayout) findViewById(R.id.dateLayout);

        for (int i = 0; i<dateLayout.getChildCount();i++){
            TextView date = (TextView) dateLayout.getChildAt(i);
            String name = getResources().getResourceEntryName(date.getId());
            name = name.substring(5);
            Set keySet = foodDatabase.getDatabaseKeys();
            Iterator iter = keySet.iterator();
            while (iter.hasNext()){
                String iterKey = (String) iter.next();
                if (iterKey.equals(name)){
                    foodTag foodTag = foodDatabase.getTag(iterKey);
                    date.setText(Integer.toString(foodTag.getDate()));

                }
            }
        }
        return;
    }

    private void parseWifiString(String wifiString){
        String dateString = "";
        int date = 0;
        String tagNumberString = "";
        int tagNumber = 0;
        foodTag tag = null;
        while (!wifiString.isEmpty()){
            if (wifiString.charAt(0) == 'H') return;
            if (wifiString.charAt(0) == ';'){
                tagNumberString = tagNumberString.substring(1);
                tagNumber = Integer.parseInt(tagNumberString);
                tag = foodDatabase.getTag(NUMBERS[tagNumber]);
                tag.setDate(date/10);
                dateString = "";
                tagNumberString = "";
            }
            if (wifiString.charAt(0) == ':'){
                if(dateString.charAt(0) == ';'){
                    date = Integer.parseInt(dateString.substring(1));
                } else{
                    date = Integer.parseInt(dateString);
                }
                tagNumberString = "";
            }
            tagNumberString = tagNumberString + wifiString.substring(0,1);
            dateString = dateString + wifiString.substring(0,1);
            wifiString = wifiString.substring(1);
        }
        return;
    }
}
