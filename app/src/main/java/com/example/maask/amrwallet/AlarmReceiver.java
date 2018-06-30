package com.example.maask.amrwallet;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;

/**
 * Created by Maask on 2/19/2018.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String db = intent.getStringExtra("depositBox");
        String income = intent.getStringExtra("income");
        String expense = intent.getStringExtra("expense");

        Notification.Builder builder = new Notification.Builder(context);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            Notification notification = builder.setContentTitle("Current Balance : " + db + " TK")
                    .setContentText("Income : " + income +" TK , Expense : " + expense + " TK")
                    .setSmallIcon(R.drawable.login_icon)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setLights(Color.GREEN,1000,500)
                    .build();
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notification);
        }
    }
}
