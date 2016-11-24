package com.qwert2603.layouttest;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LineLayout extends ViewGroup {

    public LineLayout(Context context) {
        this(context, null);
    }

    public LineLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LineLayout);
        try {
            TextView titleTextView = new TextView(context);
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.bottomMargin = (int) typedArray.getDimension(R.styleable.LineLayout_title_margin_bottom, 0);
            titleTextView.setLayoutParams(layoutParams);
            titleTextView.setGravity(Gravity.CENTER_HORIZONTAL);

            String title = typedArray.getString(R.styleable.LineLayout_title);
            titleTextView.setText(title);

            float defaultTitleTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 24, getResources().getDisplayMetrics());
            float titleTextSize = typedArray.getDimension(R.styleable.LineLayout_title_text_size, defaultTitleTextSize);
            titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);

            @SuppressWarnings("deprecation")
            int defaultTitleTextColor = getResources().getColor(R.color.colorPrimaryDark);
            titleTextView.setTextColor(typedArray.getColor(R.styleable.LineLayout_title_text_color, defaultTitleTextColor));

            addView(titleTextView, 0);
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();

        int maxWidth = 0;
        int height = 0;
        int childState = 0;

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();

                measureChildWithMargins(
                        child,
                        widthMeasureSpec,
                        getPaddingLeft() + getPaddingRight() + lp.layoutOffset,
                        heightMeasureSpec,
                        getPaddingTop() + getPaddingBottom()
                );

                maxWidth = Math.max(maxWidth, child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin + lp.layoutOffset);
                height += child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
                childState = combineMeasuredStates(childState, child.getMeasuredState());
            }
        }

        maxWidth += getPaddingLeft() + getPaddingRight();
        height += getPaddingTop() + getPaddingBottom();

        Log.d("AASSDD", "LineLayout#onMeasure " + maxWidth + " " + getSuggestedMinimumWidth());
        Log.d("AASSDD", "LineLayout#onMeasure " + height + " " + getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
        height = Math.max(height, getSuggestedMinimumHeight());

        setMeasuredDimension(
                resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(height, heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT)
        );
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();

        t += getPaddingTop();
        b -= getPaddingBottom();
        l += getPaddingLeft();
        r -= getPaddingRight();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();

                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();

                if (ViewCompat.getLayoutDirection(child) == ViewCompat.LAYOUT_DIRECTION_LTR) {
                    child.layout(
                            l + lp.leftMargin + lp.layoutOffset,
                            t + lp.topMargin,
                            l + lp.leftMargin + width + lp.layoutOffset,
                            t + height + lp.topMargin
                    );
                } else {
                    child.layout(
                            r - lp.rightMargin - lp.layoutOffset - width,
                            t + lp.topMargin,
                            r - lp.rightMargin - lp.layoutOffset,
                            t + height + lp.topMargin
                    );
                }

                t += lp.topMargin + height + lp.bottomMargin;
            }
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    public static class LayoutParams extends MarginLayoutParams {

        public static final int OFFSET_ZERO = -1;
        public static final int OFFSET_TWELVE = -2;

        public int layoutOffset;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            TypedArray typedArray = c.obtainStyledAttributes(attrs, R.styleable.LineLayout);
            try {
                layoutOffset = (int) typedArray.getDimension(R.styleable.LineLayout_layout_offset, 0);
                if (layoutOffset == OFFSET_ZERO) {
                    layoutOffset = 0;
                } else if (layoutOffset == OFFSET_TWELVE) {
                    DisplayMetrics displayMetrics = c.getResources().getDisplayMetrics();
                    layoutOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, displayMetrics);
                }
            } finally {
                typedArray.recycle();
            }
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            layoutOffset = OFFSET_ZERO;
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
            if (source instanceof LayoutParams) {
                layoutOffset = ((LayoutParams) source).layoutOffset;
            }
        }
    }
}
