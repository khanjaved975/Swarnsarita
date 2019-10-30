package com.project.jewelmart.swarnsarita.listner;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.project.jewelmart.swarnsarita.MainActivity;
import com.project.jewelmart.swarnsarita.R;


public class SendNotification {

    public static NotificationManager mNotificationManager ;//= (NotificationManager) contextN.getSystemService(Context.NOTIFICATION_SERVICE);
    public static int notificationID = 1100;
    public static int numMessages = 0;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void displayNotification(String Title,String Message,Context context) {
        Log.i("Start", "notification");
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context);
        mBuilder.setContentTitle(Title);
        mBuilder.setContentText(Message);
        mBuilder.setTicker("New Message Alert!");
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setNumber(++numMessages);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        //String[] events =array;
        inboxStyle.setBigContentTitle(Title);
       /*  for (int i=0; i < events.length; i++) {
            inboxStyle.addLine(events[i]);
        }*/
        mBuilder.setStyle(inboxStyle);
      /* Creates an explicit intent for an Activity in your app */
        Bundle b=new Bundle();
       // b.putStringArray("body",events);
        b.putString("from", "Yoga ");
        b.putBoolean("backcall",true);
        Intent resultIntent = new Intent(context, MainActivity.class);

        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        resultIntent.putExtras(b);
        mBuilder.setAutoCancel(true);
      /* Adds the Intent that starts the Activity to the top of the stack */
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                context,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
               /* stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );*/


        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationID, mBuilder.build());
    }

    public void cancelNotification() {
        Log.i("Cancel", "notification");
        mNotificationManager.cancel(notificationID);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void updateNotification(Context context) {
        Log.i("Update", "notification");
      /* Invoking the default notification service */
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context);
        mBuilder.setContentTitle("Updated Message");
        mBuilder.setContentText("You've got updated message.");
        mBuilder.setTicker("Updated Message Alert!");
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
     /* Increase notification number every time a badgenew notification arrives */
        mBuilder.setNumber(++numMessages);

            /* Creates an explicit intent for an Activity in your app */
        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.putExtra("from", "javed khan");
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
      /* Adds the Intent that starts the Activity to the top of the stack */
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

      /* Update the existing notification using same notification ID */
        mNotificationManager.notify(notificationID, mBuilder.build());
    }

}
