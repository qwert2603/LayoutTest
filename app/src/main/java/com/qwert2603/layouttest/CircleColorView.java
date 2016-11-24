package com.qwert2603.layouttest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

public class CircleColorView extends ColorView {

    private final Paint mPaint;

    public CircleColorView(Context context) {
        this(context, null);
    }

    public CircleColorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(CircleColorView.this.getColor());
        int w = canvas.getWidth() / 2;
        int h = canvas.getHeight() / 2;
        canvas.drawCircle(w, h, Math.max(w, h), mPaint);
    }
}
