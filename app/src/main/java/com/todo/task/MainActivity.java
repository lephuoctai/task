package com.todo.task;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    // Tạo danh sách tạm thời để lưu trữ các Task
    static ArrayList<Task> taskList = new ArrayList<>();

    // Các thành phần giao diện
    private EditText taskNameInput, taskQuestInput, taskDateCompletedInput;
    private LinearLayout taskLayout;
    private Button taskAddBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Đảm bảo layout hiển thị chính xác khi có thanh điều hướng/system bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ view
        taskNameInput = findViewById(R.id.task_name);
        taskQuestInput = findViewById(R.id.task_quest);
        taskDateCompletedInput = findViewById(R.id.task_date_completed);
        taskLayout = findViewById(R.id.task_list);
        taskAddBtn = findViewById(R.id.add_btn);

        // Mở DatePicker khi người dùng chọn ô nhập ngày
        taskDateCompletedInput.setFocusable(false);
        taskDateCompletedInput.setOnClickListener(v -> showDatePicker(taskDateCompletedInput));

        // Sự kiện khi nhấn nút thêm Task
        taskAddBtn.setOnClickListener(view -> {
            scanTask();
        });

        // Chuyển sang màn hình thêm Project
        Button openProjectBtn = findViewById(R.id.btn_open_project);
        openProjectBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, addProjectActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Lấy dữ liệu từ EditText, kiểm tra hợp lệ và tạo Task mới nếu đúng
     */
    private void scanTask() {
        String name = taskNameInput.getText().toString().trim();
        String quest = taskQuestInput.getText().toString().trim();
        String date = taskDateCompletedInput.getText().toString().trim();

        // Kiểm tra nếu thiếu thông tin
        if (name.isEmpty() || quest.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền thông tin đầy đủ!", Toast.LENGTH_SHORT).show();
        }
        // Kiểm tra định dạng ngày hợp lệ và chưa quá hạn
        else if (!isDateValid(date)) {
            Toast.makeText(this, "Ngày hoàn thành không hợp lệ!", Toast.LENGTH_SHORT).show();
        }
        // Nếu hợp lệ → tạo Task, hiển thị trên màn hình
        else {
            Task task = new Task(name, quest, date);
            taskList.add(task);
            TextView view = new TextView(MainActivity.this);
            view.setText((taskList.indexOf(task) + 1) + " - " + task.getName() + " | " + task.getStatus());
            taskLayout.addView(view);
        }
    }

    /**
     * Kiểm tra xem ngày hoàn thành có hợp lệ (tức là trong tương lai) hay không
     */
    private boolean isDateValid(String date) {
        LocalDateTime now = LocalDateTime.now();
        return now.isBefore(parseDateTime(date));
    }

    /**
     * Chuyển chuỗi ngày hoàn thành thành kiểu LocalDateTime
     */
    private LocalDateTime parseDateTime(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
        return LocalDateTime.parse(dateStr, formatter);
    }

    /**
     * Hiển thị DatePicker cho người dùng chọn ngày hoàn thành.
     * Gán kết quả vào ô EditText theo định dạng "00:00 dd-MM-yyyy"
     */
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
