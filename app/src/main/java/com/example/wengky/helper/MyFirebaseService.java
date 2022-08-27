package com.example.wengky.helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.wengky.Dashboard;
import com.example.wengky.R;
import com.example.wengky.ui.fragment.BeritaAcara;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import static com.example.wengky.ui.fragment.BeritaAcara.NOTIFICATION_CHANNEL_ID;

public class MyFirebaseService extends FirebaseMessagingService {
//    Uri defaultSoundUri;
//    private static final String TAG = "PushNotification";
//    private static final String CHANNEL_ID = "101";
//    private NotificationManager mNotificationManager;
//    private NotificationCompat.Builder mBuilder;
//    String GROUP_NOTIFY = "com.example.wengky.helper.MyFirebaseService";
//
//    @Override
//    public void onNewToken(@NonNull String token) {
//        super.onNewToken(token);
//    }
//
//    @Override
//    public void onMessageReceived(@NonNull RemoteMessage message) {
//        super.onMessageReceived(message);
//
//        Log.d("Informasi", "From: " + message.getFrom());
//        Log.d("Informasi", "title: " + message.getNotification().getTitle());
//        Log.d("Informasi", "message: " + message.getNotification().getBody());
//
//        if (message.getData().size() > 0) {
//            Log.d(TAG, "Message data payload: " + message.getData());
//            Toast.makeText(getApplicationContext(), "ada notif", Toast.LENGTH_SHORT).show();
////            if (/* Check if data needs to be processed by long running job */ true) {
////                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
////
////            } else {
////                // Handle message within 10 seconds
////
////            }
//        }
//        // Check if message contains a notification payload.
//        if (message.getNotification() != null) {
//            ada(message.getNotification().getTitle(), message.getNotification().getBody());
//            Log.d(TAG, "Message Notification Body: " + message.getNotification().getBody());
//        }
//
//
//    }
//
//    private void ada(String title,String message) {
//        defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            Intent intent = new Intent(this, BeritaAcara.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            PendingIntent pendingIntent =
//                    PendingIntent.getActivity(this, 0, intent, 0);
//            PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
//                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            Notification newMessageNotification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_absensi)
//                .setContentTitle(title)
//                .setContentText(message)
//                    .setAutoCancel(true)
//                .setContentIntent(pendingIntent)
//                .setFullScreenIntent(fullScreenPendingIntent,true)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setSound(defaultSoundUri)
////                    .setLargeIcon(emailObject.getSenderAvatar())
//                .setGroup(GROUP_NOTIFY)
//                .build();
//
//            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//            notificationManager.notify(1, newMessageNotification);

//            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                    .setSmallIcon(R.drawable.ic_absensi)
//                    .setContentTitle(title)
//                    .setContentText(message)
//                    .setPriority(NotificationCompat.PRIORITY_HIGH)
//                    // Set the intent that will fire when the user taps the notification
//                    .setContentIntent(pendingIntent)
//                    .setGroup(GROUP_NOTIFY)
//                    .setAutoCancel(true);
//
//
//            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//
//            // notificationId is a unique int for each notification that you must define
//            notificationManager.notify(1, builder.build());
//        }
//        else{
//            Intent intent = new Intent(this, Dashboard.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                    .setSmallIcon(R.drawable.ic_launcher_background)
//                    .setContentTitle(title)
//                    .setContentText(message)
//                    .setPriority(NotificationCompat.PRIORITY_HIGH)
//                    // Set the intent that will fire when the user taps the notification
//                    .setContentIntent(pendingIntent)
//                    .setAutoCancel(true);
//
//            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//
//            // notificationId is a unique int for each notification that you must define
//            notificationManager.notify(1, builder.build());
//        }
//    }
}