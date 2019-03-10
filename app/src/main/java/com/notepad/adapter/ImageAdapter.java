package com.notepad.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.notepad.R;
import com.notepad.appdata.Constant;
import com.notepad.dialog.ViewImagesDialog;
import com.notepad.util.Log;
import com.notepad.util.Utils;

import java.io.File;
import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private final String            TAG = ImageAdapter.class.getName();
    private       ArrayList<String> imageList;
    private       Activity          activity;

    public ImageAdapter(Activity activity, ArrayList<String> imageList) {
        if (imageList == null) {
            this.imageList = new ArrayList<>();
        } else {
            this.imageList = imageList;
        }
        this.activity = activity;
    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, final int pos) {
        final int position = holder.getAdapterPosition();
        Glide.with(holder.ivImage).asBitmap().load(getImagePath(imageList.get(position))).into(holder.ivImage);
        holder.parentLayout.setOnClickListener(v -> new ViewImagesDialog.Builder(activity).images(imageList).position(position).build().show());
    }

    private String getImagePath(String imageName) {
        Log.e(TAG, "Imagename :: " + imageName);
        String filePath = new File(Utils.getDirectory(activity, Constant.FileType.ATTACHMENT), imageName).getAbsolutePath();
        Log.e(TAG, "Image Path ::" + filePath);
        return filePath;
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public void refreshAdapterSet(ArrayList<String> imageList) {
        this.imageList = imageList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View      parentLayout;
        private ImageView ivImage;

        ViewHolder(View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.parentLayout);
            ivImage = itemView.findViewById(R.id.ivImage);
        }
    }
}
