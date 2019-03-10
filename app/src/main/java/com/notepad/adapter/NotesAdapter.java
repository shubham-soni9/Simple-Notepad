package com.notepad.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.notepad.R;
import com.notepad.database.Note;
import com.notepad.util.Utils;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
    private List<Note>     notesList;
    private OnItemListener onItemListener;
    private Context        context;

    public NotesAdapter(OnItemListener onItemListener, List<Note> notesList) {
        this.notesList = notesList;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new NotesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        position = holder.getAdapterPosition();
        Note note = notesList.get(position);
        holder.tvTextData.setText(note.getTextData());
        if (Utils.isEmpty(note.getTitle())) {
            holder.tvTitle.setVisibility(View.GONE);
        } else {
            holder.tvTitle.setVisibility(View.VISIBLE);
            holder.tvTitle.setText(note.getTitle());
        }
        if (note.getNoteColor() != 0) {
            holder.parentLayout.setBackgroundColor(note.getNoteColor());
        } else {
            holder.parentLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }
        if (note.getFavorite() == 1) {
            holder.ivFavorite.getDrawable().setColorFilter(ContextCompat.getColor(context, R.color.ic_favorite_color_selected), PorterDuff.Mode.SRC_IN);
        } else {
            holder.ivFavorite.getDrawable().setColorFilter(ContextCompat.getColor(context, R.color.ic_favorite_color_unselected), PorterDuff.Mode.SRC_IN);
        }
        holder.ivFavorite.setOnClickListener(v -> onItemListener.onFavoriteUpdated(note));
        holder.dataLayout.setOnClickListener(v -> onItemListener.onItemSelected(note));
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public void refreshAdapterSet(List<Note> notesList) {
        this.notesList = notesList;
        notifyDataSetChanged();
    }

    public interface OnItemListener {
        void onItemSelected(Note note);

        void onFavoriteUpdated(Note note);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView           tvTextData;
        private View               dataLayout;
        private TextView           tvTitle;
        private RecyclerView       rvImageList;
        private AppCompatImageView ivFavorite;
        private View               parentLayout;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTextData = itemView.findViewById(R.id.tvTextData);
            dataLayout = itemView.findViewById(R.id.dataLayout);
            rvImageList = itemView.findViewById(R.id.rvImageList);
            ivFavorite = itemView.findViewById(R.id.ivFavorite);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }
    }
}
