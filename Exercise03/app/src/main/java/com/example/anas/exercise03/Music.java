package com.example.anas.exercise03;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class Music extends AppCompatActivity implements SensorEventListener, MediaPlayer.OnPreparedListener, android.location.LocationListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private MediaPlayer mediaPlayerJog, mediaPlayerBike;
    private Button pickRunFile, pickBikeFile;
    private TextView tSpeed;
    private boolean buttonRun;
    private LocationManager mLocationClient;
    boolean runPressed;
    int speed;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        mLocationClient = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mLocationClient.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        // Init Media players for both activities
        mediaPlayerJog = new MediaPlayer();
        mediaPlayerBike = new MediaPlayer();


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        pickRunFile = (Button) findViewById(R.id.btnRun);
        pickRunFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runPressed=true;
                mediaPlayerJog.release();
                mediaPlayerJog = new MediaPlayer();
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 10);
            }
        });


        pickBikeFile = (Button) findViewById(R.id.btnBike);
        pickBikeFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonRun = false;
                /*
                Release and initialize Media Player every time the button is pressed in order to allow the user to change the song.
                */
                mediaPlayerBike.release();
                mediaPlayerBike = new MediaPlayer();
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 10);
            }
        });

        textView= (TextView) findViewById(R.id.textView2);


    }

    @Override
    public void onLocationChanged(Location location) {
       // edit the speed when locationn changes, Speed is in Km per hour
        if (location != null) {
            speed = (int) ((location.getSpeed() * 3600) / 1000);
            Log.d("Speed: ", String.valueOf(speed));

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //Check if user is moving in the first plcae
            if ((getAccelerometer(event) > 0.9 && getAccelerometer(event) < 1.1)) {
               //not moving, then stop music
                mediaPlayerBike.pause();
                mediaPlayerJog.pause();
                textView.setText("No music, GET MOVIN'!!");
            } else {
              //moving, then we use the speed detected from location to determine the activity
                if (speed > 1.9 && speed <= 10) {
                   //running, Speed > 1.5 and <= 11 km/h
                    mediaPlayerBike.pause();
                    mediaPlayerJog.start();
                    textView.setText("Playing jogging track. Your speed "+ speed+" Km/h");

                }
                if (speed > 10 && speed <= 20) {
                   //cycling, Speed > 10 and <= 20 km/h
                    mediaPlayerJog.pause();
                    mediaPlayerBike.start();
                   textView.setText("Playing cycling track. Your speed "+ speed+" Km/h");
                }
                if (speed > 20) {
                   // in a moving vehicle
                    mediaPlayerBike.pause();
                    mediaPlayerJog.pause();
                   textView.setText("No need for music, you'r in car");
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void playRun(Context context, Uri uri) {

        try {
            mediaPlayerJog.setDataSource(context, uri);
            mediaPlayerJog.setOnPreparedListener(this);
            mediaPlayerJog.prepareAsync();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void playBike(Context context, Uri uri) {

        try {
            mediaPlayerBike.setDataSource(context, uri);
            mediaPlayerBike.setOnPreparedListener(this);
            mediaPlayerBike.prepareAsync();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        mp.pause();
    }




    public double getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        //Log.d("Values", "x: " + x + " y: " + y + " z: " + z);

        float accelerationSQRT = (float) ((x * x + y * y + z * z)
                / (9.8 * 9.8));

        return Math.sqrt(accelerationSQRT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == 10) {
            Uri uriSound = data.getData();
            if (buttonRun)
                playRun(this, uriSound);

            else
                playBike(this, uriSound);
        }
    }
}
