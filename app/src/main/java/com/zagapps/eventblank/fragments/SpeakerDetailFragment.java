package com.zagapps.eventblank.fragments;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commonsware.cwac.merge.MergeAdapter;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.GenericDraweeView;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;
import com.zagapps.eventblank.adapters.SpeakersCardViewAdapter;
import com.zagapps.eventblank.events.BackStackChanged;
import com.zagapps.eventblank.models.ModelManager;
import com.zagapps.eventblank.models.Speakers;
import com.zagapps.eventblank.R;

import de.greenrobot.event.EventBus;

public class SpeakerDetailFragment extends ListFragment
{
    private Speakers mSpeaker;

    private ImageView mBackNavigation;
    private Drawable likeFull;
    private Drawable likeEmpty;
    private TextView mTitle;
    private EventBus mEventBus=EventBus.getDefault();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.speaker_detail_fragment, container, false);

        Bundle bundle=getArguments();


        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        int speakerID=bundle.getInt(SpeakersCardViewAdapter.SPEAKER_ID, 0);
        int ternaryColor= ModelManager.get(getActivity()).getEvent().getTernaryColor();
        mSpeaker=ModelManager.get(getActivity()).getSpeakerById(speakerID);

        SpeakerDetails speakerDetails=new SpeakerDetails(getActivity());

        view.findViewById(R.id.speaker_detail_header).setBackgroundColor(ModelManager.get(getActivity()).getEvent().getMainColor());
        mTitle=(TextView)view.findViewById(R.id.speaker_detail_title);
        mTitle.setTextColor(ternaryColor);
        mTitle.setText(mSpeaker.getName());

        speakerDetails.mName.setText(mSpeaker.getName());
        speakerDetails.mDescription.setText(mSpeaker.getBio());

        speakerDetails.mTwitterTag.setText(mSpeaker.getSpeakerTwitter());
        speakerDetails.mTwitterTag.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                WebViewFragment fragment = new WebViewFragment();
                Bundle urlBundle = new Bundle();
                urlBundle.putInt(WebViewFragment.URL_TYPE, WebViewFragment.TWITTER_URL);
                urlBundle.putString(WebViewFragment.URL, mSpeaker.getSpeakerTwitter());
                fragment.setArguments(urlBundle);
                getFragmentManager().beginTransaction().addToBackStack(fragment.getClass().getSimpleName()).
                        setCustomAnimations(R.anim.slide_in_left,R.anim.no_anim, R.anim.no_anim, R.anim.slide_out_right).
                        add(R.id.speakersContainer, fragment, fragment.getClass().getSimpleName()).commit();

            }
        });

        speakerDetails.mUrl.setText(mSpeaker.getSpeakerUrl());
        speakerDetails.mUrl.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                WebViewFragment fragment = new WebViewFragment();
                Bundle urlBundle = new Bundle();
                urlBundle.putInt(WebViewFragment.URL_TYPE, WebViewFragment.WEBSITE_URL);
                urlBundle.putString(WebViewFragment.URL, mSpeaker.getSpeakerUrl());
                fragment.setArguments(urlBundle);

                getFragmentManager().beginTransaction().addToBackStack(fragment.getClass().getSimpleName()).
                        setCustomAnimations(R.anim.slide_in_left,R.anim.no_anim, R.anim.no_anim, R.anim.slide_out_right).
                        add(R.id.speakersContainer, fragment, fragment.getClass().getSimpleName()).commit();
            }
        });

        BitmapDrawable profile=new BitmapDrawable(getActivity().getResources(), mSpeaker.getPhoto());
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(getActivity().getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setPlaceholderImage(profile)
                .setRoundingParams(RoundingParams.fromCornersRadius(10f))
                .build();
        speakerDetails.mPicture.setHierarchy(hierarchy);

        Drawable favouriteIconFull= ContextCompat.getDrawable(getActivity(), R.mipmap.favourite_full_36);
        favouriteIconFull.mutate().setColorFilter(Color.parseColor("#e5520b"), PorterDuff.Mode.SRC_ATOP);
        likeFull=favouriteIconFull;

        Drawable favouriteIcon= ContextCompat.getDrawable(getActivity(),R.mipmap.favourite_36);
        favouriteIcon.mutate().setColorFilter(Color.parseColor("#999999"), PorterDuff.Mode.SRC_ATOP);
        likeEmpty=favouriteIcon;

        final ImageView favouriteImage=speakerDetails.mFavouriteImage;

        if(mSpeaker.isFavourite())
        {
            favouriteImage.setImageDrawable(likeFull);
            favouriteImage.setActivated(true);
        }
        else
        {
            favouriteImage.setImageDrawable(likeEmpty);
            favouriteImage.setActivated(false);
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
                if(!favouriteImage.isActivated())
                    favouriteImage.setImageDrawable(likeEmpty);
                else
                    favouriteImage.setImageDrawable(likeFull);

                favouriteImage.startAnimation(shakeDown);
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });

        speakerDetails.mFavouriteImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!favouriteImage.isActivated())
                {
                    favouriteImage.setActivated(true);
                    favouriteImage.startAnimation(shakeUp);
                    ModelManager.get(getActivity()).favouriteSpeakerChanged(mSpeaker.getIDSpeaker(), true);
                    mEventBus.post(new BackStackChanged());
                } else
                {
                    favouriteImage.setActivated(false);
                    favouriteImage.startAnimation(shakeUp);
                    ModelManager.get(getActivity()).favouriteSpeakerChanged(mSpeaker.getIDSpeaker(), false);
                    mEventBus.post(new BackStackChanged());
                }
            }
        });

        mBackNavigation=(ImageView)view.findViewById(R.id.speaker_detail_back_navigation);
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

        MergeAdapter mergeAdapter=new MergeAdapter();

        UserTimeline userTimeline = new UserTimeline.Builder().screenName(mSpeaker.getSpeakerTwitter()).build();

        final TweetTimelineListAdapter twitterAdapter = new TweetTimelineListAdapter.Builder(getActivity())
                .setTimeline(userTimeline)
                .build();
        mergeAdapter.addView(speakerDetails);
        mergeAdapter.addAdapter(twitterAdapter);
        setListAdapter(mergeAdapter);

        return view;
    }

    public static class SpeakerDetails extends LinearLayout
    {
        protected TextView mSpeakersDetails;
        protected TextView mLatestTweets;
        protected GenericDraweeView mPicture;
        protected TextView mName;
        protected TextView mTwitterTag;
        protected TextView mUrl;
        protected TextView mDescription;
        protected ImageView mFavouriteImage;

        public SpeakerDetails(Context context)
        {
            super(context);
            init((Activity)context);
        }

        public SpeakerDetails(Context context, AttributeSet attrs)
        {
            super(context, attrs);
            init((Activity) context);
        }

        public SpeakerDetails(Context context, AttributeSet attrs, int defStyleAttr)
        {
            super(context, attrs, defStyleAttr);
            init((Activity) context);
        }

        private void init(Activity activity)
        {
            inflate(activity,R.layout.speaker_detail_first_item,this);
            mSpeakersDetails=(TextView)findViewById(R.id.speaker_speaker_details);
            mLatestTweets=(TextView)findViewById(R.id.speaker_latest_tweets);
            mPicture=(GenericDraweeView)findViewById(R.id.speaker_detail_picture);
            mName=(TextView)findViewById(R.id.speaker_detail_name);
            mTwitterTag=(TextView)findViewById(R.id.speaker_detail_speaker_twitter);
            mUrl=(TextView)findViewById(R.id.speaker_detail_speaker_url);
            mDescription=(TextView)findViewById(R.id.speaker_detail_description);
            mFavouriteImage=(ImageView)findViewById(R.id.speaker_detail_favourite);
        }

    }
}
