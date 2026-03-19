package com.todoapp.ui.main;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.todoapp.R;
import com.todoapp.data.ListItem;
import com.todoapp.data.Task;
import com.todoapp.repository.TaskRepo;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TaskAdapter extends ListAdapter<ListItem, RecyclerView.ViewHolder> {
    public interface OnTaskListener {
        void onChecked(Task task, boolean isChecked);
        void onDelete(Task task);
    }

    private final OnTaskListener listener;

    public TaskAdapter(OnTaskListener listener){
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        if (viewType == ListItem.TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_date_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_task, parent, false);
            return new TaskViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position){
        ListItem item = getItem(position);

        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).bind(item.getDate());
        } else if (holder instanceof TaskViewHolder) {
            bindTask((TaskViewHolder) holder, item.getTask());
        }
    }

    private void bindTask(TaskViewHolder holder, Task task) {
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setText(task.title);
        holder.checkBox.setChecked(task.isCompleted);

        // Since the Header handles the DAY, the item only needs the TIME
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String formattedTime = sdf.format(new java.util.Date(task.createdAt));
        holder.tvCreatedAt.setText(
                holder.itemView.getContext().getString(R.string.task_time_format, formattedTime)
        );

        // Handle styling (Completed vs Active)
        if (task.isCompleted) {
            holder.checkBox.setAlpha(0.5f);
            holder.tvCreatedAt.setAlpha(0.4f);
        } else {
            holder.checkBox.setAlpha(1.0f);
            holder.tvCreatedAt.setAlpha(0.7f);
        }

        holder.checkBox.setOnCheckedChangeListener((btn, isChecked) -> {
            listener.onChecked(task, isChecked);
        });

        holder.deleteBtn.setOnClickListener(v -> listener.onDelete(task));
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView deleteBtn, tvCreatedAt;

        TaskViewHolder(@NonNull View itemView){
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkboxTodo);
            deleteBtn = itemView.findViewById(R.id.btnDelete);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
        }

    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvHeader;

        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHeader = (TextView) itemView; // Assuming the root of item_date_header is a TextView
        }

        void bind(String date) {
            tvHeader.setText(date);
        }
    }


    private static final DiffUtil.ItemCallback<ListItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<ListItem>() {
                @Override
                public boolean areItemsTheSame(@NonNull ListItem oldItem, @NonNull ListItem newItem) {
                    if (oldItem.getType() != newItem.getType()) return false;
                    if (oldItem.getType() == ListItem.TYPE_HEADER) {
                        return oldItem.getDate().equals(newItem.getDate());
                    } else {
                        return oldItem.getTask().id == newItem.getTask().id;
                    }
                }

                @Override
                public boolean areContentsTheSame(@NonNull ListItem oldItem, @NonNull ListItem newItem) {
                    return oldItem.equals(newItem); // Assumes you generated equals() in ListItem class
                }
            };
}
