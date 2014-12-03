package com.whiteout.pantrytracker.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.whiteout.pantrytracker.R;
import com.whiteout.pantrytracker.adapters.PantryPagerAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends FragmentActivity {

    private final Handler handler = new Handler();

    @InjectView(R.id.view_pager) ViewPager pager;
    @InjectView(R.id.tabs) PagerSlidingTabStrip tabs;



    PantryPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        adapter = new PantryPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);

        tabs.setShouldExpand(true);;
        tabs.setViewPager(pager);

    }

    private Drawable.Callback drawableCallback = new Drawable.Callback() {
        @Override
        public void invalidateDrawable(Drawable who) {
            getActionBar().setBackgroundDrawable(who);
        }

        @Override
        public void scheduleDrawable(Drawable who, Runnable what, long when) {
            handler.postAtTime(what, when);
        }

        @Override
        public void unscheduleDrawable(Drawable who, Runnable what) {
            handler.removeCallbacks(what);
        }
    };
}
