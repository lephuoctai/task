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
    private Button btnAddProject;
    private ImageButton btnBack, btnNoti;

    // Biến lưu trữ ngày bắt đầu và ngày kết thúc
    private String startDate = "", endDate = "";

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

        // Sự kiện nút "Quay lại"
        btnBack.setOnClickListener(v -> finish());

        // Sự kiện khi nhấn vào biểu tượng thông báo
        btnNoti.setOnClickListener(v -> Toast.makeText(this, "Bạn đã nhấn vào thông báo", Toast.LENGTH_SHORT).show());

        // Sự kiện khi chọn nhóm công việc
        tvTaskGroup.setOnClickListener(v -> Toast.makeText(this, "Bạn có thể chọn nhóm công việc khác", Toast.LENGTH_SHORT).show());

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
            Task task = new Task(name, desc, startDate, endDate);
            list.add(task);

            // Quay về màn hình trước
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
