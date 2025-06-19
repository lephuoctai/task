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
    static ArrayList<Task> taskList = new ArrayList<>();
    private EditText taskNameInput, taskQuestInput, taskDateCompletedInput;
    private LinearLayout taskLayout;
    private Button taskAddBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        taskNameInput = findViewById(R.id.task_name);
        taskQuestInput = findViewById(R.id.task_quest);
        taskDateCompletedInput = findViewById(R.id.task_date_completed);
        taskLayout = findViewById(R.id.task_list);
        taskAddBtn = findViewById(R.id.add_btn);

        taskAddBtn.setOnClickListener(view -> {
            scanTask();
            printArray();
        });

        Button openProjectBtn = findViewById(R.id.btn_open_project);
        openProjectBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, addProjectActivity.class);
            startActivity(intent);
        });
    }

    private void scanTask() {
        String name = taskNameInput.getText().toString().trim();
        String quest = taskQuestInput.getText().toString().trim();
        String date = taskDateCompletedInput.getText().toString().trim();

        if (name.isEmpty() || quest.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidDateFormat(date)) {
            Toast.makeText(this, "Ngày không đúng định dạng: HH:mm dd-MM-yyyy", Toast.LENGTH_LONG).show();
            return;
        }

        Task task = new Task(name, quest, date);
        taskList.add(task);

        TextView view = new TextView(MainActivity.this);
        view.setText((taskList.indexOf(task) + 1) + " - " + task.toString());
        taskLayout.addView(view);
    }

    private boolean isValidDateFormat(String dateStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
            LocalDateTime.parse(dateStr, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void printArray() {
        for (Task task : taskList) {
            // xử lý thêm nếu cần
        }
    }
}
