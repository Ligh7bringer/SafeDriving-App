package com.safe.sgeor.safedriving;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by sgeor on 14/02/2018.
 */
//This adapter class renders the appropriate Fragment to each ViewPager’s page.
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private static int TAB_COUNT = 2;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    //returns item at given position
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return HomeFragment.newInstance();
            case 1:
                return MapFragment.newInstance();
        }
        return null;
    }

    //number of tabs
    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    //returns tab title at position
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return HomeFragment.TITLE;
            case 1:
                return MapFragment.TITLE;
        }
        return super.getPageTitle(position);
    }

}
