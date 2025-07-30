package com.todo.task;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class addProjectActivity extends AppCompatActivity {

    ArrayList<Task> list = MainActivity.taskList;

    private CardView cardStarDate, cardEndDate;
    private TextView tvTaskGroup, tvStarDate, tvEndDate;
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

        cardStarDate = findViewById(R.id.cardStarDate);
        cardEndDate = findViewById(R.id.cardEndDate);
        btnBack = findViewById(R.id.btnBack);
        btnNoti = findViewById(R.id.btnNoti);
        tvTaskGroup = findViewById(R.id.tvTaskGroup);
        tvStarDate = findViewById(R.id.tvStarDate);
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

        cardStarDate.setOnClickListener(v -> showDatePicker(tvStarDate));
        cardEndDate.setOnClickListener(v -> showDatePicker(tvEndDate));

        // --- Fix: Chỉ đăng ký 1 lần onClickListener duy nhất ---
        btnAddProject.setOnClickListener(v -> {
            Log.d("addProjectActivity", "Nút thêm mới đã được bấm");
            Toast.makeText(this, "Đã bấm nút thêm mới", Toast.LENGTH_SHORT).show();
            addProject();
        });
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
            String dateStr = String.format("00:00 %02d-%02d-%d", dayOfMonth, month + 1, year);
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
            return;
        }
        if (startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền thông tin đầy đủ!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isDateValid(startDate, endDate)) {
            Toast.makeText(this, "Ngày hoàn thành không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        String info = "Công việc đã thêm:\nTên: " + name + "\nMô tả: " + desc +
                "\nTừ ngày: " + startDate + "\nĐến ngày: " + endDate;
        Toast.makeText(this, info, Toast.LENGTH_LONG).show();

        // Tạo Task object
        Task task = new Task(name, desc, startDate, endDate, group);

        // Lưu lên Firebase, sau khi thành công mới cập nhật local và đóng Activity
        addProjectToDB(task);
    }

    private boolean isDateValid(String begin, String end) {
        LocalDateTime now = LocalDateTime.now();
        if (parseDateTime(begin).isBefore(now)) return false;
        if (parseDateTime(end).isBefore(parseDateTime(begin))) return false;
        return true;
    }

    private LocalDateTime parseDateTime(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
        return LocalDateTime.parse(dateStr, formatter);
    }

///
///     HAM LƯU LÊN FB O ĐÂY !!!!!!!!!!!!!
///
    private void addProjectToDB(Task task) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("tasks");
        String taskId = database.push().getKey();

        if (taskId == null) {
            Toast.makeText(this, "Lỗi tạo ID cho task", Toast.LENGTH_SHORT).show();
            return;
        }

        database.child(taskId).setValue(task)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Lưu dữ liệu lên Firebase thành công!", Toast.LENGTH_SHORT).show();
                    // Cập nhật danh sách local và đóng activity
                    list.add(task);
                    TaskStorage.saveTasks(this, list);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi lưu Firebase: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
