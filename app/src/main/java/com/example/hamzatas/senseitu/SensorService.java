package com.example.hamzatas.senseitu;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by hamzatas on 9/18/17.
 */

public class SensorService extends Service {
    private SensorManager mSensorManager;
    private Sensor mlight;
    private SensorEventListener listen;
    private float data;
    private final int READ_PERIOD = 5000;
    public static final String ACTION_DATA = SensorService.class.getName() + "DataBroadcast", EXTRA_DATA = "extra_data";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(getApplicationContext(), "Started", Toast.LENGTH_SHORT).show();
        super.onCreate();
        // Register the handler
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                logLightData();
                new Handler().postDelayed(this, READ_PERIOD);
            }
        }, READ_PERIOD);
    }

    private void logLightData() {
        Log.d("ON_SENSOR_CHANGE", "Sensor Data: " + data);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mSensorManager = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);

        mlight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        listen = new SensorListen();

        mSensorManager.registerListener(listen, mlight, SensorManager.SENSOR_DELAY_NORMAL);


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mSensorManager.unregisterListener(listen);
        Toast.makeText(this, "Destroy", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    public class SensorListen implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            data = event.values[0];
            sendBroadcastMessage(data);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {


        }
    }

    private void sendBroadcastMessage(float data) {
        Intent intent = new Intent(this.ACTION_DATA);
        intent.putExtra(EXTRA_DATA, (double)data);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


}


