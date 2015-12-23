package com.zagapps.eventblank.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.GenericDraweeView;
import com.zagapps.eventblank.adapters.ScheduleCardViewAdapter;
import com.zagapps.eventblank.models.Locations;
import com.zagapps.eventblank.models.ModelManager;
import com.zagapps.eventblank.models.Sessions;
import com.zagapps.eventblank.models.Speakers;
import com.zagapps.eventblank.R;

import java.util.Locale;


public class ScheduleDetailFragment extends Fragment
{
    private TextView mTitle;
    private TextView mSpeaker;
    private TextView mDescription;
    private GenericDraweeView mPicture;
    private ImageView mBackNavigation;
    private Sessions mSession;
    private TextView mTwitter;
    private TextView mUrl;
    private TextView mLocation;
    private ImageView mFavouriteImage;
    private Drawable likeFull;
    private Drawable likeEmpty;
    private TextView mHeaderTitle;



    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState)
    {
        View view= inflater.inflate(R.layout.schedule_detail_fragment, parent, false);
        Bundle bundle=getArguments();
        mTitle=(TextView)view.findViewById(R.id.schedule_detail_title);
        mSpeaker =(TextView)view.findViewById(R.id.schedule_detail_speaker);
        mDescription=(TextView)view.findViewById(R.id.schedule_detail_description);
        mPicture=(GenericDraweeView)view.findViewById(R.id.schedule_detail_picture);
        mBackNavigation=(ImageView)view.findViewById(R.id.schedule_detail_back_navigation);
        mTwitter=(TextView)view.findViewById(R.id.schedule_detail_speaker_twitter);
        mUrl=(TextView)view.findViewById(R.id.schedule_detail_speaker_url);
        mFavouriteImage=(ImageView)view.findViewById(R.id.schedule_detail_favourite);
        mLocation= (TextView)view.findViewById(R.id.schedule_detail_location);
        mHeaderTitle= (TextView)view.findViewById(R.id.schedule_detail_header_title);

        int sessionId=bundle.getInt(ScheduleCardViewAdapter.SESSION_ID);
        int ternaryColor= ModelManager.get(getActivity()).getEvent().getTernaryColor();

        mSession = ModelManager.get(getActivity()).getSessionById(sessionId);
        final Speakers speaker=ModelManager.get(getActivity()).getSpeakerById(mSession.getFkSpeaker());


        view.findViewById(R.id.schedule_detail_header).setBackgroundColor(ModelManager.get(getActivity()).getEvent().getMainColor());

        mHeaderTitle.setTextColor(ternaryColor);

        mTitle.setText(String.format(getResources().getString(R.string.schedule_detail_title),
                ModelManager.getDate(mSession.getBeginTime(), "h:mm a"),
                mSession.getTitle()));
        mTitle.setTextColor(ModelManager.get(getActivity()).getEvent().getMainColor());

        mSpeaker.setText(speaker.getName());
        mDescription.setText(mSession.getDescription());

        mTwitter.setText(speaker.getSpeakerTwitter());
        mTwitter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                WebViewFragment fragment = new WebViewFragment();
                Bundle urlBundle = new Bundle();
                urlBundle.putInt(WebViewFragment.URL_TYPE, WebViewFragment.TWITTER_URL);
                urlBundle.putString(WebViewFragment.URL, speaker.getSpeakerTwitter());
                fragment.setArguments(urlBundle);

                getFragmentManager().beginTransaction().addToBackStack(fragment.getClass().getSimpleName()).
                        setCustomAnimations(R.anim.slide_in_left, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out_right).
                        replace(R.id.scheduleContainer, fragment, fragment.getClass().getSimpleName()).commit();

            }
        });

        mUrl.setText(speaker.getSpeakerUrl());
        mUrl.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                WebViewFragment fragment = new WebViewFragment();
                Bundle urlBundle = new Bundle();
                urlBundle.putInt(WebViewFragment.URL_TYPE, WebViewFragment.WEBSITE_URL);
                urlBundle.putString(WebViewFragment.URL, speaker.getSpeakerUrl());
                fragment.setArguments(urlBundle);

                getFragmentManager().beginTransaction().addToBackStack(fragment.getClass().getSimpleName()).
                        setCustomAnimations(R.anim.slide_in_left, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out_right).
                        replace(R.id.scheduleContainer, fragment, fragment.getClass().getSimpleName()).commit();
            }
        });

        BitmapDrawable profile=new BitmapDrawable(getActivity().getResources(), speaker.getPhoto());
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(getActivity().getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setPlaceholderImage(profile)
                .setRoundingParams(RoundingParams.fromCornersRadius(10f))
                .build();
        mPicture.setHierarchy(hierarchy);

        Drawable backNavigator= ContextCompat.getDrawable(getActivity(), R.mipmap.arrow_back);
        backNavigator.mutate().setColorFilter(ternaryColor, PorterDuff.Mode.SRC_ATOP);

        mBackNavigation.setImageDrawable(backNavigator);
        mBackNavigation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getFragmentManager().popBackStackImmediate();

            }
        });

        Drawable favouriteIconFull= ContextCompat.getDrawable(getActivity(), R.mipmap.favourite_full_36);
        favouriteIconFull.mutate().setColorFilter(Color.parseColor("#e5520b"), PorterDuff.Mode.SRC_ATOP);
        likeFull=favouriteIconFull;

        Drawable favouriteIcon= ContextCompat.getDrawable(getActivity(),R.mipmap.favourite_36);
        favouriteIcon.mutate().setColorFilter(Color.parseColor("#999999"), PorterDuff.Mode.SRC_ATOP);
        likeEmpty=favouriteIcon;

        final Animation shakeUp= AnimationUtils.loadAnimation(getActivity(),R.anim.image_shake_up);
        final Animation shakeDown= AnimationUtils.loadAnimation(getActivity(), R.anim.image_shake_up);

        if(mSession.isFavourite())
        {
            mFavouriteImage.setImageDrawable(likeFull);
            mFavouriteImage.setActivated(true);
        }
        else
        {
            mFavouriteImage.setImageDrawable(likeEmpty);
            mFavouriteImage.setActivated(false);
        }

        mFavouriteImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!mFavouriteImage.isActivated())
                {
                    mFavouriteImage.setActivated(true);
                    mFavouriteImage.startAnimation(shakeUp);
                    mFavouriteImage.setImageDrawable(likeFull);
                    mFavouriteImage.startAnimation(shakeDown);
                    ModelManager.get(getActivity()).favouriteSessionChanged(mSession.getID(), true);
                } else
                {
                    mFavouriteImage.setActivated(false);
                    mFavouriteImage.startAnimation(shakeUp);
                    mFavouriteImage.setImageDrawable(likeEmpty);
                    mFavouriteImage.startAnimation(shakeDown);
                    ModelManager.get(getActivity()).favouriteSessionChanged(mSession.getID(), false);
                }
            }
        });

        final Locations location= ModelManager.get(getActivity()).getLocationById(mSession.getFkLocation());
        mLocation.setText(location.getLocationName());

        if(location.getLat()!=0 && location.getLng()!=0)
        {
            mLocation.setClickable(true);
            mLocation.setTextColor(Color.parseColor("#3366BB"));
            mLocation.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    double lat = location.getLat();
                    double lon = location.getLng();
                    String uri = String.format(
                            Locale.ENGLISH, "geo:%f,%f?q=%f,%f",
                            lat, lon,
                            lat, lon
                    );
                    if(location.getLocationName() != null)
                    {
                        uri += String.format("(%s)", location.getLocationName().replace("&", String.format(" %s ", getString(R.string.and))));
                    }
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    if(intent.resolveActivity(getActivity().getPackageManager()) != null)
                    {
                        startActivity(intent);
                    }
                }
            });
        }
        else
            mLocation.setClickable(false);

        return view;
    }


}
