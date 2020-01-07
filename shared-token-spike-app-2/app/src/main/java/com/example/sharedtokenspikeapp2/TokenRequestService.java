package com.example.sharedtokenspikeapp2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import java.util.Random;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class TokenRequestService extends Service {
    private static final String SOURCE_PACKAGE_NAME = "SOURCE_PACKAGE_NAME";
    private static final String BEARER_TOKEN = "BEARER_TOKEN";

    @Override
    public void onCreate() {
        super.onCreate();

        initiateForeground(getPackageName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String packageName = intent.getStringExtra(SOURCE_PACKAGE_NAME);
        initiateForeground(packageName);

        Intent resultIntent = new Intent(packageName + ".TokenResponseReceiver");
        // Generate a random "token"
        resultIntent.putExtra(BEARER_TOKEN, String.valueOf(new Random().nextInt() & Integer.MAX_VALUE));

        sendBroadcast(resultIntent, null);

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * If the app targets API level 26 or higher we are required to call the startForeground() which
     * shows a constant notification message to indicate that the application is running. The
     * notification will create its own title and text, no need to pass it in.
     * https://developer.android.com/guide/components/services#StartingAService
     */
    private void initiateForeground(String channelName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelName,
                    "Long running service",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);


            Notification notification = new NotificationCompat.Builder(this, channelName)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
