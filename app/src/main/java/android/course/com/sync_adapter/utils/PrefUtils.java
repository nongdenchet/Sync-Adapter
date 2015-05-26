package android.course.com.sync_adapter.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by nongdenchet on 4/29/15.
 */
public class PrefUtils {
    public static final String PREFS_NAME = "droids_preferences";
    private static PrefUtils sInstance;
    private SharedPreferences mPrefs;
    private static PrefUtils prefUtils;

    private PrefUtils(Context context) {
        mPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static PrefUtils getInstance(Context context) {
        if (prefUtils == null)
            prefUtils = new PrefUtils(context);
        return prefUtils;
    }

    private static void applyOrCommit(SharedPreferences.Editor editor) {
        if (ApiUtils.isMin(ApiUtils.HONEYCOMB))
            editor.apply();
        else
            editor.commit();
    }

    public void remove(String key) {
        applyOrCommit(mPrefs.edit().remove(key));
    }

    public String get(String key, String defaultValue) {
        return mPrefs.getString(key, defaultValue);
    }

    public boolean get(String key, boolean defaultValue) {
        return mPrefs.getBoolean(key, defaultValue);
    }

    public int get(String key, int defaultValue) {
        return mPrefs.getInt(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return mPrefs.getInt(key, defaultValue);
    }

    public long get(String key, long defaultValue) {
        return mPrefs.getLong(key, defaultValue);
    }

    public float get(String key, float defaultValue) {
        return mPrefs.getFloat(key, defaultValue);
    }

    public void set(String key, boolean value) {
        applyOrCommit(mPrefs.edit().putBoolean(key, value));
    }

    public void set(String key, int value) {
        applyOrCommit(mPrefs.edit().putInt(key, value));
    }

    public void set(String key, float value) {
        applyOrCommit(mPrefs.edit().putFloat(key, value));
    }

    public void set(String key, long value) {
        applyOrCommit(mPrefs.edit().putLong(key, value));
    }

    public void set(String key, String value) {
        applyOrCommit(mPrefs.edit().putString(key, value));
    }

}
