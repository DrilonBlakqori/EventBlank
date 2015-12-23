package com.zagapps.eventblank.utils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class NonSlideViewPager extends ViewPager
{
    public NonSlideViewPager(Context context)
    {
        super(context);
    }

    public NonSlideViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return false;
    }
}
