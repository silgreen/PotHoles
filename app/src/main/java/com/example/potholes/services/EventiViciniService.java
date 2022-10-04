package com.example.potholes.services;

import android.content.Context;
import android.media.metrics.Event;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.potholes.communication.SocketClient;
import com.example.potholes.entity.Evento;
import com.example.potholes.ui.eventi.EventiFragment;
import com.example.potholes.ui.eventi.EventiViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class EventiViciniService {
    private final PosizioneService posizioneService;
    private final SocketClient socketClient;
    private static List<Evento> eventoList;
    private final Callable<List<Evento>> callable = new Callable<List<Evento>>() {
        @Override
        public List<Evento> call() throws Exception {
            Log.d("TAG", "sono nel callable");
            eventiViciniRequest();
            return eventoList;
        }
    };

    public Callable<List<Evento>> getCallable() {
        return callable;
    }

    public List<Evento> getEventoList() {
        return eventoList;
    }

    public EventiViciniService(PosizioneService posizioneService, SocketClient socketClient) {
        if(eventoList == null) eventoList = new ArrayList<>();
        this.posizioneService = posizioneService;
        this.socketClient = socketClient;
    }

    public void eventiViciniRequest() {
        synchronized (posizioneService) {
            while (posizioneService.getPosizione() == null) {
                try {
                    posizioneService.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            socketClient.startEventiViciniRequest(posizioneService.getPosizione(),eventoList);
        }
    }
}
