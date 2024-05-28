package com.example.bmi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SpecificSqlite extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "UserDatabase.db";
    private static final int DATABASE_VERSION = 1;

    // Table name and column names
    private static final String TABLE_BOOKING = "booking";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_KITCHEN = "kitchen";
    private static final String COLUMN_BATHROOM = "bathroom";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_CARPET_CLEANING = "carpet_cleaning";
    private static final String COLUMN_WINDOW_WASHING = "window_washing";
    private static final String COLUMN_FREQUENCY = "frequency";
    private static final String COLUMN_SPECIAL_INSTRUCTIONS = "special_instructions";
    private static final String COLUMN_CHARGE = "charge"; // New column for charge

    // SQL statement to create the table
    private static final String SQL_CREATE_BOOKING_TABLE =
            "CREATE TABLE " + TABLE_BOOKING + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_KITCHEN + " INTEGER DEFAULT 0," +
                    COLUMN_BATHROOM + " INTEGER DEFAULT 0," +
                    COLUMN_CARPET_CLEANING + " INTEGER DEFAULT 0," +
                     COLUMN_EMAIL + " TEXT, " +
                    COLUMN_WINDOW_WASHING + " INTEGER DEFAULT 0," +
                    COLUMN_FREQUENCY + " TEXT," +
                    COLUMN_SPECIAL_INSTRUCTIONS + " TEXT," +
                    COLUMN_CHARGE + " REAL)";

    public SpecificSqlite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_BOOKING_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implement schema changes and data migration if needed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKING);
        onCreate(db);
    }

    // Method to add a booking entry
    public void addBooking(String email, int kitchen, int bathroom, int carpetCleaning, int windowWashing,
                           String frequency, String specialInstructions, double charge) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (!isTableExists(db)) {
            onCreate(db);
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_KITCHEN, kitchen);
        values.put(COLUMN_BATHROOM, bathroom);
        values.put(COLUMN_CARPET_CLEANING, carpetCleaning);
        values.put(COLUMN_WINDOW_WASHING, windowWashing);
        values.put(COLUMN_FREQUENCY, frequency);
        values.put(COLUMN_SPECIAL_INSTRUCTIONS, specialInstructions);
        values.put(COLUMN_CHARGE, charge);

        long result = db.insert(TABLE_BOOKING, null, values);
        if (result == -1) {
            Log.e("DatabaseHelper", "Failed to insert data");
        } else {
            Log.d("DatabaseHelper", "Data inserted successfully with id: " + result);
        }
        db.close();

    }
    private boolean isTableExists(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + SpecificSqlite.TABLE_BOOKING + "'", null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
}
