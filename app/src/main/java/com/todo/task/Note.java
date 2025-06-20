package com.todo.task;

public class Note {
    private String content;
    private long createdTime;

    public Note(String content) {
        this.content = content;
        this.createdTime = System.currentTimeMillis();
    }

    public String getContent() {
        return content;
    }

    public long getCreatedTime() {
        return createdTime;
    }
}

