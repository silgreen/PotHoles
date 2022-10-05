package com.example.potholes.ui.loading;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.potholes.R;
import com.example.potholes.services.EventiViciniService;
import com.example.potholes.ui.eventi.EventiFragment;

public class Container extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        handleLoading();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void handleLoading() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                NavDirections navDirections = LoadingFragmentDirections.actionLoadingFragmentToNavigationEventi();
                Navigation.findNavController(Container.this,R.id.fragmentContainerView).navigate(navDirections);
            }
        }, 5000);
    }
}