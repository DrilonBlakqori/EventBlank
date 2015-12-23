package com.zagapps.eventblank.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.twitter.sdk.android.tweetcomposer.TweetUploadService;

public class TwitterResultReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        System.out.println(intent.getAction());

        if (TweetUploadService.UPLOAD_SUCCESS.equals(intent.getAction()))
        {
//            final Long tweetId = intent.getExtras().getLong(TweetUploadService.EXTRA_TWEET_ID);
            Toast.makeText(context,"Tweet succeed",Toast.LENGTH_SHORT).show();
        }
        else
        {
//            final Intent retryIntent = intent.getExtras().getParcelable(TweetUploadService.EXTRA_RETRY_INTENT);
            Toast.makeText(context,"Tweet failed",Toast.LENGTH_SHORT).show();
        }
    }
}