package com.example.sensordetector;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SensorTable {
    public static DataHelper dataHelper = new DataHelper(null, null, null, 1);
    public static Cursor cursor;

    public static ArrayList<LogSensor> getAll() {
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        String sql = "select * from sensor_data";
        cursor = db.rawQuery(sql, null);
        ArrayList<LogSensor> data = new ArrayList<>();
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            data.add(
                    new LogSensor(
                            cursor.getString(1),
                            cursor.getFloat(2),
                            cursor.getFloat(3),
                            cursor.getFloat(4)
                    )
            );
        }
        return data;
    }

    public static void insert(float x, float y, float z) {
        SQLiteDatabase db = dataHelper.getWritableDatabase();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        DecimalFormat df = new DecimalFormat("#.##");
        String strDate = dateFormat.format(new Date());
        String sql = "insert into sensor_data(time, x, y, z) values('" + strDate + "'," + df.format(x) + "," + df.format(y) + "," + df.format(z) + ")";
        db.execSQL(sql);
    }
}
