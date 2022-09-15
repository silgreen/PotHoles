package com.example.potholes.communication;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.example.potholes.RilevazioneEventiAdapter;
import com.example.potholes.entity.Evento;
import com.example.potholes.services.PosizioneService;
import com.example.potholes.ui.eventi.EventiAdapter;
import com.example.potholes.ui.rilevazione.RilevazioneViewModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SocketClient {
    private final Context context;
    private final String LISTA = "lista";
    private BufferedReader reader;
    private PrintWriter writer;
    private Socket socket;

    public SocketClient(Context context) {
        this.context = context;
    }

    private final Runnable listaThread = () -> {

    };

    private final Runnable sogliaThread = () -> {
        initSocket();
        sendUsername();
        if(checkResponseOK()) {
            richiestaSoglia();
            leggiSoglia();
        }
        socket = null;
    };

    private void initSocket() {
        if(socket == null) {
            try {
                InetAddress serverAddress = InetAddress.getByName("172.26.155.19");
                socket = new Socket(serverAddress,8080);
                writer = new PrintWriter(socket.getOutputStream(),true);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
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
    public void startListaRequest(PosizioneService posizioneService) {
        Runnable listaThread = () -> {
            Location location;
            initSocket();
            sendUsername();
            if(checkResponseOK()) {
                richiestaLista();
                if(checkResponseOK()) {
                    synchronized (posizioneService) {
                        posizioneService.retrieveLocation();
                        while((posizioneService.getPosizione()) == null) try {
                            posizioneService.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        location = posizioneService.getPosizione();
                        Log.d("valore di location all'interno di syncro",location.toString());
                        writer.println(getUsernameFromPreferences() + ";" + location.getLatitude() + ";" + location.getLongitude());
                    }
                    String s = " ";
                    while(s != null) {
                        try {
                            s = reader.readLine();
                            if(s != null) {
                                String[] arr = s.split(";", 4);
                                Evento evento = new Evento(Double.valueOf(arr[2]),Double.valueOf(arr[3]),arr[1]);
                                Evento.EventoListClass.getEventoListEventiVicini().add(evento);
                                Log.d("evento dopo lo split",evento.toString());
                            } else Log.d("risultato negativo","non ci sono eventi vicini");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d("contenuto lista vicini",Evento.EventoListClass.getEventoListEventiVicini().toString());
                }
            }
        };
        new Thread(listaThread).start();
    }

    public void richiestaLista() {
        String LISTA = "lista";
        writer.println(LISTA);
    }

    public void startEventoRequest(Evento evento) {
        Runnable eventoThread = () -> {
            initSocket();
            sendUsername();
            if(checkResponseOK()) {
                richiestaEvento();
                if(checkResponseOK()) {
                    writer.println(getUsernameFromPreferences() + ";" + evento.toString());
                    try {
                        String tipoEvento = reader.readLine();
                        Log.d("tipo evento",tipoEvento);
                        evento.setTipoEvento(tipoEvento);
                        Evento.EventoListClass.getEventoListRilevazione().add(evento);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    socket = null;
                }
            }
        };
        new Thread(eventoThread).start();
    }
}
