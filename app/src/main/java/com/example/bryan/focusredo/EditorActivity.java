package com.example.bryan.focusredo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class EditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Bundle bundle = getIntent().getExtras();
        boolean itemExists = bundle.getBoolean("itemExists");

        if (itemExists == false) {
            EditText editText = (EditText) findViewById(R.id.coolID);
            editText.setText("#YOLOSWAGMONEY");
        }
    }
}
