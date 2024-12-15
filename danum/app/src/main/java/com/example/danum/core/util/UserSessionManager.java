package com.example.danum.core.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.danum.core.data.model.UserProfile;
import com.google.gson.Gson;

public class UserSessionManager {

    private static final String PREF_NAME = "user_session";
    private static final String KEY_USER_PROFILE = "user_profile";
    private static final String KEY_ACCESS_TOKEN = "access_token";

    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;

    public UserSessionManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void saveUserProfile(UserProfile userProfile) {
        Gson gson = new Gson();
        String json = gson.toJson(userProfile);
        editor.putString(KEY_USER_PROFILE, json);
        editor.putString(KEY_ACCESS_TOKEN, userProfile.getToken());
        editor.apply();
        Log.d("UserSessionManager", "사용자 정보 저장 성공");
    }

    public UserProfile getUserProfile() {
        String json = preferences.getString(KEY_USER_PROFILE, null);
        if (json == null) return null;

        return new Gson().fromJson(json, UserProfile.class);
    }

    public String getAccessToken() {
        return preferences.getString(KEY_ACCESS_TOKEN, null);
    }

    public void clearSession() {
        editor.clear().apply();
        Log.d("UserSessionManager", "사용자 세션이 초기화되었습니다.");
    }
}
