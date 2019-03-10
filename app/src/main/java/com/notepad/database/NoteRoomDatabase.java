package com.notepad.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@TypeConverters({NoteListTypeConverter.class})
@Database(entities = {Note.class, Hashtag.class}, version = 1)
public abstract -class NoteRoomDatabase extends RoomDatabase {
    private static volatile NoteRoomDatabase INSTANCE;

    static NoteRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (NoteRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), NoteRoomDatabase.class, "word_database").build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract RoomDao noteDao();
}
