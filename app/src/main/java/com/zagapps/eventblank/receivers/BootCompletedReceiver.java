package com.zagapps.eventblank.receivers;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zagapps.eventblank.services.AlarmService;

public class BootCompletedReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent pushIntent=new Intent(context, AlarmService.class);
        context.startService(pushIntent);
    }
}
