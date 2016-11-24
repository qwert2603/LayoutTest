package com.qwert2603.layouttest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class SunsetActivity extends AppCompatActivity {

    private View mSceneView;
    private View mSunView;
    private ColorView mSkyView;
    private View mRaysView;

    private int mBlueSkyColor;
    private int mSunsetSkyColor;
    private int mNightSkyColor;

    private int mSunMidday = -1;
    private boolean mIsSetAnimation = true;

    private Animator mCurrentAnimator = null;
    private long mAnimPos;

    private static final long ANIMATION_NIGHT_LENGTH = 1500;
    private static final long ANIMATION_SUNSET_LENGTH = 3000;
    private static final long ANIMATION_LENGTH = ANIMATION_SUNSET_LENGTH + ANIMATION_NIGHT_LENGTH;

    private AnimatorSet mRaysAnimator = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sunset);

        mSceneView = findViewById(R.id.sun_set);
        mSunView = findViewById(R.id.sun);
        mSkyView = (ColorView) findViewById(R.id.sky);
        mRaysView = findViewById(R.id.rays);

        mBlueSkyColor = getResources().getColor(R.color.blue_sky);
        mSunsetSkyColor = getResources().getColor(R.color.sunset_sky);
        mNightSkyColor = getResources().getColor(R.color.night_sky);

        mRaysView.setVisibility(View.INVISIBLE);

        mSceneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                if (mRaysAnimator != null) {
                    mRaysView.setVisibility(View.INVISIBLE);
                    mRaysAnimator.cancel();
                    mRaysAnimator = null;
                }

                if (mIsSetAnimation) {
                    startAnimationSunSet(mAnimPos);
                } else {
                    startAnimationSunRise(mAnimPos);
                }
                mIsSetAnimation = !mIsSetAnimation;
            }
        });
    }

    private void startAnimationSunSet(long startPos) {
        if (mSunMidday == -1) {
            mSunMidday = mSunView.getTop();
        }

        ObjectAnimator objectAnimatorNight = ObjectAnimator.ofInt(mSkyView, ColorView.COLOR_PROPERTY, mNightSkyColor)
                .setDuration(Math.min(ANIMATION_NIGHT_LENGTH, ANIMATION_LENGTH - startPos));
        objectAnimatorNight.setEvaluator(new ArgbEvaluator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(objectAnimatorNight);

        if (startPos < ANIMATION_SUNSET_LENGTH) {
            ObjectAnimator objectAnimatorSunset = ObjectAnimator.ofFloat(mSunView, "y", mSkyView.getHeight())
                    .setDuration(ANIMATION_SUNSET_LENGTH - startPos);
            objectAnimatorSunset.setInterpolator(new AccelerateDecelerateInterpolator());

            ObjectAnimator objectAnimatorColor = ObjectAnimator.ofInt(mSkyView, ColorView.COLOR_PROPERTY, mSunsetSkyColor)
                    .setDuration(ANIMATION_SUNSET_LENGTH - startPos);
            objectAnimatorColor.setEvaluator(new ArgbEvaluator());

            animatorSet.play(objectAnimatorSunset).before(objectAnimatorNight);
            animatorSet.play(objectAnimatorColor).before(objectAnimatorNight);
        }

        mCurrentAnimator = animatorSet;
        final long animStart = SystemClock.uptimeMillis();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimPos += SystemClock.uptimeMillis() - animStart;
                if (mAnimPos > ANIMATION_LENGTH) {
                    mAnimPos = ANIMATION_LENGTH;
                }
            }
        });
        animatorSet.start();
    }

    private void startAnimationSunRise(long startPos) {
        ObjectAnimator objectAnimatorSunset = ObjectAnimator.ofFloat(mSunView, "y", mSunMidday)
                .setDuration(Math.min(ANIMATION_SUNSET_LENGTH, startPos));
        objectAnimatorSunset.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator objectAnimatorColor = ObjectAnimator.ofInt(mSkyView, ColorView.COLOR_PROPERTY, mBlueSkyColor)
                .setDuration(Math.min(ANIMATION_SUNSET_LENGTH, startPos));
        objectAnimatorColor.setEvaluator(new ArgbEvaluator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(objectAnimatorSunset)
                .with(objectAnimatorColor);

        if (startPos > ANIMATION_SUNSET_LENGTH) {
            ObjectAnimator objectAnimatorNight = ObjectAnimator.ofInt(mSkyView, ColorView.COLOR_PROPERTY, mSunsetSkyColor)
                    .setDuration(startPos - ANIMATION_SUNSET_LENGTH);
            objectAnimatorNight.setEvaluator(new ArgbEvaluator());

            animatorSet.play(objectAnimatorNight).before(objectAnimatorSunset);
        }

        mCurrentAnimator = animatorSet;
        final long animStart = SystemClock.uptimeMillis();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimPos -= SystemClock.uptimeMillis() - animStart;
                if (mAnimPos <= 0) {
                    mAnimPos = 0;
                    startAnimationRays();
                }
            }
        });
        animatorSet.start();
    }

    private void startAnimationRays() {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mRaysView, "scaleX", 1f, 1.2f)
                .setDuration(1000);
        scaleX.setRepeatCount(ValueAnimator.INFINITE);
        scaleX.setRepeatMode(ValueAnimator.REVERSE);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mRaysView, "scaleY", 1f, 1.2f)
                .setDuration(1000);
        scaleY.setRepeatCount(ValueAnimator.INFINITE);
        scaleY.setRepeatMode(ValueAnimator.REVERSE);
        mRaysAnimator = new AnimatorSet();
        mRaysAnimator.play(scaleX)
                .with(scaleY);

        mRaysView.setVisibility(View.VISIBLE);
        mRaysAnimator.start();
    }
}
