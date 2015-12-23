package com.zagapps.eventblank.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class Locations
{
    private int mIDLocation;
    private String mLocation;
    private String mLocationDescription;
    private Bitmap mLocationMap;
    private double mLat;
    private double mLng;

    public Locations(int IDLocation, String location, String locationDescription, byte[] locationMap, double lat, double lng)
    {
        mIDLocation = IDLocation;
        mLocation = location;
        mLocationDescription = locationDescription;
        if(locationMap!=null)
            mLocationMap = BitmapFactory.decodeByteArray(locationMap,0,locationMap.length);
        mLat = lat;
        mLng = lng;

    }

    public int getIDLocation()
    {
        return mIDLocation;
    }


    public String getLocationName()
    {
        return mLocation;
    }


    public double getLat()
    {
        return mLat;
    }

    public double getLng()
    {
        return mLng;
    }

}
