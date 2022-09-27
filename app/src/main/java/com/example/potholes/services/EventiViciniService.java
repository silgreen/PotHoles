package com.example.potholes.services;

import android.content.Context;
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
    private final Callable<List<Evento>> callable = new Callable<List<Evento>>() {
        @Override
        public List<Evento> call() throws Exception {
            List<Evento> eventoList = new ArrayList<>();
            Log.d("TAG", "sono nel callable");
            eventiViciniRequest(eventoList);

            return eventoList;
        }
    };

    public Callable<List<Evento>> getCallable() {
        return callable;
    }

    public EventiViciniService(PosizioneService posizioneService, SocketClient socketClient) {
        this.posizioneService = posizioneService;
        this.socketClient = socketClient;
    }

    public void eventiViciniRequest(List<Evento> eventoList) {
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
