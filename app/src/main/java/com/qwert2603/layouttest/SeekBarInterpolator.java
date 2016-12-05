package com.qwert2603.layouttest;

import android.view.animation.Interpolator;
import android.widget.SeekBar;

public class SeekBarInterpolator implements Interpolator {
    private SeekBar mSeekBar;

    public SeekBarInterpolator(SeekBar seekBar) {
        mSeekBar = seekBar;
    }

    @Override
    public float getInterpolation(float input) {
        return 1.0f * mSeekBar.getProgress() / mSeekBar.getMax();
    }
}
