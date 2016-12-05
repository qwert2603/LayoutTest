package com.qwert2603.layouttest;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.SeekBar;

public class SeekAnimActivity extends AppCompatActivity {

    @Override
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek_anim);

        ImageView imageView = (ImageView) findViewById(R.id.img);

        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(imageView, "rotationX", 0, 180);
        objectAnimatorX.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimatorX.setInterpolator(new SeekBarInterpolator((SeekBar) findViewById(R.id.seek_x)));
        objectAnimatorX.start();

        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(imageView, "rotationY", 0, 180);
        objectAnimatorY.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimatorY.setInterpolator(new SeekBarInterpolator((SeekBar) findViewById(R.id.seek_y)));
        objectAnimatorY.start();
    }
}
