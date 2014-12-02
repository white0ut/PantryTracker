package com.whiteout.pantrytracker.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.whiteout.pantrytracker.fragments.ItemListFragment;

/**
 * Author:  Kendrick Cline
 * Date:    10/29/14
 * Email:   kdecline@gmail.com
 */
public class PantryPagerAdapter extends FragmentPagerAdapter {

    FragmentManager fm;

    public PantryPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Test";
    }

    @Override
    public Fragment getItem(int position) {
        return new ItemListFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }


}
