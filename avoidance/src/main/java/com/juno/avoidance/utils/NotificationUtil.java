package com.juno.avoidance.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.app.NotificationCompat;

import java.lang.ref.WeakReference;

/**
 * Created by Juno.
 * Date : 2019/4/19.
 * Time : 16:06.
 * When I met you in the summer.
 * Description :
 */
public class NotificationUtil {

    public static class Manager {

        public static final String CHANNEL_ID = "SocketService";
        public static final String CHANNEL_NAME = "Avoidance";

        private WeakReference<Context> contextWeakReference;
        private NotificationManager manager;

        public Manager context(Context context) {
            contextWeakReference = new WeakReference<>(context);
            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            register();
            return this;
        }

        @TargetApi(Build.VERSION_CODES.O)
        private void register() {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            manager.createNotificationChannel(channel);
        }

        public void create(String title, String content, @DrawableRes int icon, Class<? extends Activity> enterActivityClass) {
            Context context = contextWeakReference.get();
            if (content != null) {
                Intent i = new Intent(context, enterActivityClass);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, i, PendingIntent.FLAG_UPDATE_CURRENT);
                Notification notification = new NotificationCompat.Builder(context)
                        .setChannelId(CHANNEL_ID)
                        .setSmallIcon(icon)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .build();
                manager.notify(100, notification);
            }

        }

        public void destroy() {
           manager.cancelAll();
        }

    }

}
