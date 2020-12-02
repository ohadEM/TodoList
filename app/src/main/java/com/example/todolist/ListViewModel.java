package com.example.todolist;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ListViewModel extends ViewModel {

    MutableLiveData<List<ListItem>> mStream = new MutableLiveData<>();;

    public MutableLiveData<List<ListItem>> getStream() {
        return mStream;
    }

    public void activityCreated(Context context) {
        getData(context);
    }

    private void getData(final Context context) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                List<ListItem> listUnfinishedItems = DatabaseClient.getInstance(context).getAppDatabase()
                        .listItemDao()
                        .loadAllUnfinishedTasks();

                mStream.postValue(listUnfinishedItems);

            }
        });
    }

}
