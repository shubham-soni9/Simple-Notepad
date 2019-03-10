package com.notepad.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.notepad.R;
import com.notepad.appdata.Constant;
import com.notepad.util.Log;
import com.notepad.util.Utils;

/**
 * Class that holds ready to use dialogs
 *
 * @author Rishabh
 */
public class TagInputDialog {

    public static final String MESSAGE = "message";

    private final String TAG = TagInputDialog.class.getSimpleName();

    /**
     * Dialog that holds the View
     */
    private Dialog dialog;

    /*
     * The context which would listen to the Events
     * of the TagInputDialog (must be an implementation of
     * Listener)
     */
    private Listener listener;

    /*
     * The activity on which the AlertDialog would be displayed
     */
    private Activity activity;

    /*
     * A unique purpose code to identify the action for which
     * the Dialog is Created
     */
    private int purposeCode;

    // The message for the Options Dialog
    private String message;

    // The hint to be conveyed to the User
    private String hint;

    // The hint to be conveyed to the User
    private String errorMessage;

    // The text for Ok button
    private String positiveButton;

    // The text for Cancel button
    private String negativeButton;

    private Bundle backpack;

    /**
     * Method to showOn a multi-event Dialog
     */
    private TagInputDialog init() {

        try {

            dialog = new Dialog(activity);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_text_input);
            WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
            layoutParams.dimAmount = 0.6f;
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            // Initialize all the Views
            final EditText etMessage = dialog.findViewById(R.id.etMessage);
            TextView btnOk = dialog.findViewById(R.id.btnOk);
            TextView btnCancel = dialog.findViewById(R.id.btnCancel);

            // Set text to the Views
            etMessage.setHint(hint);
            etMessage.setText(message);
            btnOk.setText(positiveButton);
            btnCancel.setText(negativeButton);

            btnOk.setOnClickListener(view -> {

                if (Utils.isEmpty(etMessage, errorMessage)) return;
                Utils.hideSoftKeyboard(activity,etMessage);
                dialog.dismiss();

                if (listener != null) {
                    // Create a new backpack if null
                    if (backpack == null) backpack = new Bundle();

                    // put the Message entered by user into backpack
                    backpack.putString(MESSAGE, "#" + Utils.get(etMessage));
                    // notify the Listener
                    listener.performPositiveAction(purposeCode, backpack);
                }
            });

            btnCancel.setOnClickListener(view -> {
                Utils.hideSoftKeyboard(activity,etMessage);
                dialog.dismiss();

                if (listener != null)
                    listener.performNegativeAction(purposeCode, backpack);
            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "init: " + e.getMessage());
        }

        return this;
    }

    /**
     * Method to init the initialized dialog
     */
    public void show() {

        // Check if activity lives
        if (activity != null)
            // Check if dialog contains data
            if (dialog != null)
                // Show the Dialog
                dialog.show();
    }

    /**
     * Listener to listen to the events when a main_button
     * on the Options Dialog was pressed
     * <p>
     * Developer: Rishabh
     * Dated: 19/05/15.
     */
    public interface Listener {

        /**
         * Override this method to perform operations
         * after OK button was pressed
         *
         * @param purpose
         */
        void performPositiveAction(int purpose, Bundle backpack);


        /**
         * Override this method to perform operations
         * after CANCEL button was pressed
         *
         * @param purpose
         */
        void performNegativeAction(int purpose, Bundle backpack);
    }

    /**
     * Class to build the OptionsDialog
     */
    public static class Builder {

        private TagInputDialog tagInputDialog = new TagInputDialog();


        /**
         * Constructor to initialize the OptionsDialog
         * (for an Activity)
         *
         * @param activity
         */
        public Builder(Activity activity) {

            tagInputDialog.activity = activity;

            if (activity instanceof Listener)
                tagInputDialog.listener = (Listener) activity;
        }

        /**
         * Constructor to initialize the OptionsDialog
         * (for a Fragment)
         *
         * @param fragment
         */
        public Builder(Fragment fragment) {

            tagInputDialog.activity = fragment.getActivity();

            if (fragment instanceof Listener)
                tagInputDialog.listener = (Listener) fragment;
        }

        /**
         * Method to set purpose code to identify the Dialog
         *
         * @param purposeCode
         * @return
         */
        public Builder purpose(int purposeCode) {

            tagInputDialog.purposeCode = purposeCode;
            return this;
        }

        /**
         * Method to set purpose code to identify the Dialog
         *
         * @param errorMessage
         * @return
         */
        public Builder errorMessage(String errorMessage) {

            tagInputDialog.errorMessage = errorMessage;
            return this;
        }

        /**
         * Method to set a custom listener to listen
         * the events from OptionsDialog
         *
         * @param listener
         * @return
         */
        public Builder listener(Listener listener) {

            tagInputDialog.listener = listener;
            return this;
        }

        /**
         * Method to set a backpack containing the data for
         * the intended action
         *
         * @param backpack
         * @return
         */
        public Builder backpack(Bundle backpack) {

            tagInputDialog.backpack = backpack;
            return this;
        }

        /**
         * Method to set title to the Title
         *
         * @param hint
         * @return
         */
        public Builder hint(String hint) {

            tagInputDialog.hint = hint;
            return this;
        }

        /**
         * Method to set message to the Dialog
         *
         * @param message
         * @return
         */
        public Builder message(String message) {

            tagInputDialog.message = message;
            return this;
        }

        /**
         * Method to set text to the Positive Button
         *
         * @param buttonText
         * @return
         */
        public Builder positiveButton(String buttonText) {

            tagInputDialog.positiveButton = buttonText;
            return this;
        }

        /**
         * Method to set Text to the Negative Button
         *
         * @param cancel
         * @return
         */
        public Builder negativeButton(String cancel) {

            tagInputDialog.negativeButton = cancel;
            return this;
        }

        /**
         * Method to init the Options Dialog
         *
         * @return
         */
        public TagInputDialog build() {

            tagInputDialog.hint = Utils.assign(tagInputDialog.hint, tagInputDialog.activity.getString(R.string.add_text_here_text));
            tagInputDialog.message = Utils.assign(tagInputDialog.message, Constant.EMPTY_STRING);
            tagInputDialog.errorMessage = Utils.assign(tagInputDialog.errorMessage, tagInputDialog.activity.getString(R.string.field_cannot_be_left_blank));
            tagInputDialog.positiveButton = Utils.assign(tagInputDialog.positiveButton, tagInputDialog.activity.getString(R.string.ok_text));
            tagInputDialog.negativeButton = Utils.assign(tagInputDialog.negativeButton, tagInputDialog.activity.getString(R.string.cancel_text));

            return tagInputDialog.init();
        }

        /**
         * Method to retrieve a String from the resources
         *
         * @param resourceId
         * @return
         */
        private String getString(int resourceId) {
            return tagInputDialog.activity.getString(resourceId);
        }
    }
}
