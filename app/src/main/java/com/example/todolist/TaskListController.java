package com.example.todolist;

import android.util.Log;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TaskListController {

    public MainActivity mActivity;
    public ListViewModel mModel;

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

                List<ListItem> listFinishedItems = DatabaseClient.getInstance(mActivity.getApplicationContext()).getAppDatabase()
                        .listItemDao()
                        .loadAllFinishedTasks();

                mActivity.setFinishedItems(listFinishedItems);
            }
        });

        mModel.fetchDatabase(mActivity);

//        /*Create handle for the RetrofitInstance interface*/
//        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
//        Call<SellerResponse> call = service.getAllItems();
//        call.enqueue(new Callback<SellerResponse>() {
//            @Override
//            public void onResponse(Call<SellerResponse> call, Response<SellerResponse> response) {
//                generateDataList(response.body().getData().getItems());
//            }
//
//            @Override
//            public void onFailure(Call<SellerResponse> call, Throwable t) {
//                Log.d("s", "onFailure: ");
//            }
//        });
    }

    private void generateDataList(List<Seller> body) {
        for (Seller seller: body) {
            addTask(new ListItem(seller.toString(), false));
        }
    }

    public void remove(final String currentData) {
        mModel.remove(currentData, mActivity);
//        if (mActivity != null) {
//            Executor myExecutor = Executors.newSingleThreadExecutor();
//            myExecutor.execute(new Runnable() {
//                @Override
//                public void run() {
//                    DatabaseClient.getInstance(mActivity).getAppDatabase()
//                            .listItemDao()
//                            .delete(currentData);
//                }
//            });
//        }
    }

    public void addTask(final ListItem item) {
        mModel.addTask(item, mActivity);
//        Executor myExecutor = Executors.newSingleThreadExecutor();
//        myExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
//                DatabaseClient.getInstance(mActivity).getAppDatabase()
//                        .listItemDao()
//                        .insertAll(item);
//            }
//        });
    }

    public void replace(final String item) {
        mModel.replace(item, mActivity);
//        Executor myExecutor = Executors.newSingleThreadExecutor();
//        myExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
//                ListItem tempItem = DatabaseClient.getInstance(mActivity).getAppDatabase()
//                        .listItemDao()
//                        .getItem(item);
//                DatabaseClient.getInstance(mActivity).getAppDatabase()
//                        .listItemDao()
//                        .updateTour(item, !tempItem.isDone);
//                mActivity.replace(item, !tempItem.isDone);
//            }
//        });
    }

}
