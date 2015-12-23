package com.zagapps.eventblank.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;


import com.zagapps.eventblank.models.Event;
import com.zagapps.eventblank.models.ModelManager;
import com.zagapps.eventblank.R;
import com.zagapps.eventblank.models.Sessions;

import java.util.ArrayList;
import java.util.Calendar;


public class MainFragment extends Fragment
{
    private TextView mEventStatus;
    private boolean mAnimationRunning;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view= inflater.inflate(R.layout.main_fragment, container, false);
        final Event event=ModelManager.get(getActivity()).getEvent();

        ImageView logo = (ImageView) view.findViewById(R.id.main_logo);
        logo.setImageBitmap(event.getLogo());

        TextView eventTitle = (TextView) view.findViewById(R.id.main_event_title);
        eventTitle.setText(event.getTitle());
        eventTitle.setTextColor(event.getMainColor());

        TextView eventSubtitle = (TextView) view.findViewById(R.id.main_event_subtitle);
        eventSubtitle.setText(event.getSubtitle());

        TextView eventOrganizer = (TextView) view.findViewById(R.id.main_event_organizer);
        eventOrganizer.setText(event.getOrganizer());


        mEventStatus=(TextView)view.findViewById(R.id.main_event_status);



        mEventStatus.setTextColor(event.getSecondaryColor());

        final Animation animation=setUpAnimations();
        setEventStatus(event);

        mEventStatus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setEventStatus(event);
                if(!mAnimationRunning)
                    mEventStatus.startAnimation(animation);
            }
        });


        return view;
    }

    private void setEventStatus(Event event)
    {
        long beginTime= event.getBeginDate();
        long endTime= event.getEndDate();
        long rightNow=Calendar.getInstance().getTimeInMillis();
        if(rightNow<beginTime)
        {
            int days=(int)(beginTime-rightNow)/ModelManager.DAY_TO_MILLIS;

            if(days>1)
                mEventStatus.setText(String.format(getResources().getString(R.string.event_starting_in),days));
            else
                mEventStatus.setText(R.string.event_starting_within);
        }
        else
        if(beginTime<rightNow && endTime>rightNow)
        {
            ArrayList<Sessions> todaySessions=ModelManager.get(getActivity()).getSessionsByDate(rightNow);
            if(!todaySessions.isEmpty())
            {
                int currentIndex=0;
                for(int i=0; i<todaySessions.size(); i++)
                {
                    Sessions session=todaySessions.get(i);
                    if(rightNow <session.getBeginTime() && i==0)
                    {
                        mEventStatus.setText(String.format(getResources().getString(R.string.event_next_session),
                                session.getTitle(),
                                ModelManager.getDate(session.getBeginTime(), "h:mm a"),
                                ModelManager.get(getActivity()).getSpeakerById(session.getFkSpeaker()).getName()));
                        return;
                    }
                    else
                    if(rightNow >= session.getBeginTime() && i==todaySessions.size()-1)
                    {
                        mEventStatus.setText(String.format(getResources().getString(R.string.event_currently),
                                session.getTitle(),
                                ModelManager.get(getActivity()).getSpeakerById(session.getFkSpeaker()).getName()));
                        return;
                    }
                    else
                    if(rightNow >=session.getBeginTime())
                    {
                        currentIndex=i;
                    }
                    else
                    if(rightNow<session.getBeginTime())
                    {
                        mEventStatus.setText(String.format(getResources().getString(R.string.event_currently_and_next),
                                todaySessions.get(currentIndex).getTitle(),
                                ModelManager.get(getActivity()).getSpeakerById(todaySessions.get(currentIndex).getFkSpeaker()).getName(),
                                ModelManager.getDate(session.getBeginTime(), "h:mm a"),
                                session.getTitle(),
                                ModelManager.get(getActivity()).getSpeakerById(session.getFkSpeaker()).getName()));
                        return;
                    }
                }

                mEventStatus.setText(String.format(getResources().getString(R.string.event_currently),
                        todaySessions.get(currentIndex).getTitle(),
                        ModelManager.get(getActivity()).getSpeakerById(todaySessions.get(currentIndex).getFkSpeaker())));
            }
            else
                mEventStatus.setText(R.string.event_ongoing);
        }
        else
        if(rightNow>event.getEndDate())
            mEventStatus.setText(R.string.event_finished);
    }



    private Animation setUpAnimations()
    {
        final ScaleAnimation[] animations= new ScaleAnimation[8];

        animations[0]=new ScaleAnimation(1f,1.8f,1f,1.8f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animations[1]=new ScaleAnimation(1f,0.75f,1f,0.75f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animations[2]=new ScaleAnimation(1f,1.20f,1f,1.20f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animations[3]=new ScaleAnimation(1f,0.85f,1f,0.85f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animations[4]=new ScaleAnimation(1f,1.1f,1f,1.1f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animations[5]=new ScaleAnimation(1f,0.95f,1f,0.95f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animations[6]=new ScaleAnimation(1f,1.03f,1f,1.07f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animations[7]=new ScaleAnimation(1f,0.99f,1f,0.99f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);

        for(int i=0;i<animations.length;i++)
        {
            if(i==0)
            {
                animations[i].setDuration(80);
                animations[i].setInterpolator(new DecelerateInterpolator());
            }
            else
            {
                animations[i].setDuration(80);
                animations[i].setInterpolator(new LinearInterpolator());
            }
            animations[i].setRepeatMode(Animation.REVERSE);
            animations[i].setRepeatCount(1);
            animations[i].setFillAfter(true);

            if(i!=animations.length-1)
            {
                final ScaleAnimation animation1=animations[i+1];

                animations[i].setAnimationListener(new Animation.AnimationListener()
                {
                    @Override
                    public void onAnimationStart(Animation animation)
                    {
                        mAnimationRunning=true;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation)
                    {
                        mEventStatus.startAnimation(animation1);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation)
                    {

                    }
                });
            }
            else
            {
                animations[i].setAnimationListener(new Animation.AnimationListener()
                {
                    @Override
                    public void onAnimationStart(Animation animation)
                    {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation)
                    {
                        mAnimationRunning=false;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation)
                    {
                    }
                });
            }

        }

        return animations[0];
    }
}
