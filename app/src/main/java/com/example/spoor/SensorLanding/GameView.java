package com.example.spoor.SensorLanding;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by spoor on 1/25/2017.
 */

public class GameView extends View {

    private Bitmap ball;
    private Bitmap barb_one;
    private Bitmap barb_two;
    private Bitmap barb_three;
    private Bitmap barb_four;
    private Bitmap ground;


    public float getxMax() {
        return xMax;
    }

    public void setxMax(float xMax) {
        this.xMax = xMax;
    }

    public float getyMax() {
        return yMax;
    }

    public void setyMax(float yMax) {
        this.yMax = yMax;
    }

    public float getxPos() {
        return xPos;
    }

    public void setxPos(float xPos) {
        this.xPos = xPos;
    }

    public float getyPos() {
        return yPos;
    }

    public void setyPos(float yPos) {
        this.yPos = yPos;
    }

    private float xMax, yMax;

    public float getzMax() {
        return zMax;
    }

    public float getzPos() {
        return zPos;
    }

    public void setzPos(float zPos) {
        this.zPos = zPos;
    }

    public float xPos, yPos;
    public float zPos, zMax;
    public GameView(Context context) {
        super(context);
        Bitmap ballsrc = BitmapFactory.decodeResource(getResources(), R.drawable.penguin);
        Bitmap barb_src = BitmapFactory.decodeResource(getResources(), R.drawable.brownbarb);

        Bitmap groundsrc = BitmapFactory.decodeResource(getResources(),R.drawable.ground3);
        final int dstWidth_ball = 200;
        final int dstHeight_ball = 300;
        final int dstWidth_line = 600;
        final int dstHeight_line = 500;
        ball = Bitmap.createScaledBitmap(ballsrc, dstWidth_ball, dstHeight_ball, true);
        barb_one = Bitmap.createScaledBitmap(barb_src, dstWidth_line, dstHeight_line, true);
        barb_two = Bitmap.createScaledBitmap(barb_src, dstWidth_line, dstHeight_line, true);
        barb_three = Bitmap.createScaledBitmap(barb_src, dstWidth_line, dstHeight_line, true);
        barb_four = Bitmap.createScaledBitmap(barb_src, dstWidth_line, dstHeight_line, true);
        ground = Bitmap.createScaledBitmap(groundsrc,1500,400,true);


    }

    @Override
    protected void onDraw(Canvas canvas) {

        Constants.SCREEN_HEIGHT = canvas.getHeight();
        Constants.SCREEN_WIDTH = canvas.getWidth();


        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.rgb(216,236,249));
        canvas.drawPaint(paint);
        canvas.drawBitmap(ball, xPos,yPos, null);
        canvas.drawBitmap(barb_one, 0, 300, null);
        canvas.drawBitmap(barb_two,0,900,null);
        canvas.drawBitmap(barb_three,840,700,null);
        canvas.drawBitmap(barb_four,840,1300,null);
        canvas.drawBitmap(ground, 0,1800,null);
        invalidate();

    }
}

