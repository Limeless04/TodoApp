package com.todoapp.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "todo_table")
public class Task {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public boolean isCompleted;

    public long createdAt;

    public Task(String title){
        this.title = title;
        this.isCompleted = false;
        this.createdAt = System.currentTimeMillis();
    }
}
