package com.akram.huaweihelper;

import android.content.Context;
import android.content.SharedPreferences;

public class Pref {

    public static int InterCount = 2;
    public static int InterHolder = 0;

    private SharedPreferences preferences;

    public Pref(Context context) {
        preferences = context.getSharedPreferences("pp_prefs", Context.MODE_PRIVATE);
    }


    public void saveSplash(String id) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("splash_id", id);
        editor.apply();
    }

    public String getSplashId() {
        return preferences.getString("splash_id", "");
    }

    public void saveNative(String id) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("native_id", id);
        editor.apply();
    }

    public String getNativeId() {
        return preferences.getString("native_id", "");
    }

    public void saveInter(String id) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("inter_id", id);
        editor.apply();
    }

    public String getInterId() {
        return preferences.getString("inter_id", "");
    }


    public void saveReward(String id) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("reward_id", id);
        editor.apply();
    }

    public String getRewardId() {
        return preferences.getString("reward_id", "");
    }

}
