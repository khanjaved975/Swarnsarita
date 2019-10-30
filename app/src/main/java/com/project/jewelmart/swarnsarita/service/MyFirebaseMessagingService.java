package com.project.jewelmart.swarnsarita.service;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.project.jewelmart.swarnsarita.SplashActivity;
import com.project.jewelmart.swarnsarita.utils.Constant;
import com.project.jewelmart.swarnsarita.utils.NotificationUtils;
import com.project.jewelmart.swarnsarita.utils.UserSessionManager;

import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    String userType;
    private NotificationUtils notificationUtils;
    UserSessionManager userSessionManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        userSessionManager = new UserSessionManager(getApplicationContext());
        if (remoteMessage == null)
            return;

        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                Map<String, String> data = remoteMessage.getData();
                String title = data.get("title");
                // String pushid = data.get("push_id");
                String subtitle = data.get("subtitle");
                String order_id = data.get("order_id");
              /*  if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                    // app is in foreground, broadcast the push message
                    Intent pushNotification = badgenew Intent(Constants.PUSH_NOTIFICATION);
                    pushNotification.putExtra("title", title);
                    pushNotification.putExtra("order_id", order_id);
                    pushNotification.putExtra("subtitle", subtitle);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                    // play notification sound
                    NotificationUtils notificationUtils = badgenew NotificationUtils(getApplicationContext());
                    notificationUtils.playNotificationSound();
                } else {*/
                // app is in background, show the notification in notification tray
// app is in background, show the notification in notification tray
                Intent resultIntent;
                if (userSessionManager.getUserType().equalsIgnoreCase("client")) {
                    resultIntent = new Intent(getApplicationContext(), SplashActivity.class);
                } else {
                    resultIntent = new Intent(getApplicationContext(), SplashActivity.class);
                }
                //resultIntent.putExtra("pushid",pushid);
                resultIntent.putExtra("title", title);
                resultIntent.putExtra("order_id", order_id);
                resultIntent.putExtra("subtitle", subtitle);
                resultIntent.putExtra("isFromPush", true);

                userSessionManager.setNotifiacation("yes");
                userSessionManager.setISNotifiacation(true);
                showNotificationMessage(getApplicationContext(), title, subtitle, order_id, resultIntent);

                //NotificationUtils notificationUtils = badgenew NotificationUtils(getApplicationContext());
               // notificationUtils.playNotificationSound();
                //     }
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
        // }

    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Constant.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification


        }
    }

    /**
     * Showing notification with text only
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
       // notificationUtils.playNotificationSound();
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}