package com.todo.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    private String name;                  // Tên nhiệm vụ
    private String quest;                 // Nội dung hoặc mô tả nhiệm vụ
    private LocalDateTime dateBegin;     // Ngày bắt đầu nhiệm vụ
    private LocalDateTime dateCompleted; // Hạn chót hoàn thành nhiệm vụ

    /**
     * Constructor tạo Task có cả ngày bắt đầu và ngày hoàn thành.
     * Nếu chuỗi ngày bắt đầu không hợp lệ hoặc rỗng, sẽ lấy thời gian hiện tại.
     */
    public Task(String name, String quest, String dateBeginStr, String dateCompletedStr) {
        this.name = name;
        this.quest = quest;
        this.dateBegin = parseDateTime(dateBeginStr);
        this.dateCompleted = parseDateTime(dateCompletedStr);
    }

    /**
     * Constructor tạo Task chỉ với ngày hoàn thành.
     * Ngày bắt đầu sẽ được set là thời điểm hiện tại.
     */
    public Task(String name, String quest, String dateCompletedStr) {
        this.name = name;
        this.quest = quest;
        this.dateBegin = LocalDateTime.now();
        this.dateCompleted = parseDateTime(dateCompletedStr);
    }

    /**
     * Chuyển chuỗi định dạng "HH:mm dd-MM-yyyy" thành đối tượng LocalDateTime.
     */
    private LocalDateTime parseDateTime(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
        return LocalDateTime.parse(dateStr, formatter);
    }

    // Getter tên nhiệm vụ
    public String getName() {
        return name;
    }

    // Getter nội dung nhiệm vụ
    public String getQuest() {
        return quest;
    }

    // Trả về ngày bắt đầu đã được định dạng lại thành chuỗi
    public String getDateBegin() {
        return formatDateTime(dateBegin);
    }

    // Trả về ngày hoàn thành đã được định dạng lại thành chuỗi
    public String getDateCompleted() {
        return formatDateTime(dateCompleted);
    }

    /**
     * Kiểm tra nhiệm vụ đã quá hạn chưa (so với thời điểm hiện tại).
     * @return true nếu đã trễ hạn
     */
    public boolean isOverdue() {
        return LocalDateTime.now().isAfter(dateCompleted);
    }

    /**
     * Định dạng LocalDateTime thành chuỗi "HH:mm dd-MM-yyyy".
     */
    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
        return dateTime.format(formatter);
    }

    /**
     * Hiển thị thông tin Task dưới dạng chuỗi: name | quest | dateBegin | dateCompleted
     */
    @Override
    public String toString() {
        return name + " | " + quest + " | " + getDateBegin() + " | " + getDateCompleted();
    }
}
