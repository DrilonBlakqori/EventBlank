package com.zagapps.eventblank.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.zagapps.eventblank.fragments.FeedChatterFragment;
import com.zagapps.eventblank.fragments.FeedNewsFragment;


public class FeedPagerAdapter extends FragmentStatePagerAdapter {


    public FeedPagerAdapter(FragmentManager fm){

        super(fm);

    }

    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:
                return new FeedNewsFragment();

            case 1:
                return new FeedChatterFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position){

        switch (position)
        {
            case 0:
                return "News";
            case 1:
                return "Chatter";
            default:
                return "Tabs";
        }
    }
}
