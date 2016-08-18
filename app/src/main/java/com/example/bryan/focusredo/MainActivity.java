package com.example.bryan.focusredo;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import layout.FinishedFragment;
import layout.MasterListFragment;
import layout.TodayFragment;

public class MainActivity extends AppCompatActivity {

    DBOpenHelper dbOpenHelper;
    SimpleCursorAdapter simpleCursorAdapter;
    AHBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        dbOpenHelper = new DBOpenHelper(this);

        setListViewItemClick();
        setListViewItemLongClick();

        initBar();
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        populateListView();
    }

    public void deleteItem(long id) {
        dbOpenHelper.deleteItem(id);
        populateListView();
    }

    public void openEditor(long id) {
        Intent intent = new Intent(MainActivity.this, EditorActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public void openEditorForNewNote(View view) {
        openEditor(-1);
    }

    private void setListViewItemClick() {
        ListView listView = (ListView) findViewById(R.id.main_activity_list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                setAsToday(id);
            }
        });
    }

    private void setListViewItemLongClick() {
        ListView listView = (ListView) findViewById(R.id.main_activity_list_view);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long id) {
                openEditor(id);
                return false;
            }
        });
    }

    public void deleteAllItems(View view) {
        dbOpenHelper.deleteAllItems();
        populateListView();
    }

    public void populateListView() { //call this whenever one of the crud methods is called
        Cursor cursor = dbOpenHelper.getAllRows();
        startManagingCursor(cursor);
        String[] from = new String[]{dbOpenHelper.ITEM_TEXT};
        int[] to = new int[]{R.id.list_item_layout_text};
        simpleCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.list_item_layout, cursor, from, to, 0);
        ListView listView = (ListView) findViewById(R.id.main_activity_list_view);
        listView.setAdapter(simpleCursorAdapter);
    }

    private void initBar() {

// Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Today", R.drawable.goal, R.color.colorPrimary);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Master List", R.drawable.crown, R.color.colorAccent);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Finished", R.drawable.check, R.color.colorBottomNavigationAccent);

// Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);

// Set background color
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));

// Disable the translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(false);

// Change colors
        bottomNavigation.setAccentColor(Color.parseColor("#F63D2B"));
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));

// Force to tint the drawable (useful for font with icon for example)
        bottomNavigation.setForceTint(true);

// Force the titles to be displayed (against Material Design guidelines!)
        bottomNavigation.setForceTitlesDisplay(true);

// Use colored navigation with circle reveal effect
        bottomNavigation.setColored(true);

// Set current item programmatically
        bottomNavigation.setCurrentItem(1);

// Customize notification (title, background, typeface)
        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));

// Add or remove notification for each item
        bottomNavigation.setNotification("4", 1);
        bottomNavigation.setNotification("", 1);

// Set listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if (position == 0) {
                    TodayFragment todayFragment = new TodayFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_id, todayFragment).commit();
                } else if (position == 1) {
                    MasterListFragment masterListFragment = new MasterListFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_id, masterListFragment).commit();
                } else if (position == 2) {
                    FinishedFragment finishedFragment = new FinishedFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_id, finishedFragment).commit();
                }
                return true;
            }
        });
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {
                // Manage the new y position
            }
        });
    }

    private void setAsToday(final long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("What order do you wish to set this item in?");
        builder.setItems(new CharSequence[]
                {"1", "2", "3"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int button) {
                switch (button + 1) {
                    case 1: setEmpty1(null);
                        break;
                    case 2: setEmpty2(null);
                        break;
                    case 3: setEmpty3(null);
                }
                dbOpenHelper.setAsToday(id, button + 1);
            }
        }
        );
        builder.create().show();
    }
    public void setEmpty1 (View view) {
        Cursor cursor = dbOpenHelper.getAllRows();
        int usedToday = 0;
        if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                usedToday = cursor.getInt(cursor.getColumnIndex(dbOpenHelper.ITEM_USED_TODAY));
                if (usedToday == 1 || usedToday == -1) {
                    dbOpenHelper.setAsToday(cursor.getInt(cursor.getColumnIndex(dbOpenHelper.ITEM_ID)), 0);
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        restartToday();
    }
    public void setEmpty2 (View view) {
        Cursor cursor = dbOpenHelper.getAllRows();
        int usedToday = 0;
        if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                usedToday = cursor.getInt(cursor.getColumnIndex(dbOpenHelper.ITEM_USED_TODAY));
                if (usedToday == 2 || usedToday == -2) {
                    dbOpenHelper.setAsToday(cursor.getInt(cursor.getColumnIndex(dbOpenHelper.ITEM_ID)), 0);
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        restartToday();
    }
    public void setEmpty3 (View view) {
        Cursor cursor = dbOpenHelper.getAllRows();
        int usedToday = 0;
        if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                usedToday = cursor.getInt(cursor.getColumnIndex(dbOpenHelper.ITEM_USED_TODAY));
                if (usedToday == 3 || usedToday == -3) {
                    dbOpenHelper.setAsToday(cursor.getInt(cursor.getColumnIndex(dbOpenHelper.ITEM_ID)), 0);
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        restartToday();
    }
    public void setFinished1 (View view) {
        Cursor cursor = dbOpenHelper.getAllRows();
        int usedToday = 0;
        if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                usedToday = cursor.getInt(cursor.getColumnIndex(dbOpenHelper.ITEM_USED_TODAY));
                if (usedToday == 1) {
                    dbOpenHelper.setAsToday(cursor.getInt(cursor.getColumnIndex(dbOpenHelper.ITEM_ID)), -1);
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        restartToday();
    }
    public void setFinished2 (View view) {
        Cursor cursor = dbOpenHelper.getAllRows();
        int usedToday = 0;
        if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                usedToday = cursor.getInt(cursor.getColumnIndex(dbOpenHelper.ITEM_USED_TODAY));
                if (usedToday == 2) {
                    dbOpenHelper.setAsToday(cursor.getInt(cursor.getColumnIndex(dbOpenHelper.ITEM_ID)), -2);
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        restartToday();
    }
    public void setFinished3 (View view) {
        Cursor cursor = dbOpenHelper.getAllRows();
        int usedToday = 0;
        if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                usedToday = cursor.getInt(cursor.getColumnIndex(dbOpenHelper.ITEM_USED_TODAY));
                if (usedToday == 3) {
                    dbOpenHelper.setAsToday(cursor.getInt(cursor.getColumnIndex(dbOpenHelper.ITEM_ID)), -3);
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        restartToday();
    }
    public void restartToday() {
        MasterListFragment masterListFragment = new MasterListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_id, masterListFragment).commit();
        TodayFragment todayFragment = new TodayFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_id, todayFragment).commit();
    }
}
