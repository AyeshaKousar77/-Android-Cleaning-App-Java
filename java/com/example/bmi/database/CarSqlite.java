package com.example.bmi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CarSqlite extends SQLiteOpenHelper {

    private static final String TAG = CarSqlite.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UserDatabase.db";

    private static final String TABLE_CAR_SERVICES = "car_services";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_VEHICLE_TYPE = "vehicle_type";
    private static final String COLUMN_WASH_OPTION = "wash_option";
    private static final String COLUMN_TIME_OPTION = "time_option";
    private static final String COLUMN_COST = "cost";

    // SQL statement to create the car services table
    private static final String SQL_CREATE_CAR_SERVICES_TABLE =
            "CREATE TABLE " + TABLE_CAR_SERVICES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_EMAIL + " TEXT," +
                    COLUMN_VEHICLE_TYPE + " TEXT," +
                    COLUMN_WASH_OPTION + " TEXT," +
                    COLUMN_TIME_OPTION + " TEXT," +
                    COLUMN_COST + " REAL)";

    public CarSqlite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(SQL_CREATE_CAR_SERVICES_TABLE);
            Log.d(TAG, "Car services table created successfully.");
        } catch (SQLException e) {
            Log.e(TAG, "Error creating car services table: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAR_SERVICES);
        onCreate(db);
    }

    // Method to add a new car service record
    public long addCarService(String email, String vehicleType, String washOption, String timeOption, double cost) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (!isTableExists(db)) {
            onCreate(db);
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_VEHICLE_TYPE, vehicleType);
        values.put(COLUMN_WASH_OPTION, washOption);
        values.put(COLUMN_TIME_OPTION, timeOption);
        values.put(COLUMN_COST, cost);

        long newRowId = db.insert(TABLE_CAR_SERVICES, null, values);
        db.close();
        return newRowId;
    }

    // Method to retrieve all car service records
    public Cursor getAllCarServices() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_CAR_SERVICES, null, null, null, null, null, null);
    }
    private boolean isTableExists(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + CarSqlite.TABLE_CAR_SERVICES + "'", null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
    // Add more methods as needed, such as updating or deleting records
}
