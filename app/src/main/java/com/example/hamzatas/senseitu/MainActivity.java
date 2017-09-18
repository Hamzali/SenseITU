package com.example.hamzatas.senseitu;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mLight;
    private Sensor mPressure;
    private TextView mText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mPressure = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

        mText = (TextView) findViewById(R.id.sensor_data);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (mLight != null) {
            float lux = sensorEvent.values[0];
            this.dataLogger("light: " + lux);
            mText.setText(lux + " lux");
        } else {
            Log.e("SENSOR_CHANGED", "No light sensor found!");
        }

        if (mPressure != null) {
            float pressure = sensorEvent.values[1];
            this.dataLogger("pressure: "  + pressure);
        } else {
            Log.e("SENSOR_CHANGED", "No pressure sensor found!");
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    private void dataLogger(String d) {
        Log.d("SENSOR_DATA", "read: " + d);
    }
}
