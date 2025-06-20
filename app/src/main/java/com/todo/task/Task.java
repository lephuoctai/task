package com.todo.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    private String name;                  // Tên nhiệm vụ
    private String quest;                 // Nội dung hoặc mô tả nhiệm vụ
    private LocalDateTime dateBegin;     // Ngày bắt đầu nhiệm vụ
    private LocalDateTime dateCompleted; // Hạn chót hoàn thành nhiệm vụ
    private String group;                 // Nhóm nhiệm vụ

    /**
     * Constructor tạo Task có cả ngày bắt đầu và ngày hoàn thành.
     * Nếu chuỗi ngày bắt đầu không hợp lệ hoặc rỗng, sẽ lấy thời gian hiện tại.
     */
    public Task(String name, String quest, String dateBeginStr, String dateCompletedStr, String group) {
        this.name = name;
        this.quest = quest;
        this.dateBegin = parseDateTime(dateBeginStr);
        this.dateCompleted = parseDateTime(dateCompletedStr);
        this.group = group;
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

    // Getter nội dung
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

    // Getter nhóm nhiệm vụ
    public String getGroup() {
        return group;
    }

    // Setter tên nhiệm vụ
    public void setName(String name) {
        this.name = name;
    }

    // Setter nội dung nhiệm vụ
    public void setQuest(String quest) {
        this.quest = quest;
    }

    // Setter nhóm nhiệm vụ
    public void setGroup(String group) {
        this.group = group;
    }

    // Setter ngày bắt đầu (dạng chuỗi)
    public void setDateBegin(String dateBeginStr) {
        this.dateBegin = parseDateTime(dateBeginStr);
    }

    // Setter ngày hoàn thành (dạng chuỗi)
    public void setDateCompleted(String dateCompletedStr) {
        this.dateCompleted = parseDateTime(dateCompletedStr);
    }

    /**
     * Trả về số ngày còn lại
     */
    public int dayLeft() {
        return dateCompleted.getDayOfYear() - LocalDateTime.now().getDayOfYear();
    }

    /**
     * string trả về trạng thái của Task
     */
    public String getStatus(){
        String status = "Trạng thái: ";
        int dayLeft = dayLeft();
        if(dayLeft() <= 0) {
            status += "Qúa hạn (vượt " + (- dayLeft) + " ngày)";
        } else if (dayLeft > 0) {
            status += "Còn hạn (" + dayLeft + " ngày còn lại)";
        }

        return status;
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
        return name + " | " + quest + " | " + getDateBegin() + " | " + getDateCompleted() + " | " + group;
    }
}