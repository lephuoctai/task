package com.todo.task;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class EditProjectActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_project);

        EditText etProjectName = findViewById(R.id.etProjectName);
        EditText etDescription = findViewById(R.id.etDescription);
        TextView tvTaskGroup = findViewById(R.id.tvTaskGroup);
        TextView tvStarDate = findViewById(R.id.tvStarDate);
        TextView tvEndDate = findViewById(R.id.tvEndDate);
        ImageView igIcDropdown = findViewById(R.id.ig_ic_dropdown);
        Button btnAddProject = findViewById(R.id.btnAddProject);
        ImageButton btnBack = findViewById(R.id.btnBack);
        ImageButton btnNoti = findViewById(R.id.btnNoti);
        String[] taskGroups = {"Công việc", "Cá nhân", "Học tập", "Khác"};
        final Calendar calendar = Calendar.getInstance();

        // Nhận dữ liệu project từ Intent
        android.content.Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String desc = intent.getStringExtra("desc");
        String group = intent.getStringExtra("group");
        String start = intent.getStringExtra("start");
        String end = intent.getStringExtra("end");
        int position = intent.getIntExtra("position", -1);

        if (name != null) etProjectName.setText(name);
        if (desc != null) etDescription.setText(desc);
        if (group != null) tvTaskGroup.setText(group);
        if (start != null) tvStarDate.setText(start);
        if (end != null) tvEndDate.setText(end);

        btnBack.setOnClickListener(v -> finish());

        tvTaskGroup.setOnClickListener(v -> {
            new android.app.AlertDialog.Builder(this)
                .setTitle("Chọn nhóm công việc")
                .setItems(taskGroups, (dialog, which) -> {
                    tvTaskGroup.setText(taskGroups[which]);
                })
                .show();
        });
        if (igIcDropdown != null) {
            igIcDropdown.setOnClickListener(v -> {
                new android.app.AlertDialog.Builder(this)
                    .setTitle("Chọn nhóm công việc")
                    .setItems(taskGroups, (dialog, which) -> {
                        tvTaskGroup.setText(taskGroups[which]);
                    })
                    .show();
            });
        }

        tvStarDate.setOnClickListener(v -> showDatePicker(tvStarDate));
        tvEndDate.setOnClickListener(v -> showDatePicker(tvEndDate));

        btnAddProject.setOnClickListener(v -> {
            // Lưu thay đổi vào danh sách project
            String newName = etProjectName.getText().toString().trim();
            String newDesc = etDescription.getText().toString().trim();
            String newGroup = tvTaskGroup.getText().toString();
            String newStart = tvStarDate.getText().toString();
            String newEnd = tvEndDate.getText().toString();
            if (position >= 0 && position < MainActivity.taskList.size()) {
                Task task = MainActivity.taskList.get(position);
                task.setName(newName);
                task.setQuest(newDesc);
                task.setGroup(newGroup);
                task.setDateBegin(newStart);
                task.setDateCompleted(newEnd);
                // Lưu lại danh sách Task sau khi sửa
                TaskStorage.saveTasks(this, MainActivity.taskList);
                // Trả kết quả về ProjectListActivity
                setResult(RESULT_OK);
            }
            Toast.makeText(this, "Đã lưu thay đổi!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void showDatePicker(TextView target) {
        Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String dateStr = String.format("00:00 %02d-%02d-%d", dayOfMonth, month + 1, year);
            target.setText(dateStr);
        }, y, m, d).show();
    }
}
