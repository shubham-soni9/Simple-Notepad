package com.notepad.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface RoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Note note);

    @Query("SELECT * from note_table ORDER BY lastUpdatedTime DESC")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM note_table WHERE noteId IN(:noteId)")
    Single<Note> getNoteById(String noteId);

    @Query("SELECT * from note_table WHERE favorite = 1 ORDER BY lastUpdatedTime DESC")
    LiveData<List<Note>> getFavoriteNote();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Hashtag... hashtag);

    @Query("SELECT * from tag_table Order By tagName ASC")
    LiveData<List<Hashtag>> getAllTags();

}
