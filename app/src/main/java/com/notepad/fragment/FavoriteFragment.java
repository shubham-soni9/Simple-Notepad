package com.notepad.fragment;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.notepad.AddNotesActivity;
import com.notepad.R;
import com.notepad.Transition;
import com.notepad.adapter.NotesAdapter;
import com.notepad.appdata.Codes;
import com.notepad.database.Note;
import com.notepad.database.NoteViewModel;
import com.notepad.structure.BaseFragment;
import com.notepad.util.Utils;

import java.util.List;

public class FavoriteFragment extends BaseFragment implements NotesAdapter.OnItemListener {
    private RecyclerView  rvNotes;
    private Context       context;
    private NoteViewModel noteViewModel;
    private NotesAdapter  notesAdapter;

    public static FavoriteFragment newInstance() {
        Bundle args = new Bundle();
        FavoriteFragment fragment = new FavoriteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_note, container, false);
        initViews(rootView);
        noteViewModel.getFavoriteNotes().observe(this, this::setAdapter);
        return rootView;
    }

    private void setAdapter(List<Note> notesList) {
        rvNotes.setLayoutManager(new GridLayoutManager(context, 2));
        Utils.objecttoJson(notesList);
        rvNotes.setLayoutManager(new GridLayoutManager(context, 2));
        Utils.objecttoJson(notesList);
        if (notesAdapter == null) {
            notesAdapter = new NotesAdapter(this, notesList);
            rvNotes.setAdapter(notesAdapter);
        } else {
            notesAdapter.refreshAdapterSet(notesList);
        }
    }

    private void initViews(ViewGroup rootView) {
        rvNotes = rootView.findViewById(R.id.rvNotes);
    }

    @Override
    public void onItemSelected(Note note) {
        Bundle bundle = new Bundle();
        bundle.putString(Note.class.getName(), note.getNoteId());
        Transition.transitForResult((Activity) context, AddNotesActivity.class, Codes.Request.OPEN_ADD_NOTES_ACTIVITY, bundle);
    }

    @Override
    public void onFavoriteUpdated(Note note) {
        note.setFavorite(note.getFavorite() == 1 ? 0 : 1);
        noteViewModel.insert(note);
    }
}
