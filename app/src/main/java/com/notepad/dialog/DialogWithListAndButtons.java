package com.notepad.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.notepad.R;
import com.notepad.adapter.DialogListWithButtonAdapter;
import com.notepad.database.Hashtag;
import com.notepad.listener.OnListItemClickListener;

import java.util.ArrayList;

/**
 * Created by TOOKAN © 2018. ALL RIGHTS RESERVED.
 */
public class DialogWithListAndButtons implements OnListItemClickListener {
    private static final float DIM_AMOUNT = 0.6f;

    //ui params
    private RecyclerView rvListItems;
    private Dialog       mDialog;

    //general params
    private int                         mSelectedPos       = -1;
    private String                      mSelectedTagId     = "";
    private Context                     mContext;
    private OnListItemClickListener     mListItemClickListener;
    private boolean                     mIsButtonsRequired = false;
    private DialogListWithButtonAdapter mDialogListAdapter;

    private DialogWithListAndButtons(final Context context) {
        this.mContext = context;
    }

    /**
     * @param context the provided context
     */
    public static DialogWithListAndButtons with(final Context context) {
        return new DialogWithListAndButtons(context);
    }


    /**
     * show dialog with buttons
     *
     * @param header
     * @param listItems
     * @param isButtonsRequired
     * @param listener
     */
    public void show(final String header, final ArrayList<Hashtag> listItems, final boolean isButtonsRequired,
                     final OnListItemClickListener listener, final String selectTagId) {
        try {
            this.mListItemClickListener = listener;
            this.mIsButtonsRequired = isButtonsRequired;

            mDialog = new Dialog(mContext);
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mDialog.setContentView(R.layout.dialog_with_list_and_buttons);
            WindowManager.LayoutParams layoutParams = mDialog.getWindow().getAttributes();
            layoutParams.dimAmount = DIM_AMOUNT;
            mDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            mDialog.setCancelable(true);
            mDialog.setCanceledOnTouchOutside(true);

            TextView tvHeader = mDialog.findViewById(R.id.dialog_buttons_tv_header);
            LinearLayout llButtons = mDialog.findViewById(R.id.dialog_buttons_ll_buttons);
            Button btnCancel = mDialog.findViewById(R.id.dialog_buttons_btn_cancel);
            Button btnOk = mDialog.findViewById(R.id.dialog_buttons_btn_ok);
            RecyclerView rvListItems = mDialog.findViewById(R.id.dialog_buttons_rv_list);

            rvListItems.setItemAnimator(new DefaultItemAnimator());
            rvListItems.setLayoutManager(new LinearLayoutManager(mContext));

            if (header == null || header.isEmpty()) {
                tvHeader.setVisibility(View.GONE);
            } else {
                tvHeader.setText(header);
            }

            if (isButtonsRequired) {
                llButtons.setVisibility(View.VISIBLE);
            } else {
                llButtons.setVisibility(View.GONE);
            }
            for (int i = 0; i < listItems.size(); i++) {
                Hashtag hashtag = listItems.get(i);
                if (hashtag.getTagId().equalsIgnoreCase(selectTagId)) {
                    mSelectedTagId = selectTagId;
                    mSelectedPos = i;
                }
            }
            btnCancel.setOnClickListener(view -> mDialog.dismiss());

            btnOk.setOnClickListener(view -> {
                if (mSelectedPos != -1) {
                    mDialog.dismiss();
                    mListItemClickListener.onListItemSelected(mSelectedPos, mSelectedTagId);
                }
            });

            // ListView
            mDialogListAdapter = new DialogListWithButtonAdapter(mContext, this, listItems, mSelectedPos);
            rvListItems.setAdapter(mDialogListAdapter);
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onListItemSelected(final int clickedPosition, final String clickedPositionText) {
        if (mIsButtonsRequired) {
            mSelectedPos = clickedPosition;
            mSelectedTagId = clickedPositionText;
            mDialogListAdapter.setSelectedPosition(clickedPosition);
            mDialogListAdapter.notifyDataSetChanged();
        } else {
            mDialog.dismiss();
            mListItemClickListener.onListItemSelected(clickedPosition, clickedPositionText);
        }
    }
}
