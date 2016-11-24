package com.qwert2603.layouttest.recycler;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextSwitcher;

import com.qwert2603.layouttest.CircleColorView;
import com.qwert2603.layouttest.ColorView;
import com.qwert2603.layouttest.R;

class Q_ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

    enum RemoveStyle {
        NONE,
        SWIPE,
        ROTATE
    }

    private ColorView mBack;
    ColorView mColorView;
    ImageView mHeartImageView;
    TextSwitcher mTextSwitcher;
    ImageView mHandleView;
    CircleColorView mItemHeartBack;
    ImageView mItemHeart;

    private Item mItem;

    private RemoveStyle mRemoveStyle;

    Q_ViewHolder(View itemView) {
        super(itemView);
        mBack = (ColorView) itemView.findViewById(R.id.back);
        mColorView = (ColorView) itemView.findViewById(R.id.color_view);
        mHeartImageView = (ImageView) itemView.findViewById(R.id.heart);
        mTextSwitcher = (TextSwitcher) itemView.findViewById(R.id.text_view);
        mHandleView = (ImageView) itemView.findViewById(R.id.handle);
        mItemHeartBack = (CircleColorView) itemView.findViewById(R.id.item_heart_back);
        mItemHeart = (ImageView) itemView.findViewById(R.id.item_heart);
    }

    void bind(Item item) {
        mItem = item;
        //mBack.setColor(item.getColor() & 0x1bffffff);
        mColorView.setColor(item.getColor());
        mTextSwitcher.setCurrentText(item.getText());
        mHeartImageView.setImageResource(item.isLiked() ? R.drawable.heart_full : R.drawable.heart_empty);
        mItemHeartBack.setColor(item.getColor() & 0x70ffffff);
    }

    @Override
    public void onItemSelected() {
        mBack.setColor(mColorView.getColor() & 0x1bffffff);
    }

    @Override
    public void onItemClear() {
        mBack.setColor(Color.WHITE);
    }

    Item getItem() {
        return mItem;
    }

    RemoveStyle getRemoveStyle() {
        return mRemoveStyle;
    }

    void setRemoveStyle(RemoveStyle removeStyle) {
        mRemoveStyle = removeStyle;
    }

    void resetAfterRotateRemoving() {

    }
}
