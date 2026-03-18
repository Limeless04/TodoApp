package com.todoapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.todoapp.data.Task;
import com.todoapp.repository.TaskRepo;

import java.util.ArrayList;
import java.util.List;

public class TaskModelView extends AndroidViewModel {

    private final TaskRepo repo;
    private final LiveData<List<Task>> allTasks;

    public TaskModelView(@NonNull Application app){
        super(app);
        repo = new TaskRepo(app);
        allTasks = repo.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks(){
        return allTasks;
    }

    public void insert(Task task) {
        repo.insert(task);
    }

    public void update(Task task) {
        repo.update(task);
    }

    public void delete(Task task) {
        repo.delete(task);
    }

    public void deleteAllCompleted() {
        repo.deleteAllCompleted();
    }
}
