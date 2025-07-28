package com.todo.task;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class addProjectActivity extends AppCompatActivity {

    ArrayList<Task> list = MainActivity.taskList;

    private CardView cardStartDate, cardEndDate;
    private TextView tvTaskGroup, tvStartDate, tvEndDate;
    private EditText etProjectName, etDescription;
    private ImageButton btnAddProject;
    private ImageButton btnBack, btnNoti;

    private String startDate = "", endDate = "";
    private String selectedTaskGroup = "Công việc";
    private String[] taskGroups = {"Công việc", "Cá nhân", "Học tâp", "Khác"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);

        cardStartDate = findViewById(R.id.cardStarDate); // Nếu ID vẫn là cardStarDate thì giữ nguyên hoặc sửa xml
        cardEndDate = findViewById(R.id.cardEndDate);
        btnBack = findViewById(R.id.btnBack);
        btnNoti = findViewById(R.id.btnNoti);
        tvTaskGroup = findViewById(R.id.tvTaskGroup);
        tvStartDate = findViewById(R.id.tvStarDate); // Tương tự giữ hoặc sửa xml
        tvEndDate = findViewById(R.id.tvEndDate);
        etProjectName = findViewById(R.id.etProjectName);
        etDescription = findViewById(R.id.etDescription);
        btnAddProject = findViewById(R.id.btnAddProject);
        ImageView igIcDropdown = findViewById(R.id.ig_ic_dropdown);

        btnBack.setOnClickListener(v -> finish());

        btnNoti.setOnClickListener(v -> {
            if (list.isEmpty()) {
                Toast.makeText(this, "Chưa có dự án nào được thêm!", Toast.LENGTH_SHORT).show();
            } else {
                StringBuilder sb = new StringBuilder();
                for (Task t : list) {
                    sb.append("- ").append(t.getName()).append(" (").append(t.getGroup()).append(")\n");
                }
                new android.app.AlertDialog.Builder(this)
                        .setTitle("Danh sách dự án đã thêm")
                        .setMessage(sb.toString())
                        .setPositiveButton("Đóng", null)
                        .show();
            }
        });

        tvTaskGroup.setOnClickListener(v -> showTaskGroupDialog());
        if (igIcDropdown != null) {
            igIcDropdown.setOnClickListener(v -> showTaskGroupDialog());
        }

        cardStartDate.setOnClickListener(v -> showDatePicker(tvStartDate));
        cardEndDate.setOnClickListener(v -> showDatePicker(tvEndDate));

        btnAddProject.setOnClickListener(v -> addProject());
    }

    private void showTaskGroupDialog() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Chọn nhóm công việc")
                .setItems(taskGroups, (dialog, which) -> {
                    selectedTaskGroup = taskGroups[which];
                    tvTaskGroup.setText(selectedTaskGroup);
                })
                .show();
    }

    private void showDatePicker(TextView target) {
        Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String dateStr = String.format("00:00 %02d-%02d-%04d", dayOfMonth, month + 1, year);
            target.setText(dateStr);

            if (target.getId() == R.id.tvStarDate) startDate = dateStr;
            else if (target.getId() == R.id.tvEndDate) endDate = dateStr;

        }, y, m, d).show();
    }

    private void addProject() {
        String name = etProjectName.getText().toString().trim();
        String desc = etDescription.getText().toString().trim();
        String group = selectedTaskGroup;

        if (name.isEmpty()) {
            etProjectName.setError("Tên Công việc không được trống!");
            etProjectName.requestFocus();
        } else if (startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền thông tin đầy đủ!", Toast.LENGTH_SHORT).show();
        } else if (!isDateValid(startDate, endDate)) {
            Toast.makeText(this, "Ngày hoàn thành không hợp lệ!", Toast.LENGTH_SHORT).show();
        } else {
            String info = "Công việc đã thêm:\nTên: " + name + "\nMô tả: " + desc +
                    "\nTừ ngày: " + startDate + "\nĐến ngày: " + endDate;
            Toast.makeText(this, info, Toast.LENGTH_LONG).show();

            Task task = new Task(name, desc, startDate, endDate, group);
            list.add(task);

            TaskStorage.saveTasks(this, list);
            finish();
        }
    }

    private boolean isDateValid(String begin, String end) {
        try {
            LocalDateTime beginDateTime = parseDateTime(begin);
            LocalDateTime endDateTime = parseDateTime(end);
            LocalDateTime now = LocalDateTime.now();

            // Cho phép ngày bắt đầu từ hiện tại trở đi (không chấp nhận start < hiện tại)
            if (beginDateTime.isBefore(now)) return false;
            if (endDateTime.isBefore(beginDateTime)) return false;

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private LocalDateTime parseDateTime(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
        return LocalDateTime.parse(dateStr, formatter);
    }
}
