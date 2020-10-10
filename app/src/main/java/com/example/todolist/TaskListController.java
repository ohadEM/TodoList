package com.example.todolist;

import android.app.Activity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TaskListController {

    public MainActivity mActivity;

    private static TaskListController instance;

    public static  TaskListController getInstance() {
     if (instance == null) {
         instance = new TaskListController();
     }
     return instance;
    }

    public void activityCreate() {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {

                if (mActivity == null) return;

                List<ListItem> listUnfinishedItems = DatabaseClient.getInstance(mActivity.getApplicationContext()).getAppDatabase()
                        .listItemDao()
                        .loadAllUnfinishedTasks();

                mActivity.setUnfinishedItems(listUnfinishedItems);


                List<ListItem> listFinishedItems = DatabaseClient.getInstance(mActivity.getApplicationContext()).getAppDatabase()
                        .listItemDao()
                        .loadAllFinishedTasks();

                mActivity.setFinishedItems(listFinishedItems);
            }
        });
    }

    public void remove(final String currentData) {
        if (mActivity != null) {
            Executor myExecutor = Executors.newSingleThreadExecutor();
            myExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    DatabaseClient.getInstance(mActivity).getAppDatabase()
                            .listItemDao()
                            .delete(currentData);
                }
            });
        }
    }

    public void addTask(final ListItem item) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                DatabaseClient.getInstance(mActivity).getAppDatabase()
                        .listItemDao()
                        .insertAll(item);
            }
        });
    }

    public void replace(final String item) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ListItem tempItem = DatabaseClient.getInstance(mActivity).getAppDatabase()
                        .listItemDao()
                        .getItem(item);
                DatabaseClient.getInstance(mActivity).getAppDatabase()
                        .listItemDao()
                        .updateTour(item, !tempItem.isDone);
                mActivity.replace(item, !tempItem.isDone);
            }
        });
    }

}
