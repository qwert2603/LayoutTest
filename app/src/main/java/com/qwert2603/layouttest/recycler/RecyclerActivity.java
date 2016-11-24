package com.qwert2603.layouttest.recycler;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.qwert2603.layouttest.LogUtils;
import com.qwert2603.layouttest.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecyclerActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private int mCurrLM;
    private List<RecyclerView.LayoutManager> mLayoutManagers = new ArrayList<>();
    private ItemTouchHelper mItemTouchHelper;
    private Q_Adapter mQAdapter;
    private Q_ItemDecorator mQItemDecorator;

    private CoordinatorLayout mRootView;

    private Toolbar mToolbar;
    private AppBarLayout mAppBarLayout;
    private TextView mToolbarTitle;
    private MenuItem mMenuItem;
    private FloatingActionButton mFloatingActionButton;

    private boolean mPendingIntroAnimation;

    private static final int ANIMATION_DURATION_TOOLBAR = 400;
    private static final int ANIMATION_DURATION_FAB = 500;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LogUtils.d("onCreate b");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        mRootView = (CoordinatorLayout) findViewById(R.id.root_view);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);

        mToolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.smoothScrollToPosition(0);
            }
        });
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.heart_empty_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerActivity.this.finish();
            }
        });

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(mRootView, "click!", Snackbar.LENGTH_SHORT).show();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        int columns = getResources().getInteger(R.integer.columns);

        mLayoutManagers.add(new LinearLayoutManager(RecyclerActivity.this));
        mLayoutManagers.add(new GridLayoutManager(RecyclerActivity.this, columns));
        mLayoutManagers.add(new StaggeredGridLayoutManager(columns, StaggeredGridLayoutManager.VERTICAL));

        mCurrLM = new Random().nextInt(3);

        mQAdapter = new Q_Adapter(new OnStartDragListener() {
            @Override
            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                mItemTouchHelper.startDrag(viewHolder);
            }
        });
        mRecyclerView.setAdapter(mQAdapter);
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(1, 14);
        mRecyclerView.setLayoutManager(mLayoutManagers.get(mCurrLM));
        mRecyclerView.setItemAnimator(new Q_ItemAnimator());

        mQItemDecorator = new Q_ItemDecorator(RecyclerActivity.this);
        mRecyclerView.addItemDecoration(mQItemDecorator);


        mItemTouchHelper = new ItemTouchHelper(new SimpleItemTouchHelperCallback(mQAdapter));
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);


        if (savedInstanceState == null) {
            mPendingIntroAnimation = true;
        } else {
            mQAdapter.addItems(RecyclerActivity.this, false);
        }
        LogUtils.d("onCreate e");

        /*
        * todo
        *
        * 1. скрытие fab при скролинге
        * */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        LogUtils.d("onCreateOptionsMenu b");
        getMenuInflater().inflate(R.menu.recycler_activity, menu);
        mMenuItem = menu.findItem(R.id.change_layout_manager);
        mMenuItem.setActionView(R.layout.menu_item_change_lm);
        mMenuItem.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrLM = (mCurrLM + 1) % mLayoutManagers.size();
                mRecyclerView.setLayoutManager(mLayoutManagers.get(mCurrLM));
            }
        });

        if (mPendingIntroAnimation) {
            mPendingIntroAnimation = false;
            startIntroAnimation();
        }
        LogUtils.d("onCreateOptionsMenu e");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.draw_borders) {
            mQItemDecorator.setDrawBorders(!mQItemDecorator.isDrawBorders());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startIntroAnimation() {
        LogUtils.d("startIntroAnimation");
        ViewGroup.MarginLayoutParams mFABLayoutParams = (ViewGroup.MarginLayoutParams) mFloatingActionButton.getLayoutParams();
        mFloatingActionButton.setTranslationY(mFloatingActionButton.getHeight() + mFABLayoutParams.bottomMargin);

        mToolbar.setTranslationY(-1 * mAppBarLayout.getHeight());
        mToolbar.animate()
                .translationY(0)
                .setStartDelay(300)
                .setDuration(ANIMATION_DURATION_TOOLBAR);

        mToolbarTitle.setTranslationY(-1 * mAppBarLayout.getHeight());
        mToolbarTitle.animate()
                .translationY(0)
                .setStartDelay(400)
                .setDuration(ANIMATION_DURATION_TOOLBAR);

        mMenuItem.getActionView().setTranslationY(-1 * mAppBarLayout.getHeight());
        mMenuItem.getActionView().animate()
                .translationY(0)
                .setStartDelay(500)
                .setDuration(ANIMATION_DURATION_TOOLBAR)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        startContentAnimation();
                    }
                });
    }

    private void startContentAnimation() {
        mFloatingActionButton.animate()
                .translationY(0)
                .setStartDelay(800)
                .setDuration(ANIMATION_DURATION_FAB)
                .setInterpolator(new OvershootInterpolator(2.0f));

        mQAdapter.addItems(RecyclerActivity.this, true);
    }
}
