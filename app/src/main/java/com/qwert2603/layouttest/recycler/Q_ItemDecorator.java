package com.qwert2603.layouttest.recycler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

class Q_ItemDecorator extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    private boolean drawBorders;
    private int mMargin;

    Q_ItemDecorator(Context context) {
        mMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, context.getResources().getDisplayMetrics());

        drawBorders = false;

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
    }

    boolean isDrawBorders() {
        return drawBorders;
    }

    void setDrawBorders(boolean drawBorders) {
        this.drawBorders = drawBorders;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        c.drawColor(Color.LTGRAY);
        mPaint.setColor(Color.BLUE);
        if (drawBorders) {
            c.drawLine(0, 0, c.getWidth(), c.getHeight(), mPaint);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (drawBorders) {
            mPaint.setColor(Color.RED);
            c.drawLine(0, c.getHeight(), c.getWidth(), 0, mPaint);

            mPaint.setColor(Color.MAGENTA);
            for (int i = 0; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                c.drawRect(child.getLeft(), child.getTop(), child.getRight(), child.getBottom(), mPaint);
                if (parent.getChildViewHolder(child).getAdapterPosition() == 1) {
                    c.drawCircle(
                            child.getLeft() + child.getWidth() / 2,
                            child.getTop() + child.getHeight() / 2,
                            Math.min(child.getWidth(), child.getHeight()) / 2,
                            mPaint
                    );
                }
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(mMargin, mMargin, mMargin, mMargin);
    }

}
