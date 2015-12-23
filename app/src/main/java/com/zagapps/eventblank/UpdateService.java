package com.zagapps.eventblank;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.zagapps.eventblank.activities.MainActivity;
import com.zagapps.eventblank.models.ModelManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class UpdateService extends Service {


    private boolean processing;
    public static final String DOWNLOAD_URI_ARG = "DOWNLOAD_URI_ARG";
    private static final long NOTIFICATION_ID = 10000;

    NotificationManagerCompat mNotificationManager;

    private ServiceHandler mServiceHandler;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            String url = msg.getData().getString(DOWNLOAD_URI_ARG);
            downloadFile(url);
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            processing = false;
            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        Looper mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("UpdateService", "service starting");
        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        if (!processing) {
            processing = true;
            Message msg = mServiceHandler.obtainMessage();
            msg.arg1 = startId;
            msg.setData(intent.getExtras());
            mServiceHandler.sendMessage(msg);
        }

        // If we get killed, after returning from here, restart
        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d("UpdateService", "service done");

    }

    protected void downloadFile(String downloadUrl) {
        if (!TextUtils.isEmpty(downloadUrl)) {

            // Toast.makeText(this, "App is updating!", Toast.LENGTH_SHORT).show();

            mNotificationManager = NotificationManagerCompat.from(this);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setContentTitle("Updating app!")
                    .setContentText("Download in progress")
                    .setSmallIcon(android.R.drawable.stat_sys_download)
                    .setProgress(100, 0, true);

            Notification notification=builder.build();
            notification.flags = Notification.FLAG_ONGOING_EVENT;

            mNotificationManager.notify((int)NOTIFICATION_ID, notification);
            makeApiRequest(downloadUrl);
        }
    }

    private void makeApiRequest(String downloadUrl) {
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        OkHttpClient client = new OkHttpClient();

        try {
            Response response = client.newCall(request).execute();
            Log.d("UpdateService", "responseReceiver");
            processResponse(response);

        } catch (IOException e) {
            Log.d("UpdateService", "IOException");
            e.printStackTrace();
            updateNotification(false);
        }


    }

    private void processResponse(Response response) {
        AlarmManager alm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(UpdateService.this, MainActivity.class);
        intent.putExtra(MainActivity.DOWNLOAD_FINISHED, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(MainActivity.LAST_DATABASE_VERSION, ModelManager.get(UpdateService.this).getMeta().getVersion());

        File folder = new File(getFilesDir().getPath() + "/");

        String fileName = getResources().getString(R.string.database_name);
        File file = new File(folder, fileName);
        ResponseBody responseBody = response.body();
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(responseBody.bytes());
            out.close();
            responseBody.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ModelManager.resetSingletons();

        updateNotification(true);
       /* alm.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
                PendingIntent.getActivity(UpdateService.this, 0, intent, 0));*/
        startActivity(intent);
    }

    private void updateNotification(boolean successful) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(UpdateService.this);
        builder.setSmallIcon(R.mipmap.ic_download)
                .setProgress(0, 0, false);
            if (successful) {
            builder.setContentTitle("Download finished!")
                    .setContentText("App restarted");
        } else {
            builder.setContentTitle("Download failed!");
        }

        mNotificationManager.notify((int)NOTIFICATION_ID, builder.build());
        Log.d("UpdateService", "notificationStatus" + successful);

    }

}
