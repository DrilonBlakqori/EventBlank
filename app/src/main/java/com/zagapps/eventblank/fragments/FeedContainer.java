package com.zagapps.eventblank.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zagapps.eventblank.R;


public class FeedContainer extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.feed_container,parent,false);

        FragmentManager fm=getFragmentManager();
        android.support.v4.app.Fragment fragment =new FeedPagerFragment();
        fm.beginTransaction().add(R.id.feedContainer,fragment).commit();

        return view;
    }
}
