package com.notepad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.notepad.util.Utils;


public class Transition {

    /**
     * Jump to back to an {@link Activity} followed by Animation when
     * <code>isBack</code> is true
     *
     * @param fromContext Current {@link Activity}
     * @param toClass     Intended {@link Activity}
     * @param isBack      <code>true</code>, if firing a backevent
     */
    public static void transit(Activity fromContext, Class<?> toClass, boolean isBack, Bundle extras) {

        Intent intention = new Intent(fromContext, toClass);

        if (extras != null)
            intention.putExtras(extras);

        fromContext.startActivity(intention);

        fromContext.finish();

        if (isBack) fromContext.overridePendingTransition(R.anim.left_in, R.anim.right_out);
        else fromContext.overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    /**
     * Jump to back to an {@link Activity} followed by Animation when
     * <code>isBack</code> is true
     *
     * @param fromContext Current {@link Activity}
     * @param toClass     Intended {@link Activity}
     * @param isBack      <code>true</code>, if firing a backevent
     */
    public static void transit(Activity fromContext, Class<?> toClass,
                               boolean isBack) {

        transit(fromContext, toClass, isBack, null);
    }

    /**
     * Jump forward to an activity
     *
     * @param fromContext Current {@link Activity}
     * @param toClass     Intended {@link Activity}
     */
    public static void transit(Activity fromContext, Class<?> toClass) {

        transit(fromContext, toClass, false, null);
    }

    /**
     * Jump forward to an activity
     *
     * @param fromContext Current {@link Activity}
     * @param toClass     Intended {@link Activity}
     * @param extra       The extras {@link String} parameter to be transmit
     */
    public static void transit(Activity fromContext, Class<?> toClass,
                               String extra) {

        Bundle extras = new Bundle();
        extras.putBoolean(extra, true);

        transit(fromContext, toClass, false, extras);
    }

    /**
     * Jump forward to an activity
     *
     * @param fromContext Current {@link Activity}
     * @param toClass     Intended {@link Activity}
     * @param extras      The extras {@link String} parameter to be transmit
     */
    public static void transit(Activity fromContext, Class<?> toClass, Bundle extras) {

        transit(fromContext, toClass, false, extras);
    }

    /**
     * Transit forward to an Activity with some data,leaving current one alive
     *
     * @param fromContext current activity
     * @param toClass     the intended activity
     * @param requestCode the request code to look up
     * @param extras      the data to be tunneled towards the intended activity
     */
    public static void transitForResult(Activity fromContext, Class<?> toClass,
                                        int requestCode, Bundle extras) {
        transitForResult(fromContext, toClass, requestCode, extras, true);
    }

    public static void transitForResult(Activity fromContext, Class<?> toClass,
                                        int requestCode, Bundle extras, boolean animation) {

        Intent intention = new Intent(fromContext, toClass);

        if (extras != null)
            intention.putExtras(extras);

        fromContext.startActivityForResult(intention, requestCode);
        if (animation) {
            fromContext.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
    }

    /**
     * Exits the current activity
     *
     * @param fromContext the activity to be finished
     */
    public static void exit(Activity fromContext) {

        fromContext.finish();
        fromContext.overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    public static void exit(Activity fromContext, boolean isBack) {
        fromContext.finish();
        if (isBack) {
            if (Utils.isRTL(fromContext)) {
                fromContext.overridePendingTransition(R.anim.right_in, R.anim.left_out);
            } else {
                fromContext.overridePendingTransition(R.anim.left_in, R.anim.right_out);
            }
        } else {
            if (Utils.isRTL(fromContext)) {
                fromContext.overridePendingTransition(R.anim.left_in, R.anim.right_out);
            } else {
                fromContext.overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        }
    }

    public static void startActivity(Activity fromContext, Class<?> toClass) {
        startActivity(fromContext, toClass, null);
    }

    private static void startActivity(Activity fromContext, Class<?> toClass, Bundle bundle) {
        Intent intention = new Intent(fromContext, toClass);
        if (bundle != null) {
            intention.putExtras(bundle);
        }
        fromContext.startActivity(intention);
        fromContext.overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
}
