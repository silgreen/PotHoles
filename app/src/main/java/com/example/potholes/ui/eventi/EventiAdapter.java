package com.example.potholes.ui.eventi;

import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.potholes.R;
import com.example.potholes.entity.Evento;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EventiAdapter extends RecyclerView.Adapter<EventiAdapter.EventiViewHolder> {
    private final List<Evento> eventoList;

    public EventiAdapter(List<Evento> eventoList) {
        this.eventoList = eventoList;
    }
    @NonNull
    @Override
    public EventiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_row_item,parent,false);
        return new EventiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventiViewHolder holder, int position) {
        holder.getItem().setText(eventoList.get(position).eventiViciniToString());
    }

    @Override
    public int getItemCount() {
        return eventoList.size();
    }

    public static class EventiViewHolder extends RecyclerView.ViewHolder {
        private final TextView item;
        private final Geocoder geocoder;

        public TextView getItem() {
            return item;
        }

        public EventiViewHolder(@NonNull View itemView) {
            super(itemView);
            geocoder = new Geocoder(itemView.getContext(), Locale.getDefault());
            item = itemView.findViewById(R.id.itemText);
        }
    }
}
