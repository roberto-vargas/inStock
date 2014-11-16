package com.example.roberto.instock;

/**
 * Created by Roberto on 11/5/14.
 */
/**
 * Created by Alex Lin on 9/25/2014.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.Handler;
import android.os.Message;

import android.util.Log;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class WifiThread extends Thread {

    private static final String TAG = "WifiConnect";
    private static final int BUFFER_SIZE = 1024;
    private final String ipAddress;

    // Streams that we read from and write to
    private OutputStream outStream;
    private InputStream inStream;

    // Handlers used to pass data between threads
    private final Handler readHandler;
    //private final Handler writeHandler;

    public WifiThread(String ipAddress, Handler handler) {
        this.ipAddress = ipAddress.toUpperCase();
        this.readHandler = handler;
    }
}