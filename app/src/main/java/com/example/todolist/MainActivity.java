package com.example.todolist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TaskDbHelper mHelper;
    private RecyclerView mTaskTodoRecycleView, mTaskDoneRecycleView;
    private CustomAdapter mTodoAdapter, mDoneAdapter;
    private RecyclerView.LayoutManager mTodoLayoutManager, mDoneLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Creating database

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TaskListController controller = TaskListController.getInstance();
        controller.mActivity = this;

        mHelper = new TaskDbHelper(this);
        mTaskTodoRecycleView = findViewById(R.id.list_todo).findViewById(R.id.list);
        mTaskDoneRecycleView = findViewById(R.id.list_done).findViewById(R.id.list);

        // Use a linear layout manager
        mTodoLayoutManager = new LinearLayoutManager(this);
        mTaskTodoRecycleView.setLayoutManager(mTodoLayoutManager);

        mDoneLayoutManager = new LinearLayoutManager(this);
        mTaskDoneRecycleView.setLayoutManager(mDoneLayoutManager);



        controller.activityCreate();

        // Specify an adapter
        mTodoAdapter = new CustomAdapter(controller);
        mTaskTodoRecycleView.setAdapter(mTodoAdapter);

        mDoneAdapter = new CustomAdapter(controller);
        mTaskDoneRecycleView.setAdapter(mDoneAdapter);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_add_task) {
            final EditText taskEditText = new EditText(this);

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Add a new task")
                    .setMessage("What do you want to do next?")
                    .setView(taskEditText)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ListItem item = new ListItem();
                            item.task = String.valueOf((taskEditText.getText()));
                            item.isDone = false;
                            //String task = String.valueOf((taskEditText.getText()));
                            addTask(item);

                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create();
            dialog.show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TaskListController.getInstance().mActivity = null;
    }

    //TODO: insert in controller.
    private void addTask(final ListItem item) {

        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
                               @Override
                               public void run() {
                                   DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                                           .listItemDao()
                                           .insertAll(item);
                               }
                           });

//        ArrayList<String> taskList = new ArrayList<>();
//        taskList.add(item.task);
//        Log.d(TAG, taskList.toString());

        mTodoAdapter.add(item.task);
//        mTodoAdapter.notifyDataSetChanged();

    }

    //TODO: insert in controller.
    public void remove(final String currentData) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .listItemDao()
                        .delete(currentData);
            }
        });
    }

    public void setUnfinishedItems(List<ListItem> listUnfinishedItems) {
        mTodoAdapter.addList(listUnfinishedItems);
    }

    public void setFinishedItems(List<ListItem> listFinishedItems) {
        mDoneAdapter.addList(listFinishedItems);
    }
}
