package com.example.bryan.focusredo;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import layout.FinishedFragment;
import layout.MasterListFragment;
import layout.TodayFragment;

public class MainActivity extends AppCompatActivity {

    DBOpenHelper dbOpenHelper;
    SimpleCursorAdapter simpleCursorAdapter;
    AHBottomNavigation bottomNavigation;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Focus.");


        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);


        dbOpenHelper = new DBOpenHelper(this);
        initBar();
        startOnToday();

        setListViewItemClick();
        setListViewItemLongClick();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void startOnToday() {
        TodayFragment todayFragment = new TodayFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_id, todayFragment).commit();
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
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
                openEditor(id);

            }
        });
    }

    private void setListViewItemLongClick() {
        ListView listView = (ListView) findViewById(R.id.main_activity_list_view);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long id) {
                setAsToday(id);
                return true;
            }
        });
    }

    public void populateListView() {
        Cursor cursor = dbOpenHelper.getAllRows();

        String[] from = new String[]{dbOpenHelper.ITEM_IMPORTANCE, dbOpenHelper.ITEM_URGENCY, dbOpenHelper.ITEM_TEXT};
        int[] to = new int[]{R.id.list_item_layout_importance, R.id.list_item_layout_urgency, R.id.list_item_layout_text};
        simpleCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.list_item_layout, cursor, from, to, 0);
        ListView listView = (ListView) findViewById(R.id.main_activity_list_view);
        listView.setAdapter(simpleCursorAdapter);
        cursor.close();
    }

    private void initBar() {
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Today", R.drawable.goal, R.color.colorPrimary);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Master List", R.drawable.crown, R.color.colorAccent);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Reminder", R.drawable.reminder, R.color.colorBottomNavigationAccent);

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);

// Set background color
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));

// Disable the translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(false);

// Change colors
        bottomNavigation.setAccentColor(Color.parseColor("#F63D2B"));
        bottomNavigation.setInactiveColor(Color.parseColor("#F63D2B"));

// Force to tint the drawable (useful for font with icon for example)
        bottomNavigation.setForceTint(true);

// Force the titles to be displayed (against Material Design guidelines!)
        bottomNavigation.setForceTitlesDisplay(true);

// Use colored navigation with circle reveal effect
        bottomNavigation.setColored(true);

// Set current item programmatically
        bottomNavigation.setCurrentItem(0);

// Customize notification (title, background, typeface)
        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));


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
                            case 1:
                                setEmpty1(null);
                                break;
                            case 2:
                                setEmpty2(null);
                                break;
                            case 3:
                                setEmpty3(null);
                        }
                        dbOpenHelper.setAsToday(id, button + 1);
                    }
                }
        );
        builder.create().show();
    }

    public void setEmpty1(View view) {
        Cursor cursor = dbOpenHelper.getAllRows();
        int usedToday = 0;
        if (cursor.moveToFirst()) {
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

    public void setEmpty2(View view) {
        Cursor cursor = dbOpenHelper.getAllRows();
        int usedToday = 0;
        if (cursor.moveToFirst()) {
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
        populateListView();
    }

    public void setEmpty3(View view) {
        Cursor cursor = dbOpenHelper.getAllRows();
        int usedToday = 0;
        if (cursor.moveToFirst()) {
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
        populateListView();
    }

    public void setFinished1(View view) {
        Cursor cursor = dbOpenHelper.getAllRows();
        int usedToday = 0;
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                usedToday = cursor.getInt(cursor.getColumnIndex(dbOpenHelper.ITEM_USED_TODAY));
                if (usedToday == 1) {
                    dbOpenHelper.deleteItem(cursor.getInt(cursor.getColumnIndex(dbOpenHelper.ITEM_ID)));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        restartToday();
        populateListView();
        Toast.makeText(MainActivity.this, "Good Job! :)", Toast.LENGTH_SHORT).show();
    }

    public void setFinished2(View view) {
        Cursor cursor = dbOpenHelper.getAllRows();
        int usedToday = 0;
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                usedToday = cursor.getInt(cursor.getColumnIndex(dbOpenHelper.ITEM_USED_TODAY));
                if (usedToday == 2) {
                    dbOpenHelper.deleteItem(cursor.getInt(cursor.getColumnIndex(dbOpenHelper.ITEM_ID)));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        restartToday();
        Toast.makeText(MainActivity.this, "Good Job! :)", Toast.LENGTH_SHORT).show();
    }

    public void setFinished3(View view) {
        Cursor cursor = dbOpenHelper.getAllRows();
        int usedToday = 0;
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                usedToday = cursor.getInt(cursor.getColumnIndex(dbOpenHelper.ITEM_USED_TODAY));
                if (usedToday == 3) {
                    dbOpenHelper.deleteItem(cursor.getInt(cursor.getColumnIndex(dbOpenHelper.ITEM_ID)));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        restartToday();
        Toast.makeText(MainActivity.this, "Good Job! :)", Toast.LENGTH_SHORT).show();
    }

    public void restartToday() {
        MasterListFragment masterListFragment = new MasterListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_id, masterListFragment).commit();
        TodayFragment todayFragment = new TodayFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_id, todayFragment).commit();
    }

    public void TodayEmptyMessage(View view) {
        Toast.makeText(MainActivity.this, "Go to the master list tab to choose a task to add here.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.bryan.focusredo/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.bryan.focusredo/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
