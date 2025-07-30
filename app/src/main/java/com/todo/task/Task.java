package com.todo.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    private String name;                  // Tên nhiệm vụ
    private String quest;                 // Nội dung hoặc mô tả nhiệm vụ
    private String dateBegin;            // Ngày bắt đầu nhiệm vụ (String để lưu Firebase)
    private String dateCompleted;        // Hạn chót hoàn thành nhiệm vụ (String để lưu Firebase)
    private String group;                // Nhóm nhiệm vụ
    private String updatedAt;            // Thời gian chỉnh sửa gần nhất - lưu dạng String

    // Constructor mặc định bắt buộc cho Firebase
    public Task() {
    }

    // Constructor đầy đủ - truyền vào String ngày tháng
    public Task(String name, String quest, String dateBegin, String dateCompleted, String group) {
        this.name = name;
        this.quest = quest;
        this.dateBegin = dateBegin;
        this.dateCompleted = dateCompleted;
        this.group = group;
        this.updatedAt = getCurrentTimeString();
    }

    // Constructor chỉ có ngày hoàn thành (khởi tạo ngày bắt đầu là hiện tại)
    public Task(String name, String quest, String dateCompleted) {
        this.name = name;
        this.quest = quest;
        this.dateBegin = getCurrentTimeString();
        this.dateCompleted = dateCompleted;
        this.group = "";
        this.updatedAt = getCurrentTimeString();
    }


    // --- Getter và Setter ---

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.updatedAt = getCurrentTimeString();
    }

    public String getQuest() {
        return quest;
    }

    public void setQuest(String quest) {
        this.quest = quest;
        this.updatedAt = getCurrentTimeString();
    }

    public String getDateBegin() {
        return dateBegin != null ? dateBegin : "";
    }

    public void setDateBegin(String dateBegin) {
        this.dateBegin = dateBegin;
        this.updatedAt = getCurrentTimeString();
    }

    public String getDateCompleted() {
        return dateCompleted != null ? dateCompleted : "";
    }

    public void setDateCompleted(String dateCompleted) {
        this.dateCompleted = dateCompleted;
        this.updatedAt = getCurrentTimeString();
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
        this.updatedAt = getCurrentTimeString();
    }

    public String getUpdatedAt() {
        return updatedAt != null ? updatedAt : "";
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    // --- Các phương thức hỗ trợ ---

    // Chuyển String ngày giờ thành LocalDateTime
    public static LocalDateTime parseDateTime(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
        return LocalDateTime.parse(dateStr, formatter);
    }

    // Lấy thời gian hiện tại dạng String theo định dạng "HH:mm dd-MM-yyyy"
    private static String getCurrentTimeString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
        return LocalDateTime.now().format(formatter);
    }

    // Tính số ngày còn lại cho hạn chót hoàn thành
    public int dayLeft() {
        LocalDateTime completed = parseDateTime(dateCompleted);
        if (completed == null) return Integer.MAX_VALUE;

        LocalDateTime now = LocalDateTime.now();

        return (int) java.time.Duration.between(now.toLocalDate().atStartOfDay(), completed.toLocalDate().atStartOfDay()).toDays();
    }

    // Trạng thái deadline
    public String getStatus() {
        LocalDateTime completed = parseDateTime(dateCompleted);
        if (completed == null) {
            return "Trạng thái: Không xác định (thiếu ngày hoàn thành)";
        }
        int dayLeft = dayLeft();
        if (dayLeft < 0) {
            return "Trạng thái: Quá hạn (vượt " + (-dayLeft) + " ngày)";
        } else {
            return "Trạng thái: Còn hạn (" + dayLeft + " ngày còn lại)";
        }
    }

    @Override
    public String toString() {
        return name + " | " + quest + " | " + dateBegin + " | " + dateCompleted + " | " + group;
    }
}
