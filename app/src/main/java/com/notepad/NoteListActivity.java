package com.notepad;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.notepad.adapter.NotesAdapter;
import com.notepad.appdata.Codes;
import com.notepad.appdata.Keys;
import com.notepad.database.Hashtag;
import com.notepad.database.Note;
import com.notepad.database.NoteViewModel;
import com.notepad.structure.BaseActivity;
import com.notepad.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class NoteListActivity extends BaseActivity implements NotesAdapter.OnItemListener, View.OnClickListener {
    private RecyclerView  rvNotes;
    private NoteViewModel noteViewModel;
    private NotesAdapter  notesAdapter;
    private Hashtag       hashtag;
    private TextView      tvHashTagName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        init();
    }

    private void init() {
        rvNotes = findViewById(R.id.rvNotes);
        tvHashTagName = findViewById(R.id.tvHashTagName);
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        hashtag = getIntent().getExtras().getParcelable(Keys.Extras.HASH_TAG);
        if (hashtag != null) {
            tvHashTagName.setText(hashtag.getTagName());
        }
        noteViewModel.getAllNotes().observe(this, this::setAdapter);
        Utils.setOnClickListener(this, findViewById(R.id.iv_add_note_back));
    }

    private void setAdapter(List<Note> notesList) {
        ArrayList<Note> filterNoteList = new ArrayList<>();
        for (Note note : notesList) {
            if (!Utils.isEmpty(note.getTagId())) {
                if (note.getTagId().equalsIgnoreCase(hashtag.getTagId())) {
                    filterNoteList.add(note);
                }
            }
        }
        rvNotes.setLayoutManager(new GridLayoutManager(this, 2));
        if (notesAdapter == null) {
            notesAdapter = new NotesAdapter(this, filterNoteList);
            rvNotes.setAdapter(notesAdapter);
        } else {
            notesAdapter.refreshAdapterSet(filterNoteList);
        }
    }

    @Override
    public void onItemSelected(Note note) {
        Bundle bundle = new Bundle();
        bundle.putString(Note.class.getName(), note.getNoteId());
        Transition.transitForResult(this, AddNotesActivity.class, Codes.Request.OPEN_ADD_NOTES_ACTIVITY, bundle);
    }

    @Override
    public void onFavoriteUpdated(Note note) {
        note.setFavorite(note.getFavorite() == 1 ? 0 : 1);
        noteViewModel.insert(note);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add_note_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Transition.exit(this);
    }
}
