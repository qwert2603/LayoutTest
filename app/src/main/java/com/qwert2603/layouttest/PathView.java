package com.qwert2603.layouttest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class PathView extends View {
    public PathView(Context context) {
        super(context);
    }

    public PathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Path path = new Path();
    private Paint paint = new Paint();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        path.moveTo(10, 10);
        path.lineTo(50, 10);
        path.rLineTo(0, 50);

        path.quadTo(100, 100, 250, 100);
        path.rCubicTo(100, 0, 0, 100, 100, 100);

        path.addRect(400, 200, 540, 380, Path.Direction.CW);
        path.arcTo(500, 400, 600, 600, 45, 180, true);
        path.addArc(300, 400, 650, 600, 45, 180);

        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(20);

        canvas.drawPath(path, paint);
        canvas.drawTextOnPath("abcdefghijklmnopqrstuvwxyz", path, 0, 0, paint);
    }
}
