package com.todo.task;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class addProjectActivity extends AppCompatActivity {
    private TextView tvTaskGroup, tvStarDate, tvEndDate;
    private EditText etProjectName, etDescription;
    private Button btnAddProject;
    private ImageButton btnBack, btnNoti;
    private String startDate = "", endDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);

        btnBack = findViewById(R.id.btnBack);
        btnNoti = findViewById(R.id.btnNoti);
        tvTaskGroup = findViewById(R.id.tvTaskGroup);
        tvStarDate = findViewById(R.id.tvStarDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        etProjectName = findViewById(R.id.etProjectName);
        etDescription = findViewById(R.id.etDescription);
        btnAddProject = findViewById(R.id.btnAddProject);

        btnBack.setOnClickListener(v -> finish());
        btnNoti.setOnClickListener(v -> Toast.makeText(this, "Notification Clicked", Toast.LENGTH_SHORT).show());
        tvTaskGroup.setOnClickListener(v -> Toast.makeText(this, "Change Task Group", Toast.LENGTH_SHORT).show());

        tvStarDate.setOnClickListener(v -> showDatePicker(tvStarDate));
        tvEndDate.setOnClickListener(v -> showDatePicker(tvEndDate));
        btnAddProject.setOnClickListener(v -> addProject());
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

        if (name.isEmpty()) {
            etProjectName.setError("Project name can't be empty!");
        } else if (!startDate.isEmpty() && !endDate.isEmpty()) {
            String info = "Project added:\nTên: " + name + "\nMô tả: " + desc + "\nTừ ngày: " + startDate + "\nĐến ngày: " + endDate;
            Toast.makeText(this, info, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Vui lòng chọn ngày bắt đầu và ngày kết thúc!", Toast.LENGTH_SHORT).show();
        }
    }
}
