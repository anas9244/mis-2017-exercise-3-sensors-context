/**
 * Created by Anas on 18.05.2017.
 */

package com.example.anas.exercise03;

public  class SensorObject {
    public double x = 0.0d;
    public double y = 0.0d;
    public double z = 0.0d;
    public double omegaG= 0.0d;
    public SensorObject(double x, double y, double z,double omegaG){
        this.x = x;
        this.y = y;
        this.z = z;
        this.omegaG = omegaG;
    }
}
