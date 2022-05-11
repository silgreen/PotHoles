package com.example.potholes.services;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.math.BigDecimal;
import java.math.MathContext;

public class RilevazioneService{
    private final SensorManager sensorManager;
    private final Sensor sensor;
    double max;
    double min;

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
            if(alpha == 0) ;

            if(alpha < 0){
                min = Math.min(min,alpha);
                Log.d("DOWN","sto cadendo " + alpha );
                Log.d("MIN", min + "");

            }else if(alpha > 0){
                max = Math.max(max,alpha);
                Log.d("UP","sto salendo "+ alpha);
                Log.d("MAX", max + "");
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };
    public RilevazioneService(Context context){
        sensorManager=(SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void startRilevazione(){
        sensorManager.registerListener(sensorEventListener,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stopRilevazione(){
        sensorManager.unregisterListener(sensorEventListener,sensor);
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }


}
