package com.example.bmi.database;
import android.content.Context;
import android.content.SharedPreferences;
public class token {



    private static final String PREF_NAME = "UserSession";
    private static final String KEY_EMAIL = "email";

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    private Context context;

    public token(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUserEmail(String email) {
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public String getUserEmail() {
        return sharedPreferences.getString(KEY_EMAIL, null);
    }

    // Add other session management methods if needed, e.g., clearing session
}
