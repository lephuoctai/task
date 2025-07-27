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

    // Danh sách Task được chia sẻ từ MainActivity (dùng chung)
    ArrayList<Task> list = MainActivity.taskList;

    // Khai báo các thành phần giao diện
    private CardView cardStarDate, cardEndDate;
    private TextView tvTaskGroup, tvStarDate, tvEndDate;
    private EditText etProjectName, etDescription;
    private ImageButton btnAddProject;
    private ImageButton btnBack, btnNoti;

    // Biến lưu trữ ngày bắt đầu và ngày kết thúc
    private String startDate = "", endDate = "";
    private String selectedTaskGroup = "Công việc";
    private String[] taskGroups = {"Công việc", "Cá nhân", "Học tâp", "Khác"};

    /**
     * Hàm onCreate được gọi khi activity được khởi tạo
     * → Thiết lập giao diện, ánh xạ view và gán các sự kiện
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);

        // Ánh xạ ID các view từ layout
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

        // Sự kiện nút "Quay lại"
        btnBack.setOnClickListener(v -> finish());

        // Sự kiện khi nhấn vào biểu tượng thông báo
        btnNoti.setOnClickListener(v -> {
            if (list.isEmpty()) {
                Toast.makeText(this, "Chưa có dự án nào được thêm!", Toast.LENGTH_SHORT).show();
            } else {
                StringBuilder sb = new StringBuilder();
                for (Task t : list) {
                    sb.append("- ").append(t.getName()).append(" (" + t.getGroup() + ")\n");
                }
                new android.app.AlertDialog.Builder(this)
                    .setTitle("Danh sách dự án đã thêm")
                    .setMessage(sb.toString())
                    .setPositiveButton("Đóng", null)
                    .show();
            }
        });

        // Sự kiện khi chọn nhóm công việc
        tvTaskGroup.setOnClickListener(v -> {
            new android.app.AlertDialog.Builder(this)
                .setTitle("Chọn nhóm công việc")
                .setItems(taskGroups, (dialog, which) -> {
                    selectedTaskGroup = taskGroups[which];
                    tvTaskGroup.setText(selectedTaskGroup);
                })
                .show();
        });
        if (igIcDropdown != null) {
            igIcDropdown.setOnClickListener(v -> {
                new android.app.AlertDialog.Builder(this)
                    .setTitle("Chọn nhóm công việc")
                    .setItems(taskGroups, (dialog, which) -> {
                        selectedTaskGroup = taskGroups[which];
                        tvTaskGroup.setText(selectedTaskGroup);
                    })
                    .show();
            });
        }

        // Hiển thị DatePicker để chọn ngày bắt đầu và kết thúc
        cardStarDate.setOnClickListener(v -> showDatePicker(tvStarDate));
        cardEndDate.setOnClickListener(v -> showDatePicker(tvEndDate));

        // Sự kiện khi nhấn nút "Thêm dự án"
        btnAddProject.setOnClickListener(v -> addProject());
    }

    /**
     * Hiển thị DatePicker và gán ngày được chọn vào TextView tương ứng
     */
    private void showDatePicker(TextView target) {
        Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String dateStr = String.format("00:00 %02d-%02d-%d", dayOfMonth, month + 1, year);
            target.setText(dateStr);

            // Gán vào biến ngày tương ứng
            if (target.getId() == R.id.tvStarDate) startDate = dateStr;
            else if (target.getId() == R.id.tvEndDate) endDate = dateStr;

        }, y, m, d).show();
    }

    /**
     * Kiểm tra đầu vào và thêm công việc mới vào danh sách nếu hợp lệ
     */
    private void addProject() {
        String name = etProjectName.getText().toString().trim();
        String desc = etDescription.getText().toString().trim();
        String group = selectedTaskGroup;

        if (name.isEmpty()) {
            etProjectName.setError("Tên Công việc không được trống!");
        } else if (startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền thông tin đầy đủ!", Toast.LENGTH_SHORT).show();
        } else if (!isDateValid(startDate, endDate)) {
            Toast.makeText(this, "Ngày hoàn thành không hợp lệ!", Toast.LENGTH_SHORT).show();
        } else {
            // Thông báo thông tin đã nhập
            String info = "Công việc đã thêm:\nTên: " + name + "\nMô tả: " + desc +
                    "\nTừ ngày: " + startDate + "\nĐến ngày: " + endDate;
            Toast.makeText(this, info, Toast.LENGTH_LONG).show();

            // Tạo task mới và thêm vào danh sách chung
            Task task = new Task(name, desc, startDate, endDate, group);
            list.add(task);

            // Sau khi thêm task mới vào list
            TaskStorage.saveTasks(this, list);
            finish();
        }
    }

    /**
     * Kiểm tra tính hợp lệ của ngày
     */
    private boolean isDateValid(String begin, String end) {
        LocalDateTime now = LocalDateTime.now();
        if (parseDateTime(begin).isBefore(now)) return false;
        if (parseDateTime(end).isBefore(parseDateTime(begin))) return false;
        return true;
    }

    /**
     * Chuyển chuỗi ngày theo định dạng "HH:mm dd-MM-yyyy" thành đối tượng LocalDateTime
     */
    private LocalDateTime parseDateTime(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
        return LocalDateTime.parse(dateStr, formatter);
    }
}