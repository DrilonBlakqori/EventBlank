package com.zagapps.eventblank.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.zagapps.eventblank.fragments.FeedContainer;
import com.zagapps.eventblank.fragments.MainFragment;
import com.zagapps.eventblank.fragments.MoreFragmentContainer;
import com.zagapps.eventblank.fragments.ScheduleContainer;
import com.zagapps.eventblank.fragments.SpeakersContainer;


public class ActivityViewPagerAdapter extends FragmentStatePagerAdapter
{

    private String[] mTitles={"Main","Schedule","Feed","Speakers","More"};


    public ActivityViewPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int i)
    {
        switch(i)
        {
            case 0:
                return new MainFragment();
            case 1:
                return new ScheduleContainer();
            case 2:
                return new FeedContainer();
            case 3:
                return new SpeakersContainer();
            case 4:
                return new MoreFragmentContainer();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return mTitles[position];
    }


    @Override
    public int getCount()
    {
        return 5;
    }


}
