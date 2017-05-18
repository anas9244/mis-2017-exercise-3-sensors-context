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
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.robinhood.spark.SparkAdapter;
import com.robinhood.spark.SparkView;

import android.os.Handler;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends Activity implements SensorEventListener {

    long lastUpdate, sampleRate;
    int intrate;
     double[] yData= null;
    TextView tvRate, tvWinSize;
    SensorManager sm;
    Sensor accel;
    AccelView accView;
    SparkView sparkView;
    public SensorObject sensorData = new SensorObject(0.0d,0.0d,0.0d,0.0d);
    GraphManager graphManagerger= new GraphManager();
    NotificationCompat.Builder notifyBuilder;
    NotificationManager notificationManager;
    int currentActivity;

    Button btnMusic;
    final Context context = this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvRate = (TextView) findViewById(R.id.tvSampleRate);
        tvWinSize = (TextView) findViewById(R.id.tvWinsize);
        SeekBar sbFFT = (SeekBar) findViewById(R.id.seekbarFFT);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel=sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sparkView = (SparkView) findViewById(R.id.sparkview);
        accView = (AccelView) findViewById(R.id.accView);
        SeekBar sbAcc = (SeekBar) findViewById(R.id.seekbarACC);
        graphManagerger.setWindowSize(5);
        sampleRate = 100;
        tvWinSize.setText("Window size: " + 5);
        tvRate.setText("Rate: " + 100 + "");
        sparkView.invalidate();

        accView = (AccelView) findViewById(R.id.accView);
        accView.sensorData=graphManagerger.sensorData;






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

        sbFFT.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                graphManagerger.setWindowSize(progress);
                tvWinSize.setText("Window size: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        notifyBuilder =
                new NotificationCompat.Builder(this).setSmallIcon(R.drawable.sitting)
                        .setContentTitle("Activity").setContentText("Recognizing your activity...").setOngoing(true);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(10815, notifyBuilder.build());
        //check Activity in background and change the notification
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        sleep(1000);
                        int activity = graphManagerger.GetActivity();
                        if (currentActivity != activity) {
                            int iconId = 0;
                            String message = "";
                            switch (activity) {
                                case 0:
                                    iconId = R.drawable.sitting;
                                    message = "You are sitting !";
                                    break;
                                case 1:
                                    iconId = R.drawable.walking;
                                    message = "You are walking !";
                                    break;
                                case 2:
                                    iconId = R.drawable.running;
                                    message = "You are running !";
                                    break;
                            }
                            notifyBuilder.setContentText(message);
                            notifyBuilder.setSmallIcon(iconId);
                            notificationManager.notify(10815, notifyBuilder.build());
                            currentActivity = activity;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();

        btnMusic= (Button) findViewById(R.id.btnMusic);
        btnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Music.class);
                startActivity(intent);
            }
        });

    }



        @Override
        public  void onSensorChanged(SensorEvent event) {

            graphManagerger.pushPoints(event);

            long now = System.currentTimeMillis();
            if (now - lastUpdate > sampleRate) {
                accView.sensorData = graphManagerger.sensorData;
                accView.invalidate();
                lastUpdate = now;
            }

            yData= graphManagerger._FFTResult;
            sparkView.setAdapter(new SparkAdapter() {
                @Override
                public int getCount() {
                    return yData.length;
                }

                @Override
                public Object getItem(int index) {
                    return yData[index];
                }

                @Override
                public float getY(int index) {
                    return ((float) yData[index]);
                }
            });
            sparkView.invalidate();



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


    }


}

