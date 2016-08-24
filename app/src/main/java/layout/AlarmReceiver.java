package layout;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {

        String[] planningQuotes = {
                "Productivity is never an accident. It is always the result of a commitment to excellence, intelligent planning, and focused effort. Paul J. Meyer",
                "Failing to plan is planning to fail. Alan Lakein",
                "Planning is bringing the future into the present so that you can do something about it now. Alan Lakein",
                "The future belongs to those who prepare for it today. Malcolm X",
                "The will to succeed is important, but what's more important is the will to prepare. Bobby Knight",
                "You were born to win, but to be a winner, you must plan to win, prepare to win, and expect to win. Zig Ziglar"
        };

        //notifications stuff
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        Intent repeatingIntent = new Intent(context, RepeatingActivity.class);
        context.startService(repeatingIntent);
        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(android.R.drawable.arrow_up_float)
                .setContentTitle("Declare tomorrow's focus!")
                .setContentText(planningQuotes[chooseRandom(planningQuotes.length)])
                .setAutoCancel(true);
        notificationManager.notify(0, builder.build());

    }
    private int chooseRandom(int size) {
        int randomNum = (int) (Math.random() * size);
        return randomNum;
    }
}
