package com.example.lazy_diabetic;

import java.util.Locale;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import android.arch.persistence.room.TypeConverter;

public class DateConverter {
    static DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);
    static DateFormat timeFormat = new SimpleDateFormat("HH:mm");
    static DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    static DateFormat dateFormatGraph = new SimpleDateFormat("dd.MM");

    @TypeConverter
    public static String fromDate(Date date) {
        return df.format(date);
    }

    @TypeConverter
    public static Date toDate(String date_str) {
        try {
            return df.parse(date_str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date(System.currentTimeMillis());
    }
}
