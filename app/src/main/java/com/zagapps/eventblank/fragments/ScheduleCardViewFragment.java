package com.zagapps.eventblank.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zagapps.eventblank.adapters.ScheduleCardViewAdapter;
import com.zagapps.eventblank.adapters.SchedulePagerAdapter;
import com.zagapps.eventblank.events.FavouriteSessionFilter;
import com.zagapps.eventblank.models.ModelManager;
import com.zagapps.eventblank.R;

import de.greenrobot.event.EventBus;

public class ScheduleCardViewFragment extends Fragment
{
    private RecyclerView mRecyclerView;
    private ScheduleCardViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int mPosition;
    private EventBus mEventBus = EventBus.getDefault();




    @Override
    public void onCreate(Bundle onSavedInstanceState)
    {
        super.onCreate(onSavedInstanceState);
        Bundle bundle=getArguments();
        mPosition=bundle.getInt(SchedulePagerAdapter.TAB_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.schedule_recyclerview, parent, false);

        mRecyclerView=(RecyclerView)view.findViewById(R.id.schedule_recycler);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager=new LinearLayoutManager(getActivity());
        
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter= new ScheduleCardViewAdapter(getActivity(),getParentFragment().getFragmentManager(),mPosition);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    public void onEvent(FavouriteSessionFilter filter)
    {
        mAdapter.switchLists(filter.isFavourite());
        ModelManager.get(getActivity()).setFavouriteFilter(filter.isFavourite());
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mEventBus.unregister(this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mEventBus.register(this);
    }
}