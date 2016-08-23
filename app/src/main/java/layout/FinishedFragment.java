package layout;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.bryan.focusredo.R;

public class FinishedFragment extends Fragment {
    AlarmManager alarmManager;
    TimePicker timePicker;
    TextView alarmText;
    PendingIntent pendingIntent;

    public FinishedFragment() {}

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.fragment_finished, container, false);

        alarmText = (TextView) view.findViewById(R.id.alarmText);
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);

        final Button setAlarm = (Button) view.findViewById(R.id.setAlarm);
        Button unsetAlarm = (Button) view.findViewById(R.id.unsetAlarm);

        final Calendar calendar = Calendar.getInstance();

        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                calendar.set(Calendar.MINUTE, timePicker.getMinute());
                alarmText.setText("Alarm set");

                Intent intent = new Intent(view.getContext(), AlarmReceiver.class);

                int hour = timePicker.getHour();
                int min = timePicker.getMinute();

                String hourString = String.valueOf(hour);
                String minString = String.valueOf(min);

                String AMPM = "AM";

                if (hour > 12) {
                    hour -= 12;
                    AMPM = "PM";
                    hourString = String.valueOf(hour);
                }
                if (min < 10) {
                    minString = "0" + String.valueOf(min);
                }

                alarmText.setText("Alarm set to " + hourString + ":" + minString + AMPM);
                pendingIntent = pendingIntent.getBroadcast(view.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

            }
        });

        unsetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarmText.setText("Alarm unset");
                alarmManager.cancel(pendingIntent);
            }
        });

        return view;
    }

}