package com.example.sensordetector;

public class LogSensor {
    final String timestamp;
    final float x;
    final float y;
    final float z;

    public LogSensor(String timestamp, float x, float y, float z) {
        this.timestamp = timestamp;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
