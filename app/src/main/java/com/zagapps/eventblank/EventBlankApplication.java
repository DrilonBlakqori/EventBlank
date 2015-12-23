package com.zagapps.eventblank;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;


public class EventBlankApplication extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "iAGeexRK5WgwpMMCVB0YHlty8";
    private static final String TWITTER_SECRET = "c7opobbIc04peehedpJYqk37W3aT1tbfHAOJkD9ENbYnBAKOe2";

    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        Fresco.initialize(this);
    }
}
