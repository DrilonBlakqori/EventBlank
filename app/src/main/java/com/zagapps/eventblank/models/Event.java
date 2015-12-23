package com.zagapps.eventblank.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;


public class Event
{
    private int mID;
    private String mTitle;
    private String mSubtitle;
    private long mBeginDate;
    private long mEndDate;
    private String mOrganizer;
    private Bitmap mLogo;
    private String mTwitterTag;
    private String mTwitterAdmin;
    private String mMainColor;
    private String mSecondaryColor;
    private boolean mTwitterChatter;
    private String mTernaryColor;
    private String mUpdateFileUrl;

    public Event(int id, String title, String subtitle, long beginDate, long endDate,String organizer,
                 byte[] logo, String twitterTag, String twitterAdmin, String mainColor, String secondaryColor,
                 boolean twitterChatter, String ternaryColor, String updateFileUrl)
    {
        mID=id;
        mTitle=title;
        mSubtitle=subtitle;

        String time=""+beginDate;
        while(time.length()<13)
        {
            beginDate*=10;
            time=""+beginDate;
        }
        mBeginDate=beginDate;

        mEndDate=endDate;

        time=""+endDate;
        while(time.length()<13)
        {
            endDate*=10;
            time=""+endDate;
        }
        mEndDate=endDate;

        mOrganizer=organizer;
        mLogo= BitmapFactory.decodeByteArray(logo,0,logo.length);
        mTwitterTag=twitterTag;
        mTwitterAdmin=twitterAdmin;
        mMainColor=mainColor;
        mSecondaryColor=secondaryColor;
        mTwitterChatter=twitterChatter;
        mTernaryColor=ternaryColor;
        mUpdateFileUrl=updateFileUrl;
    }



    public String getTitle()
    {
        return mTitle;
    }

    public void setTitle(String title)
    {
        mTitle = title;
    }

    public String getSubtitle()
    {
        return mSubtitle;
    }

    public long getBeginDate()
    {
        return mBeginDate;
    }

    public long getEndDate()
    {
        return mEndDate;
    }

    public String getOrganizer()
    {
        return mOrganizer;
    }

    public Bitmap getLogo()
    {
        return mLogo;
    }

    public String getTwitterTag()
    {
        return mTwitterTag;
    }

    public String getTwitterAdmin()
    {
        return mTwitterAdmin;
    }

    public int getMainColor()
    {
        return Color.parseColor("#"+mMainColor);
    }

    public int getSecondaryColor()
    {
        return Color.parseColor("#"+mSecondaryColor);
    }

    public int getTernaryColor()
    {
        return Color.parseColor("#"+mTernaryColor);
    }

    public int getStatusBarColor()
    {
        float[] hsv = new float[3];
        int color = getMainColor();
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f;
        return Color.HSVToColor(hsv);
    }

    public String getUpdateFileUrl()
    {
        return mUpdateFileUrl;
    }
}
