package com.example.anas.exercise03;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import com.robinhood.spark.SparkView;
import android.widget.TextView;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import com.robinhood.spark.SparkView;

import java.util.ArrayList;

/**
 * TODO: document your custom view class.
 */




public class AccelView extends View {
    public SensorObject sensorData;
    private Paint paint;
    public AccelView(Context context, AttributeSet attributeSet) {

        super(context, attributeSet);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint = new Paint();
        float centerPoint =getMeasuredWidth()/2;
        paint.setStrokeWidth(15);
        paint.setTextSize(25);
        paint.setColor(Color.RED);
        canvas.drawText("x "+sensorData.x, 10, 25, paint);
        canvas.drawLine(centerPoint,25,(float)(centerPoint+sensorData.x*10),25,paint);
        paint.setColor(Color.GREEN);
        canvas.drawText("y "+sensorData.y, 10, 75, paint);
        canvas.drawLine(centerPoint,75,(float)(centerPoint+sensorData.y*10),75,paint);
        paint.setColor(Color.BLUE);
        canvas.drawText("z "+sensorData.z, 10, 125, paint);
        canvas.drawLine(centerPoint,125,(float)(centerPoint+sensorData.z*10),125,paint);
        paint.setColor(Color.WHITE);
        canvas.drawText("magnitude ", 10, 175, paint);
        canvas.drawLine(centerPoint,175,(float)(centerPoint+sensorData.omegaG*10),175,paint);
    }


}
