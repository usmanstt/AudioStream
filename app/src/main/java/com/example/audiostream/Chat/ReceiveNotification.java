package com.example.audiostream.Chat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.audiostream.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ReceiveNotification extends FirebaseMessagingService {

    String title, address, order_id;
    private static final String ADMIN_CHANNEL_ID = "adminchannel";
    NotificationManager notificationManager;
    NotificationChannel notificationChannel;
    NotificationCompat.Builder notificationCompat;
    PendingIntent pendingIntent;
    MediaPlayer player;
    Intent intent;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull @NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        handlemessage(remoteMessage.getData());
    }

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void handlemessage(Map<String, String> data) {
        if (data != null) {
            String title =data.get("title");
            String message = data.get("text");
            intent = new Intent(this, AllUserActivity.class);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                notificationChannel = new NotificationChannel(ADMIN_CHANNEL_ID, title, NotificationManager.IMPORTANCE_MAX);
                notificationChannel.setDescription(message);
                notificationChannel.setName(title);
                notificationChannel.setVibrationPattern(notificationChannel.getVibrationPattern());
                notificationManager.createNotificationChannel(notificationChannel);
            }

            notificationCompat = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID).setLargeIcon
                    (BitmapFactory.decodeResource(getResources(), R
                            .mipmap.ic_launcher)).setContentText(message).setContentTitle(title).setPriority(NotificationManager.IMPORTANCE_DEFAULT)
                    .setAutoCancel(true).setContentIntent(pendingIntent).setSound(soundUri).setSmallIcon(R.mipmap.ic_launcher).setWhen(System.currentTimeMillis());
            notificationManager.notify(234, notificationCompat.build());
        }
    }

}
