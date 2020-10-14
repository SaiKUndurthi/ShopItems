package com.example.myapplication;

import android.app.Application;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class MyApplication extends Application {

    private static final String KEY_USER_ID = "PREF_KEY_USER_ID";
    private static final String KEY_USER_PASSWORD = "PREF_KEY_USER_PASSWORD";
    private SharedPreferences mAppSharedPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppSharedPreferences = getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE);
    }

    public SharedPreferences getAppSharedPreferences() {
        return mAppSharedPreferences;
    }

    public String getUserInfo(String email) {
        return mAppSharedPreferences.getString(email, null);
    }

    public void setUserInfo(String userEmail, String userPassword) {
        mAppSharedPreferences.edit().putString(userEmail, userPassword).apply();
    }

    public boolean doesUserEmailExist(String email) {
        Map<String, ?> map = mAppSharedPreferences.getAll();
        return map.containsKey(email);
    }


    // abc@123.com : gfj
}
