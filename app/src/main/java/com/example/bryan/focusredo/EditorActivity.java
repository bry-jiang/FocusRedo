package com.example.bryan.focusredo;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditorActivity extends AppCompatActivity {
    DBOpenHelper dbOpenHelper;
    EditText editText;
    boolean isNew;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        dbOpenHelper = new DBOpenHelper(this);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getLong("id");

        editText = (EditText) findViewById(R.id.coolID);

        if (id == -1) {
            isNew = true;
        } else {
            Cursor cursor = dbOpenHelper.getRow(id);
            if (cursor.moveToFirst()) {
                String existingText = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ITEM_TEXT));
                editText.setText(existingText);
            }
            cursor.close();
        }
    }

    public void saveItem(View view) {
        String newText = editText.getText().toString();
        if (isNew) {
            dbOpenHelper.addItem(newText, 2, 2, 2, 2);
        } else {
            dbOpenHelper.updateRow(id, newText, 2, 2, 2, 2);
        }
    }
}


