package com.todo.task;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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

        if (!(name.isEmpty() || quest.isEmpty())) {
            Task task = new Task(name, quest, date);
            taskList.add(task);
            TextView view = new TextView(MainActivity.this);
            view.setText((taskList.indexOf(task) + 1) + " - " + task.toString());
            taskLayout.addView(view);
        }
    }

    private void printArray() {
        for (Task task : taskList) {
            // Tuỳ bạn muốn xử lý gì thêm
        }
    }
}
