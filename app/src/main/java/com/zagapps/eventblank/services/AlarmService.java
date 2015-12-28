package com.zagapps.eventblank.services;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.zagapps.eventblank.models.ModelManager;
import com.zagapps.eventblank.models.Sessions;
import com.zagapps.eventblank.receivers.NotificationReceiver;

import java.util.ArrayList;


public class AlarmService extends Service
{
    private ArrayList<Sessions> mSessions;
    private static final int ID=123456789;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        mSessions=ModelManager.get(this).getSessions();
        SetupAlarms setupAlarms=new SetupAlarms();
        setupAlarms.execute();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }


    private class SetupAlarms extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            AlarmManager alarmManager = (AlarmManager)getSystemService(Activity.ALARM_SERVICE);



            for(int i=0;i<mSessions.size();i++)
            {
                Sessions session= mSessions.get(i);
                Intent myIntent = new Intent(getApplicationContext(), NotificationReceiver.class);
                myIntent.putExtra(NotificationReceiver.SESSION_ID,session.getID());
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),ID+i , myIntent,PendingIntent.FLAG_ONE_SHOT);

                if(session.getBeginTime() -10 *60* 1000 >= System.currentTimeMillis())
                    alarmManager.set(AlarmManager.RTC,session.getBeginTime()- 10 *60 *1000 , pendingIntent);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            Toast.makeText(getApplicationContext(),"Alarm registered",Toast.LENGTH_SHORT).show();
            super.onPostExecute(aVoid);
        }
    }
}
