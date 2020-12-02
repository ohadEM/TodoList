package com.example.todolist;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ListItemDao {
    @Query("SELECT * FROM ListItem")
    List<ListItem> getAll();

    @Query("SELECT * FROM ListItem WHERE NOT isDone")
    List<ListItem> loadAllUnfinishedTasks();

    @Query("SELECT * FROM ListItem WHERE isDone")
    List<ListItem> loadAllFinishedTasks();

    @Update
    void updateItems(ListItem... items);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ListItem... items);

    @Query("SELECT * FROM ListItem WHERE task = :item")
    ListItem getItem(String item);

    @Query("DELETE FROM ListItem WHERE task = :item")
    void delete(String item);

    @Query("UPDATE ListItem SET isDone = :value WHERE task = :item")
    void updateTour(String item, boolean value);
}
