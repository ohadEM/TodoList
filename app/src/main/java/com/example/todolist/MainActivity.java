package com.example.todolist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView mTaskTodoRecycleView, mTaskDoneRecycleView;
    private CustomAdapter mTodoAdapter, mDoneAdapter;
    private RecyclerView.LayoutManager mTodoLayoutManager, mDoneLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        String[] permissions = {Manifest.permission.INTERNET};
//        requestPermissions(permissions,);

        final TaskListController controller = TaskListController.getInstance();
        controller.mActivity = this;

        ListViewModel model = new ViewModelProvider(this).get(ListViewModel.class);
        model.getStream().observe(this, new Observer<List<ListItem>>() {
            @Override
            public void onChanged(List<ListItem> users) {
                // update UI
                setUnfinishedItems(users);
            }
        });

        model.activityCreated(this);

        mTaskTodoRecycleView = findViewById(R.id.list_todo).findViewById(R.id.list);
        mTaskDoneRecycleView = findViewById(R.id.list_done).findViewById(R.id.list);

        TextView text = findViewById(R.id.list_done).findViewById(R.id.list_header);
        text.setText("Finished List:");

        // Use a linear layout manager
        mTodoLayoutManager = new LinearLayoutManager(this);
        mTaskTodoRecycleView.setLayoutManager(mTodoLayoutManager);

        mDoneLayoutManager = new LinearLayoutManager(this);
        mTaskDoneRecycleView.setLayoutManager(mDoneLayoutManager);

        // TODO: put after the adapters creations

        // Specify an adapter
        mTodoAdapter = new CustomAdapter(controller, false);
        mTaskTodoRecycleView.setAdapter(mTodoAdapter);

        mDoneAdapter = new CustomAdapter(controller, true);
        mTaskDoneRecycleView.setAdapter(mDoneAdapter);

        controller.activityCreate();
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

    private void addTask(final ListItem item) {
        mTodoAdapter.add(item);
    }


    public void setUnfinishedItems(List<ListItem> listUnfinishedItems) {
        mTodoAdapter.addList(listUnfinishedItems);
    }

    public void setFinishedItems(List<ListItem> listFinishedItems) {
        mDoneAdapter.addList(listFinishedItems);
    }

    public void replace(final String item, final boolean isDone) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ListItem listItem = new ListItem(item, isDone);
                listItem.task = item;
                listItem.isDone = isDone;

                if (isDone) {
                    mDoneAdapter.add(listItem, true);
                } else {
                    mTodoAdapter.add(listItem, true);
                }
            }
        });
    }
}
