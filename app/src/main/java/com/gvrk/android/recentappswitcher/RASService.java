package com.gvrk.android.recentappswitcher;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by G V RAVI KUMAR on 7/20/2018.
 */

public class RASService extends Service {
    RASReceiver myReceiver = new RASReceiver();
    IntentFilter mFilter;;
    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(myReceiver);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "You Can start double tap on recent apps button to switch recent apps.", Toast.LENGTH_SHORT).show();
        Global.isServiceRunning = true;
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("RAS")
                .setContentText("Just double tap to switch recent app")
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setLargeIcon(
                        Bitmap.createScaledBitmap(icon, 128, 128, false))
                .build();
        startForeground(1111,notification);

        mFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(myReceiver,mFilter);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
