package com.clubcom.inclub.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by adamwalter3 on 7/6/16.
 */
public class SharedPreferenceLoader {
    private final static String APP_PREFERENCES = "com.clubcom.inclub.SHARED_PREFERENCES_FILE";
    public final static String PREF_WIFI_NAME = "com.clubcom.inclub.WIFI_NAME";

    public static String getPreference(Context ctx, String key) {
        return getPreferencesObject(ctx).getString(key, null);
    }

    public static void savePreference(Context ctx, String key, String value) {
        SharedPreferences.Editor editor = getPreferencesObject(ctx).edit();
        editor.putString(key, value);
        editor.apply();
    }

    private static SharedPreferences getPreferencesObject(Context ctx) {
        return ctx.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }
}
