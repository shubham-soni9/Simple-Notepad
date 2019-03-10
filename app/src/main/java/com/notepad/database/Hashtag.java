package com.notepad.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Objects;

@Entity(tableName = "tag_table")
public class Hashtag implements Parcelable {
    public static final Creator<Hashtag> CREATOR = new Creator<Hashtag>() {
        @Override
        public Hashtag createFromParcel(Parcel in) {
            return new Hashtag(in);
        }

        @Override
        public Hashtag[] newArray(int size) {
            return new Hashtag[size];
        }
    };
    @NonNull
    @PrimaryKey
    private String tagId;

    private String tagName;
    private int notesCount;

    public Hashtag() {

    }

    public Hashtag(@NonNull String tagId, String tagName) {
        this.tagId = tagId;
        this.tagName = tagName;
    }

    protected Hashtag(Parcel in) {
        tagId = Objects.requireNonNull(in.readString());
        tagName = in.readString();
        notesCount=in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tagId);
        dest.writeString(tagName);
        dest.writeInt(notesCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    @NonNull
    public String getTagId() {
        return tagId;
    }

    public void setTagId(@NonNull String tagId) {
        this.tagId = tagId;
    }

    public int getNotesCount() {
        return notesCount;
    }

    public void setNotesCount(int notesCount) {
        this.notesCount = notesCount;
    }

}
