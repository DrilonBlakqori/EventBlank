package com.zagapps.eventblank.models;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import org.markdown4j.Markdown4jProcessor;


import com.zagapps.eventblank.database.EventDatabase;
import com.zagapps.eventblank.receivers.NotificationReceiver;
import com.zagapps.eventblank.services.AlarmService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class ModelManager
{
    Context mContext;
    private Event mEvent;
    private ArrayList<Speakers> mSpeakers;
    private ArrayList<Sessions> mSessions;
    private ArrayList<Tracks> mTracks;
    private ArrayList<Locations> mLocations;
    private ArrayList<Texts> mTexts;

    private Meta mMeta;

    private EventDatabase mDatabase;

    private boolean mFavouriteFilter;

    public static final int DAY_TO_MILLIS=24*60*60*1000;

    private static ModelManager sModelManager;

    private ModelManager(Context context,EventDatabase database)
    {
        mContext=context;
        mDatabase=database;
        mEvent=database.getEvent();
        mSpeakers=mDatabase.getSpeakers();
        mSessions=mDatabase.getSessions();
        mTracks=mDatabase.getTracks();
        mLocations=mDatabase.getLocations();
        mFavouriteFilter =false;
        mMeta=mDatabase.getMeta();
        mTexts=mDatabase.getTexts();
        for(Texts texts: mTexts)
        {
            texts.setContent(stringToHtml(texts.getContent()));
        }

        Intent pushIntent=new Intent(context, AlarmService.class);
        context.startService(pushIntent);
    }


    public static void resetSingletons() {
        if (sModelManager != null) {
            EventDatabase.resetSingletons();
            sModelManager = null;
        }
    }


    public static ModelManager get(Context context)
    {
        if(sModelManager ==null)
        {
            sModelManager =new ModelManager(context.getApplicationContext(),
                    EventDatabase.get(context));
        }

        return sModelManager;
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public Event getEvent()
    {
        return mEvent;
    }

    public ArrayList<Speakers> getSpeakers()
    {
        return mSpeakers;
    }

    public ArrayList<Texts> getTexts()
    {
        return mTexts;
    }

    public Speakers getSpeakerById(int id)
    {

        for(Speakers speakers:mSpeakers)
        {
            if(speakers.getIDSpeaker()==id)
                return speakers;
        }

        return mSpeakers.get(0);
    }


    public Tracks getTrackById(int id)
    {

        for(Tracks tracks:mTracks)
        {
            if(tracks.getIDTrack()==id)
                return tracks;
        }

        return mTracks.get(0);
    }

    public Locations getLocationById(int id)
    {

        for(Locations location:mLocations)
        {
            if(location.getIDLocation()==id)
                return location;
        }

        return mLocations.get(0);
    }



    public ArrayList<Sessions> getSessionsByDate(long beginTime)
    {
        ArrayList<Sessions> dateSessions=new ArrayList<>();

        for(Sessions sessions:mSessions)
        {
            if(getDate(sessions.getBeginTime(),"dd").equals(getDate(beginTime,"dd")))
                dateSessions.add(sessions);
        }


        return dateSessions;
    }


    public Sessions getSessionById(int id)
    {
        for(Sessions session:mSessions)
            if(session.getID()==id)
                return session;

        return mSessions.get(0);
    }

    public int getEventLength()
    {
        long beginTime=mEvent.getBeginDate();
        long endTime=mEvent.getEndDate();

        for(Sessions sessions:mSessions)
        {
            if(sessions.getBeginTime()<beginTime)
                beginTime=sessions.getBeginTime();

            if(sessions.getBeginTime()>endTime)
                endTime=sessions.getBeginTime();
        }

        int length=0;

        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(beginTime);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        beginTime=calendar.getTimeInMillis();
        while(beginTime<=endTime)
        {
            length++;
            beginTime+=DAY_TO_MILLIS;
        }

        return  length;
    }



    public Meta getMeta()
    {
        return mMeta;
    }

    public boolean isFavouriteFilter()
    {
        return mFavouriteFilter;
    }

    public void setFavouriteFilter(boolean favourite)
    {
        mFavouriteFilter = favourite;
    }

    public void favouriteSessionChanged(int id, boolean favourite)
    {
        WriteSessionFavourites task=new WriteSessionFavourites();
        task.execute(id,favourite?1:0);
    }

    public void favouriteSpeakerChanged(int id, boolean favourite)
    {
        WriteSpeakerFavourites task=new WriteSpeakerFavourites();
        task.execute(id,favourite?1:0);
    }


    public String stringToHtml(String text)
    {
        String html="";

        try
        {
            html= new Markdown4jProcessor().process(text);
            html=html.replace("<a ", "<font color=\"" + mEvent.getMainColor() + "\"><a ");
            html=html.replace("</a>","</a></font>");
            for(int i=1;i<=6;i++)
            {
                String tag="h"+i;
                html = html.replace("<"+tag+">", "<"+tag+"><font color=\"" + mEvent.getMainColor() + "\"><b>");
                html = html.replace("</"+tag+">", "</b></font></"+tag+">");
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return html;
    }

    public ArrayList<Sessions> getSessions()
    {
        return mSessions;
    }




    private class WriteSessionFavourites extends AsyncTask <Integer,Void,Void>
    {

        @Override
        protected Void doInBackground(Integer... params)
        {
            int id=params[0];

            mDatabase.setSessionFavourite(id, params[1]);
            getSessionById(id).setFavorite(params[1]==1);
            return null;
        }
    }

    private class WriteSpeakerFavourites extends AsyncTask <Integer,Void,Void>
    {

        @Override
        protected Void doInBackground(Integer... params)
        {
            int id=params[0];

            mDatabase.setSpeakerFavourite(id, params[1]);
            getSpeakerById(id).setFavourite(params[1]==1);
            return null;
        }
    }



}
