package com.example.potholes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import com.example.potholes.services.RilevazioneService;

public class RilevazioneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rilevazione);

        String [] dati = new String[] {"mela","banana","caffè"};
        RecyclerView recyclerView = findViewById(R.id.recyclerViewEventiRilevazione);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RilevazioneEventiAdapter(dati));

        RilevazioneService rilevazioneService = new RilevazioneService(this);
        rilevazioneService.startRilevazione();

        Button interrompiButton= findViewById(R.id.interrompiButton);
        interrompiButton.setOnClickListener(v-> {
            rilevazioneService.stopRilevazione();
            //Intent intent = new Intent(this,HomePage.class);
            //startActivity(intent);

        });
    }
}