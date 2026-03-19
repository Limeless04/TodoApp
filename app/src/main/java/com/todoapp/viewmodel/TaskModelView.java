package com.todoapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.todoapp.data.ListItem;
import com.todoapp.data.Task;
import com.todoapp.repository.TaskRepo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public LiveData<List<ListItem>> getFlattenedTasks() {
        return Transformations.map(repo.getGroupedTask(), groupedMap -> {
            List<ListItem> flatList = new ArrayList<>();

            // Inside your transformation logic:
            List<String> sortedDates = groupedMap.keySet().stream()
                    .sorted(Comparator.reverseOrder()) // Newest date first
                    .collect(Collectors.toList());

            for(String date : sortedDates){
                flatList.add(new ListItem(date));

                for (Task task : Objects.requireNonNull(groupedMap.get(date))){
                    flatList.add(new ListItem(task));
                }
            }
            return flatList;
        });
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
