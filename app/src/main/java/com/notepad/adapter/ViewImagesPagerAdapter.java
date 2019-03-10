package com.notepad.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.notepad.R;
import com.notepad.appdata.Constant;
import com.notepad.util.Log;
import com.notepad.util.Utils;

import java.io.File;
import java.util.List;

/**
 * Created by TOOKAN © 2017. ALL RIGHTS RESERVED.
 */
public class ViewImagesPagerAdapter extends PagerAdapter {
    private final List<String>   captionList;
    private final String         TAG = ViewImagesPagerAdapter.class.getName();
    private       LayoutInflater layoutInflater;
    private       Activity       mActivity;
    private       List<String>   mImagesList;

    /**
     * constructor for view images pager adapter
     *
     * @param activity    the provided activity
     * @param imagesList  the provided list of images
     * @param captionList the provided list of captions
     */
    public ViewImagesPagerAdapter(final Activity activity, final List<String> imagesList, final List<String> captionList) {
        this.mActivity = activity;
        this.mImagesList = imagesList;
        this.captionList = captionList;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        View customFieldImageView = layoutInflater.inflate(R.layout.list_item_custom_field_image, container, false);
        String imageListItem = getImagePath(mImagesList.get(position));
        // View to display the Image
        final ImageView imgSnapshot = customFieldImageView.findViewById(R.id.imgSnapshot);
        // To be used for Telling that we are Loading
        final TextView tvInformation = customFieldImageView.findViewById(R.id.tvInformation);
        tvInformation.setVisibility(View.VISIBLE);
        final TextView tvCaption = customFieldImageView.findViewById(R.id.tvCaptions);
        if (captionList != null && !captionList.isEmpty() && captionList.size() > position
                && !captionList.get(position).isEmpty()) {
            tvCaption.setVisibility(View.VISIBLE);
            tvCaption.setText(captionList.get(position));
        } else {
            tvCaption.setVisibility(View.GONE);
        }

        if (Utils.isEmpty(imageListItem)) {
            tvInformation.setText(R.string.could_not_read_from_source_text);
        } else {
            RequestListener requestListener = new RequestListener() {
                @Override
                public boolean onLoadFailed(@Nullable final GlideException e, final Object model, final Target target,
                                            final boolean isFirstResource) {
                    tvInformation.setText(mActivity.getString(R.string.internet_connectivity_issue_text));
                    return false;
                }

                @Override
                public boolean onResourceReady(final Object resource, final Object model, final Target target,
                                               final DataSource dataSource, final boolean isFirstResource) {
                    tvInformation.setVisibility(View.GONE);
                    return false;
                }
            };
            Glide.with(imgSnapshot).asBitmap().load(imageListItem).into(imgSnapshot);
        }
        // Add the View to container
        container.addView(customFieldImageView);
        return customFieldImageView;
    }

    @Override
    public int getCount() {
        return mImagesList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull final View view, @NonNull final Object object) {
        return object == view;
    }

    @Override
    public void destroyItem(@NonNull final ViewGroup container, final int position, @NonNull final Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NonNull final Object object) {
        return POSITION_NONE;
    }

    private String getImagePath(String imageName) {
        Log.e(TAG, "Imagename :: " + imageName);
        String filePath = new File(Utils.getDirectory(mActivity, Constant.FileType.ATTACHMENT), imageName).getAbsolutePath();
        Log.e(TAG, "Image Path ::" + filePath);
        return filePath;
    }
}
