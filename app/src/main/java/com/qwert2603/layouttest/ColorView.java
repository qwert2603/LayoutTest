package com.qwert2603.layouttest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;

public class ColorView extends View {

    public static final String COLOR_PROPERTY = "color";

    @ColorInt
    private int mColor;

    public ColorView(Context context) {
        this(context, null);
    }

    public ColorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorView);
        try {
            mColor = typedArray.getColor(R.styleable.ColorView_color, Color.RED);
        } finally {
            typedArray.recycle();
        }
    }

    @ColorInt
    public int getColor() {
        return mColor;
    }

    public void setColor(@ColorInt int color) {
        mColor = color;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(mColor);
    }
}
