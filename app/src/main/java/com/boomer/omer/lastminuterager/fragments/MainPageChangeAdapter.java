package com.boomer.omer.lastminuterager.fragments;

import android.support.v4.view.ViewPager;

/**
 * Created by Omer on 8/15/2015.
 */
public class MainPageChangeAdapter implements ViewPager.OnPageChangeListener {
    android.support.v7.app.ActionBar actionBar;
    public MainPageChangeAdapter( android.support.v7.app.ActionBar ab) {
        super();
        actionBar = ab;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        actionBar.setSelectedNavigationItem(position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
