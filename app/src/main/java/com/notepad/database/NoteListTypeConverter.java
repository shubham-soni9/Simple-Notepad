package com.notepad.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class NoteListTypeConverter {

    @TypeConverter
    public static ArrayList<String> stringToImageList(String data) {
        if (data == null) {
            return new ArrayList<>();
        }
        Type listType = new TypeToken<ArrayList<String>>() {
        }.getType();

        return new Gson().fromJson(data, listType);
    }

    @TypeConverter
    public static String listToImageList(ArrayList<String> imageList) {
        return new Gson().toJson(imageList);
    }

    @TypeConverter
    public static ArrayList<Note> stringToNoteList(String data) {
        if (data == null) {
            return new ArrayList<>();
        }
        Type listType = new TypeToken<ArrayList<Note>>() {
        }.getType();

        return new Gson().fromJson(data, listType);
    }

    @TypeConverter
    public static String listToNoteList(ArrayList<Note> imageList) {
        return new Gson().toJson(imageList);
    }
}
