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
public class TodayItem2Fragment extends Fragment {
    TextView placeHolder2;
    DBOpenHelper dbOpenHelper;
    TodayFragment todayFragment;


    public TodayItem2Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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
                    placeHolder2 = (TextView) view.findViewById(R.id.PlaceHolder2);
                    placeHolder2.setText(text);
                }
                cursor.moveToNext();
            }
        }
        return view;
    }
}
