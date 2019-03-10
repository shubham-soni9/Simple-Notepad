package com.notepad.appdata;

import android.content.Context;

import com.notepad.util.Prefs;

import static com.notepad.appdata.Keys.Prefs.IS_FIRST_LOGIN;

public class Dependencies {

    public static boolean isFirstTime(Context context) {
        return Prefs.with(context).getBoolean(IS_FIRST_LOGIN, true);
    }

    public static void setFirstTime(Context context, boolean isFirstTime) {
        Prefs.with(context).save(IS_FIRST_LOGIN, isFirstTime);
    }
}
