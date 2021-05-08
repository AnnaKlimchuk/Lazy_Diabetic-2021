package com.example.lazy_diabetic;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import android.arch.persistence.room.Dao;

import java.util.List;
import java.util.Date;

@Dao
@TypeConverters({DateConverter.class})
public interface MeasurementDao {
    @Insert
    void insert(Measurement measurement);

    @Delete
    void delete(Measurement measurement);

    @Update
    void update(Measurement measurement);

    @Query("SELECT * FROM measurement")
    List<Measurement> loadAll();

    @Query("SELECT * FROM measurement where measurement_date_and_time >= :measurement_date_start and measurement_date_and_time <= :measurement_date_end")
    List<Measurement> loadBetweenDates(Date measurement_date_start, Date measurement_date_end);

    @Query("SELECT * FROM measurement where measurement_type = :measurement_type")
    List<Measurement> getMeasurementByType(String measurement_type);

    @Query("SELECT * FROM measurement WHERE id = :id")
    Measurement getById(long id);
}