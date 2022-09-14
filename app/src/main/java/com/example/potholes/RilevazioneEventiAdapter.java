package com.example.potholes;

import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.potholes.entity.Evento;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class RilevazioneEventiAdapter extends RecyclerView.Adapter<RilevazioneEventiAdapter.RilevazioneViewHolder> {

    private final List<Evento> eventoList;

    public RilevazioneEventiAdapter(List<Evento> eventoList) {
        this.eventoList = eventoList;
    }

    public static class RilevazioneViewHolder extends RecyclerView.ViewHolder {

        private final TextView item;
        private final Geocoder geocoder;

        public RilevazioneViewHolder(@NonNull View itemView) {
            super(itemView);
            geocoder = new Geocoder(itemView.getContext(), Locale.getDefault());
            item = itemView.findViewById(R.id.itemText);
        }

        public TextView getItem() {
            return item;
        }
    }

    @NonNull
    @Override
    public RilevazioneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_row_item,parent,false);
        return new RilevazioneViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RilevazioneViewHolder holder, int position) {
        try {
            holder.getItem().setText(eventoList.get(position).eventoAddress(holder.geocoder));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return eventoList.size();
    }
}
