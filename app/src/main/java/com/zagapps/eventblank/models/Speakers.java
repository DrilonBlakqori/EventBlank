package com.zagapps.eventblank.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class Speakers
{

    private int mIDSpeaker;
    private String mSpeaker;
    private String mSpeakerTwitter;
    private String mSpeakerUrl;
    private Bitmap mPhoto;
    private String mBio;
    private boolean mFavourite;

    public Speakers(int id,String speaker, String speakerTwitter, String speakerUrl,byte[] photo,String bio,
                    boolean favourite)
    {
        mIDSpeaker=id;
        mSpeaker=speaker;
        mSpeakerTwitter=speakerTwitter;
        mSpeakerUrl=speakerUrl;
        mPhoto= decodeBitmap(photo,null);
        mBio=bio;
        mFavourite=favourite;
    }

    private static Bitmap decodeBitmap(byte[] bytes,BitmapFactory.Options options)
    {
        try
        {
            if (options == null)
            {
                options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                options.inSampleSize = 1;
            }

            return BitmapFactory.decodeByteArray(bytes,0,bytes.length,options);

        }
        catch (OutOfMemoryError e)
        {
            assert options != null;
            options.inSampleSize++;
            return  decodeBitmap(bytes, options);
        }
    }


    public int getIDSpeaker()
    {
        return mIDSpeaker;
    }

    public String getName()
    {
        return mSpeaker;
    }

    public String getSpeakerTwitter()
    {
        return mSpeakerTwitter;
    }

    public String getSpeakerUrl()
    {
        return mSpeakerUrl;
    }

    public Bitmap getPhoto()
    {
        return mPhoto;
    }

    public String getBio()
    {
        return mBio;
    }

    public boolean isFavourite()
    {
        return mFavourite;
    }

    public void setFavourite(boolean favourite)
    {
        mFavourite = favourite;
    }
}
