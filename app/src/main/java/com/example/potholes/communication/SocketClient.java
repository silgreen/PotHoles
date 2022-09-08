package com.example.potholes.communication;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.Buffer;
import java.nio.CharBuffer;

public class SocketClient {
    private final Context context;
    private final String SOGLIA = "soglia";
    private final String EVENTO = "evento";
    private final String LISTA = "lista";

    public SocketClient(Context context) {
        this.context = context;
    }

    private String getUsernameFromPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("info",Context.MODE_PRIVATE);
        return sharedPreferences.getString("username","UTENTE");
    }

    private void sendUsername(PrintWriter writer) {
            Log.d("sono sendUsername",getUsernameFromPreferences());
            writer.println(getUsernameFromPreferences());
    }

    private boolean checkResponseOK(BufferedReader reader) {
        String response = "";
        try {
            response = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.equals("ok");
    }

    private void inviaSoglia(PrintWriter writer) {
            writer.println(SOGLIA);
    }

    private void leggiSoglia(BufferedReader reader) {
        String soglia = "";
        try {
            soglia = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        salvaSoglia(soglia);
    }

    private void salvaSoglia(String soglia) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("info",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("soglia",soglia);
        editor.apply();
    }

    public void sogliaRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InetAddress serverAddress = InetAddress.getByName("172.18.227.0");
                    Socket socket = new Socket(serverAddress,8080);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter writer = new PrintWriter(socket.getOutputStream());
                    //sendUsername(writer);
                    writer.println("mario");
                    //if(checkResponseOK(reader)) {
                        //inviaSoglia(writer);
                        //leggiSoglia(reader);
                    //}
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("fine thread","questa è la fine");
            }
        }).start();
    }
}
