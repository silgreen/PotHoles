package com.example.potholes.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.potholes.R;
import com.example.potholes.RilevazioneActivity;
import com.example.potholes.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SharedPreferences sharedPreferences = root.getContext().getSharedPreferences("info", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username","UTENTE");
        Log.d("shared preferences",username);
        TextView textHome = root.findViewById(R.id.text_home);
        textHome.append(" " + username);



        Button rilevaButton = root.findViewById(R.id.rilevaButton);
        rilevaButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), RilevazioneActivity.class);
            startActivity(intent);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}