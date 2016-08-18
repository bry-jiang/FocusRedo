package com.example.bryan.focusredo;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class EditorActivity extends AppCompatActivity {
    DBOpenHelper dbOpenHelper;
    EditText editText;
    boolean isNew;
    long id;

    String text;
    int deadLine;
    int importance;
    int tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        text = "";
        deadLine = 0;
        importance = 0;
        tag = 0;


        dbOpenHelper = new DBOpenHelper(this);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getLong("id");

        editText = (EditText) findViewById(R.id.coolID);

        if (id == -1) {
            isNew = true;
        } else {
            Cursor cursor = dbOpenHelper.getRow(id);
            if (cursor.moveToFirst()) {
                text = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ITEM_TEXT));
                editText.setText(text.trim());

                deadLine = cursor.getInt(cursor.getColumnIndex(DBOpenHelper.ITEM_DEADLINE)); // do a setcolor or something to indicate that these are already selected
                importance = cursor.getInt(cursor.getColumnIndex(DBOpenHelper.ITEM_IMPORTANCE));
                tag = cursor.getInt(cursor.getColumnIndex(DBOpenHelper.ITEM_TAG));
            }
            cursor.close();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        dbOpenHelper.deleteItem(id);
        editText.setText("");
        return true;
    }

    public void setDeadline (View view) {
        deadLine = 1;
    }

    public void setImportanceA (View view) {
        importance = 1;
    }
    public void setImportanceB (View view) {
        importance = 2;
    }
    public void setImportanceC (View view) {
        importance = 3;
    }
    public void setTag (View view) {
        tag = 1;
    }

    public void saveItem(View view) {
        String newText = editText.getText().toString();
        if (isNew) {
            dbOpenHelper.addItem(newText, deadLine, 0, importance, tag);
        } else {
            dbOpenHelper.updateRow(id, newText, deadLine, 0, importance, tag);
        }
    }
}


