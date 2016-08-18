package layout;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.bryan.focusredo.DBOpenHelper;
import com.example.bryan.focusredo.R;

import java.util.ArrayList;

public class FinishedFragment extends Fragment {

    public FinishedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_finished, container, false);
        ArrayList<String> itemsArrayList = new ArrayList<>();
        DBOpenHelper dbOpenHelper = new DBOpenHelper(getActivity());
        Cursor cursor = dbOpenHelper.getAllRows();
        int usedToday = 0;
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                usedToday = cursor.getInt(cursor.getColumnIndex(dbOpenHelper.ITEM_USED_TODAY));
                if (usedToday == -1 || usedToday == -2 || usedToday == -3) {
                    String text = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ITEM_TEXT));
                    itemsArrayList.add(text);
                }
                cursor.moveToNext();
            }
        }

        String[] itemsArray = new String[itemsArrayList.size()];
        for (int i = 0; i < itemsArrayList.size(); i++) {
            itemsArray[i] = itemsArrayList.get(i);
        }

        ListView listView = (ListView) view.findViewById(R.id.FinishedList);
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                itemsArray
        );
        listView.setAdapter(listViewAdapter);

        return view;
    }
}
