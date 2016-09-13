package madroid.healthtracker;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import madroid.healthtracker.activity.ToolsActivity;
import madroid.healthtracker.activity.WeightTrackerActivity;

/**
 * Created by madroid on 13-09-2016.
 */
public class AlarmManagerBroadcastReceiver extends BroadcastReceiver  {
    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"Reminded").acquire(5000);
        Log.d("info_reminder","reminded");
        pop_notify(context);
    }

    private void pop_notify(Context context){
        NotificationCompat.Builder mBuilder= new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.ic_stat_name);
        mBuilder.setContentTitle("Health Tracker");
        mBuilder.setContentText("Time for your weight log");
        mBuilder.setAutoCancel(true);
        mBuilder.setVibrate(new long[]{100,100,100,100});
        mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        Intent intent = new Intent(context,WeightTrackerActivity.class);
        int mid=001;

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ToolsActivity.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(mid,mBuilder.build());
    }
}
