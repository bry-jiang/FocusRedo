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
public class TodayItem1Fragment extends Fragment {
    TextView placeHolder1;
    DBOpenHelper dbOpenHelper;
    TodayFragment todayFragment;


    public TodayItem1Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_today_item1, container, false);

        todayFragment = new TodayFragment();
        dbOpenHelper = new DBOpenHelper(getActivity());

        Cursor cursor = dbOpenHelper.getAllRows();
        int usedToday = 0;
        if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                usedToday = cursor.getInt(cursor.getColumnIndex(dbOpenHelper.ITEM_USED_TODAY));
                if (usedToday == 1) {
                    String text = cursor.getString(cursor.getColumnIndex(dbOpenHelper.ITEM_TEXT));
                    placeHolder1 = (TextView) view.findViewById(R.id.PlaceHolder1);
                    placeHolder1.setText(text);
                }
                cursor.moveToNext();
            }
        }
        return view;
    }
}
