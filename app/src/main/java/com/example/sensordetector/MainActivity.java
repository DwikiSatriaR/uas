package com.example.sensordetector;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    NavController navController;

    private Handler handler;
    private SensorManager sensorManager;
    private Sensor sensor;
    public float[] sensorValues;

    Runnable writeSensorLog = new Runnable() {
        @Override
        public void run() {
            try {
                if (sensorValues != null) {
                    SensorTable.insert(sensorValues[0], sensorValues[1], sensorValues[2]);
                    handler.postDelayed(writeSensorLog, 1000 * 60 * 2);
                } else {
                    handler.postDelayed(writeSensorLog, 1000 * 2);
                }
            } finally {
                // Delay every 2 minutes

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navController = Navigation.findNavController(this, R.id.myNavHostFragment);
        NavigationUI.setupActionBarWithNavController(this, navController);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // Register sensor listener
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        handler = new Handler();
        startWriteSensorLogs();
    }

    private void startWriteSensorLogs() {
        writeSensorLog.run();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        sensorValues = event.values;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}