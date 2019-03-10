package com.notepad.util;


import com.notepad.BuildConfig;

/**
 * Custom log class overrides Android Log (android.util.Log)
 * <p>
 * Created by ramangoyal on 09/04/15.
 */
public class Log {

    private static final boolean PRINT = BuildConfig.DEBUG; // true for printing and false
    // for not

    public Log() {
    }

    public static void i(String tag, String message) {
        if (PRINT) {
            android.util.Log.i(tag, message);
        }
    }

    public static void d(String tag, String message) {
        if (PRINT) {
            android.util.Log.d(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (PRINT) {
            android.util.Log.e(tag, message);
        }
    }

    public static void v(String tag, String message) {
        if (PRINT) {
            android.util.Log.v(tag, message);
        }
    }

    public static void w(String tag, String message) {
        if (PRINT) {
            android.util.Log.w(tag, message);
        }
    }
}