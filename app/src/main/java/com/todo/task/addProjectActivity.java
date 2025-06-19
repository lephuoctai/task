package com.todo.task;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class addProjectActivity extends AppCompatActivity {

    // Khai báo view và biến cần dùng
    private TextView tvTaskGroup, tvStarDate, tvEndDate;
    private EditText etProjectName, etDescription;
    private Button btnAddProject;
    private ImageButton btnBack, btnNoti;
    private String startDate = "", endDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);

        // Ánh xạ view
        btnBack = findViewById(R.id.btnBack);
        btnNoti = findViewById(R.id.btnNoti);
        tvTaskGroup = findViewById(R.id.tvTaskGroup);
        tvStarDate = findViewById(R.id.tvStarDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        etProjectName = findViewById(R.id.etProjectName);
        etDescription = findViewById(R.id.etDescription);
        btnAddProject = findViewById(R.id.btnAddProject);

        // Sự kiện nút quay lại
        btnBack.setOnClickListener(v -> finish());

        // Sự kiện thông báo
        btnNoti.setOnClickListener(v ->
                Toast.makeText(this, "Notification Clicked", Toast.LENGTH_SHORT).show());

        // Đổi nhóm task (giả lập)
        tvTaskGroup.setOnClickListener(v ->
                Toast.makeText(this, "Change Task Group", Toast.LENGTH_SHORT).show());

        // Chọn ngày bắt đầu
        tvStarDate.setOnClickListener(v -> showDatePicker(tvStarDate));

        // Chọn ngày kết thúc
        tvEndDate.setOnClickListener(v -> showDatePicker(tvEndDate));

        // Thêm dự án
        btnAddProject.setOnClickListener(v -> addProject());
    }

    /**
     * Hiển thị DatePicker và định dạng ngày giờ theo "HH:mm dd-MM-yyyy"
     */
    private void showDatePicker(TextView target) {
        Calendar calendar = Calendar.getInstance();

        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);

        // Mở DatePickerDialog
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            // Sau khi chọn ngày, lấy giờ/phút hiện tại
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            // Định dạng theo "HH:mm dd-MM-yyyy"
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.getDefault());
            String dateStr = sdf.format(calendar.getTime());

            // Hiển thị lên TextView và lưu vào biến
            target.setText(dateStr);
            if (target.getId() == R.id.tvStarDate) {
                startDate = dateStr;
            } else if (target.getId() == R.id.tvEndDate) {
                endDate = dateStr;
            }
        }, y, m, d).show();
    }

    /**
     * Kiểm tra và thêm thông tin dự án
     */
    private void addProject() {
        String name = etProjectName.getText().toString().trim();
        String desc = etDescription.getText().toString().trim();

        if (name.isEmpty()) {
            etProjectName.setError("Project name can't be empty!");
        } else if (!startDate.isEmpty() && !endDate.isEmpty()) {
            String info = "Project added:\nTên: " + name
                    + "\nMô tả: " + desc
                    + "\nTừ ngày: " + startDate
                    + "\nĐến ngày: " + endDate;
            Toast.makeText(this, info, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Vui lòng chọn ngày bắt đầu và ngày kết thúc!", Toast.LENGTH_SHORT).show();
        }
    }
}