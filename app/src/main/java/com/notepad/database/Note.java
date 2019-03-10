package com.notepad.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.notepad.util.AssignUtils;

import java.util.ArrayList;
import java.util.Objects;

@Entity(tableName = "note_table")
public class Note implements Parcelable {

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
    @NonNull
    @PrimaryKey
    private String noteId;
    private String            textData;
    private String            title;
    private int               noteColor;
    private ArrayList<String> imageList;
    private String            tagId;
    private int               favorite;
    private long              lastUpdatedTime;

    public Note() {

    }

    protected Note(Parcel in) {
        noteId = Objects.requireNonNull(in.readString());
        textData = in.readString();
        title = in.readString();
        noteColor = in.readInt();
        imageList = in.createStringArrayList();
        favorite = in.readInt();
        lastUpdatedTime = in.readLong();
        tagId = in.readString();
    }

    public ArrayList<String> getImageList() {
        return imageList == null ? new ArrayList<>() : imageList;
    }

    public void setImageList(ArrayList<String> imageList) {
        this.imageList = imageList;
    }

    public String getTextData() {
        return textData;
    }

    public void setTextData(String textData) {
        this.textData = textData;
    }

    public String getTitle() {
        return AssignUtils.assign(title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNoteColor() {
        return noteColor;
    }

    public void setNoteColor(int noteColor) {
        this.noteColor = noteColor;
    }

    @NonNull
    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void setTag(String tagId) {
        this.tagId = tagId;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(noteId);
        dest.writeString(textData);
        dest.writeString(title);
        dest.writeInt(noteColor);
        dest.writeStringList(imageList);
        dest.writeInt(favorite);
        dest.writeLong(lastUpdatedTime);
        dest.writeString(tagId);
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public long getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(long lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

}
