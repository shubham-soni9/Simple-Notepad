package com.notepad.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.notepad.R;
import com.notepad.appdata.Codes;
import com.notepad.appdata.Constant;

import java.io.File;
import java.util.UUID;

public class Utils {

    public static void setOnClickListener(View.OnClickListener listener, View... views) {

        for (View view : views)
            view.setOnClickListener(listener);
    }

    public static String getUniqueId() {
        return UUID.randomUUID().toString();
    }

    public static boolean isRTL(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return false;
        }
        return context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    public static boolean isEmpty(String mString) {
        return mString == null || mString.isEmpty();
    }

    public static boolean isEmpty(TextView textView) {
        return textView.getText().toString().trim().isEmpty();
    }

    public static boolean isEmpty(EditText editText, String message) {

        if (get(editText).isEmpty()) {
            setErrorOn(editText, message);
            return true;
        }

        return false;
    }

    private static void setErrorOn(EditText editText, String message) {

        editText.requestFocus();

        if (message.trim().equals(Constant.EMPTY_STRING))
            editText.setError(message);
        else
            editText.setError(message);
    }

    public static String get(TextView textView) {
        return textView.getText().toString().trim();
    }

    public static void hideSoftKeyboard(Activity activity) {

        View focusedView = activity.getCurrentFocus();
        if (focusedView == null) return;

        IBinder windowToken = focusedView.getWindowToken();
        if (windowToken == null) return;

        InputMethodManager inputMethodManager = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
    }

    public static void hideSoftKeyboard(Activity activity, View... views) {

        try {

            InputMethodManager inputMethodManager = (InputMethodManager) activity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            for (View view : views) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showSoftKeyboard(Activity activity, View view) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showSoftKeyboard(Activity activity) {

        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String objecttoJson(Object object) {
        if (object == null) return "null";
        try {
            return new Gson().toJson(object);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void snackBar(final Activity activity, final String message, final View view, final int type) {

        if (activity == null) return;

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {
                    Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG).setDuration(2500);
                    View view = snackbar.getView();
                    TextView tv = view.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setMaxLines(3);
                    tv.setGravity(Gravity.CENTER_HORIZONTAL);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    }
                    tv.setTextAppearance(activity, R.style.CustomTextAppearance_Light);
                    tv.setTextColor(ContextCompat.getColor(activity, R.color.white));
                    view.setBackgroundColor(ContextCompat.getColor(activity, type == Codes.SnackBarType.SUCCESS ? R.color.snackbar_bg_color_success : R.color.snackbar_bg_color_error));
                    snackbar.show();


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
    }


    public static void snackBar(Activity activity, String message, int type) {
        try {
            snackBar(activity, message, activity.getWindow().getDecorView().findViewById(android.R.id.content), type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void snackBar(Activity activity, int messageId, int type) {
        snackBar(activity, activity.getString(messageId), type);
    }

    public static void snackBar(Activity activity, String message) {
        try {
            snackBar(activity, message, Codes.SnackBarType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int convertDpToPixels(Context context, float dpValue) {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    /**
     * Method to retrieve the App Directory,
     * where files like logs can be Saved
     *
     * @param type The FileType
     * @return directory corresponding to the FileType
     */
    public static File getDirectory(Activity activity, Constant.FileType type) {

        try {

            String strFolder = activity.getFilesDir() + File.separator + type.directory;

            File folder = new File(strFolder);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            return folder;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String assign(String assignable, String alternative) {

        return assignable == null || assignable.isEmpty() ?
                (alternative == null ? Constant.EMPTY_STRING : alternative) :
                (assignable.equals("null") ? assign(alternative) : assignable);
    }

    public static String assign(String assignable) {

        return assignable == null || assignable.equalsIgnoreCase("[]") || assignable.equals("null") ?
                Constant.EMPTY_STRING : assignable;
    }
}
