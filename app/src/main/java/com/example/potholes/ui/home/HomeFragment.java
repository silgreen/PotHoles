package com.example.potholes.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.potholes.R;
import com.example.potholes.RilevazioneActivity;

public class HomeFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("info", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username","UTENTE");
        Log.d("shared preferences",username);
        TextView textHome = view.findViewById(R.id.text_home);
        textHome.append(" " + username);
        Button rilevaButton = view.findViewById(R.id.rilevaButton);
        rilevaButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), RilevazioneActivity.class);
            startActivity(intent);
        });
        return view;
    }
}