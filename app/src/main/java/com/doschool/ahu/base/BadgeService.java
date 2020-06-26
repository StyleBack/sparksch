package com.doschool.ahu.base;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.tot.badges.IconBadgeNumManager;

import java.util.Objects;

/**
 * Created by X on 2018/12/4.
 */
public class BadgeService extends Service {

    IconBadgeNumManager setIconBadgeNumManager;
    private int count=0;

    public BadgeService() {
        setIconBadgeNumManager = new IconBadgeNumManager();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Objects.requireNonNull(intent.getExtras()).containsKey("badgeCount")){
            count=intent.getIntExtra("badgeCount",0);
        }
        sendIconNumNotification();
        return super.onStartCommand(intent, flags, startId);
    }

    private void sendIconNumNotification() {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm == null) return;
        String notificationChannelId = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = createNotificationChannel();
            nm.createNotificationChannel(notificationChannel);
            notificationChannelId = notificationChannel.getId();
        }
        Notification notification = null;
        try {
            notification = new NotificationCompat.Builder(this, notificationChannelId)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setNumber(count)
                    .build();
            notification = setIconBadgeNumManager.setIconBadgeNum(getApplication(), notification, count);

            nm.notify(2026, notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private NotificationChannel createNotificationChannel() {
        String channelId = "badge";
        NotificationChannel channel;
        channel = new NotificationChannel(channelId,
                "badge_app", NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true); //是否在桌面icon右上角展示小红点
        channel.setLightColor(Color.RED); //小红点颜色
        channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
        return channel;
    }
}
