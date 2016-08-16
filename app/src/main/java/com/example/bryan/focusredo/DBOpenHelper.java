package com.example.bryan.focusredo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "focus.db";
    private static final int DATABASE_VERSION = 3;

    public static final String TABLE_NAME = "items";

    public static final String ITEM_ID = "_id";
    public static final String ITEM_TEXT = "itemText";
    public static final String ITEM_DEADLINE = "itemDeadline";
    public static final String ITEM_USED_TODAY = "itemUsedToday";
    public static final String ITEM_IMPORTANCE = "itemImportance";
    public static final String ITEM_TAG = "itemTag";

    public static final String ITEM_CREATED = "itemCreated";

    public static final String[] ALL_COLUMNS =
            {ITEM_ID, ITEM_TEXT, ITEM_DEADLINE, ITEM_USED_TODAY, ITEM_IMPORTANCE, ITEM_TAG, ITEM_CREATED};

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ITEM_TEXT + " TEXT, " +
                ITEM_DEADLINE + " INTEGER, " +
                ITEM_USED_TODAY + " INTEGER, " +
                ITEM_IMPORTANCE + " INTEGER, " +
                ITEM_TAG + " INTEGER, " +
                ITEM_CREATED + " TEXT default CURRENT_TIMESTAMP" +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addItem(String text, int deadline, int usedToday, int importance, int tag) {//add these to contentvalues
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ITEM_TEXT, text);
        values.put(ITEM_DEADLINE, deadline);
        values.put(ITEM_USED_TODAY, usedToday);
        values.put(ITEM_IMPORTANCE, importance);
        values.put(ITEM_TAG, tag);
        db.insert(TABLE_NAME, null, values);

    }
    public boolean deleteItem(long id) {
        SQLiteDatabase db = getWritableDatabase();
        String where = ITEM_ID + "=" + id;
        return db.delete(TABLE_NAME, where, null) != 0;
    }
    public boolean updateRow(long id, String text, int deadline, int usedToday, int importance, int tag) {
        SQLiteDatabase db = getReadableDatabase();
        String where = id + "=" + ITEM_ID;
        ContentValues values = new ContentValues();
        values.put(ITEM_TEXT, text);
        values.put(ITEM_DEADLINE, deadline);
        values.put(ITEM_USED_TODAY, usedToday);
        values.put(ITEM_IMPORTANCE, importance);
        values.put(ITEM_TAG, tag);
        return db.update(TABLE_NAME, values, where, null) != 0;
    }
    public Cursor getRow(long id) {
        SQLiteDatabase db = getReadableDatabase();
        String where = id + "=" + ITEM_ID;
        Cursor cursor = db.query(true, TABLE_NAME, ALL_COLUMNS, where, null, null, null, null, null);
        return cursor;
    }
    public Cursor getAllRows() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(true, TABLE_NAME, ALL_COLUMNS, null, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public void deleteAllItems() {
        Cursor cursor = getAllRows();
        long id = cursor.getColumnIndexOrThrow(ITEM_ID);
        if (cursor.moveToFirst()) {
            do {
                deleteItem(cursor.getLong((int)id));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
