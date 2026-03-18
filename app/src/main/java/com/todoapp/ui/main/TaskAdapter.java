package com.todoapp.ui.main;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.todoapp.R;
import com.todoapp.data.Task;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TaskAdapter extends ListAdapter<Task, TaskAdapter.TaskViewHolder> {
    public interface OnTaskListener {
        void onChecked(Task task, boolean isChecked);
        void onDelete(Task task);
    }

    private final OnTaskListener listener;

    public TaskAdapter(OnTaskListener listener){
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position){
        Task task = getItem(position);

        holder.checkBox.setOnCheckedChangeListener(null);

        holder.checkBox.setText(task.title);
        holder.checkBox.setChecked(task.isCompleted);

        // Format and show created date
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy hh:mm a", Locale.getDefault());
        holder.tvCreatedAt.setText(sdf.format(new Date(task.createdAt)));

        // Apply or remove strikethrough + grey based on completion
        if (task.isCompleted) {

            holder.checkBox.setTextColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.task_completed)
            );
            holder.tvCreatedAt.setAlpha(0.4f);
        } else {
            // Resolve ?attr/colorOnSurface from the current theme at runtime
            TypedValue typedValue = new TypedValue();
            holder.itemView.getContext().getTheme().resolveAttribute(
                    com.google.android.material.R.attr.colorOnSurface, typedValue, true
            );
            holder.checkBox.setTextColor(typedValue.data);

            holder.tvCreatedAt.setAlpha(1.0f);
        }

        holder.checkBox.setOnCheckedChangeListener((btn, isChecked) -> {
            task.isCompleted = isChecked;
            listener.onChecked(task, isChecked);
        });

        holder.deleteBtn.setOnClickListener(v -> listener.onDelete(task));
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView deleteBtn;

        TextView tvCreatedAt;

        TaskViewHolder(@NonNull View itemView){
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkboxTodo);
            deleteBtn = itemView.findViewById(R.id.btnDelete);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
        }
    }

    private static final DiffUtil.ItemCallback<Task> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull Task a, @NonNull Task b) {
                    return a.id == b.id;
                }

                @Override
                public boolean areContentsTheSame(@NonNull Task a, @NonNull Task b) {
                    return a.title.equals(b.title) && a.isCompleted == b.isCompleted;
                }
            };
}
