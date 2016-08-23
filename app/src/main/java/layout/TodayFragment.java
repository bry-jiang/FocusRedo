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

public class TodayFragment extends Fragment{
    DBOpenHelper dbOpenHelper;

    TextView largeText;
    TextView smallText;

    public TodayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, container, false);
        dbOpenHelper = new DBOpenHelper(getActivity());
        setEmpty1();
        setEmpty2();
        setEmpty3();

        initToday();

        setLargeText(view);
        setSmallText(view);

        return view;

    }

    private void setSmallText(View view) {
        smallText = (TextView) view.findViewById(R.id.smallText);
        String[] smallOptions = {"Tasks to Dominate:", "Easy Peasy Lemon Squeezy:", "Follow through:", "Worth the sweat:"};
        smallText.setText(smallOptions[chooseRandom(smallOptions.length)]);
    }

    private void setLargeText(View view) {
        largeText = (TextView) view.findViewById(R.id.largeText);
        String[] largeOptions = {"Stay Focused!", "Go get it!", "No Distractions.", "Laser Focus.", "Tunnel Vision.", "Ignore the Noise.", "Amaze Yourself."};
        largeText.setText(largeOptions[chooseRandom(largeOptions.length)]);
    }

    public void setEmpty1() {
        TodayEmptyFragment todayEmptyFragment = new TodayEmptyFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.today_container1, todayEmptyFragment).commit();
    }
    public void setEmpty2() {
        TodayEmptyFragment todayEmptyFragment = new TodayEmptyFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.today_container2, todayEmptyFragment).commit();
    }
    public void setEmpty3() {
        TodayEmptyFragment todayEmptyFragment = new TodayEmptyFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.today_container3, todayEmptyFragment).commit();
    }

    public void initToday() {
        Cursor cursor = dbOpenHelper.getAllRows();
        int usedToday = 0;
        if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                usedToday = cursor.getInt(cursor.getColumnIndex(dbOpenHelper.ITEM_USED_TODAY));
                if (usedToday == 1) {
                    setToday1();
                }
                if (usedToday == 2) {
                    setToday2();
                }
                if (usedToday == 3) {
                    setToday3();
                }

                cursor.moveToNext();
            }
        }
    }

    private void setToday1 () {
        TodayItem1Fragment item = new TodayItem1Fragment();
        getChildFragmentManager().beginTransaction().detach(item).attach(item).commit();
        getChildFragmentManager().beginTransaction().replace(R.id.today_container1, item).commit();
    }
    private void setToday2 () {
        TodayItem2Fragment item = new TodayItem2Fragment();
        getChildFragmentManager().beginTransaction().detach(item).attach(item).commit();
        getChildFragmentManager().beginTransaction().replace(R.id.today_container2, item).commit();
    }
    private void setToday3 () {
        TodayItem3Fragment item = new TodayItem3Fragment();
        getChildFragmentManager().beginTransaction().detach(item).attach(item).commit();
        getChildFragmentManager().beginTransaction().replace(R.id.today_container3, item).commit();
    }
    private int chooseRandom(int size) {
        int randomNum = (int) (Math.random() * size);
        return randomNum;
    }
}
