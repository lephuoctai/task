package com.todo.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    private String name;                  // Tên nhiệm vụ
    private String quest;                 // Nội dung hoặc mô tả nhiệm vụ
    private LocalDateTime dateBegin;     // Ngày bắt đầu nhiệm vụ
    private LocalDateTime dateCompleted; // Hạn chót hoàn thành nhiệm vụ
    private String group;                // Nhóm nhiệm vụ
    private LocalDateTime updatedAt; // Thời gian chỉnh sửa gần nhất

    // Constructor đầy đủ
    public Task(String name, String quest, String dateBeginStr, String dateCompletedStr, String group) {
        this.name = name;
        this.quest = quest;
        this.dateBegin = parseDateTime(dateBeginStr);
        this.dateCompleted = parseDateTime(dateCompletedStr);
        this.group = group;
        this.updatedAt = LocalDateTime.now();
    }

    // Constructor chỉ có ngày hoàn thành
    public Task(String name, String quest, String dateCompletedStr) {
        this.name = name;
        this.quest = quest;
        this.dateBegin = LocalDateTime.now();
        this.dateCompleted = parseDateTime(dateCompletedStr);
        this.updatedAt = LocalDateTime.now();
    }

    // Chuyển chuỗi định dạng "HH:mm dd-MM-yyyy" thành LocalDateTime
    private LocalDateTime parseDateTime(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
        return LocalDateTime.parse(dateStr, formatter);
    }

    // Getter tên nhiệm vụ
    public String getName() {
        return name;
    }

    // Getter nội dung
    public String getQuest() {
        return quest;
    }

    // Trả về ngày bắt đầu dưới dạng chuỗi
    public String getDateBegin() {
        return formatDateTime(dateBegin);
    }

    // Trả về ngày hoàn thành dưới dạng chuỗi
    public String getDateCompleted() {
        return formatDateTime(dateCompleted);
    }

    // Getter nhóm
    public String getGroup() {
        return group;
    }

    // Getter thời gian chỉnh sửa
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Setter tên
    public void setName(String name) {
        this.name = name;
        this.updatedAt = LocalDateTime.now(); // Cập nhật thời gian chỉnh sửa
    }

    // Setter nội dung
    public void setQuest(String quest) {
        this.quest = quest;
        this.updatedAt = LocalDateTime.now(); // Cập nhật thời gian chỉnh sửa
    }

    // Setter nhóm
    public void setGroup(String group) {
        this.group = group;
        this.updatedAt = LocalDateTime.now(); // Cập nhật thời gian chỉnh sửa
    }

    // Setter ngày bắt đầu
    public void setDateBegin(String dateBeginStr) {
        this.dateBegin = parseDateTime(dateBeginStr);
        this.updatedAt = LocalDateTime.now(); // Cập nhật thời gian chỉnh sửa
    }

    // Setter ngày hoàn thành
    public void setDateCompleted(String dateCompletedStr) {
        this.dateCompleted = parseDateTime(dateCompletedStr);
        this.updatedAt = LocalDateTime.now(); // Cập nhật thời gian chỉnh sửa
    }

    // Cập nhật thông tin nhiệm vụ
    public void updateTask(String name, String quest, String dateBeginStr, String dateCompletedStr, String group) {
        this.name = name;
        this.quest = quest;
        this.dateBegin = parseDateTime(dateBeginStr);
        this.dateCompleted = parseDateTime(dateCompletedStr);
        this.group = group;
        this.updatedAt = LocalDateTime.now();
    }

    // Tính số ngày còn lại
    public int dayLeft() {
        if (dateCompleted == null) {
            return Integer.MAX_VALUE; // hoặc -1 nếu muốn báo lỗi
        }
        try {
            LocalDateTime now = LocalDateTime.now();
            if (dateCompleted.toLocalDate() == null) return Integer.MAX_VALUE;
            if (now.toLocalDate() == null) return Integer.MAX_VALUE;
            return (int) java.time.Duration.between(now.toLocalDate().atStartOfDay(), dateCompleted.toLocalDate().atStartOfDay()).toDays();
        } catch (Exception e) {
            return Integer.MAX_VALUE;
        }
    }


    // Trạng thái deadline
    public String getStatus() {
        if (dateCompleted == null) {
            return "Trạng thái: Không xác định (thiếu ngày hoàn thành)";
        }

        int dayLeft = dayLeft();
        String status = "Trạng thái: ";

        if (dayLeft <= 0) {
            status += "Quá hạn (vượt " + (-dayLeft) + " ngày)";
        } else {
            status += "Còn hạn (" + dayLeft + " ngày còn lại)";
        }

        return status;
    }

    // Định dạng ngày thành chuỗi
    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "Không xác định";
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
            return dateTime.format(formatter);
        } catch (Exception e) {
            return "Không xác định";
        }
    }

    // Hiển thị thông tin Task
    @Override
    public String toString() {
        return name + " | " + quest + " | " + getDateBegin() + " | " + getDateCompleted() + " | " + group;
    }
}
