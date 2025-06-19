package com.todo.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    private String name;
    private String quest;
    private LocalDateTime dateBegin;
    private LocalDateTime dateCompleted;

    public Task(String name, String quest, String dateCompletedStr) {
        this.name = name;
        this.quest = quest;
        this.dateBegin = LocalDateTime.now();
        this.dateCompleted = parseDateTime(dateCompletedStr);
    }

    private LocalDateTime parseDateTime(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"); // ví dụ định dạng
        return LocalDateTime.parse(dateStr, formatter);
    }

    public String getName() {
        return name;
    }

    public String getQuest() {
        return quest;
    }

    public String getDateBegin() {
        return formatDateTime(dateBegin);
    }

    public String getDateCompleted() {
        return formatDateTime(dateCompleted);
    }

    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dateTime.format(formatter);
    }

    @Override
    public String toString() {
        return name + " | " + quest + " | " + getDateBegin() + " | " + getDateCompleted();
    }
}
