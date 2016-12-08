package com.qwert2603.layouttest.integer_view.vector_integer_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ViewAnimator;

import com.qwert2603.layouttest.R;
import com.qwert2603.layouttest.integer_view.IntegerView;

public class VectorIntegerView extends ViewAnimator implements IntegerView {

    private DigitAdapter mDigitAdapter;

    public VectorIntegerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.vector_digit_view, this, true);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VectorIntegerView);
        int digit = typedArray.getInteger(R.styleable.VectorIntegerView_vector_integer, 0);
        typedArray.recycle();

        mDigitAdapter = new DigitAdapter();
        recyclerView.setAdapter(mDigitAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true));
        recyclerView.setItemAnimator(new DigitItemAnimator());
        mDigitAdapter.setInteger(digit);
        mDigitAdapter.notifyDataSetChanged();
    }

    @Override
    public void setInteger(int digit) {
        mDigitAdapter.setInteger(digit);
    }

    @Override
    public int getInteger() {
        return mDigitAdapter.getInteger();
    }

}
