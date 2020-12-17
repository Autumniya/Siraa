package apk.siraal.siraa;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


public class NotificationService extends Service {
    Context context;

    public static Notification notification;
    private  final String LOG_TAG = "NotificationService";
    int importance = NotificationManager.IMPORTANCE_DEFAULT;

    public static String MAIN_ACTION = "com.marothiatechs.customnotification.action.main";
    public static String INIT_ACTION = "com.marothiatechs.customnotification.action.init";
    public static String PREV_ACTION = "com.marothiatechs.customnotification.action.prev";
    public static String PLAY_ACTION = "com.marothiatechs.customnotification.action.play";
    public static String NEXT_ACTION = "com.marothiatechs.customnotification.action.next";
    public static String STARTFOREGROUND_ACTION = "com.marothiatechs.customnotification.actionstartforeground";
    public static String STOPFOREGROUND_ACTION = "com.marothiatechs.customnotification.action.stopforeground";
    public static int FOREGROUND_SERVICE = 101;



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        createNotification();

        if (intent.getAction().equals(PREV_ACTION)) {
            Toast.makeText(this, "Clicked Previous", Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG, "Clicked Previous");
        } else if (intent.getAction().equals(PLAY_ACTION)) {
            Toast.makeText(this, "Clicked Play", Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG, "Clicked Play");
        } else if (intent.getAction().equals(NEXT_ACTION)) {
            Toast.makeText(this, "Clicked Next", Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG, "Clicked Next");
        } else if (intent.getAction().equals(
                STOPFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Stop Foreground Intent");
            Toast.makeText(this, "Service Stoped", Toast.LENGTH_SHORT).show();
            stopForeground(true);
            stopSelf();
        }

        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews views = new RemoteViews(getPackageName(),
                R.layout.status_bar);
        RemoteViews bigViews = new RemoteViews(getPackageName(),
                R.layout.status_bar_expanded);

        views.setViewVisibility(R.id.status_bar_icon, View.VISIBLE);
        views.setViewVisibility(R.id.status_bar_album_art, View.GONE);
        bigViews.setImageViewBitmap(R.id.status_bar_album_art,
                Constants.getDefaultAlbumArt(this));

        Intent notificationIntent = new Intent("com.marothiatechs.customnotification.action.main");
        notificationIntent.setAction(MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 101,
                notificationIntent, 0);

        Intent previousIntent = new Intent("com.marothiatechs.customnotification.action.prev");
        previousIntent.setAction(PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getBroadcast(this, 101,
                previousIntent, 0);

        Intent playIntent = new Intent("com.marothiatechs.customnotification.action.play");
        playIntent.setAction(PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getBroadcast(this, 101,
                playIntent, 0);

        Intent nextIntent = new Intent("com.marothiatechs.customnotification.action.next");
        nextIntent.setAction(NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getBroadcast(this, 101,
                nextIntent, 0);

        Intent closeIntent = new Intent("com.marothiatechs.customnotification.action.stopforeground");
        closeIntent.setAction(STOPFOREGROUND_ACTION);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 101,
                closeIntent, 0);

        views.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);

        views.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);

        views.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);

        views.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);

        views.setImageViewResource(R.id.status_bar_play,
                R.drawable.ic_baseline_pause_24);
        bigViews.setImageViewResource(R.id.status_bar_play,
                R.drawable.ic_baseline_pause_24);

        views.setTextViewText(R.id.status_bar_track_name, "Song Title");
        bigViews.setTextViewText(R.id.status_bar_track_name, "Song Title");

        views.setTextViewText(R.id.status_bar_artist_name, "Artist Name");
        bigViews.setTextViewText(R.id.status_bar_artist_name, "Artist Name");

        bigViews.setTextViewText(R.id.status_bar_album_name, "Album Name");



        notification=new NotificationCompat.Builder(context,STARTFOREGROUND_ACTION)
                .setCustomContentView(views)
                .setCustomBigContentView(bigViews)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_note)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        startForeground(1, notification);


        return START_NOT_STICKY;
    }

    private void createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    STARTFOREGROUND_ACTION,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }

    }



}
