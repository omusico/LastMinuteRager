package com.boomer.omer.lastminuterager.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.boomer.omer.lastminuterager.AppUtils;

/**
 * Created by Omer on 8/15/2015.
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter {

    Bundle bundle;
    public MainPagerAdapter(FragmentManager fm) {

        super(fm);
        this.bundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch(position){
            case 0:
                fragment = new Fragment_UserProfile();
                break;
            case 1:
                fragment = new Fragment_Events();
                break;
            case 2:
                fragment = new Fragment_Tickets();
                break;
        }

            return fragment;

    }


    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return AppUtils.TAB_USER_PROFILE;
                //break;
            case 1:
                return AppUtils.TAB_EVENTS;
                //break;
            case 2:
                return AppUtils.TAB_TICKETS;
                //break;

        }
        return null;
    }
}
