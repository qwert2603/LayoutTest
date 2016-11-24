package com.qwert2603.layouttest.recycler;

import android.support.annotation.ColorInt;

class Item {

    private static int sNextId = 0;

    private final int mId;

    @ColorInt
    private final int mColor;

    private String mText;

    private boolean mIsLiked;

    Item(@ColorInt int color, String text) {
        mId = sNextId++;
        mColor = color;
        mText = text;
        mIsLiked = false;
    }

    public int getId() {
        return mId;
    }

    @ColorInt
    int getColor() {
        return mColor;
    }

    String getText() {
        return mText;
    }

    boolean isLiked() {
        return mIsLiked;
    }

    void like() {
        mIsLiked = true;
        mText = mText + " Liked!";
    }

}
