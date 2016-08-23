package layout;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bryan.focusredo.DBOpenHelper;
import com.example.bryan.focusredo.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class TodayItem3Fragment extends Fragment {
    TextView placeHolder3;
    TextView i3;
    TextView u3;
    DBOpenHelper dbOpenHelper;
    TodayFragment todayFragment;


    public TodayItem3Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_today_item3, container, false);

        todayFragment = new TodayFragment();
        dbOpenHelper = new DBOpenHelper(getActivity());

        Cursor cursor = dbOpenHelper.getAllRows();
        int usedToday = 0;
        if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                usedToday = cursor.getInt(cursor.getColumnIndex(dbOpenHelper.ITEM_USED_TODAY));
                if (usedToday == 3) {
                    String text = cursor.getString(cursor.getColumnIndex(dbOpenHelper.ITEM_TEXT));
                    String importance = cursor.getString(cursor.getColumnIndex(dbOpenHelper.ITEM_IMPORTANCE));
                    String urgency = cursor.getString(cursor.getColumnIndex(dbOpenHelper.ITEM_URGENCY));
                    placeHolder3 = (TextView) view.findViewById(R.id.PlaceHolder3);
                    i3 = (TextView) view.findViewById(R.id.i3);
                    u3 = (TextView) view.findViewById(R.id.u3);
                    placeHolder3.setText(text);
                    i3.setText(importance);
                    u3.setText(urgency);
                }
                cursor.moveToNext();
            }
        }
        return view;
    }
}
