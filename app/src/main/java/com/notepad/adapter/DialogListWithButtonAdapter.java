package com.notepad.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.notepad.R;
import com.notepad.database.Hashtag;
import com.notepad.listener.OnListItemClickListener;

import java.util.ArrayList;

/**
 * Created by TOOKAN © 2017. ALL RIGHTS RESERVED.
 */
public class DialogListWithButtonAdapter extends RecyclerView.Adapter<DialogListWithButtonAdapter.ViewHolder> {
    private Context                 mContext;
    private OnListItemClickListener mListItemClickListener;
    private ArrayList<Hashtag>      mArrayItems;
    private int                     mSelectedPos;

    /**
     * constructor for dialog list adapter
     *
     * @param context               the activity context
     * @param listItemClickListener the click listener for dialog
     * @param arrayItems            the array of list items
     * @param selectedPos           selected position
     */
    public DialogListWithButtonAdapter(final Context context, final OnListItemClickListener listItemClickListener,
                                       final ArrayList<Hashtag> arrayItems, final int selectedPos) {
        this.mContext = context;
        this.mListItemClickListener = listItemClickListener;
        this.mArrayItems = arrayItems;
        this.mSelectedPos = selectedPos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        View listItem = LayoutInflater.from(mContext).inflate(R.layout.item_dialog_list_with_buttons, parent, false);
        return new ViewHolder(listItem);
    }


    @Override
    public int getItemCount() {
        return mArrayItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        viewHolder.tvItemName.setText(mArrayItems.get(position).getTagName());
        if (mSelectedPos == position) {
            viewHolder.ivSelected.setBackgroundResource(R.drawable.ic_icon_filter_check);
        } else {
            viewHolder.ivSelected.setBackgroundResource(R.drawable.ic_icon_filter_uncheck);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mListItemClickListener.onListItemSelected(position, mArrayItems.get(position).getTagId());
            }
        });
    }

    /**
     * set selected position
     *
     * @param selectedPos the provided updated position
     */
    public void setSelectedPosition(final int selectedPos) {
        mSelectedPos = selectedPos;
    }

    /**
     * Class to hold the Views
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView  tvItemName;
        private ImageView ivSelected;

        /**
         * constructor for View holder
         *
         * @param itemView the provided item view
         */
        ViewHolder(final View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.dialog_list_button_tv_name);
            ivSelected = itemView.findViewById(R.id.dialog_list_button_iv_selected);
        }
    }
}
