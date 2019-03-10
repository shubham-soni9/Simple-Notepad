package com.notepad.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Single;

public class NoteViewModel extends AndroidViewModel {
    private NoteRepository          noteRepository;
    private LiveData<List<Note>>    mAllNotes;
    private LiveData<List<Note>>    mFavoriteNotes;
    private LiveData<List<Hashtag>> mAllTags;

    public NoteViewModel(Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
        mAllNotes = noteRepository.getAllNotes();
        mFavoriteNotes = noteRepository.getFavoriteNotes();
        mAllTags = noteRepository.getAllTags();
    }

    public LiveData<List<Note>> getAllNotes() {
        return mAllNotes;
    }

    public LiveData<List<Note>> getFavoriteNotes() {
        return mFavoriteNotes;
    }

    public LiveData<List<Hashtag>> getAllTags() {
        return mAllTags;
    }

    public void insert(Note note) {
        noteRepository.insertNote(note);
    }

    public void insertAllTags(Hashtag... hashtag) {
        noteRepository.insertTags(hashtag);
    }


    public Single<Note> getNoteById(String noteId) {
        return noteRepository.getNoteById(noteId);
    }
}
