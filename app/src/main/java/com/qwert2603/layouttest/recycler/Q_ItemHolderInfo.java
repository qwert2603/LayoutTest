package com.qwert2603.layouttest.recycler;

import android.support.v7.widget.RecyclerView;

class Q_ItemHolderInfo extends RecyclerView.ItemAnimator.ItemHolderInfo {
    static final String ACTION_LIKE_BUTTON_CLICKED = "action_like_button_button";
    static final String ACTION_LIKE_TEXT_CLICKED = "action_like_text_button";

    final String mActionString;

    Q_ItemHolderInfo(String actionString) {
        mActionString = actionString;
    }
}
