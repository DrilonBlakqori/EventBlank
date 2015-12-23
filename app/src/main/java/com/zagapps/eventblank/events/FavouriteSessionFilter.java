package com.zagapps.eventblank.events;


public class FavouriteSessionFilter
{
    private boolean mIsFavourite;

    public FavouriteSessionFilter(boolean isFavourite)
    {
        mIsFavourite=isFavourite;
    }

    public boolean isFavourite()
    {
        return mIsFavourite;
    }
}
