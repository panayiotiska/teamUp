package com.kappa.teamup.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kappa.teamup.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService
{
    private final String TAG = "MessagingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        super.onMessageReceived(remoteMessage);

        try
        {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            if(Build.VERSION.SDK_INT >= 26)
            {
                NotificationChannel notificationChannel = new NotificationChannel(getString(R.string.default_notification_channel_id), "My Notifications", NotificationManager.IMPORTANCE_HIGH);

                notificationChannel.setDescription("Channel description");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Notification.DEFAULT_LIGHTS);
                notificationChannel.setVibrationPattern(new long[]{0, 100, 100, 100, 100, 100});
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }

            String notificationTitle = remoteMessage.getData().get("title");
            String notificationMessage = remoteMessage.getData().get("body");
            String notificationAction = remoteMessage.getData().get("click_action");

            NotificationCompat.Builder notification = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationMessage)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

            Intent intent = new Intent(notificationAction);

            int notificationId = (int) System.currentTimeMillis() % 65535;

            PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationId, intent, PendingIntent.FLAG_ONE_SHOT);
            notification.setContentIntent(pendingIntent);
            notificationManager.notify(notificationId, notification.build());
        }
        catch(Exception e)
        {
            Log.d(TAG, "error: " + e.getMessage());
        }
    }
}