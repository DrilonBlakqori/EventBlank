package com.zagapps.eventblank.models;


public class Tracks
{
    private int mIDTrack;
    private String mTrack;
    private String mTrackDescription;

    public Tracks(int id, String track, String trackDescription)
    {
        mIDTrack=id;
        mTrack=track;
        mTrackDescription=trackDescription;
    }

    public String getTrack()
    {
        return mTrack;
    }

    public void setTrack(String track)
    {
        mTrack = track;
    }

    public int getIDTrack()
    {
        return mIDTrack;
    }
}
