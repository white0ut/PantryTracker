package com.whiteout.pantrytracker.activities;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.astuetz.PagerSlidingTabStrip;
import com.whiteout.pantrytracker.R;
import com.whiteout.pantrytracker.adapters.PantryPagerAdapter;
import com.whiteout.pantrytracker.barcode.BarcodeScanner;
import com.whiteout.pantrytracker.data.PantryDataSource;
import com.whiteout.pantrytracker.data.model.RecipeSearch;
import com.whiteout.pantrytracker.data.web.interfaces.YummlyRecipeSearchRetriever;
import com.whiteout.pantrytracker.fragments.ItemListFragment;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends FragmentActivity {


    private PantryDataSource dataSource;

    @InjectView(R.id.view_pager) ViewPager pager;
    @InjectView(R.id.tabs) PagerSlidingTabStrip tabs;



    PantryPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity", "onCreate running");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        dataSource = new PantryDataSource(this);
        adapter = new PantryPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);

        tabs.setShouldExpand(true);;
        tabs.setViewPager(pager);

    }

    public PantryDataSource getDataSource() {
        return dataSource;
    }

    // Proof of concept
    private class RecipeRetrieveTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            YummlyRecipeSearchRetriever ret = new YummlyRecipeSearchRetriever();
            List<RecipeSearch> response = ret.fetchRecipes(new String[]{"vegetarian"});
            for (RecipeSearch s : response) {
                Log.d("Kenny", s.getRecipeName());
            }
            return null;
        }
    }
}
