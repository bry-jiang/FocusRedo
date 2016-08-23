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
    long databaseId;

    String text;
    String importance;
    int urgency;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        text = "";
        urgency = 0;
        importance = "";

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

                databaseId = cursor.getLong(cursor.getColumnIndex(DBOpenHelper.ITEM_ID));
                importance = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ITEM_IMPORTANCE));
                urgency = cursor.getInt(cursor.getColumnIndex(DBOpenHelper.ITEM_URGENCY)); // do a setcolor or something to indicate that these are already selected

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
        editText.setText("");
        return true;
    }
    public void setImportanceA (View view) {
        importance = "A";
    }
    public void setImportanceB (View view) {
        importance = "B";
    }
    public void setImportanceC (View view) {
        importance = "C";
    }
    public void setUrgency1 (View view) {
        urgency = 1;
    }
    public void setUrgency2 (View view) {
        urgency = 2;
    }
    public void setUrgency3 (View view) {
        urgency = 3;
    }
    public void saveItem() {
        String newText = editText.getText().toString().trim();
        if (isNew) {
            if (newText.length() != 0) {
                dbOpenHelper.addItem(newText, importance, urgency, 0);
            }
        } else {
            if (newText.length() == 0) {
                dbOpenHelper.deleteItem(databaseId);
            }

        }
        finish();
    }
    @Override
    public void onBackPressed() {
        saveItem();
    }
}


