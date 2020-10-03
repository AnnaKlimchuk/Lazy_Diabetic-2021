package com.example.lazy_diabetic;

import android.app.Application;
import android.arch.persistence.room.Room;

public class LazyDiabeticApplication extends Application {
    private static MeasurementDatabase database;

    public static MeasurementDao getMeasurementDao() {
        return database.MeasurementDao();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        database = Room.databaseBuilder(this, MeasurementDatabase.class, "database")
                .build();
    }
}
