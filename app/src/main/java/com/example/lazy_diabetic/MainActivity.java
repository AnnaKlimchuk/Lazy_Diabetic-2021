package com.example.lazy_diabetic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    Bundle b = new Bundle();
    private ImageView imageGlucometer, imageSyringe, imageMeal, imageGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageGlucometer = findViewById(R.id.glucometer_picture);
        imageGlucometer.setOnClickListener(view -> {
            Intent startActivity = new Intent(MainActivity.this, MeasurementActivity.class);
            b.putString("activityType", "glucometer");
            startActivity.putExtras(b);
            startActivity(startActivity);
        });

        imageSyringe = findViewById(R.id.syringe_picture);
        imageSyringe.setOnClickListener(view -> {
            Intent startActivity = new Intent(MainActivity.this, MeasurementActivity.class);
            b.putString("activityType", "syringe");
            startActivity.putExtras(b);
            startActivity(startActivity);
        });

        imageMeal = findViewById(R.id.meal_picture);
        imageMeal.setOnClickListener(view -> {
            Intent startActivity = new Intent(MainActivity.this, MeasurementActivity.class);
            b.putString("activityType", "meal");
            startActivity.putExtras(b);
            startActivity(startActivity);
        });

        imageGraph = findViewById(R.id.graph_picture);
        imageGraph.setOnClickListener(view -> {
            Intent startActivity = new Intent(MainActivity.this, GraphsActivity.class);
            startActivity(startActivity);
        });

    }
}
