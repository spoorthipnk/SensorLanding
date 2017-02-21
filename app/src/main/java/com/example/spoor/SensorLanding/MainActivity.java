package com.example.spoor.SensorLanding;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    GameView gameView;
    private float xAccel, xVel = 0.0f;
    private float yAccel, yVel = 0.0f;
    private float zAccel, zVel = 0.0f;
    private SensorManager sensorManager;
    private Sensor magnometer;
    private Sensor accelerometer;
    private OrientationData orientationData;
    private long frameTime;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        gameView = new GameView(this);
        setContentView(gameView);
        Point size = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);
        Log.i("size", size.toString());
        gameView.setxMax((float) size.x - 200);
        gameView.setyMax((float) size.y - 800);
        Constants.CURRENT_CONTEXT = this;
        orientationData = new OrientationData();
         orientationData.register();
        updateBall();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

    }

    @Override
    protected void onStart() {
        super.onStart();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, magnometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop() {
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            xAccel = sensorEvent.values[0];
            yAccel = -sensorEvent.values[1];
            zAccel = sensorEvent.values[2];

            updateBall();
        }

    }

    private void updateBall() {

        if (orientationData.getOrientation() != null && orientationData.getStartOrientation() != null) {
            float pitch = orientationData.getOrientation()[1] - orientationData.getStartOrientation()[1];
            float roll = orientationData.getOrientation()[2] - orientationData.getStartOrientation()[2];

            float xSpeed = 2 * roll * Constants.SCREEN_WIDTH / 1000f;
            float ySpeed = pitch * Constants.SCREEN_HEIGHT / 1000f;

            float frameTime = 0.666f;
            xVel += (xAccel * frameTime);
            yVel += (yAccel * frameTime);
            zVel += (zAccel * frameTime);
            xVel += (xSpeed * frameTime);
            yVel += (ySpeed * frameTime);
            float xS = (xVel / 20) * frameTime;
            float yS = (yVel / 60) * frameTime;
            float zS = (zVel /40) * frameTime;
            gameView.setxPos(gameView.getxPos() - xS);

            gameView.setyPos(gameView.getyPos() - yS);
            if(gameView.getyPos()>300){
                if(gameView.getxPos()>-100&&gameView.getxPos()<550){
                    sensorManager.unregisterListener(MainActivity.this);
                   gameOver();
                }
            }
            if(gameView.getyPos()>900){
                if(gameView.getxPos()>-100&&gameView.getxPos()<550){
                    sensorManager.unregisterListener(MainActivity.this);
                    gameOver();
                }
            }
           if(gameView.getyPos()> 700){
                if(gameView.getxPos()>900&&gameView.getxPos()<1440){
                    sensorManager.unregisterListener(MainActivity.this);
                    gameOver();
                }
            }
            if(gameView.getyPos()> 1300){
                if(gameView.getxPos()>900&&gameView.getxPos()<1440){
                    sensorManager.unregisterListener(MainActivity.this);
                    gameOver();
                }
            }
            gameView.setzPos(gameView.getzPos() - zS);

            if (gameView.getxPos() > gameView.getxMax()) {

                gameView.setxPos(gameView.getxMax());

            } else if (gameView.getxPos() < 0) {
                gameView.setxPos(0);
            }

            if (gameView.getyPos() > gameView.getyMax()) {

                gameView.setyPos(gameView.getyMax());
                sensorManager.unregisterListener(MainActivity.this);
                Handler handler = new Handler(getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                MainActivity.this);

                        alertDialogBuilder.setTitle("Well Done!");

                        alertDialogBuilder.setMessage("Good job, smarty pants. Do you want to play again?")
                                .setCancelable(true)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        gameView.setxPos(0);
                                        gameView.setyPos(0);

                                        MainActivity.this.recreate();


                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        MainActivity.this.finish();
                                    }
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                });
            } else if (gameView.getyPos() < 0) {
                gameView.setyPos(0);
            }
            if (gameView.getzPos() > gameView.getzMax()) {
                gameView.setzPos(gameView.getzMax());
            } else if (gameView.getzPos() < 0) {
                gameView.setzPos(0);
            }


        }
    }




    public void gameOver(){

            Handler handler = new Handler(getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        MainActivity.this);

                alertDialogBuilder.setTitle("Game Over");

                alertDialogBuilder.setMessage("Oops! Looks like you got stuck on a tree. Do you want to try again?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();

                                gameView.setxPos(0);
                                gameView.setyPos(0);

                                MainActivity.this.recreate();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                MainActivity.this.finish();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });


    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}




