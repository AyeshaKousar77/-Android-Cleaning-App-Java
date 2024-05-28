package com.example.bmi.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.util.Log;
import androidx.annotation.Nullable;

public class CleanSqlite extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserDatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "cleaning_services";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_SIZE = "size";
    private static final String COLUMN_ROOMS = "rooms";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_FREQUENCY = "frequency";
    private static final String COLUMN_AMOUNT = "amount";

    public CleanSqlite(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_TYPE + " TEXT,"
                + COLUMN_SIZE + " INTEGER,"
                + COLUMN_ROOMS + " INTEGER,"
                + COLUMN_TIME + " TEXT,"
                + COLUMN_FREQUENCY + " TEXT,"
                + COLUMN_AMOUNT + " REAL" + ")";
        db.execSQL(CREATE_TABLE);
        Log.d("DatabaseHelper", "Table created with query: " + CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addCleaningService(String email, String type, int size, int rooms, String time, String frequency, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (!isTableExists(db)) {
            onCreate(db);
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_SIZE, size);
        values.put(COLUMN_ROOMS, rooms);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_FREQUENCY, frequency);
        values.put(COLUMN_AMOUNT, amount);

        long result = db.insert(TABLE_NAME, null, values);
        if (result == -1) {
            Log.e("DatabaseHelper", "Failed to insert data");
        } else {
            Log.d("DatabaseHelper", "Data inserted successfully with id: " + result);
        }
        db.close();
    }

    private boolean isTableExists(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + CleanSqlite.TABLE_NAME + "'", null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
}
