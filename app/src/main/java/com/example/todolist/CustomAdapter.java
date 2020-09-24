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

    public CustomAdapter(TaskListController controller) {

        mController = controller;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string and button in this case
        public TextView textView;
        public Button button;

        public MyViewHolder(ConstraintLayout v) {
            super(v);
            textView = v.findViewById(R.id.task_title);
            button = v.findViewById(R.id.task_delete);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CustomAdapter() {
        mDataset = new ArrayList<>();
    }

    public CustomAdapter(List<ListItem> myDataset) {
        mDataset = listToArrayList(myDataset);
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

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataset.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), mDataset.size());
//                mAdapter.notifyDataSetChanged();

                mController.remove(currentData);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void add(String task) {

        mDataset.add(task);
        notifyItemInserted(mDataset.size() - 1);
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
