package com.example.potholes.ui.eventi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.potholes.R;
import com.example.potholes.entity.Evento;
import java.util.List;

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
        holder.getItem().setText(eventoList.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return eventoList.size();
    }

    public static class EventiViewHolder extends RecyclerView.ViewHolder {
        private final TextView item;

        public TextView getItem() {
            return item;
        }

        public EventiViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.itemText);
        }
    }
}
