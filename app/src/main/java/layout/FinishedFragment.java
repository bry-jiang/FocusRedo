package layout;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.bryanjiang.bryan.focusredo.R;


public class FinishedFragment extends Fragment {
    AlarmManager alarmManager;
    TimePicker timePicker;
    TextView alarmText;
    PendingIntent pendingIntent;

    boolean isAlarmSet;

    public FinishedFragment() {}

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.fragment_finished, container, false);

        isAlarmSet = false;

        alarmText = (TextView) view.findViewById(R.id.alarmText);
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);

        final Button setAlarm = (Button) view.findViewById(R.id.setAlarm);
        Button unsetAlarm = (Button) view.findViewById(R.id.unsetAlarm);

        final Calendar calendar = Calendar.getInstance();
        //SET ALARM
        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                calendar.set(Calendar.MINUTE, timePicker.getMinute());

                Intent intent = new Intent(view.getContext(), AlarmReceiver.class);

                pendingIntent = pendingIntent.getBroadcast(view.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                int hour = timePicker.getHour();
                int min = timePicker.getMinute();

                setIsAlarmSet(true);
                setHour(hour);
                setMin(min);
                displayStatus();
            }
        });
        //UNSET ALARM
        unsetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarmManager.cancel(pendingIntent);
                setIsAlarmSet(false);
                displayStatus();
            }
        });
        displayStatus();
        return view;
    }
    //DISPLAY STATUS
    public void displayStatus() {
        if (getIsAlarmSet()) {

            String hourString = String.valueOf(getHour());
            String minString = String.valueOf(getMin());

            String AMPM = "AM";

            if (getHour() > 12) {
                int hour = getHour() -12;
                AMPM = "PM";
                hourString = String.valueOf(hour);
            }
            if (getMin() < 10) {
                minString = "0" + String.valueOf(getMin());
            }
            alarmText.setText("Alarm set to " + hourString + ":" + minString + AMPM);
        } else {
            alarmText.setText("Alarm unset");
        }
    }
    //shared preferences stuff
    public void setIsAlarmSet (Boolean isAlarmSet) {
        SharedPreferences sharedPreferences = getActivity().getPreferences(0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isAlarmSet", isAlarmSet);
        editor.commit();
    }
    public void setHour (int hour) {
        SharedPreferences sharedPreferences = getActivity().getPreferences(0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("hour", hour);
        editor.commit();
    }
    public void setMin (int min) {
        SharedPreferences sharedPreferences = getActivity().getPreferences(0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("min", min);
        editor.commit();
    }

    public boolean getIsAlarmSet () {
        SharedPreferences sharedPreferences = getActivity().getPreferences(0);
        return sharedPreferences.getBoolean("isAlarmSet", false);
    }
    public int getHour() {
        SharedPreferences sharedPreferences = getActivity().getPreferences(0);
        return sharedPreferences.getInt("hour", 0);
    }
    public int getMin() {
        SharedPreferences sharedPreferences = getActivity().getPreferences(0);
        return sharedPreferences.getInt("min", 0);
    }
}