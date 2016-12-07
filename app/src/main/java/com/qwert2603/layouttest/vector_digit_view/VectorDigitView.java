package com.qwert2603.layouttest.vector_digit_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ViewAnimator;

import com.qwert2603.layouttest.R;

public class VectorDigitView extends ViewAnimator {

    DigitAdapter mDigitAdapter;

    public VectorDigitView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.digit_view, this, true);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VectorDigitView);
        int digit = typedArray.getInteger(R.styleable.VectorDigitView_digit, 0);
        typedArray.recycle();

        mDigitAdapter = new DigitAdapter();
        recyclerView.setAdapter(mDigitAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true));
        recyclerView.setItemAnimator(new DigitItemAnimator());
        mDigitAdapter.setDigit(digit);
        mDigitAdapter.notifyDataSetChanged();
    }

    public void setDigit(int digit) {
        mDigitAdapter.setDigit(digit);
    }

    public int getDigit() {
        return mDigitAdapter.getDigit();
    }

}
