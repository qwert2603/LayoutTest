package com.qwert2603.layouttest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.qwert2603.layouttest.recycler.RecyclerActivity;

public class MainActivity extends AppCompatActivity {

    ViewGroup mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRootView = (ViewGroup) findViewById(R.id.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.make_switch) {
            View newView;
            if (mRootView.getChildAt(0) instanceof CustomView) {
                newView = new ScaleView(this);
            } else if (mRootView.getChildAt(0) instanceof ScaleView) {
                newView = new PathView(this);
            } else if (mRootView.getChildAt(0) instanceof PathView) {
                newView = new PicsView(this);
            } else {
                newView = new CustomView(this);
            }
            mRootView.removeViewAt(0);
            mRootView.addView(newView);
            return true;
        } else if (item.getItemId() == R.id.to_anims) {
            startActivity(new Intent(this, AnimVectorsActivity.class));
            return true;
        } else if (item.getItemId() == R.id.to_line_activity) {
            startActivity(new Intent(this, LineActivity.class));
            return true;
        } else if (item.getItemId() == R.id.to_sunset) {
            startActivity(new Intent(this, SunsetActivity.class));
            return true;
        } else if (item.getItemId() == R.id.to_recycler) {
            startActivity(new Intent(this, RecyclerActivity.class));
            return true;
        } else if (item.getItemId() == R.id.to_seek_anim) {
            startActivity(new Intent(this, SeekAnimActivity.class));
            return true;
        } else if (item.getItemId() == R.id.to_digits) {
            startActivity(new Intent(this, VectorDigitsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
