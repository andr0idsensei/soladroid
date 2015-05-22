package com.androidsensei.soladroid.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Utility class for working with shared preferences.
 *
 * Created by mihai on 5/22/15.
 */
public final class SharedPrefsUtil {
    private SharedPrefsUtil() {}

    /**
     * The shared preferences file name.
     */
    private static String PREF_FILE__NAME = "soladroid";

    /**
     * Saves a given string value using the given key in the shared preferences file managed by this util.
     *
     * @param key the key by which we refer the shared preference value.
     * @param value the value of the shared preference.
     * @param context the context used for getting the shared pref file.
     */
    public static void savePreferenceString(String key, String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_FILE__NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
    }

    /**
     * Loads a string shared preference value represented by the given key.
     *
     * @param key the key by which we refer the shared preference value.
     * @param context the context used for getting the shared pref file.
     * @return the value referred by the key or empty string if the key isn't saved.
     */
    public static String loadPreferenceString(String key, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_FILE__NAME, Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }
}
