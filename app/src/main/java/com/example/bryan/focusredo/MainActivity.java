package com.example.bryan.focusredo;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    DBOpenHelper dbOpenHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbOpenHelper = new DBOpenHelper(this);
        setListViewItemClick();
        setListViewItemLongClick();

        addItem("Please work");
        populateListView();
    }
    public void addItem(String text) {
        dbOpenHelper.getWritableDatabase();
        dbOpenHelper.addItem(text);
        populateListView();
    }
    public void deleteItem(long id) {
        dbOpenHelper.deleteItem(id);
        populateListView();
    }
    private void update(long id) {
        EditText editText = (EditText) findViewById(R.id.editText);
        Cursor cursor = dbOpenHelper.getRow(id);
        if(cursor.moveToFirst()) {
            String text = editText.getText().toString();
            dbOpenHelper.updateRow(id, text);
        }
        cursor.close();
    }
    //read
    //print table ^ reads all the data from the database and prints it
    private void setListViewItemClick() {
        ListView listView = (ListView) findViewById(R.id.main_activity_list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                update(id);
                populateListView();
            }
        });
    }
    private void setListViewItemLongClick() {
        ListView listView = (ListView) findViewById(R.id.main_activity_list_view);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long id) {
                deleteItem(id);
                return false;
            }
        });
    }

    public void deleteAllItems(View view) {
        dbOpenHelper.deleteAllItems();
        populateListView();
    }
    private void populateListView() { //call this whenever one of the crud methods is called
        Cursor cursor = dbOpenHelper.getAllRows();
        startManagingCursor(cursor);
        String[] from = new String[] {dbOpenHelper.ITEM_TEXT};
        int[] to = new int[] {R.id.list_item_layout_text};
        SimpleCursorAdapter simpleCursorAdapter;
        simpleCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.list_item_layout, cursor, from, to, 0);
        ListView listView = (ListView) findViewById(R.id.main_activity_list_view);
        listView.setAdapter(simpleCursorAdapter);
    }
}
