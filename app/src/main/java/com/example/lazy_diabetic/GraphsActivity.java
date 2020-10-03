package com.example.lazy_diabetic;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GraphsActivity extends AppCompatActivity {

    TextView TextGlucometer, TextSyringeShort, TextSyringeLong, TextMeal;
    ImageView PictureGlucometer, PictureSyringeShort, PictureSyringeLong, PictureMeal;
    LineChart GraphGlucometer, GraphSyringeShort, GraphSyringeLong, GraphMeal;
    Button DistanceButton;
    static Date dateStart, dateEnd;
    static Calendar calendar;
    static ArrayList<Measurement> measurementList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);

        TextGlucometer = findViewById(R.id.text_glucometer);
        TextSyringeShort = findViewById(R.id.text_syringe_short);
        TextSyringeLong = findViewById(R.id.text_syringe_long);
        TextMeal = findViewById(R.id.text_meal);
        PictureGlucometer = findViewById(R.id.picture_glucometer);
        PictureSyringeShort = findViewById(R.id.picture_syringe_short);
        PictureSyringeLong = findViewById(R.id.picture_syringe_long);
        PictureMeal = findViewById(R.id.picture_meal);
        GraphGlucometer = findViewById(R.id.graph_glucometer);
        GraphSyringeShort = findViewById(R.id.graph_syringe_short);
        GraphSyringeLong = findViewById(R.id.graph_syringe_long);
        GraphMeal = findViewById(R.id.graph_meal);

        DistanceButton = findViewById(R.id.measurement_distance);
        DistanceButton.setOnClickListener(view -> {
            if (DistanceButton.getText().toString().matches("День")) {
                DistanceButton.setText("Неделя");
            } else if (DistanceButton.getText().toString().matches("Неделя")) {
                DistanceButton.setText("Месяц");
            } else {
                DistanceButton.setText("День");
            }

            createDates(DistanceButton.getText().toString());
            loadData();
            truncatedDates();

            showGraph("glucometer", TextGlucometer, PictureGlucometer,
                    GraphGlucometer);
            showGraph("meal", TextMeal, PictureMeal,
                    GraphMeal);
            showGraph("syringe короткий", TextSyringeShort, PictureSyringeShort,
                    GraphSyringeShort);
            showGraph("syringe длинный", TextSyringeLong, PictureSyringeLong,
                    GraphSyringeLong);
         });

        createDates(DistanceButton.getText().toString());
        loadData();
        truncatedDates();

        showGraph("glucometer", TextGlucometer, PictureGlucometer,
                GraphGlucometer);
        showGraph("meal", TextMeal, PictureMeal,
                GraphMeal);
        showGraph("syringe короткий", TextSyringeShort, PictureSyringeShort,
                GraphSyringeShort);
        showGraph("syringe длинный", TextSyringeLong, PictureSyringeLong,
                GraphSyringeLong);
    }

    private static void loadData() {
        LoadMeasurements loadList = new LoadMeasurements();
        loadList.execute(DateConverter.fromDate(dateStart)
                + ";" + DateConverter.fromDate(dateEnd));

        try {
            String loadListStr = loadList.get();
            Gson gson = new Gson();
            Type userListType = new TypeToken<ArrayList<Measurement>>(){}.getType();
            measurementList = gson.fromJson(loadListStr, userListType);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void truncatedDates() {
        try {
            dateStart = new Date(measurementList.stream()
                    .mapToLong(m -> m.measurement_date_and_time.getTime()).min()
                    .orElseThrow(NoSuchElementException::new));
            calendar.setTime(dateStart);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            dateStart = calendar.getTime();

            dateEnd = new Date(measurementList.stream()
                    .mapToLong(m -> m.measurement_date_and_time.getTime()).max()
                    .orElseThrow(NoSuchElementException::new));
            calendar.setTime(dateEnd);
            calendar.add(Calendar.DATE, +1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            dateEnd = calendar.getTime();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private static void createDates(String DateDistance) {
        dateEnd = new Date(System.currentTimeMillis());
        calendar = Calendar.getInstance();
        calendar.setTime(dateEnd);
        calendar.add(Calendar.DATE, +1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        dateEnd = calendar.getTime();
        if (DateDistance.matches("Неделя")) {
            calendar.add(Calendar.DATE, -7);
            dateStart = calendar.getTime();
        } else if (DateDistance.matches("Месяц")) {
            calendar.add(Calendar.MONTH, -1);
            dateStart = calendar.getTime();
        } else {
            calendar.add(Calendar.DATE, -1);
            dateStart = calendar.getTime();
        }
    }

    private static void showGraph(String graphDiscription, TextView graphText,
                                  ImageView graphPicture,  LineChart graphChart) {
        List<Measurement> graphList = measurementList.stream()
                .filter(m -> m.measurement_type.matches(graphDiscription))
                .collect(Collectors.toList());
        List<Entry> points = new ArrayList<>();
        for(int i = 0; i < graphList.size(); i++) {
            Entry point = new Entry(graphList.get(i).measurement_date_and_time.getTime(),
                    graphList.get(i).measurement_value);
            points.add(point);
        }
        if (graphList.size() == 0) {
            graphText.setText(graphDiscription);
            graphChart.clear();
            graphChart.setVisibility(View.GONE);
            graphPicture.setVisibility(View.VISIBLE);
            return;
        } else {
            Double avgValue = new ArrayList<>(graphList.stream()
                    .collect(Collectors.groupingBy(m ->
                            DateConverter.dateFormat.format(m.measurement_date_and_time)))
                    .values())
                    .stream().map(l -> l.stream().mapToDouble(m -> m.measurement_value).sum())
                    .collect(Collectors.toList()).stream().mapToDouble(d -> d).average()
                    .orElse(0);
            graphText.setText(String.format("%s: %.1f %.1f", graphDiscription, graphList.stream()
                    .mapToDouble(m -> m.measurement_value).average().getAsDouble(),
                    avgValue));
            graphChart.setVisibility(View.VISIBLE);
            graphPicture.setVisibility(View.GONE);
        }

        LineDataSet pointsSet =  new LineDataSet(points, graphDiscription);
        pointsSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        List<ILineDataSet> pointsDataSets = new ArrayList<>();
        pointsDataSets.add(pointsSet);
        LineData data = new LineData(pointsDataSets);
        graphChart.setData(data);
        graphChart.getDescription().setText("");
        graphChart.getXAxis().setAxisMinimum(dateStart.getTime());
        graphChart.getXAxis().setAxisMaximum(dateEnd.getTime());
        graphChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                if (dateEnd.getTime() - dateStart.getTime() <= (24 * 60 * 60 * 1000)) {
                    graphChart.getXAxis().setGranularityEnabled(true);
                    graphChart.getXAxis().setGranularity(4 * 60 * 60 * 1000);
                    return DateConverter.timeFormat.format(new Date((long) value));
                } if (dateEnd.getTime() - dateStart.getTime() <= (7 * 24 * 60 * 60 * 1000)) {
                    graphChart.getXAxis().setGranularityEnabled(true);
                    graphChart.getXAxis().setGranularity(2 * 24 * 60 * 60 * 1000);
                }
                return DateConverter.dateFormatGraph.format(new Date((long) value));
            }
        });
        graphChart.invalidate();
    }

    private static class LoadMeasurements extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(final String ... s) {
            String[] tokens = s[0].split(";");
            List<Measurement> measurements = LazyDiabeticApplication.getMeasurementDao()
                    .loadBetweenDates(tokens[0], tokens[1]);
            return measurements.toString();
        }
    }
}