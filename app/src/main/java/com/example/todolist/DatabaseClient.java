package com.example.todolist;

import android.content.Context;
import androidx.room.Room;

public class DatabaseClient {


    private static DatabaseClient mInstance;

    //Our app database object
    private AppDatabase appDatabase;

    private DatabaseClient() {

        //creating the app database with Room database builder
        //MyToDos is the name of the database
        appDatabase = Room.databaseBuilder(MyApplication.getInstance().getContext(), AppDatabase.class, "FullList").build();
    }

    public static synchronized DatabaseClient getInstance() {
        if (mInstance == null) {
            mInstance = new DatabaseClient();
        }

        return mInstance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
