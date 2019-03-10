package com.notepad.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.notepad.R;
import com.notepad.adapter.ViewImagesPagerAdapter;
import com.notepad.util.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * Developer: Rishabh
 * Dated: 23/07/15.
 */
public class ViewImagesDialog {

    private static final String TAG = ViewImagesDialog.class.getSimpleName();

    private Dialog mDialog;

    /**
     * The instance of the Activity on which the
     * AlertDialog will be displayed
     */
    private Activity activity;
    private Fragment fragment;

    /**
     * The receiver to which the AlertDialog
     * will return the CallBacks
     * <p>
     * Note: listener should be an instance of
     * AlertDialog.Listener
     */
    private Listener listener;

    /**
     * The code to differentiate the various CallBacks
     * from different Dialogs
     */
    private List<String> imagesList  = new ArrayList<>();
    private List<String> captionList = new ArrayList<>();

    /**
     * The data to sent via the Dialog from the
     * remote parts of the Activity to other
     * parts
     */
    private Bundle backpack;

    /**
     * Separates different requests
     */
    private int purpose;

    /**
     * Stores the Position of Current Description
     */
    private int currentItem;

    /**
     * Method to create and display the alert mDialog
     *
     * @return
     */
    private ViewImagesDialog init() {

        try {

            mDialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
            mDialog.setContentView(R.layout.dialog_view_images);

            Window dialogWindow = mDialog.getWindow();
            WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
            layoutParams.dimAmount = 0.6f;
            dialogWindow.getAttributes().windowAnimations = R.style.CustomDialog;
            dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            mDialog.setCancelable(true);
            mDialog.setCanceledOnTouchOutside(true);

            View llBack = mDialog.findViewById(R.id.llBack);
            llBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });


            final LinearLayout llIndicators = mDialog.findViewById(R.id.llIndicators);
            inflateIndicators(currentItem, llIndicators);

            ViewPager vpImages = mDialog.findViewById(R.id.vpImages);
            ViewImagesPagerAdapter viewImagesAdapter = new ViewImagesPagerAdapter(activity, imagesList, captionList);
            vpImages.setAdapter(viewImagesAdapter);
            vpImages.setCurrentItem(currentItem);
            llIndicators.setVisibility(imagesList.size() < 2 ? View.GONE : View.VISIBLE);
            vpImages.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    inflateIndicators(position, llIndicators);
                    currentItem = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Method to inflate the indicator for the
     */
    private void inflateIndicators(int position, LinearLayout llIndicators) {

        llIndicators.removeAllViews();

        int pixels = Utils.convertDpToPixels(activity, 8);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(pixels, pixels);

        for (int index = 0; index < imagesList.size(); index++) {
            View view = new View(activity);
            view.setLayoutParams(params);
            view.setBackgroundResource(index == position ? R.drawable.switcher_filled : R.drawable.switcher);
            llIndicators.addView(view);
        }
    }

    /**
     * Method to init the initialized mDialog
     */
    public void show() {

        // Check if activity lives
        if (activity != null)
            // Check if mDialog contains data
            if (mDialog != null) {
                try {
                    // Show the Dialog
                    mDialog.show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
    }

    /**
     * Method to dismiss the AlertDialog, if possible
     */
    private void dismiss() {

        // Check if activity lives
        if (activity != null)
            // Check if the Dialog is null
            if (mDialog != null)
                // Check whether the mDialog is visible
                if (mDialog.isShowing()) {
                    try {
                        // Dismiss the Dialog
                        mDialog.dismiss();
                        mDialog = null;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
    }


    /**
     * Interfaces the events from the AlertDialog
     * to the Calling Context
     */
    private interface Listener {

        /**
         * Override this method to listen to
         * the events fired from AlertDialog
         *
         * @param purpose
         * @param backpack
         */
        void onBackPressed(int purpose, Bundle backpack);
    }

    /**
     * Class to help building the Alert Dialog
     */
    public static class Builder {


        private ViewImagesDialog viewImagesDialog = new ViewImagesDialog();

        /**
         * Constructor to build a mDialog for Activity
         *
         * @param activity
         */
        public Builder(Activity activity) {

            viewImagesDialog.activity = activity;
//            viewImagesDialog.task=activity.getCurrentTask();

            if (activity instanceof Listener)
                viewImagesDialog.listener = (Listener) activity;
        }

        /**
         * Constructor to build a mDialog for Fragment
         *
         * @param fragment
         */
        public Builder(Fragment fragment) {

            viewImagesDialog.activity = fragment.getActivity();
            viewImagesDialog.fragment = fragment;
//            viewImagesDialog.task=viewImagesDialog.activity.getCurrentTask();

            if (fragment instanceof Listener)
                viewImagesDialog.listener = (Listener) fragment;
        }


        /**
         * Sets the a unique purpose code to differentiate
         * between the CallBacks
         *
         * @param purpose
         * @return
         */
        public Builder purpose(int purpose) {
            viewImagesDialog.purpose = purpose;
            return this;
        }

        /**
         * The position of the image to be displayed
         *
         * @param currentItem
         * @return
         */
        public Builder position(int currentItem) {
            viewImagesDialog.currentItem = currentItem;
            return this;
        }

        /**
         * Sets the a custom listener to receive the CallBacks
         *
         * @param listener
         * @return
         */
        public Builder listener(Listener listener) {
            viewImagesDialog.listener = listener;
            return this;
        }

        /**
         * Set the data to be sent via callback
         *
         * @param backpack
         * @return
         */
        public Builder backpack(Bundle backpack) {
            viewImagesDialog.backpack = backpack;
            return this;
        }

        /**
         * Method to build an AlertDialog and display
         * it on the screen. The instance returned can
         * be used to manipulate the mDialog in future.
         *
         * @return
         */
        public ViewImagesDialog build() {

            return viewImagesDialog.init();
        }

        public Builder images(List<String> imagesList) {
            viewImagesDialog.imagesList = imagesList;
            return this;
        }

        public Builder captions(String caption) {
            ArrayList<String> mCaptionList = new ArrayList<>();
            mCaptionList.add(caption);
            viewImagesDialog.captionList = mCaptionList;
            return this;
        }
    }

}


