package com.whiteout.pantrytracker.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.whiteout.pantrytracker.fragments.ItemListFragment;
import com.whiteout.pantrytracker.fragments.RecipeSearchFragment;

/**
 * Author:  Kendrick Cline
 * Date:    10/29/14
 * Email:   kdecline@gmail.com
 */
public class PantryPagerAdapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT = 2;

    FragmentManager fm;

    public PantryPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0:
                return "My Items";
            case 1:
            default:
                return "Discover";
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ItemListFragment();
            case 1:
            default:
                return new RecipeSearchFragment();
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }


}
