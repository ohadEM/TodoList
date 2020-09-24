package com.example.todolist;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ListItem {
    @PrimaryKey
    public String task;

    public boolean isDone;
}
