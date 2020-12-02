package com.example.todolist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private ArrayList<String> mDataset;
    private TaskListController mController;
    public final boolean mIsDone;

    public CustomAdapter(TaskListController controller, boolean mIsDone) {

        mController = controller;
        this.mIsDone = mIsDone;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string and button in this case
        public TextView textView;
        public Button deleteBtn;
        public Button switchBtn;

        public MyViewHolder(ConstraintLayout v) {
            super(v);
            textView = v.findViewById(R.id.task_title);
            deleteBtn = v.findViewById(R.id.task_delete);
            switchBtn = v.findViewById(R.id.task_switch);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CustomAdapter(boolean mIsDone) {
        this.mIsDone = mIsDone;
        mDataset = new ArrayList<>();
    }

    public CustomAdapter(List<ListItem> myDataset, boolean mIsDone) {
        mDataset = listToArrayList(myDataset);
        this.mIsDone = mIsDone;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_todo, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomAdapter.MyViewHolder holder, final int position) {

        final String currentData = mDataset.get(position);
        holder.textView.setText(currentData);

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataset.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), mDataset.size());

                remove(currentData);
            }
        });

        holder.switchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataset.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), mDataset.size());

                replace(currentData);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public  void add(ListItem item) {
        add(item, false);
    }

    public void add(ListItem item, boolean isExist) {

        mDataset.add(item.task);
        notifyItemInserted(mDataset.size() - 1);

        if(!isExist) {
            mController.addTask(item);
        }
    }

    public void remove(String item) {
        mController.remove(item);
    }

    private void replace(String currentData) {
        mController.replace(currentData);
    }

    public void addList(List<ListItem> listItems) {
        mDataset = listToArrayList(listItems);
    }

    private ArrayList<String> listToArrayList (List<ListItem> listItems) {
        ArrayList<String> list = new ArrayList<>();
        for (ListItem item : listItems) {
            list.add(item.task);
        }

        return list;
    }

}
