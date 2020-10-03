package com.example.lazy_diabetic;

import java.util.Date;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.google.gson.Gson;

@Entity
public class Measurement {

    @PrimaryKey
    public long id;
    @TypeConverters({DateConverter.class})
    public Date measurement_date_and_time;
    public String measurement_type;
    public float measurement_value;

    public Measurement(){
        this.id = -1;
        this.measurement_date_and_time = new Date(System.currentTimeMillis());
        this.measurement_type = "";
        this.measurement_value = -1;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public float getValue() {
        return measurement_value;
    }

    public float getId() {
        return id;
    }

    public Date getDate() {
        return measurement_date_and_time;
    }

    public String getDateString() {
        return DateConverter.fromDate(measurement_date_and_time);
    }
}
