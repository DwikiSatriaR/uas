package com.example.sensordetector;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class SensorFragment extends Fragment {
    private Handler handler;
    private TextView sensorTextView;
    private ListView logListView;
    ArrayAdapter adapter;
    ArrayList<String> result = new ArrayList<>();

    Runnable updateSensorText = new Runnable() {
        @Override
        public void run() {
            try {
                MainActivity activity = (MainActivity) requireActivity();
                DecimalFormat df = new DecimalFormat("#,##");
                sensorTextView.setText(String.format("x = %s, y = %s, z = %s", floatFormat(activity.sensorValues[0]), floatFormat(activity.sensorValues[1]), floatFormat(activity.sensorValues[2])));
            } finally {
                // Delay every 2 seconds
                handler.postDelayed(updateSensorText, 1000 * 2);
            }
        }
    };

    Runnable updateLogListView = new Runnable() {
        @Override
        public void run() {
            try {
                result.clear();
                for (LogSensor log : SensorTable.getAll()) {
                    DecimalFormat df = new DecimalFormat("#,##");
                    result.add(formatDateString(log.timestamp) + "   |  x = " + floatFormat(log.x) + ", y = " + floatFormat(log.y) + ", z = " + floatFormat(log.z));
                }
                adapter.notifyDataSetChanged();
            } finally {
                // Delay every 2 minutes
                handler.postDelayed(updateLogListView, 1000 * 60 * 2);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sensor, container, false);

        sensorTextView = view.findViewById(R.id.textViewValue);
        logListView = view.findViewById(R.id.logList);
        adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, result);
        logListView.setAdapter(adapter);

        handler = new Handler();
        startUpdateSensor();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    private void startUpdateSensor() {
        updateSensorText.run();
        updateLogListView.run();
    }

    void stopRepeatingTask() {
        handler.removeCallbacks(updateSensorText);
    }

    String formatDateString(String dateString) {
        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat newFormat = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault());
        try {
            return newFormat.format(oldFormat.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    String floatFormat(float val) {
        DecimalFormat df = new DecimalFormat("#.#");
        return df.format(val).replace(',', '.');
    }
}