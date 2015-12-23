package com.zagapps.eventblank.models;


public class Meta
{
    private double mVersion;

    public Meta(double version)
    {
        mVersion=version;
    }

    public double getVersion()
    {
        return mVersion;
    }

    public void setVersion(double version)
    {
        mVersion = version;
    }
}
