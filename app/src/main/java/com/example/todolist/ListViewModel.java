package com.example.todolist;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListViewModel extends ViewModel {

    MutableLiveData<List<ListItem>> unfinishedListItemsLiveData = new MutableLiveData<>();
    MutableLiveData<List<ListItem>> finishedListItemsLiveData = new MutableLiveData<>();

    public MutableLiveData<List<ListItem>> getUnfinishedItemsLiveData() {
        return unfinishedListItemsLiveData;
    }

    public MutableLiveData<List<ListItem>> getFinishedListItemsLiveData() {
        return finishedListItemsLiveData;
    }

    public void activityCreated() {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                List<ListItem> listFinishedItems = DatabaseClient.getInstance().getAppDatabase()
                        .listItemDao()
                        .loadAllFinishedTasks();

                finishedListItemsLiveData.postValue(listFinishedItems);
            }
        });

        fetchDatabase();
        getData();

    }

    private void getData() {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                List<ListItem> listUnfinishedItems = DatabaseClient.getInstance().getAppDatabase()
                        .listItemDao()
                        .loadAllUnfinishedTasks();

                unfinishedListItemsLiveData.postValue(listUnfinishedItems);

            }
        });
    }



    public void fetchDatabase() {
        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<SellerResponse> call = service.getAllItems();
        call.enqueue(new Callback<SellerResponse>() {
            @Override
            public void onResponse(Call<SellerResponse> call, Response<SellerResponse> response) {
                generateDataList(response.body().getData().getItems());
            }

            @Override
            public void onFailure(Call<SellerResponse> call, Throwable t) {
                Log.d("s", "onFailure: ");
            }
        });
    }

    private void generateDataList(List<Seller> body) {
        for (Seller seller : body) {
            addTask(new ListItem(seller.toString(), false));
        }
    }

    public void addTask(final ListItem item) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                DatabaseClient.getInstance().getAppDatabase()
                        .listItemDao()
                        .insertAll(item);
            }
        });
    }

    public void remove(final String currentData) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                DatabaseClient.getInstance().getAppDatabase()
                        .listItemDao()
                        .delete(currentData);
            }
        });

    }

    public void replace(final String item, OnReplaceListener listener) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ListItem tempItem = DatabaseClient.getInstance().getAppDatabase()
                        .listItemDao()
                        .getItem(item);
                DatabaseClient.getInstance().getAppDatabase()
                        .listItemDao()
                        .updateTour(item, !tempItem.isDone);
                listener.onReplace(item, !tempItem.isDone);
            }
        });
    }

    public interface OnReplaceListener{
        void onReplace(String item, boolean isDone);
    }
}
