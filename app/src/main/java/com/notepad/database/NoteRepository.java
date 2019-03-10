package com.notepad.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.notepad.util.Log;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

class NoteRepository {
    private final String                  TAG = NoteRepository.class.getName();
    private       RoomDao                 mWordDao;
    private       LiveData<List<Note>>    mAllNotes;
    private       LiveData<List<Note>>    mFavoriteNotes;
    private       LiveData<List<Hashtag>> mTagList;

    NoteRepository(Application application) {
        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(application);
        mWordDao = db.noteDao();
        mAllNotes = mWordDao.getAllNotes();
        mFavoriteNotes = mWordDao.getFavoriteNote();
        mTagList = mWordDao.getAllTags();
    }

    LiveData<List<Note>> getAllNotes() {
        return mAllNotes;
    }

    LiveData<List<Note>> getFavoriteNotes() {
        return mFavoriteNotes;
    }

    LiveData<List<Hashtag>> getAllTags() {
        return mTagList;
    }

    Single<Note> getNoteById(String noteId) {
        return mWordDao.getNoteById(noteId);
    }

    void insertNote(Note note) {
        Completable.fromAction(() -> mWordDao.insert(note)).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "Note Data Inserted");
            }

            @Override
            public void onError(Throwable e) {
            }
        });
    }

    void insertTags(Hashtag... hashtag) {
        Completable.fromAction(() -> mWordDao.insert(hashtag)).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "Hashtag Data Inserted");
            }

            @Override
            public void onError(Throwable e) {
            }
        });
    }

}
