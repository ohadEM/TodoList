package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ListItem {


    @PrimaryKey
    @NonNull
    public String task;

    public boolean isDone;

    public ListItem(String task, boolean isDone) {
        this.task = task;
        this.isDone = isDone;
    }

    public ListItem() {}
}
