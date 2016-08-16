package com.example.bryan.focusredo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    DBOpenHelper dbOpenHelper;
    SimpleCursorAdapter simpleCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbOpenHelper = new DBOpenHelper(this);
        setListViewItemClick();
        setListViewItemLongClick();

        populateListView();
    }

    public void addItem(String text, int deadline, int usedToday, int importance, int tag) {
        dbOpenHelper.addItem(text, deadline, usedToday, importance, tag);
        populateListView();
    }

    public void deleteItem(long id) {
        dbOpenHelper.deleteItem(id);
        populateListView();
    }

    //    private void updateItem(long id) { //all the updating happens here> call openEditor
//        Cursor cursor = dbOpenHelper.getRow(id);
//        if(cursor.moveToFirst()) {
//            openEditor(cursor);
//            dbOpenHelper.updateRow(id, text);
//        }
//        populateListView();
//        cursor.close();
//    }
    public void openEditor(long id) {
        Intent intent = new Intent(MainActivity.this, EditorActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
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
                deleteItem(id);
                populateListView();
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

    public void addDefault(View view) {
        addItem("Default", 2, 2, 2, 2);
    }

    public void openEditorForNewNote(View view) {
        openEditor(-1);
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        populateListView();
    }
}
