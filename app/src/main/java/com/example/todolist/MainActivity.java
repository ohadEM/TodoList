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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TaskDbHelper mHelper;
    //private ListView mTaskListView;
    private RecyclerView mTaskRecycleView;
    //private ArrayAdapter<String> mAdapter;
    private CustomAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Creating database

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new TaskDbHelper(this);
        mTaskRecycleView = findViewById(R.id.list_todo);

        // Use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mTaskRecycleView.setLayoutManager(mLayoutManager);

        // Specify an adapter
        mAdapter = new CustomAdapter();
        mTaskRecycleView.setAdapter(mAdapter);

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
                            String task = String.valueOf((taskEditText.getText()));
                            addTask(task);


                            //Using the database.
//                                SQLiteDatabase db = mHelper.getWritableDatabase();
//                                ContentValues values = new ContentValues();
//
//                                values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
//                                db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
//                                        null,
//                                        values,
//                                        SQLiteDatabase.CONFLICT_REPLACE);
//                                db.close();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create();
            dialog.show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    public void deleteTask(View view) {
//        View parent = (View) view.getParent();
//        TextView taskTextView = parent.findViewById(R.id.task_title);
//        String task = String.valueOf(taskTextView.getText());
//        mAdapter.remove(task);
//        mAdapter.notifyDataSetChanged();
//   }


    private void addTask(String task) {
        ArrayList<String> taskList = new ArrayList<>();
        taskList.add(task);
        Log.d(TAG, taskList.toString());

        mAdapter.add(task);
        mAdapter.notifyDataSetChanged();

    }
}
