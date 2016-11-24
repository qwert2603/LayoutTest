package com.qwert2603.layouttest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.VelocityTracker;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomView extends View {

    private static final int[] COLORS = {
            Color.RED,
            Color.GREEN,
            Color.BLUE,
            Color.CYAN,
            Color.YELLOW,
            Color.MAGENTA
    };

    private Map<Integer, List<PointF>> mPoints = new HashMap<>();

    private Paint mPaint = new Paint();

    private VelocityTracker mVelocityTracker = null;

    private GestureDetectorCompat mGestureDetector;

    private ScaleGestureDetector mScaleGestureDetector;

    public CustomView(Context context) {
        this(context, null);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint.setStrokeWidth(3);
        mPaint.setTextSize(42);
        mGestureDetector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                Log.d("AASSDD", "onDown " + e);
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
                Log.d("AASSDD", "onShowPress " + e);
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.d("AASSDD", "onSingleTapUp " + e);
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.d("AASSDD", "onScroll " + distanceX + " " + distanceY + " " + e1 + " " + e2);
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Log.d("AASSDD", "onLongPress " + e);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.d("AASSDD", "onFling " + velocityX + " " + velocityY + " " + e1 + " " + e2);
                return false;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Log.d("AASSDD", "onSingleTapConfirmed " + e);
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Log.d("AASSDD", "onDoubleTap " + e);
                return false;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                Log.d("AASSDD", "onDoubleTapEvent " + e);
                return false;
            }

            @Override
            public boolean onContextClick(MotionEvent e) {
                Log.d("AASSDD", "onContextClick " + e);
                return super.onContextClick(e);
            }
        });
        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                Log.d("AASSDD", "onScale " + detector.getScaleFactor() + " " + detector.getFocusX() + " " + detector.getFocusY());
                return super.onScale(detector);
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                Log.d("AASSDD", "onScaleBegin " + detector.getScaleFactor());
                return super.onScaleBegin(detector);
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                Log.d("AASSDD", "onScaleEnd " + detector.getScaleFactor());
                super.onScaleEnd(detector);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        mScaleGestureDetector.onTouchEvent(event);

        int actionIndex = event.getActionIndex();
        int pointerId = event.getPointerId(actionIndex);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mVelocityTracker = VelocityTracker.obtain();
            case MotionEvent.ACTION_POINTER_DOWN:
                mPoints.put(pointerId, new ArrayList<PointF>());
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(event);
                for (int i = 0; i < event.getPointerCount(); i++) {
                    int d = event.getPointerId(i);
                    PointF pointF = new PointF(event.getX(i), event.getY(i));
                    mPoints.get(d).add(pointF);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.addMovement(event);
                mPoints.remove(pointerId);
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (Map.Entry<Integer, List<PointF>> entry : mPoints.entrySet()) {
            mPaint.setColor(COLORS[entry.getKey()]);
            List<PointF> points = entry.getValue();
            for (int i = 0; i < points.size() - 1; i++) {
                canvas.drawLine(points.get(i).x, points.get(i).y,
                        points.get(i + 1).x, points.get(i + 1).y, mPaint);
            }
            canvas.drawCircle(points.get(points.size() - 1).x, points.get(points.size() - 1).y, 42, mPaint);
            if (mVelocityTracker != null) {
                mVelocityTracker.computeCurrentVelocity(100);
                canvas.drawCircle(100, 100, 10, mPaint);
                canvas.drawLine(100, 100,
                        100 + mVelocityTracker.getXVelocity(entry.getKey()),
                        100 + mVelocityTracker.getYVelocity(entry.getKey()), mPaint);
            }
        }
    }

}
