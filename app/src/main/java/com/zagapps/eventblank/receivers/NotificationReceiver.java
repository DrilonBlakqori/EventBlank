package com.zagapps.eventblank.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.zagapps.eventblank.activities.MainActivity;
import com.zagapps.eventblank.R;
import com.zagapps.eventblank.activities.SplashActivity;
import com.zagapps.eventblank.models.ModelManager;
import com.zagapps.eventblank.models.Sessions;


public class NotificationReceiver extends BroadcastReceiver
{
    private NotificationManager mManager;
    public static final String SESSION_ID="com.zagapps.eventblank.receiver.session_id";
    private static int id;


    @Override
    public void onReceive(Context context, Intent intent)
    {
        Sessions session= ModelManager.get(context).getSessionById(intent.getIntExtra(SESSION_ID, 0));

        mManager = (NotificationManager) context.getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(context.getApplicationContext(),SplashActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent=PendingIntent.getActivity(context, 1, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =new NotificationCompat.Builder(context);

        builder.setSmallIcon(R.mipmap.ic_launcher).
                setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setLargeIcon(ModelManager.get(context).getSpeakerById(session.getFkSpeaker()).getPhoto())
                .setContentTitle(session.getTitle())
                .setContentText("starts at: " + ModelManager.getDate(session.getBeginTime(), "hh:mm"));


        Notification notification= builder.build();

        id=(id+1)%2;
        mManager.notify(id, notification);
    }
}
