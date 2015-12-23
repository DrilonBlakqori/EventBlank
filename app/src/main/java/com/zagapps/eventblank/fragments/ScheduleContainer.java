package com.zagapps.eventblank.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zagapps.eventblank.R;


public class ScheduleContainer extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.schedule_container,parent,false);

        FragmentManager fm=getFragmentManager();
        android.support.v4.app.Fragment fragment =new SchedulePagerFragment();
        fm.beginTransaction().add(R.id.scheduleContainer,fragment).commit();

        return view;
    }
}
