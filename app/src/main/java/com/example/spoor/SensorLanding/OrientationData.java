package com.example.spoor.SensorLanding;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by spoor on 1/25/2017.
 */

public class OrientationData implements SensorEventListener{
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private float[] accelOutput;
    private float[] magOutput;
    private float[] orientation = new float[3];

    public float[] getOrientation() {
        return orientation;
    }

    private float[] startOrientation = null;
    public float[] getStartOrientation(){
        return  startOrientation;
    }


    public OrientationData(){
        sensorManager = (SensorManager)Constants.CURRENT_CONTEXT.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void register(){
        sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this,magnetometer,SensorManager.SENSOR_DELAY_GAME);

    }

    public void pause(){
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            accelOutput = sensorEvent.values;
        else if(sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            magOutput = sensorEvent.values;

        if(accelOutput!=null&& magOutput!=null){
            float[] R = new float[9];
            float[] I = new float[9];
            boolean success = SensorManager.getRotationMatrix(R,I,accelOutput,magOutput);
            if(success){

                SensorManager.getOrientation(R,orientation);
                if(startOrientation == null){
                    startOrientation = new float[orientation.length];
                    System.arraycopy(orientation,0,startOrientation,0,orientation.length);

                }


            }


        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
