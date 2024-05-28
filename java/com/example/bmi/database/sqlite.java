package com.example.bmi.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

public class sqlite extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "UserDatabase.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FNAME = "firstName";
    private static final String COLUMN_LNAME = "lastName";
    private static final String COLUMN_CONTACT = "contact";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASS = "password";
    private static final String COLUMN_BANK_NAME = "bankName";
    private static final String COLUMN_ACCOUNT_NO = "accountNo";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";

    public sqlite(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_FNAME + " TEXT, "
                + COLUMN_LNAME + " TEXT, "
                + COLUMN_CONTACT + " LONG, "
                + COLUMN_EMAIL + " TEXT, "
                + COLUMN_PASS + " TEXT, "
                + COLUMN_BANK_NAME + " TEXT, "
                + COLUMN_ACCOUNT_NO + " TEXT, "
                + COLUMN_LATITUDE + " REAL, "
                + COLUMN_LONGITUDE + " REAL);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Method to add user to the database
    public boolean addUser(String firstName, String lastName, long contact, String email, String password, String bank, String accountNo, double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FNAME, firstName);
        contentValues.put(COLUMN_LNAME, lastName);
        contentValues.put(COLUMN_CONTACT, contact);
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_PASS, password);
        contentValues.put(COLUMN_BANK_NAME, bank);
        contentValues.put(COLUMN_ACCOUNT_NO, accountNo);
        contentValues.put(COLUMN_LATITUDE, latitude);
        contentValues.put(COLUMN_LONGITUDE, longitude);
        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return result != -1; // Returns true if insertion was successful, false otherwise
    }

    public String getUserFullName(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String fullName = "";

        Cursor cursor = db.rawQuery("SELECT " + COLUMN_FNAME + ", " + COLUMN_LNAME + " FROM " + TABLE_NAME + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email});
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String firstName = cursor.getString(cursor.getColumnIndex(COLUMN_FNAME));
            @SuppressLint("Range") String lastName = cursor.getString(cursor.getColumnIndex(COLUMN_LNAME));
            fullName = firstName + " " + lastName;
        }
        cursor.close();
        db.close();

        return fullName;
    }

    @SuppressLint("Range")
    public String getUserBankAccount(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String bankAccount = "";

        Cursor cursor = db.rawQuery("SELECT " + COLUMN_BANK_NAME + " FROM " + TABLE_NAME + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email});
        if (cursor.moveToFirst()) {
            bankAccount = cursor.getString(cursor.getColumnIndex(COLUMN_BANK_NAME));
        }
        cursor.close();
        db.close();

        return bankAccount;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_EMAIL + " = ? AND " + COLUMN_PASS + " = ?";
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    public int updateUserProfile(int id, String firstName, String lastName, String phone, String email, String password, LatLng location, String bank, String accountNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FNAME, firstName);
        values.put(COLUMN_LNAME, lastName);
        values.put(COLUMN_CONTACT, phone);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASS, password);
        values.put(COLUMN_BANK_NAME, bank);
        values.put(COLUMN_ACCOUNT_NO, accountNo);
        values.put(COLUMN_LATITUDE, location.latitude);
        values.put(COLUMN_LONGITUDE, location.longitude);
        int rowsAffected = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected;
    }

    // Method to get user profile from the database
    @SuppressLint("Range")
    public UserProfile getUserProfile(String userEmail) {
        if (userEmail == null) {
            // Handle case where email is not found (user not signed in)
            return null;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {userEmail};

        Cursor cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
        UserProfile userProfile = null;

        if (cursor.moveToFirst()) {
            userProfile = new UserProfile();
            userProfile.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            userProfile.setFirstName(cursor.getString(cursor.getColumnIndex(COLUMN_FNAME)));
            userProfile.setLastName(cursor.getString(cursor.getColumnIndex(COLUMN_LNAME)));
            userProfile.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT)));
            userProfile.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
            userProfile.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASS)));
            userProfile.setBankName(cursor.getString(cursor.getColumnIndex(COLUMN_BANK_NAME)));
            userProfile.setAccountNo(cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNT_NO)));

            // Get user location
            double latitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_LATITUDE));
            double longitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_LONGITUDE));
            userProfile.setLocation(new LatLng(latitude, longitude));
        }

        cursor.close();
        db.close();
        return userProfile;
    }

    public LatLng getUserLocation(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_LATITUDE, COLUMN_LONGITUDE};
        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        LatLng location = null;
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") double latitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_LATITUDE));
            @SuppressLint("Range") double longitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_LONGITUDE));
            location = new LatLng(latitude, longitude);
        }
        cursor.close();
        db.close();
        return location;
    }

    public void updateUserLocation(String email, double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);
        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};
        db.update(TABLE_NAME, values, selection, selectionArgs);
        db.close();
    }
}
