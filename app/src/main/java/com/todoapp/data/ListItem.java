package com.todoapp.data;

import java.util.Objects;

public class ListItem {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_TASK = 1;

    private final int type;
    private String date;
    private Task task;

    // Constructor for Header
    public ListItem(String date) {
        this.type = TYPE_HEADER;
        this.date = date;
    }

    // Constructor for Task
    public ListItem(Task task) {
        this.type = TYPE_TASK;
        this.task = task;
    }

    public int getType() { return type; }
    public String getDate() { return date; }
    public Task getTask() { return task; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListItem listItem = (ListItem) o;

        if (type != listItem.type) return false;

        if (type == TYPE_HEADER) {
            return Objects.equals(date, listItem.date);
        } else {
            // Very important: Check the specific fields of the task
            return task.id == listItem.task.id &&
                    task.isCompleted == listItem.task.isCompleted &&
                    Objects.equals(task.title, listItem.task.title);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, date, task != null ? task.id : 0, task != null ? task.isCompleted : false);
    }
}
