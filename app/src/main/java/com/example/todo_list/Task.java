package com.example.todo_list;

public class Task {
    String id, title, Description;
    Boolean isChecked;

    public Task() {
    }

    public Task(String id, String title, String Description, Boolean isChecked) {
        this.id = id;
        this.title = title;
        this.Description = Description;
        this.isChecked = isChecked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) { this.id = id;}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) { this.title = title;}

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) { this.Description = Description;}

    public Boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }
}
