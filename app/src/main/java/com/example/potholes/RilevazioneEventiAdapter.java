package com.example.potholes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RilevazioneEventiAdapter extends RecyclerView.Adapter<RilevazioneEventiAdapter.RilevazioneViewHolder> {

    private String[] dataset;

    public RilevazioneEventiAdapter(String[] localset) {
        dataset = localset;
    }

    public static class RilevazioneViewHolder extends RecyclerView.ViewHolder {

        private final TextView item;

        public RilevazioneViewHolder(@NonNull View itemView) {
            super(itemView);

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
        holder.getItem().setText(dataset[position]);
    }

    @Override
    public int getItemCount() {
        return dataset.length;
    }
}
