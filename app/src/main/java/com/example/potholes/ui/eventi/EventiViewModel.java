package com.example.potholes.ui.eventi;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.potholes.entity.Evento;

import java.util.List;

public class EventiViewModel extends ViewModel {
    MutableLiveData<List<Evento>> mutableLiveData;
    List<Evento> eventoListVicini;

    public LiveData<List<Evento>> getMutableLiveData() {
        if(mutableLiveData == null) {
            mutableLiveData = new MutableLiveData<>();
            mutableLiveData.postValue(eventoListVicini);
        }
        return mutableLiveData;
    }

    public void setEventoListVicini(List<Evento> eventoListVicini) {
        this.eventoListVicini = eventoListVicini;
    }

    public boolean fillEventoList(List<Evento> list) {
        return eventoListVicini.addAll(list);
    }
}