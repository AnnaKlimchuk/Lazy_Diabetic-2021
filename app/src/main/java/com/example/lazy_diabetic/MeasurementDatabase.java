package com.example.lazy_diabetic;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;


@Database(entities = {Measurement.class}, version = 1)
public abstract class MeasurementDatabase extends RoomDatabase {
    public abstract MeasurementDao MeasurementDao();
}