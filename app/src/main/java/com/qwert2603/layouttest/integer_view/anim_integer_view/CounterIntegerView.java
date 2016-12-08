package com.qwert2603.layouttest.integer_view.anim_integer_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qwert2603.layouttest.R;
import com.qwert2603.layouttest.integer_view.IntegerView;

public class CounterIntegerView extends LinearLayout implements IntegerView {

    private int mInteger;

    private TextView mStableTextView;
    private TextView mInTextView;
    private TextView mOutTextView;

    public CounterIntegerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);

        View view = LayoutInflater.from(context).inflate(R.layout.anim_integer_view, this, true);
        mStableTextView = (TextView) view.findViewById(R.id.stable);
        mInTextView = (TextView) view.findViewById(R.id.in);
        mOutTextView = (TextView) view.findViewById(R.id.out);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CounterIntegerView);
        mInteger = typedArray.getInteger(R.styleable.CounterIntegerView_counter_integer, 0);
        int textColor = typedArray.getColor(R.styleable.CounterIntegerView_android_textColor, Color.BLACK);
        float defaultTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 24, getResources().getDisplayMetrics());
        float textSize = typedArray.getDimension(R.styleable.CounterIntegerView_android_textSize, defaultTextSize);
        typedArray.recycle();

        mStableTextView.setTextColor(textColor);
        mStableTextView.setTextSize(textSize);
        mInTextView.setTextColor(textColor);
        mInTextView.setTextSize(textSize);
        mOutTextView.setTextColor(textColor);
        mOutTextView.setTextSize(textSize);

        mStableTextView.setText(String.valueOf(mInteger));
        mInTextView.setText("");
        mOutTextView.setText("");
    }

    @Override
    public int getInteger() {
        return mInteger;
    }

    @Override
    public void setInteger(int integer) {
        int oldNumber = mInteger;
        mInteger = integer;

        String b = String.valueOf(oldNumber);
        String e = String.valueOf(mInteger);

        if (((oldNumber > 0) != (mInteger > 0)) || (b.length() != e.length())) {
            mStableTextView.setText("");
            do_switch(oldNumber, mInteger, String.valueOf(oldNumber), String.valueOf(mInteger));
            return;
        }

        int s = 0;
        for (int i = 0; i < b.length(); i++) {
            if (b.charAt(i) != e.charAt(i)) {
                break;
            }
            ++s;
        }

        mStableTextView.setText(e.substring(0, s));
        do_switch(oldNumber, mInteger, b.substring(s, b.length()), e.substring(s, e.length()));
    }

    private void do_switch(int bi, int ei, String b, String e) {
        mInTextView.setText(e);
        mOutTextView.setText(b);

        if (ei > bi) {
            mInTextView.setTranslationY(getHeight());
            mInTextView.animate().translationY(0);
            mOutTextView.setTranslationY(0);
            mOutTextView.animate().translationY(-1 * getHeight());
        } else {
            mInTextView.setTranslationY(-1 * getHeight());
            mInTextView.animate().translationY(0);
            mOutTextView.setTranslationY(0);
            mOutTextView.animate().translationY(getHeight());
        }

    }
}
