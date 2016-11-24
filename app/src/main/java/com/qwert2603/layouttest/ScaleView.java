package com.qwert2603.layouttest;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;

public class ScaleView extends View {

    private Paint mPaint = new Paint();

    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector mGestureDetector;

    private Bitmap mBitmap;

    private final float mStartScale;
    private final float mSkewX;
    private final float mSkewY;

    private float mScaleFactor = 1;
    private float mFocusX = 0;
    private float mFocusY = 0;

    public ScaleView(Context context) {
        this(context, null);
    }

    public ScaleView(Context context, final AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScaleView);
        mStartScale = typedArray.getFloat(R.styleable.ScaleView_start_scale, 1);
        try {
            mSkewX = typedArray.getFloat(R.styleable.ScaleView_skew_x, 0);
            mSkewY = typedArray.getFloat(R.styleable.ScaleView_skew_y, 0);
        } finally {
            typedArray.recycle();
        }

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.klimt1918);

        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                mScaleFactor = detector.getScaleFactor();
                mFocusX = detector.getFocusX();
                mFocusY = detector.getFocusY();
                invalidate();
                return true;
            }
        });

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                animate().rotationBy(360).setDuration(500);
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (Math.abs(velocityX) < Math.abs(velocityY)) {
                    animate().rotationXBy(180 * (velocityX < 0 ? 1 : -1)).setDuration(1200).start();
                } else {
                    int dir = velocityY > 0 ? 1 : -1;
                    animate().xBy(100 * dir).yBy(200 * dir).rotationYBy(180 * dir).setDuration(1200).start();
                }
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ScaleView.this, "ScaleFactor", 0);
                objectAnimator.setFloatValues(mScaleFactor * 1.2f);
                objectAnimator.setDuration(1000);
                objectAnimator.setInterpolator(new AnticipateInterpolator());
                objectAnimator.start();
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim1);
                startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ScaleView.this.setVisibility(GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });
    }

    public float getScaleFactor() {
        return mScaleFactor;
    }

    public void setScaleFactor(float scaleFactor) {
        mScaleFactor = scaleFactor;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("AASSDD", "ScaleView#canvas == " + canvas.getWidth() + " " + canvas.getHeight());
        Log.d("AASSDD", "ScaleView#mBitmap == " + mBitmap.getWidth() + " " + mBitmap.getHeight());
        float ws = mBitmap.getWidth() * 1.0f / canvas.getWidth();
        float hs = mBitmap.getHeight() * 1.0f / canvas.getHeight();
        float s = 1.0f / Math.max(ws, hs) * mStartScale;
        canvas.scale(mScaleFactor, mScaleFactor, mFocusX, mFocusY);
        canvas.scale(s, s);
        canvas.skew(mSkewX, mSkewY);
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d("AASSDD", "ScaleView#onMeasure width " + MeasureSpec.getSize(widthMeasureSpec) + " " + MeasureSpec.getMode(widthMeasureSpec));
        Log.d("AASSDD", "ScaleView#onMeasure height " + MeasureSpec.getSize(heightMeasureSpec) + " " + MeasureSpec.getMode(heightMeasureSpec));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
