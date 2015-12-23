package com.zagapps.eventblank.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.GenericDraweeView;
import com.zagapps.eventblank.fragments.SpeakerDetailFragment;
import com.zagapps.eventblank.models.ModelManager;
import com.zagapps.eventblank.models.Speakers;
import com.zagapps.eventblank.R;

import java.util.ArrayList;


public class SpeakersCardViewAdapter extends RecyclerView.Adapter<SpeakersCardViewAdapter.ViewHolder>
{
    private ArrayList<Speakers> mSpeakers;
    private ArrayList<Speakers> mFilteredSpeakers;
    private ArrayList<Speakers> mUsingSpeakers;
    ArrayList<Speakers> mSearchedSpeakers;
    private FragmentManager mFragmentManager;
    private boolean isFavourite;
    private Activity mActivity;
    public static final String SPEAKER_ID="speakers_id";

    public SpeakersCardViewAdapter(Activity activity, FragmentManager fragmentManager)
    {
        mFragmentManager=fragmentManager;
        mActivity=activity;
        mSpeakers= ModelManager.get(mActivity).getSpeakers();
        mFilteredSpeakers=new ArrayList<>();
        mSearchedSpeakers=new ArrayList<>();
        switchList(isFavourite);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        CardView view=(CardView) LayoutInflater.from(mActivity).inflate(R.layout.speaker_cards,parent,false);

        return new ViewHolder(view,mFragmentManager);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Speakers speaker=mUsingSpeakers.get(position);

        BitmapDrawable profile=new BitmapDrawable(mActivity.getResources(), speaker.getPhoto());
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(mActivity.getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setPlaceholderImage(profile)
                .setRoundingParams(RoundingParams.asCircle())
                .build();
        holder.mProfilePicture.setHierarchy(hierarchy);

        holder.mSpeakerName.setText(speaker.getName());

        holder.mTwitter.setText(speaker.getSpeakerTwitter());

        Drawable favouriteIconFull= ContextCompat.getDrawable(mActivity, R.mipmap.favourite_full);
        favouriteIconFull.mutate().setColorFilter(Color.parseColor("#e5520b"), PorterDuff.Mode.SRC_ATOP);
        holder.mFavourite.setImageDrawable(favouriteIconFull);
        if(speaker.isFavourite())
            holder.mFavourite.setVisibility(View.VISIBLE);
        else
            holder.mFavourite.setVisibility(View.GONE);
        
        holder.mBundle.putInt(SPEAKER_ID,speaker.getIDSpeaker());
    }

    @Override
    public int getItemCount()
    {
        return mUsingSpeakers.size();
    }


    public void switchList(boolean isFavourite)
    {
        this.isFavourite=isFavourite;

        mFilteredSpeakers.clear();

        if(isFavourite)
        {
            for(Speakers speaker:mSpeakers)
            {
                if(speaker.isFavourite())
                    mFilteredSpeakers.add(speaker);
            }
            mUsingSpeakers=mFilteredSpeakers;
        }
        else
            mUsingSpeakers=mSpeakers;

        notifyDataSetChanged();
    }

    public void searchSpeakers(String input)
    {

        if(input.equals(""))
        {
            if(isFavourite)
                mUsingSpeakers=mFilteredSpeakers;
            else
                mUsingSpeakers=mSpeakers;

            notifyDataSetChanged();
            return;
        }
        mSearchedSpeakers.clear();

        if(isFavourite)
        {
            for (Speakers speaker : mFilteredSpeakers)
            {
                if(speaker.getName().toLowerCase().contains(input.toLowerCase()))
                {
                    mSearchedSpeakers.add(speaker);
                }
            }
        }
        else
        {
            for (Speakers speaker : mSpeakers)
            {
                if(speaker.getName().toLowerCase().contains(input.toLowerCase()))
                {
                    mSearchedSpeakers.add(speaker);
                }
            }
        }

        mUsingSpeakers= mSearchedSpeakers;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        protected Bundle mBundle;
        protected TextView mSpeakerName;
        protected TextView mTwitter;
        protected GenericDraweeView mProfilePicture;
        protected ImageView mFavourite;

        public ViewHolder(View view, final FragmentManager fragmentManager)
        {
            super(view);

            mSpeakerName = (TextView)view.findViewById(R.id.speaker_card_name);
            mTwitter = (TextView)view.findViewById(R.id.speaker_card_twitter);
            mProfilePicture=(GenericDraweeView)view.findViewById(R.id.speaker_card_profile_picture);
            mFavourite= (ImageView)view.findViewById(R.id.speaker_card_favourite);

            mBundle=new Bundle();

            view.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    SpeakerDetailFragment fragment=new SpeakerDetailFragment();
                    fragment.setArguments(mBundle);

                    fragmentManager.beginTransaction().addToBackStack(fragment.getClass().getSimpleName()).
                        setCustomAnimations(R.anim.slide_in_left,R.anim.no_anim, R.anim.no_anim, R.anim.slide_out_right).
                        add(R.id.speakersContainer, fragment, fragment.getClass().getSimpleName()).
                            commit();

                }
            });
        }
    }
}
