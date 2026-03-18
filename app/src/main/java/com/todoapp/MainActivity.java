package com.todoapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.todoapp.data.Task;
import com.todoapp.ui.fragment.AddButton;
import com.todoapp.ui.main.TaskAdapter;
import com.todoapp.viewmodel.TaskModelView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskListener {

    private TaskModelView viewModel;
    private TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton fab = findViewById(R.id.fab);

        // Init viewmodel
        viewModel = new ViewModelProvider(this).get(TaskModelView.class);

        //Set up Recycler view
        adapter = new TaskAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Observer live data
        viewModel.getAllTasks().observe(this, tasks -> {
            adapter.submitList(tasks);
        });

        // Open bottom sheet on FAB click
        fab.setOnClickListener(v -> {
            AddButton bottomSheet = new AddButton();
            bottomSheet.show(getSupportFragmentManager(), AddButton.TAG);
        });

    }



    @Override
    public void onChecked(Task task, boolean isChecked) {
        task.isCompleted = isChecked;
        viewModel.update(task);
    }

    @Override
    public void onDelete(Task task) {
        viewModel.delete(task);
    }
}