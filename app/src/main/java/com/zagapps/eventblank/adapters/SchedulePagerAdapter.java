package com.zagapps.eventblank.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.zagapps.eventblank.fragments.ScheduleCardViewFragment;
import com.zagapps.eventblank.models.ModelManager;


public class SchedulePagerAdapter extends FragmentStatePagerAdapter
{
    public static final String TAB_POSITION="tab_position";

    private Activity mActivity;

    private int mEventLength;

    public SchedulePagerAdapter(FragmentManager fm,Activity activity)
    {
        super(fm);
        mActivity=activity;
        mEventLength=ModelManager.get(mActivity).getEventLength();
    }

    @Override
    public Fragment getItem(int position)
    {
        Bundle bundle=new Bundle();
        bundle.putInt(TAB_POSITION,position);
        ScheduleCardViewFragment fragment=new ScheduleCardViewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount()
    {
        return mEventLength;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return ModelManager.getDate((ModelManager.get(mActivity).getEvent().getBeginDate()+
                position*ModelManager.DAY_TO_MILLIS),"EEE, MMM dd");
    }


}
