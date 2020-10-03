package com.example.lazy_diabetic;

import java.util.Calendar;
import java.util.Date;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.Toast;


public class MeasurementActivity extends AppCompatActivity {

    public static final String CURRENT_DATE = "current_date";
    private Spinner buttonMeasurementList;
    private Button buttonSetMeasurement, buttonTime, buttonDate, buttonMeasurementType;
    private static TextView measurementValue;
    private static Calendar calendar =Calendar.getInstance();

    Bundle b;

    Toast toast;

    Date current_date;

    static String activityType = "";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CURRENT_DATE, DateConverter.fromDate(current_date));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement);

        b = getIntent().getExtras();
        if (b != null) {
            activityType = b.getString("activityType");
        }

        if (savedInstanceState != null) {
            current_date = DateConverter.toDate(savedInstanceState.getString(CURRENT_DATE));
        } else {
            current_date = new Date(System.currentTimeMillis());
        }

        calendar.setTime(current_date);
        measurementValue=findViewById(R.id.measurement_value);


        buttonMeasurementType=findViewById(R.id.measurement_type);
        buttonMeasurementList=findViewById(R.id.measurement_list);
        if (activityType.matches("glucometer")) {
            measurementValue.setHint("Показание\nглюкометра");
            buttonMeasurementList.setVisibility(View.GONE);
            buttonMeasurementType.setVisibility(View.GONE);
        } else if (activityType.matches("meal")) {
            measurementValue.setHint("Кол-во");
            buttonMeasurementList.setVisibility(View.VISIBLE);
            buttonMeasurementType.setVisibility(View.VISIBLE);
            buttonMeasurementType.setText("моя еда есть в списке");
            buttonMeasurementType.setOnClickListener(view -> {
                if (buttonMeasurementType.getText().toString().matches("моя еда есть в списке")) {
                    buttonMeasurementList.setVisibility(View.GONE);
                    buttonMeasurementType.setText("моей еды нет в списке");
                    measurementValue.setHint("Кол-во\nхлебных\nединиц");
                } else {
                    buttonMeasurementList.setVisibility(View.VISIBLE);
                    buttonMeasurementType.setText("моя еда есть в списке");
                    measurementValue.setHint("Кол-во");
                }
            });
        } else if (activityType.matches("syringe")) {
            measurementValue.setHint("Доза\nинсулина");
            buttonMeasurementList.setVisibility(View.GONE);
            buttonMeasurementType.setVisibility(View.VISIBLE);
            buttonMeasurementType.setText("короткий");
            buttonMeasurementType.setOnClickListener(view -> {
                if (buttonMeasurementType.getText().toString().matches("короткий")) {
                    buttonMeasurementType.setText("длинный");
                } else {
                    buttonMeasurementType.setText("короткий");
                }
            });
        } else {
            Intent startActivity = new Intent(MeasurementActivity.this, MainActivity.class);
            startActivity(startActivity);
        }

        buttonTime=findViewById(R.id.measurement_time);
        buttonTime.setText("время " + DateConverter.timeFormat.format(current_date));
        buttonTime.setOnClickListener(view -> {
            setTime(view);
        });

        buttonDate=findViewById(R.id.measurement_date);
        buttonDate.setText("дата " + DateConverter.dateFormat.format(current_date));
        buttonDate.setOnClickListener(view -> {
            setDate(view);
        });

        buttonSetMeasurement=findViewById(R.id.set_measurement);
        buttonSetMeasurement.setOnClickListener(view -> {
            if (measurementValue.getText().toString().matches("")) {
                toast = Toast.makeText(getApplicationContext(),
                        "Невозможно ввести пустое показание",
                        Toast.LENGTH_SHORT);
                toast.show();
            } else if (activityType.matches("glucometer")) {
                new CreateMeasurement().execute(activityType);
                Intent startActivity = new Intent(this, MainActivity.class);
                startActivity(startActivity);
                toast = Toast.makeText(getApplicationContext(),
                        "Показание записано в базу",
                        Toast.LENGTH_SHORT);
                toast.show();
            } else if (activityType.matches("syringe")) {
                new CreateMeasurement().execute(activityType + " " +
                        buttonMeasurementType.getText());
                Intent startActivity = new Intent(this, MainActivity.class);
                startActivity(startActivity);
                toast = Toast.makeText(getApplicationContext(),
                        "Показание записано в базу",
                        Toast.LENGTH_SHORT);
                toast.show();
            } else if (activityType.matches("meal")) {
                if (buttonMeasurementType.getText().toString().matches("моя еда есть в списке")) {
                    ;
                } else {
                    new CreateMeasurement().execute(activityType);
                    Intent startActivity = new Intent(this, MainActivity.class);
                    startActivity(startActivity);
                    toast = Toast.makeText(getApplicationContext(),
                            "Показание записано в базу",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            } else {
                toast = Toast.makeText(getApplicationContext(),
                        "Ошибка, невозможно ввести показание",
                        Toast.LENGTH_SHORT);
                toast.show();
                Intent startActivity = new Intent(this, MainActivity.class);
                startActivity(startActivity);
            }
        });
    }

    public void setTime(View v) {
        new TimePickerDialog(this, calendarTime,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true)
                .show();
    }

    public void setDate(View v) {
        new DatePickerDialog(this, calendarDate,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    TimePickerDialog.OnTimeSetListener calendarTime =new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            current_date = calendar.getTime();
            buttonTime.setText("время " + DateConverter.timeFormat.format(current_date));
        }
    };

    DatePickerDialog.OnDateSetListener calendarDate =new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            current_date = calendar.getTime();
            buttonDate.setText("дата " + DateConverter.dateFormat.format(current_date));
        }
    };

    static class CreateMeasurement extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(final String... params) {
            Measurement measurement = new Measurement();
            measurement.id = LazyDiabeticApplication.getMeasurementDao().loadAll().size();
            measurement.measurement_date_and_time = calendar.getTime();
            measurement.measurement_type = params[0];
            measurement.measurement_value = Float.parseFloat(measurementValue.getText().toString());
            LazyDiabeticApplication.getMeasurementDao().insert(measurement);
            return null;
        }

    }
}
