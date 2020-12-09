package com.example.todolist;

import android.content.Context;
import android.util.Log;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    public void fetchDatabase(final Context context) {
        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<SellerResponse> call = service.getAllItems();
        call.enqueue(new Callback<SellerResponse>() {
            @Override
            public void onResponse(Call<SellerResponse> call, Response<SellerResponse> response) {
                generateDataList(response.body().getData().getItems(), context);
            }

            @Override
            public void onFailure(Call<SellerResponse> call, Throwable t) {
                Log.d("s", "onFailure: ");
            }
        });
    }

    private void generateDataList(List<Seller> body, final Context context) {
        for (Seller seller: body) {
            addTask(new ListItem(seller.toString(), false), context);
        }
    }

    public void addTask(final ListItem item, final Context context) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                DatabaseClient.getInstance(context).getAppDatabase()
                        .listItemDao()
                        .insertAll(item);
            }
        });
    }

    public void remove(final String currentData, final Context context) {
        if (context != null) {
            Executor myExecutor = Executors.newSingleThreadExecutor();
            myExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    DatabaseClient.getInstance(context).getAppDatabase()
                            .listItemDao()
                            .delete(currentData);
                }
            });
        }
    }

    public void replace(final String item, final MainActivity context) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ListItem tempItem = DatabaseClient.getInstance(context).getAppDatabase()
                        .listItemDao()
                        .getItem(item);
                DatabaseClient.getInstance(context).getAppDatabase()
                        .listItemDao()
                        .updateTour(item, !tempItem.isDone);
                context.replace(item, !tempItem.isDone);
            }
        });
    }
}
