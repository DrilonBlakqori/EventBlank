package com.zagapps.eventblank.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.GenericDraweeView;
import com.zagapps.eventblank.events.BackStackChanged;
import com.zagapps.eventblank.fragments.ScheduleDetailFragment;
import com.zagapps.eventblank.models.Locations;
import com.zagapps.eventblank.models.ModelManager;
import com.zagapps.eventblank.models.Sessions;
import com.zagapps.eventblank.models.Speakers;
import com.zagapps.eventblank.models.Tracks;
import com.zagapps.eventblank.R;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;


public class ScheduleCardViewAdapter extends RecyclerView.Adapter<ScheduleCardViewAdapter.ViewHolder>
{
    private ArrayList<Sessions> mSessions;
    private ArrayList<Sessions> mFilteredSessions;
    private ArrayList<Sessions> mUsingSessions;
    private FragmentManager mFragmentManager;
    private boolean mIsFavouriteActivated;
    private Drawable likeFull;
    private Drawable likeEmpty;
    private EventBus mEventBus= EventBus.getDefault();

    public static final String SESSION_ID="session_id";
    private int mPosition;
    private Activity mActivity;

    public ScheduleCardViewAdapter(Activity activity, FragmentManager fragmentManager, int position)
    {
        mActivity=activity;
        mFragmentManager=fragmentManager;
        mPosition=position;
        mFilteredSessions=new ArrayList<>();

        mSessions = ModelManager.get(activity).getSessionsByDate(ModelManager.get(activity).getEvent()
                .getBeginDate() + (mPosition) * ModelManager.DAY_TO_MILLIS);

        switchLists(ModelManager.get(activity).isFavouriteFilter());

        Drawable favouriteIconFull= ContextCompat.getDrawable(activity, R.mipmap.favourite_full_36);
        favouriteIconFull.mutate().setColorFilter(Color.parseColor("#e5520b"), PorterDuff.Mode.SRC_ATOP);
        likeFull=favouriteIconFull;

        Drawable favouriteIcon= ContextCompat.getDrawable(activity,R.mipmap.favourite_36);
        favouriteIcon.mutate().setColorFilter(Color.parseColor("#999999"), PorterDuff.Mode.SRC_ATOP);
        likeEmpty=favouriteIcon;
    }




    @Override
    public ScheduleCardViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        CardView view=(CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_cards, parent, false);

