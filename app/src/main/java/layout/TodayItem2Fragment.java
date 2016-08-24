package layout;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bryanjiang.bryan.focus.DBOpenHelper;
import com.bryanjiang.bryan.focusredo.R;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class TodayItem2Fragment extends Fragment {
    TextView placeHolder2;
    TextView i2;
    TextView u2;
    DBOpenHelper dbOpenHelper;
    TodayFragment todayFragment;


    public TodayItem2Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_today_item2, container, false);

        todayFragment = new TodayFragment();
        dbOpenHelper = new DBOpenHelper(getActivity());

        Cursor cursor = dbOpenHelper.getAllRows();
        int usedToday = 0;
        if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                usedToday = cursor.getInt(cursor.getColumnIndex(dbOpenHelper.ITEM_USED_TODAY));
                if (usedToday == 2) {
                    String text = cursor.getString(cursor.getColumnIndex(dbOpenHelper.ITEM_TEXT));
                    String importance = cursor.getString(cursor.getColumnIndex(dbOpenHelper.ITEM_IMPORTANCE));
                    String urgency = cursor.getString(cursor.getColumnIndex(dbOpenHelper.ITEM_URGENCY));
                    placeHolder2 = (TextView) view.findViewById(R.id.PlaceHolder2);
                    i2 = (TextView) view.findViewById(R.id.i2);
                    u2 = (TextView) view.findViewById(R.id.u2);
                    placeHolder2.setText(text);
                    i2.setText(importance);
                    u2.setText(urgency);
                }
                cursor.moveToNext();
            }
        }
        return view;
    }
}
