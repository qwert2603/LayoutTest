package com.qwert2603.layouttest.recycler;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.qwert2603.layouttest.CircleColorView;
import com.qwert2603.layouttest.LogUtils;
import com.qwert2603.layouttest.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Q_ItemAnimator extends DefaultItemAnimator {

    private final Map<RecyclerView.ViewHolder, Animator> mHeartButtonAnimations = new HashMap<>();
    private final Map<RecyclerView.ViewHolder, Animator> mHeartItemAnimations = new HashMap<>();

    private int mLastAddAnimatedItem = -20;

    @Override
    public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder) {
        return true;
    }

    @NonNull
    @Override
    public ItemHolderInfo recordPreLayoutInformation(@NonNull RecyclerView.State state, @NonNull RecyclerView.ViewHolder viewHolder, int changeFlags, @NonNull List<Object> payloads) {
        if (changeFlags == FLAG_CHANGED) {
            for (Object payload : payloads) {
                if (payload instanceof String) {
                    return new Q_ItemHolderInfo((String) payload);
                }
            }
        }

        return super.recordPreLayoutInformation(state, viewHolder, changeFlags, payloads);
    }

    @Override
    public boolean animateChange(
            @NonNull RecyclerView.ViewHolder oldHolder,
            @NonNull RecyclerView.ViewHolder newHolder,
            @NonNull ItemHolderInfo preInfo,
            @NonNull ItemHolderInfo postInfo) {
        cancelCurrentAnimationIfExist(newHolder);

        if (preInfo instanceof Q_ItemHolderInfo) {
            Q_ItemHolderInfo qItemHolderInfo = (Q_ItemHolderInfo) preInfo;
            Q_ViewHolder holder = (Q_ViewHolder) newHolder;

            animateHeartButton(holder);
            updateTextSwitcher(holder, holder.getItem().getText());
            if (Q_ItemHolderInfo.ACTION_LIKE_TEXT_CLICKED.equals(qItemHolderInfo.mActionString)) {
                animateTextLike(holder);
            }
        }

        return false;
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        LogUtils.d("animateAdd " + mLastAddAnimatedItem + " " + holder.getAdapterPosition());

        if (holder.getAdapterPosition() > mLastAddAnimatedItem) {
            ++mLastAddAnimatedItem;
            runEnterAnimation((Q_ViewHolder) holder);
            return false;
        }

        dispatchAddFinished(holder);

        return false;
    }

    @Override
    public boolean animateAppearance(@NonNull RecyclerView.ViewHolder viewHolder, @Nullable ItemHolderInfo preLayoutInfo, @NonNull ItemHolderInfo postLayoutInfo) {
        return super.animateAppearance(viewHolder, preLayoutInfo, postLayoutInfo);
    }

    @Override
    public void endAnimation(RecyclerView.ViewHolder item) {
        super.endAnimation(item);
        cancelCurrentAnimationIfExist(item);
    }

    @Override
    public void endAnimations() {
        super.endAnimations();

        for (Animator animator : mHeartButtonAnimations.values()) {
            animator.cancel();
        }
        for (Animator animator : mHeartItemAnimations.values()) {
            animator.cancel();
        }
    }

    private void runEnterAnimation(final Q_ViewHolder qViewHolder) {
        LogUtils.d("runEnterAnimation " + qViewHolder);
        int heightPixels = qViewHolder.itemView.getResources().getDisplayMetrics().heightPixels;
        qViewHolder.itemView.setTranslationY(heightPixels);
        qViewHolder.itemView.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.0f))
                //.rotationXBy(360)
                .setDuration(1000)
                //.setStartDelay(qViewHolder.getAdapterPosition() * 100)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        LogUtils.d("runEnterAnimation onAnimationStart " + qViewHolder);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        LogUtils.d("runEnterAnimation onAnimationEnd " + qViewHolder);
                        dispatchAddFinished(qViewHolder);
                    }
                });
    }

    private void cancelCurrentAnimationIfExist(RecyclerView.ViewHolder viewHolder) {
        if (mHeartButtonAnimations.containsKey(viewHolder)) {
            mHeartButtonAnimations.get(viewHolder).cancel();
        }
        if (mHeartItemAnimations.containsKey(viewHolder)) {
            mHeartItemAnimations.get(viewHolder).cancel();
        }
    }

    private void animateHeartButton(final Q_ViewHolder qViewHolder) {
        LogUtils.d("animateHeartButton " + qViewHolder);

        final ImageView heartImageView = qViewHolder.mHeartImageView;
        heartImageView.setImageResource(R.drawable.heart_empty);

        AnimatorSet animator = new AnimatorSet();

        ObjectAnimator rotation = ObjectAnimator.ofFloat(heartImageView, "rotation", 0.0f, 360.0f);
        rotation.setDuration(400);
        rotation.setInterpolator(new AccelerateInterpolator());

        OvershootInterpolator overshootInterpolator = new OvershootInterpolator(4);

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(heartImageView, "scaleX", 0.2f, 1.0f);
        scaleX.setDuration(400);
        scaleX.setInterpolator(overshootInterpolator);

        ObjectAnimator scaleY = ObjectAnimator.ofFloat(heartImageView, "scaleY", 0.2f, 1.0f);
        scaleY.setDuration(400);
        scaleY.setInterpolator(overshootInterpolator);

        scaleY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                heartImageView.setImageResource(R.drawable.heart_full);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mHeartButtonAnimations.remove(qViewHolder);
                dispatchChangeFinishedIfAllAnimationsEnded(qViewHolder);
            }
        });

        animator.play(scaleX).with(scaleY).after(rotation);

        mHeartButtonAnimations.put(qViewHolder, animator);
        animator.start();
    }

    private void updateTextSwitcher(Q_ViewHolder qViewHolder, String text) {
        qViewHolder.mTextSwitcher.setCurrentText(null);
        qViewHolder.mTextSwitcher.setText(text);
    }

    private void animateTextLike(final Q_ViewHolder qViewHolder) {
        ImageView itemHeart = qViewHolder.mItemHeart;
        CircleColorView itemHeartBack = qViewHolder.mItemHeartBack;

        itemHeart.setVisibility(View.VISIBLE);
        itemHeartBack.setVisibility(View.VISIBLE);

        Animator itemHeartAnimator = AnimatorInflater.loadAnimator(itemHeart.getContext(), R.animator.heart_item);
        itemHeartAnimator.setTarget(itemHeart);

        Animator itemHeartBackAnimator = AnimatorInflater.loadAnimator(itemHeartBack.getContext(), R.animator.heart_item_back);
        itemHeartBackAnimator.setTarget(itemHeartBack);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(itemHeartAnimator).with(itemHeartBackAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mHeartItemAnimations.remove(qViewHolder);
                resetAnimationState(qViewHolder);
                dispatchChangeFinishedIfAllAnimationsEnded(qViewHolder);
            }
        });

        mHeartItemAnimations.put(qViewHolder, animatorSet);
        animatorSet.start();
    }

    private void dispatchChangeFinishedIfAllAnimationsEnded(RecyclerView.ViewHolder viewHolder) {
        if (mHeartButtonAnimations.containsKey(viewHolder) || mHeartItemAnimations.containsKey(viewHolder)) {
            return;
        }

        dispatchAnimationFinished(viewHolder);
    }

    private void resetAnimationState(Q_ViewHolder qViewHolder) {
        qViewHolder.mItemHeart.setVisibility(View.GONE);
        qViewHolder.mItemHeartBack.setVisibility(View.GONE);
    }
}