        return new ViewHolder(view,mFragmentManager);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)
    {
        int secondaryColor=ModelManager.get(mActivity).getEvent().getSecondaryColor();
        final Sessions session=mUsingSessions.get(position);

        holder.mTitle.setText(session.getTitle());

        holder.mTime.setText(ModelManager.getDate(session.getBeginTime(), "hh:mm a"));
        holder.mTime.setTextColor(ModelManager.get(mActivity).getEvent().getMainColor());

        Tracks track=ModelManager.get(mActivity).getTrackById(session.getFkTrack());
        holder.mTrack.setText(track.getTrack());
        holder.mTrack.setTextColor(secondaryColor);

        Locations location=ModelManager.get(mActivity).getLocationById(session.getFkLocation());
        holder.mLocation.setText(location.getLocationName());

        Speakers speaker=ModelManager.get(mActivity).getSpeakerById(session.getFkSpeaker());
        holder.mSpeakerName.setText(speaker.getName());

        BitmapDrawable profile=new BitmapDrawable(mActivity.getResources(), speaker.getPhoto());
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(mActivity.getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setPlaceholderImage(profile)
                .setRoundingParams(RoundingParams.asCircle())
                .build();
        holder.mProfilePicture.setHierarchy(hierarchy);
        holder.mBundle.putInt(SESSION_ID, session.getID());

        if(session.isFavourite())
        {
            holder.mFavouriteImage.setImageDrawable(likeFull);
            holder.mFavouriteImage.setActivated(true);
        }
        else
        {
            holder.mFavouriteImage.setImageDrawable(likeEmpty);
            holder.mFavouriteImage.setActivated(false);
        }

        final Animation shakeUp= AnimationUtils.loadAnimation(mActivity,R.anim.image_shake_up);
        final Animation shakeDown= AnimationUtils.loadAnimation(mActivity, R.anim.image_shake_down);
        shakeUp.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                if(!holder.mFavouriteImage.isActivated())
                    holder.mFavouriteImage.setImageDrawable(likeEmpty);
                else
                    holder.mFavouriteImage.setImageDrawable(likeFull);

                holder.mFavouriteImage.startAnimation(shakeDown);
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });

        holder.mFavouriteImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(holder.mFavouriteImage.isActivated())
                {
                    holder.mFavouriteImage.setActivated(false);
                    holder.mFavouriteImage.startAnimation(shakeUp);
                    if(mIsFavouriteActivated)
                    {
                        mUsingSessions.remove(position);
                        notifyDataSetChanged();
                    }
                    ModelManager.get(mActivity).favouriteSessionChanged(session.getID(), false);
                }
                else
                {
                    holder.mFavouriteImage.setActivated(true);
                    holder.mFavouriteImage.startAnimation(shakeUp);
                    ModelManager.get(mActivity).favouriteSessionChanged(session.getID(), true);
                }
            }
        });

    }


    @Override
    public int getItemCount()
    {
        return mUsingSessions.size();
    }


    public void switchLists(boolean isFavourite)
    {
        mFilteredSessions.clear();
        mIsFavouriteActivated = isFavourite;


        if(!isFavourite)
            mUsingSessions = mSessions;
        else
        {
            for (Sessions session : mSessions)
            {
                if(session.isFavourite())
                    mFilteredSessions.add(session);
            }
            mUsingSessions = mFilteredSessions;
        }
        notifyDataSetChanged();
    }

    public void onEvent(BackStackChanged backStackChanged)
    {
        notifyDataSetChanged();
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView)
    {
        super.onDetachedFromRecyclerView(recyclerView);
        mEventBus.unregister(this);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
        mEventBus.register(this);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        protected Bundle mBundle;
        protected TextView mTime;
        protected TextView mTitle;
        protected TextView mSpeakerName;
        protected TextView mTrack;
        protected TextView mLocation;
        protected GenericDraweeView mProfilePicture;
        private ImageView mFavouriteImage;
        protected FragmentManager mFragmentManager;
        protected RelativeLayout mCardHeader;

        public ViewHolder(View view,FragmentManager fragmentManager)
        {
                super(view);
                mTime=(TextView)view.findViewById(R.id.schedule_card_time);
                mTitle=(TextView)view.findViewById(R.id.schedule_card_title);
                mSpeakerName =(TextView)view.findViewById(R.id.schedule_card_speaker_name);
                mTrack =(TextView)view.findViewById(R.id.schedule_card_track);
                mLocation=(TextView)view.findViewById(R.id.schedule_card_location);
                mProfilePicture=(GenericDraweeView)view.findViewById(R.id.schedule_card_profile_picture);
                mFavouriteImage =(ImageView)view.findViewById(R.id.schedule_card_favourite);
                mFragmentManager=fragmentManager;
                mBundle=new Bundle();
                mCardHeader=(RelativeLayout)view.findViewById(R.id.card_header);

                view.setOnClickListener(new View.OnClickListener()
                {
                @Override
                public void onClick(View v)
                {
                    ScheduleDetailFragment fragment=new ScheduleDetailFragment();
                    fragment.setArguments(mBundle);
                    FragmentTransaction transaction=mFragmentManager.beginTransaction();

                    transaction.addToBackStack(fragment.getClass().getSimpleName()).
                            setCustomAnimations(R.anim.slide_in_left,R.anim.no_anim, R.anim.no_anim, R.anim.slide_out_right).
                            add(R.id.scheduleContainer, fragment, fragment.getClass().getSimpleName()).commit();
                }
            });
        }


    }
}
