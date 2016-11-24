package com.qwert2603.layouttest;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class AnimVectorsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim_vectors);

        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        int[] imgs = {R.id.img, R.id.img2, R.id.img3, R.id.img4, R.id.img5};
        for (final int img : imgs) {
            final ImageView imageView = (ImageView) findViewById(img);
            imageView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    final Animatable animatable = (Animatable) imageView.getDrawable();
                    if (animatable.isRunning()) {
                        animatable.stop();
                    } else {
                        animatable.start();
                    }
                }
            });
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AnimatorSet animatorSet = new AnimatorSet();
                    ObjectAnimator rotation = ObjectAnimator.ofFloat(imageView, "rotation", 45);
                    ObjectAnimator right = ObjectAnimator.ofFloat(imageView, "x", imageView.getX() + 100);
                    ObjectAnimator left = ObjectAnimator.ofFloat(imageView, "x", imageView.getX());
                    ObjectAnimator scale = ObjectAnimator.ofFloat(imageView, "scaleY", 1.5f);
                    Animator back = AnimatorInflater.loadAnimator(AnimVectorsActivity.this, R.animator.back_rotate_scale);
                    back.setTarget(imageView);
                    animatorSet.play(rotation).after(right);
                    animatorSet.play(rotation).before(left);
                    animatorSet.play(rotation).with(scale);
                    animatorSet.play(back).after(left);
                    animatorSet.setDuration(800);
                    animatorSet.start();

                    Animator pulsating = AnimatorInflater.loadAnimator(AnimVectorsActivity.this, R.animator.pulsating);
                    pulsating.setTarget(imageView);
                    pulsating.start();
                    return true;
                }
            });
        }
        //}
    }
}
