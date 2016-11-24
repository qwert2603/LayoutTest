package com.qwert2603.layouttest.recycler;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
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
                    Q_ItemHolderInfo qItemHolderInfo = new Q_ItemHolderInfo((String) payload);
                    qItemHolderInfo.setFrom(viewHolder);
                    return qItemHolderInfo;
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
        LogUtils.d("animateChange " + oldHolder + " " + newHolder + " " + preInfo + " " + postInfo);
        cancelCurrentAnimationIfExist(newHolder);

        if (preInfo instanceof Q_ItemHolderInfo) {
            Q_ItemHolderInfo qItemHolderInfo = (Q_ItemHolderInfo) preInfo;
            Q_ViewHolder holder = (Q_ViewHolder) newHolder;

            animateHeartButton(holder);
            updateTextSwitcher(holder, holder.getItem().getText());
            if (Q_ItemHolderInfo.ACTION_LIKE_TEXT_CLICKED.equals(qItemHolderInfo.mActionString)) {
                animateTextLike(holder);
            }
            return false;
        }

        return super.animateChange(oldHolder, newHolder, preInfo, postInfo);
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
    public boolean animateRemove(RecyclerView.ViewHolder holder) {
        Q_ViewHolder qViewHolder = (Q_ViewHolder) holder;
        if ((qViewHolder).getRemoveStyle() == Q_ViewHolder.RemoveStyle.ROTATE) {
            (qViewHolder).setRemoveStyle(Q_ViewHolder.RemoveStyle.NONE);
            runRemoveAnimation(qViewHolder);
            return true;
        }
        if ((qViewHolder).getRemoveStyle() == Q_ViewHolder.RemoveStyle.SWIPE) {
            (qViewHolder).setRemoveStyle(Q_ViewHolder.RemoveStyle.NONE);
            resetItemViewStateAfterRemove(qViewHolder);
            dispatchRemoveFinished(holder);
            return true;
        }
        (qViewHolder).setRemoveStyle(Q_ViewHolder.RemoveStyle.NONE);
        return super.animateRemove(holder);
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

    @Override
    public long getRemoveDuration() {
        return 500;
    }

    private void runRemoveAnimation(final Q_ViewHolder qViewHolder) {
        ObjectAnimator rotationY = ObjectAnimator.ofFloat(qViewHolder.itemView, "rotationY", 90);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(qViewHolder.itemView, "scaleY", 0.5f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(rotationY).with(scaleY);
        animatorSet.setDuration(getRemoveDuration());

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                LogUtils.d("runRemoveAnimation onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                LogUtils.d("runRemoveAnimation onAnimationEnd");
                resetItemViewStateAfterRemove(qViewHolder);
                dispatchRemoveFinished(qViewHolder);
            }
        });

        animatorSet.start();
    }

    private void runEnterAnimation(final Q_ViewHolder qViewHolder) {
        LogUtils.d("runEnterAnimation " + qViewHolder);
        int heightPixels = qViewHolder.itemView.getResources().getDisplayMetrics().heightPixels;
        qViewHolder.itemView.setTranslationY(heightPixels);
        qViewHolder.itemView.setRotationX(-60);
        qViewHolder.itemView.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.0f))
                .rotationX(0)
                .setDuration(1000)
                .setStartDelay(qViewHolder.getAdapterPosition() * 100)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        LogUtils.d("runEnterAnimation onAnimationStart " + qViewHolder);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        LogUtils.d("runEnterAnimation onAnimationEnd " + qViewHolder);
                        dispatchAddFinished(qViewHolder);
                        qViewHolder.itemView.animate().setStartDelay(0);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        qViewHolder.itemView.setTranslationY(0);
                        qViewHolder.itemView.setRotationX(0);
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
        });

        animator.play(scaleX).with(scaleY).after(rotation);

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mHeartButtonAnimations.remove(qViewHolder);
                dispatchChangeFinishedIfAllAnimationsEnded(qViewHolder);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                heartImageView.setRotation(0);
                heartImageView.setScaleX(1);
                heartImageView.setScaleY(1);
                heartImageView.setImageResource(R.drawable.heart_full);
            }
        });

        mHeartButtonAnimations.put(qViewHolder, animator);
        animator.start();
    }

    private void updateTextSwitcher(Q_ViewHolder qViewHolder, String text) {
        qViewHolder.mTextSwitcher.setCurrentText(null);
        qViewHolder.mTextSwitcher.setText(text);
    }

    private void animateTextLike(final Q_ViewHolder qViewHolder) {
        final ImageView itemHeart = qViewHolder.mItemHeart;
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
            public void onAnimationEnd(Animator animation) {
                mHeartItemAnimations.remove(qViewHolder);
                resetAnimationTextState(qViewHolder);
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

    private void resetAnimationTextState(Q_ViewHolder qViewHolder) {
        qViewHolder.mItemHeart.setVisibility(View.GONE);
        qViewHolder.mItemHeartBack.setVisibility(View.GONE);
    }

    private void resetItemViewStateAfterRemove(Q_ViewHolder qViewHolder) {
        qViewHolder.itemView.setRotationY(0);
        qViewHolder.itemView.setScaleY(1);
        qViewHolder.itemView.setAlpha(1);
    }
}
