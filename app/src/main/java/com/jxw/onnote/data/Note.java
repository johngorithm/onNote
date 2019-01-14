package com.jxw.onnote.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Nullable;

@Entity(tableName = "note_table")
public class Note {

    // MEMBER VARIABLE
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private int priority;

    @ColumnInfo(name = "created_at")
    private String createdAt;

    @ColumnInfo(name = "updated_at")
    @Nullable
    private String updatedAt;

    public Note(String title, String description, int priority, String createdAt, String updatedAt) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String date) {
        this.updatedAt = date;
    }
}
