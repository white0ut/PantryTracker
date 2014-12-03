package com.whiteout.pantrytracker.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.whiteout.pantrytracker.R;
import com.whiteout.pantrytracker.fragments.RecipeFragment;

/**
 * Author:  Kendrick Cline
 * Date:    12/3/14
 * Email:   kdecline@gmail.com
 */
public class RecipeActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        FragmentManager fm = getSupportFragmentManager();

        Fragment recipeFrag = fm.findFragmentById(R.layout.fragment_recipe);

        if (recipeFrag == null) {
            fm.beginTransaction()
                    .add(R.id.fragmentContainer,
                            new RecipeFragment())
            .commit();
        }
    }
}
