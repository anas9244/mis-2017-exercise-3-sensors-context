package com.example.anas.exercise03;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Created by Anas on 18.05.2017.
 */

public class GraphManager {

    public ArrayList<SensorObject> sensors=new ArrayList<SensorObject>();
    public double[] _FFTResult;
    public SensorObject sensorData=new SensorObject(0.0d,0.0d,0.0d,0.0d);
    private FFT _FFT;
    private int winSize;
    public void setWindowSize(int windowSize)
    {
        winSize = (int)Math.pow(2,windowSize);
        sensors.clear();
        _FFT = new FFT(winSize);
        for(int i=0 ; i < winSize ; i++)
            sensors.add(new SensorObject(0.0d,0.0d,0.0d,0.0d));
        CalculateFFT();
    }

    public double[] pushPoints(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            sensorData.x = (double) event.values[0];
            sensorData.y = (double) event.values[1];
            sensorData.z = (double) event.values[2];
            sensorData.omegaG = Math.abs(Math.sqrt(sensorData.x*sensorData.x+sensorData.y*sensorData.y+sensorData.z *sensorData.z )-9.8);
            if(winSize <= sensors.size())
                sensors.remove(0);
            sensors.add(new SensorObject(sensorData.x,sensorData.y,sensorData.z,sensorData.omegaG));
            CalculateFFT();
        }
        return _FFTResult;
    }

    private void CalculateFFT() {
        double[] actual = new double[winSize];
        double[] imaginary = new double[winSize];
        for (int i = 0; i < winSize; i++)
            actual[i] = sensors.get(i).omegaG;
        Arrays.fill(imaginary, 0.0d);
        _FFT = new FFT(winSize);
        _FFT.fft(actual, imaginary);
        _FFTResult = new double[winSize];
        for (int i = 0; i < winSize; i++)
            _FFTResult[i] = Math.sqrt(actual[i] * actual[i] + imaginary[i] * imaginary[i]);
    }
    public int GetActivity()
    {
        //We calculated the average of  _FFTResult data and then recognize the activity
        double average = 0.0d;
        for(int i = 0 ; i < winSize ; i++){
            average += _FFTResult[i];
        }
        average =Math.round( average / winSize);
        int activity=0;
        if(average <= 15.0){
            activity = 0;
        }
        else if(average <= 25.0) {
            activity = 1;
        }
        else {
            activity = 2;
        }
        return activity;
    }

}
