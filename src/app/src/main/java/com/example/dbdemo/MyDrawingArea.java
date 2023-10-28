package com.example.dbdemo;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class MyDrawingArea extends View {
    private Paint p = new Paint();
    private Path path = new Path();
    Bitmap bmp;
    public MyDrawingArea(Context context) {
        super(context);
        initPaint(p);
    }
    public MyDrawingArea(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint(p);
    }
    public MyDrawingArea(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(p);
    }
    public MyDrawingArea(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initPaint(p);
    }
    private void initPaint(Paint p) {
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5f);
    }

//    end constructors and inits

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, p);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX(), y = event.getY();
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN)
            path.moveTo(x, y);
        if (action == MotionEvent.ACTION_MOVE)
            path.lineTo(x, y);
        return true;
    }

    public Bitmap getBitmap() {
        bmp = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        Paint p = new Paint();
        Paint backgroundPaint = new Paint();
        // set a white background so the png won't blend with system black background
        backgroundPaint.setColor(Color.WHITE);
        c.drawPaint(backgroundPaint);

        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        p.setAntiAlias(true);
        p.setStrokeWidth(5f);
        c.drawPath(path, p); //path is global. The very same thing that onDraw uses.
        return bmp;
    }

    public void clearPath() {
        path.reset();
    }
}

