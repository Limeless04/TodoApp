package com.todoapp.ui.fragment;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.todoapp.R;
import com.todoapp.data.Task;
import com.todoapp.viewmodel.TaskModelView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HeaderFragment extends Fragment {

    @NonNull
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
            ){
        return inflater.inflate(R.layout.fragment_header, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvGreeting = view.findViewById(R.id.tvGreeting);
        TextView tvDate = view.findViewById(R.id.tvDate);
        ImageButton btnDeleteAll = view.findViewById(R.id.btnDeleteAll);

        // Dynamic greeting based on time of day
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour < 12) {
            tvGreeting.setText("Good morning");
        } else if (hour < 17) {
            tvGreeting.setText("Good afternoon");
        } else {
            tvGreeting.setText("Good evening");
        }

        // Dynamic date e.g. "Wednesday, March 18"
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d", Locale.getDefault());
        tvDate.setText(sdf.format(new Date()));

        // Hide button by default until we know there are completed tasks
        btnDeleteAll.setVisibility(View.GONE);

        //Share viewmodel with activity
        TaskModelView viewModel = new ViewModelProvider(requireActivity()).get(TaskModelView.class);

        // Observer task and only show button if at least on is completed
        viewModel.getAllTasks().observe(getViewLifecycleOwner(), tasks -> {
            boolean hasCompleted = false;
            for (Task task: tasks){
                if(task.isCompleted){
                    hasCompleted = true;
                    break;
                }
            }
            btnDeleteAll.setVisibility(hasCompleted ? View.VISIBLE : View.GONE);
        });

        btnDeleteAll.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Delete all tasks")
                    .setMessage("Are you sure you want to delete all completed tasks? This cannot be undone.")
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .setPositiveButton("Delete all", (dialog, which) -> {
                        viewModel.deleteAllCompleted();
                    })
                    .show();
        });
    }
}
