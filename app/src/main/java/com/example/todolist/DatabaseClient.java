package com.example.todolist;

import android.content.Context;
import androidx.room.Room;

public class DatabaseClient {

    private Context mCtx;
    private static DatabaseClient mInstance;

    //Our app database object
    private AppDatabase appDatabase;

    private DatabaseClient(Context context) {
        this.mCtx = context;

        //creating the app database with Room database builder
        //MyToDos is the name of the database
        appDatabase = Room.databaseBuilder(mCtx, AppDatabase.class, "FullList").build();
    }

    public static synchronized DatabaseClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(context);
        }

        return mInstance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
