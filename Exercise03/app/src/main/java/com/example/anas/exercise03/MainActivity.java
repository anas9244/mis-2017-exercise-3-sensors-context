package com.example.anas.exercise03;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

import com.robinhood.spark.SparkView;

import android.os.Handler;


public class MainActivity extends Activity implements SensorEventListener {

    long lastUpdate, sampleRate;
    int intrate;

    TextView tvRate, tvWinSize;
    SensorManager sm;
    Sensor accel;
    AccelView accView;
    SparkView sparkView;
    public SensorObject sensorData = new SensorObject(0.0d,0.0d,0.0d,0.0d);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvRate = (TextView) findViewById(R.id.tvSampleRate);
        tvWinSize = (TextView) findViewById(R.id.tvWinsize);
        SeekBar sbFFT = (SeekBar) findViewById(R.id.seekbarFFT);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sparkView = (SparkView) findViewById(R.id.sparkview);
        accView = (AccelView) findViewById(R.id.accView);
        SeekBar sbAcc = (SeekBar) findViewById(R.id.seekbarACC);
        magnitudeData.setWindowSize(5);
        sampleRate = 100;
        tvWinSize.setText("Window size: " + 5);
        tvRate.setText("Rate: " + 100 + "");
        accView = (AccelView) findViewById(R.id.accView);
        accView.sensorData=sensorData;





        sbAcc.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               sampleRate = progress;
                tvRate.setText("Rate: " + progress + "");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }



        @Override
        public  void onSensorChanged(SensorEvent event) {

            calculateMagnitude(event);

            long now = System.currentTimeMillis();
            if (now - lastUpdate > sampleRate) {
                accView.sensorData = sensorData;
                accView.invalidate();
                lastUpdate = now;
            }




        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }




    @Override
    public void onResume() {
        super.onResume();
        sm.registerListener(this, accel, SensorManager.SENSOR_DELAY_UI);


    }

    @Override
    public void onPause() {
        super.onPause();
        sm.unregisterListener(this);

    }

    public void calculateMagnitude(SensorEvent event) {
         if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            sensorData.x = (double) event.values[0];
            sensorData.y = (double) event.values[1];
            sensorData.z = (double) event.values[2];
            sensorData.omegaG = Math.abs(Math.sqrt(sensorData.x*sensorData.x+sensorData.y*sensorData.y+sensorData.z *sensorData.z )-9.8);
/*
            if(winSize <= sensors.size())
                sensors.remove(0);
            sensors.add(new SensorObject(sensorData.x,sensorData.y,sensorData.z,sensorData.omegaG));
            CalculateFFT();
            */

        }

    }
}

