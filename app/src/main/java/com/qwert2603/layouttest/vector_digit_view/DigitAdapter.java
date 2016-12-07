package com.qwert2603.layouttest.vector_digit_view;

import android.annotation.SuppressLint;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qwert2603.layouttest.R;

class DigitAdapter extends RecyclerView.Adapter<DigitAdapter.DigitViewHolder> {

    private int mDigit;

    @SuppressLint("InflateParams")
    @Override
    public DigitAdapter.DigitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DigitAdapter.DigitViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_digit, parent, false));
    }

    @Override
    public void onBindViewHolder(DigitAdapter.DigitViewHolder holder, int position) {
        holder.setDigit(getDigitAt(mDigit, position));
    }

    @Override
    public int getItemCount() {
        return getDigitCount(mDigit);
    }

    int getDigit() {
        return mDigit;
    }

    void setDigit(final int digit) {
        final int old = mDigit;
        mDigit = digit;
        DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return getDigitCount(old);
            }

            @Override
            public int getNewListSize() {
                return getDigitCount(digit);
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return oldItemPosition == newItemPosition;
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return getDigitAt(old, oldItemPosition) == getDigitAt(digit, newItemPosition);
            }
        }, false).dispatchUpdatesTo(this);
    }

    private int getDigitCount(int d) {
        return String.valueOf(d).length();
    }

    private int getDigitAt(int d, int pos) {
        String s = String.valueOf(d);
        char c = s.charAt(s.length() - 1 - pos);
        if (Character.isDigit(c)) {
            return Integer.parseInt("" + c);
        }
        if (c == '-') {
            return 11;
        }
        throw new IllegalArgumentException();
    }

    class DigitViewHolder extends RecyclerView.ViewHolder {
        final int[] attrs = {
                R.attr.state_zero,
                R.attr.state_one,
                R.attr.state_two,
                R.attr.state_three,
                R.attr.state_four,
                R.attr.state_five,
                R.attr.state_six,
                R.attr.state_seven,
                R.attr.state_eight,
                R.attr.state_nine,
                R.attr.state_nth,
                R.attr.state_minus,
        };

        ImageView mImageView;
        int d;

        DigitViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.img);
        }

        void setDigit(int digit) {
            d = digit;

            int[] state = new int[attrs.length];

            for (int i = 0; i < attrs.length; i++) {
                if (i == digit) {
                    state[i] = attrs[i];
                } else {
                    state[i] = -attrs[i];
                }
            }

            mImageView.setImageState(state, true);
        }
    }

}
