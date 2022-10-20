package com.example.potholes.communication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.potholes.HomePage;
import com.example.potholes.RilevazioneActivity;
import com.example.potholes.entity.Evento;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashSet;
import java.util.Set;

public class SocketClient{
    private final Context context;
    private BufferedReader reader;
    private PrintWriter writer;
    private Socket socket;

    public SocketClient(Context context) {
        this.context = context;
    }

    private final Runnable sogliaThread = () -> {
        initSocket();
        if(socket.isConnected()) {
            sendUsername();
            if (checkResponseOK()) {
                richiestaSoglia();
                leggiSoglia();
            }
        }
        socket = null;
    };

    private void initSocket() {
        if(socket == null) {
            try {
                InetAddress serverAddress = InetAddress.getByName("172.17.159.77");
                socket = new Socket();
                SocketAddress socketAddress = new InetSocketAddress(serverAddress,8080);
                socket.connect(socketAddress,2000);
                writer = new PrintWriter(socket.getOutputStream(),true);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "connessione al server fallita", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context,HomePage.class);
                        context.startActivity(intent);
                    }
                });
            }
        }
    }

    public String getUsernameFromPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("info",Context.MODE_PRIVATE);
        return sharedPreferences.getString("username","UTENTE");
    }

    private void sendUsername() {
        String username = getUsernameFromPreferences();
        writer.println(username);
    }

    private boolean checkResponseOK() {
        String response = "";
        try {
            response = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.equals("ok");
    }

    private void richiestaSoglia() {
        final String SOGLIA = "soglia";
        writer.println(SOGLIA);
    }

    private void leggiSoglia() {
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

    public void startSogliaRequest() {
        new Thread(sogliaThread).start();
    }

    public void richiestaEvento() {
        String EVENTO = "evento";
        writer.println(EVENTO);
    }

    public void richiestaLista() {
        String LISTA = "lista";
        writer.println(LISTA);
    }

    public Set<Evento> deserializeEvento() {
        String s = "";
        Set<Evento> eventoSet = new HashSet<>();
        while (s != null) {
            try {
                s = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(s!= null) {
                String[] arr = s.split(";", 4);
                Evento evento = new Evento(Double.parseDouble(arr[2]), Double.parseDouble(arr[3]), arr[1]);
                eventoSet.add(evento);
            }
        }
        return eventoSet;
    }

    public void startEventiViciniRequest(Location location, Set<Evento> eventoSet) {
        Runnable eventiViciniThread = () -> {
            initSocket();
            if(socket.isConnected()) {
                sendUsername();
                if (checkResponseOK()) {
                    richiestaLista();
                    if (checkResponseOK()) {
                        writer.println(getUsernameFromPreferences() + ";" + location.getLatitude() + ";" + location.getLongitude());
                        eventoSet.addAll(deserializeEvento());

                    }
                }
            }
            socket = null;
        };
        new Thread(eventiViciniThread).start();
    }

    public void startEventoRequest(Evento evento) {
        Runnable eventoThread = () -> {
            initSocket();
            if(socket.isConnected()) {
                sendUsername();
                if (checkResponseOK()) {
                    richiestaEvento();
                    if (checkResponseOK()) {
                        writer.println(getUsernameFromPreferences() + ";" + evento.toStringForSocket());
                        try {
                            String tipoEvento = reader.readLine();
                            Log.d("tipo evento", tipoEvento);
                            evento.setTipoEvento(tipoEvento);
                            Evento.EventoListClass.getEventoListRilevazione().add(evento);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        socket = null;
                    }
                }
            }
        };
        new Thread(eventoThread).start();
    }

    public boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null;
    }
}
