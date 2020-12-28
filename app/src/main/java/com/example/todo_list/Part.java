package com.example.todo_list;

public class Part {
    String id, title;
    int count;

    public Part() {

    }
    public Part(String id, String title, int count) {
        this.title = title;
        this.count = count;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }


}
