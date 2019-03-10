package com.notepad.util;

import android.widget.TextView;

import com.notepad.appdata.Constant;

public class AssignUtils {
    public static String assign(String assignable) {
        return assignable == null ? Constant.EMPTY_STRING : assignable;
    }

    public static String get(TextView textView) {
        return textView.getText().toString().trim();
    }
}
