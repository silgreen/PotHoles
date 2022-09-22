package com.example.potholes.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.media.metrics.Event;
import android.util.Log;

import com.example.potholes.RilevazioneActivity;
import com.example.potholes.communication.SocketClient;
import com.example.potholes.entity.Evento;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class RilevazioneService{
    private final SensorManager sensorManager;
    private final Sensor sensor;
    private final PosizioneService posizioneService;
    private final double soglia;
    private final SocketClient socketClient;
    private double last;

    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            double radix = Math.sqrt(Math.pow(x,2)+Math.pow(y,2)+Math.pow(z,2));
            BigDecimal bigDecimal = new BigDecimal(radix, new MathContext(3));
            double g = 9.81;
            double alpha = bigDecimal.doubleValue() - g;
            if (alpha < -soglia){
                    last = alpha;
            }
            if (alpha > soglia) {
                    last = alpha;
            }
            if(alpha == 0 && last != 0) {
                Evento evento = new Evento(posizioneService.getPosizione(), last);
                socketClient.startEventoRequest(evento);
                last = 0;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };
    public RilevazioneService(Context context,PosizioneService posizioneService){
        this.posizioneService = posizioneService;
        this.socketClient = new SocketClient(context);
        sensorManager=(SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SharedPreferences sharedPreferences = context.getSharedPreferences("info",Context.MODE_PRIVATE);
        this.soglia = Double.parseDouble(sharedPreferences.getString("soglia","NULL"));
    }

    public void startRilevazione(){
        sensorManager.registerListener(sensorEventListener,sensor,SensorManager.SENSOR_DELAY_NORMAL);
        posizioneService.startLocation();
    }

    public void stopRilevazione(){
        sensorManager.unregisterListener(sensorEventListener,sensor);
        posizioneService.stopLocation();
    }

}
