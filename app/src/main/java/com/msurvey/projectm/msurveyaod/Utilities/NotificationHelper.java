package com.msurvey.projectm.msurveyaod.Utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import com.msurvey.projectm.msurveyaod.FeedbackActivity;
import com.msurvey.projectm.msurveyaod.R;

public class NotificationHelper extends ContextWrapper {

    private static final String CHANNEL_ID = "com.msurvey.projectm.msurveyaod.Utilities";
    private static final String CHANEL_NAME = "mSurveyChannel";
    private NotificationManager manager;


    public NotificationHelper(Context base) {
        super(base);
        createChannels();
    }

    private void createChannels(){


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "chanelname";
            String description = "description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLightColor(Color.GREEN);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            getManager();
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public NotificationManager getManager() {
        if(manager == null){
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return manager;
    }

    public Notification.Builder getChanelNotification(String title, String body, String extra){

        Intent intent = new Intent(getApplicationContext(), FeedbackActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                    .setContentText(body)
                    .setContentTitle(title)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
        }
        return new Notification.Builder(getApplicationContext())
                .setContentText(body)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

    }


}
