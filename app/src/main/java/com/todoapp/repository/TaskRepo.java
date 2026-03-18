package com.todoapp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.todoapp.data.Task;
import com.todoapp.data.TaskDao;
import com.todoapp.data.TaskDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskRepo {

    private final TaskDao taskDao;
    private final LiveData<List<Task>> allTasks;

    // single bg thread for all DB writes
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public TaskRepo(Application app){
        TaskDatabase db = TaskDatabase.getDatabase(app);
        taskDao = db.taskDao();
        allTasks = taskDao.getAllTodos();
    }

    public LiveData<List<Task>> getAllTasks(){
        return allTasks;
    }

    public void insert(Task task){
        executorService.execute(() -> taskDao.insert(task));
    }

    public void update(Task task){
        executorService.execute(() -> taskDao.update(task));
    }

    public void delete(Task task){
        executorService.execute(() -> taskDao.delete(task));
    }

    public void deleteAllCompleted(){
        executorService.execute(taskDao::deleteAllCompleted);
    }
}
