package com.qwert2603.layouttest.recycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.qwert2603.layouttest.LogUtils;
import com.qwert2603.layouttest.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

class Q_Adapter extends RecyclerView.Adapter<Q_ViewHolder> implements ItemTouchHelperAdapter {

    private final List<Item> mItems = new ArrayList<>();
    private OnStartDragListener mOnStartDragListener;

    Q_Adapter(OnStartDragListener onStartDragListener) {
        mOnStartDragListener = onStartDragListener;
        setHasStableIds(true);
    }

    @Override
    public Q_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        @SuppressLint("InflateParams")
        View view = View.inflate(parent.getContext(), R.layout.item_recycler, null);

        final Q_ViewHolder holder = new Q_ViewHolder(view);
        LogUtils.d("Q_ViewHolder#onCreateViewHolder " + holder);

        holder.mHandleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mOnStartDragListener.onStartDrag(holder);
                }
                return false;
            }
        });
        holder.mHeartImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (!mItems.get(adapterPosition).isLiked()) {
                    mItems.get(adapterPosition).like();
                    notifyItemChanged(adapterPosition, Q_ItemHolderInfo.ACTION_LIKE_BUTTON_CLICKED);
                }
            }
        });
        holder.mTextSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (!mItems.get(adapterPosition).isLiked()) {
                    mItems.get(adapterPosition).like();
                    notifyItemChanged(adapterPosition, Q_ItemHolderInfo.ACTION_LIKE_TEXT_CLICKED);
                }
            }
        });
        holder.mColorView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                mItems.remove(adapterPosition);
                holder.setRemoveStyle(Q_ViewHolder.RemoveStyle.ROTATE);
                notifyItemRemoved(adapterPosition);
                return true;
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(final Q_ViewHolder holder, int position) {
        LogUtils.d("Q_ViewHolder#onBindViewHolder " + holder + " " + position + " " + mItems.get(position).getId());
        holder.bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public long getItemId(int position) {
        return mItems.get(position).getId();
    }

    @Override
    public void onViewRecycled(Q_ViewHolder holder) {
        LogUtils.d("Q_ViewHolder#onViewRecycled " + holder);
    }

    @Override
    public boolean onFailedToRecycleView(Q_ViewHolder holder) {
        LogUtils.d("Q_ViewHolder#onFailedToRecycleView " + holder);
        return false;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    void addItems(Context context, boolean animated) {
        mItems.clear();

        TypedArray typedArray = context.getResources().obtainTypedArray(R.array.recycler_colors);

        for (int i = 0; i < 1000; i++) {
            String s = "Item " + String.valueOf(((char) ('A' + i % 26))) + " " + i + " ";
            for (int j = 0; j < new Random().nextInt(8); j++) {
                s += "WW";
            }
            for (int j = 0; j < new Random().nextInt(8); j++) {
                s += "\nQQ";
            }
            mItems.add(new Item(typedArray.getColor(new Random().nextInt(typedArray.length()), 0), s));
        }
        typedArray.recycle();

        if (animated) {
            notifyItemRangeInserted(0, mItems.size());
        } else {
            notifyDataSetChanged();
        }
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mItems, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mItems, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }
}
