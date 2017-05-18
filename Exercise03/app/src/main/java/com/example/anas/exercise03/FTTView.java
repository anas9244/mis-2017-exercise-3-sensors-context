package com.example.anas.exercise03;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Created by Anas on 18.05.2017.
 */

public class FTTView {
    public ArrayList<SensorObject> sensors=new ArrayList<SensorObject>();
    public double[] _FFTResult;
    public SensorObject sensorData=new SensorObject(0.0d,0.0d,0.0d,0.0d);
    private FFT _FFT;
    private int winSize;

}
