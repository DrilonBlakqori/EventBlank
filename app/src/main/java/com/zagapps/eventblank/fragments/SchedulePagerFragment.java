package com.zagapps.eventblank.fragments;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.zagapps.eventblank.adapters.SchedulePagerAdapter;
import com.zagapps.eventblank.events.FavouriteSessionFilter;
import com.zagapps.eventblank.models.ModelManager;
import com.zagapps.eventblank.R;

import de.greenrobot.event.EventBus;


public class SchedulePagerFragment extends Fragment
{
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private SchedulePagerAdapter mPagerAdapter;
    private ImageView mFavouriteFilter;
    private Drawable likeFull;
    private Drawable likeEmpty;
    public FavouriteSessionFilter mFavouriteSessionBus;
    private EventBus mEventBus=EventBus.getDefault();

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup parent,Bundle onSavedInstanceState)
    {
        View view=inflater.inflate(R.layout.schedule_fragment,parent,false);
        int mainColor= ModelManager.get(getActivity()).getEvent().getMainColor();
        int secondaryColor= ModelManager.get(getActivity()).getEvent().getSecondaryColor();
        int ternaryColor= ModelManager.get(getActivity()).getEvent().getTernaryColor();

        mViewPager =(ViewPager)view.findViewById(R.id.schedule_view_pager);
        mPagerAdapter=new SchedulePagerAdapter(getChildFragmentManager(),getActivity());
        mViewPager.setAdapter(mPagerAdapter);


        mTabLayout=(TabLayout)view.findViewById(R.id.schedule_tabs);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        view.findViewById(R.id.schedule_header).setBackgroundColor(mainColor);

        Drawable favouriteIconFull= ContextCompat.getDrawable(getActivity(),R.mipmap.favourite_full );
        favouriteIconFull.mutate().setColorFilter(ternaryColor, PorterDuff.Mode.SRC_ATOP);
        likeFull=favouriteIconFull;

        Drawable favouriteIcon= ContextCompat.getDrawable(getActivity(),R.mipmap.favourite);
        favouriteIcon.mutate().setColorFilter(ternaryColor, PorterDuff.Mode.SRC_ATOP);
        likeEmpty=favouriteIcon;


        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setSelectedTabIndicatorColor(secondaryColor);
        mTabLayout.setTabTextColors(ternaryColor, ternaryColor);


        mFavouriteFilter =(ImageView)view.findViewById(R.id.schedule_favourite_image);

        if(mFavouriteSessionBus !=null && mFavouriteSessionBus.isFavourite())
        {
            mFavouriteFilter.setImageDrawable(likeFull);
            mFavouriteFilter.setActivated(true);
        }
        else
        {
            mFavouriteFilter.setImageDrawable(likeEmpty);
            mFavouriteFilter.setActivated(false);
        }

        final Animation shakeUp= AnimationUtils.loadAnimation(getActivity(),R.anim.image_shake_up);
        final Animation shakeDown= AnimationUtils.loadAnimation(getActivity(),R.anim.image_shake_down);
        shakeUp.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                if(!mFavouriteFilter.isActivated())
                    mFavouriteFilter.setImageDrawable(likeEmpty);
                else
                    mFavouriteFilter.setImageDrawable(likeFull);

                mFavouriteFilter.startAnimation(shakeDown);
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });

        mFavouriteFilter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!mFavouriteFilter.isActivated())
                {
                    mFavouriteFilter.setActivated(true);
                    mFavouriteFilter.startAnimation(shakeUp);
                    mFavouriteSessionBus =new FavouriteSessionFilter(true);
                    mEventBus.post(mFavouriteSessionBus);

                }
                else
                {
                    mFavouriteFilter.setActivated(false);
                    mFavouriteFilter.startAnimation(shakeUp);
                    mFavouriteSessionBus =new FavouriteSessionFilter(false);
                    mEventBus.post(mFavouriteSessionBus);
                }
            }

        });


        return view;
    }
}
