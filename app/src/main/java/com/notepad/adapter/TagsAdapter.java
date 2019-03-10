package com.notepad.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.notepad.R;
import com.notepad.database.Hashtag;

import java.util.ArrayList;
import java.util.List;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> {
    private List<Hashtag>         tagsList;
    private Context               context;
    private OnTagSelectedListener listener;

    public TagsAdapter(OnTagSelectedListener listener, List<Hashtag> tagsList) {
        this.listener = listener;
        this.tagsList = tagsList == null ? new ArrayList<>() : tagsList;
    }

    @NonNull
    @Override
    public TagsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_tag, parent, false);
        return new TagsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagsAdapter.ViewHolder holder, final int pos) {
        final int position = holder.getAdapterPosition();
        final Hashtag hashtag = tagsList.get(position);
        holder.tvTagTitle.setText(hashtag.getTagName());
        holder.tvNoteCount.setText(String.valueOf(hashtag.getNotesCount()));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hashtag.getNotesCount()>0){
                listener.onTagSelected(hashtag);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tagsList.size();
    }

    public void refreshAdapterSet(List<Hashtag> tagsList) {
        this.tagsList = tagsList;
        notifyDataSetChanged();
    }

    public interface OnTagSelectedListener {
        void onTagSelected(Hashtag hashtag);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View              parentLayout;
        private AppCompatTextView tvTagTitle;
        private TextView tvNoteCount;

        ViewHolder(View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.parentLayout);
            tvTagTitle = itemView.findViewById(R.id.tvTagTitle);
            tvNoteCount=itemView.findViewById(R.id.tvNoteCount);
        }
    }
}
