package com.example.bmi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class KitchenCleanSqlite extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserDatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "kitchen_cleaning_services";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_SIZE = "size";
    private static final String COLUMN_AMOUNT = "amount";

    public KitchenCleanSqlite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_EMAIL + " TEXT, "
                + COLUMN_TYPE + " TEXT, "
                + COLUMN_SIZE + " TEXT, "
                + COLUMN_AMOUNT + " REAL)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean isTableExists(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "'", null);
        if (cursor != null) {
            boolean exists = cursor.getCount() > 0;
            cursor.close();
            return exists;
        }
        return false;
    }

    public void addKitchenCleaningService(String email, String size, String type, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (!isTableExists(db, TABLE_NAME)) {
            onCreate(db);
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_SIZE, size);
        values.put(COLUMN_AMOUNT, amount);

        long result = db.insert(TABLE_NAME, null, values);
        if (result == -1) {
            Log.e("DatabaseHelper", "Failed to insert data");
        } else {
            Log.d("DatabaseHelper", "Data inserted successfully with id: " + result);
        }
        db.close();
    }

    public Cursor getAllServices() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public void deleteAllServices() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.close();
    }
}
