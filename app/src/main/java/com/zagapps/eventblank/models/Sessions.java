package com.zagapps.eventblank.models;



public class Sessions
{
    private int mIDSession;
    private String mTitle;
    private String mDescription;
    private long mBeginTime;
    private int mFkTrack;
    private int mFkLocation;
    private boolean mIsFavorite;
    private int mFkSpeaker;

    public Sessions(int id,String title,String description, long beginTime, int fkTrack,
                        int fkLocation,boolean isFavorite,int fkSpeaker)
    {
        mIDSession=id;
        mTitle=title;
        mDescription=description;

        String time=""+beginTime;

        while(time.length()<13)
        {
            beginTime*=10;
            time=""+beginTime;
        }
        mBeginTime=beginTime;
        mFkTrack=fkTrack;
        mFkLocation=fkLocation;
        mIsFavorite=isFavorite;
        mFkSpeaker=fkSpeaker;
    }

    public int getID()
    {
        return mIDSession;
    }

    public String getTitle()
    {
        return mTitle;
    }

    public void setTitle(String title)
    {
        mTitle = title;
    }

    public String getDescription()
    {
        return mDescription;
    }

    public long getBeginTime()
    {
       return mBeginTime;
    }

    public int getFkTrack()
    {
        return mFkTrack;
    }

    public int getFkLocation()
    {
        return mFkLocation;
    }

    public boolean isFavourite()
    {
        return mIsFavorite;
    }

    public void setFavorite(boolean isFavorite)
    {
        mIsFavorite = isFavorite;
    }

    public int getFkSpeaker()
    {
        return mFkSpeaker;
    }
}
