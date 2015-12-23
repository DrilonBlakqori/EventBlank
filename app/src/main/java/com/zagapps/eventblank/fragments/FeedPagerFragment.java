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
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.zagapps.eventblank.adapters.FeedPagerAdapter;
import com.zagapps.eventblank.models.ModelManager;
import com.zagapps.eventblank.R;


public class FeedPagerFragment extends Fragment
{
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private FeedPagerAdapter mPagerAdapter;
    private ImageButton mImageButton;
    RelativeLayout relativeTwitter;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup parent,Bundle onSavedInstanceState)
    {
        final View view=inflater.inflate(R.layout.feed_fragment,parent,false);

        int mainColor= ModelManager.get(getActivity()).getEvent().getMainColor();
        int secondaryColor= ModelManager.get(getActivity()).getEvent().getSecondaryColor();
        int ternaryColor= ModelManager.get(getActivity()).getEvent().getTernaryColor();



        mViewPager =(ViewPager)view.findViewById(R.id.feed_view_pager);
        mPagerAdapter=new FeedPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);

        mTabLayout=(TabLayout)view.findViewById(R.id.feed_tabs);


        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setSelectedTabIndicatorColor(secondaryColor);
        mTabLayout.setTabTextColors(ternaryColor, ternaryColor);

        relativeTwitter = (RelativeLayout)view.findViewById(R.id.relative_image_container);

        mImageButton=(ImageButton)view.findViewById(R.id.image_twitter);
        Drawable drawable= ContextCompat.getDrawable(getActivity(), R.drawable.tw__ic_logo_default);
        drawable.mutate().setColorFilter(ternaryColor, PorterDuff.Mode.SRC_ATOP);
        mImageButton.setImageDrawable(drawable);
        mImageButton.setVisibility(View.VISIBLE);
        view.findViewById(R.id.feed_header).setBackgroundColor(mainColor);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                if(position == 0)
                {
                    mImageButton.setClickable(false);
                } else
                {
                    mImageButton.setClickable(true);
                }

                if(position != 1 || positionOffset != 0)
                {
                    mImageButton.setAlpha(positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position)
            {


            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });

        mImageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                TweetComposer.Builder builder = new TweetComposer.Builder(getContext())
                        .text(ModelManager.get(getActivity()).getEvent().getTwitterTag());
                builder.show();

            }
        });
        return view;
    }

}