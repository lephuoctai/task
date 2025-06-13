package com.todo.task;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static ArrayList<Task> task_list = new ArrayList<>();
    public void scanTask() {
        EditText task_name = findViewById(R.id.task_name);
        EditText task_quest = findViewById(R.id.task_quest);
        EditText task_date_completed = findViewById(R.id.task_date_completed);

        String name = task_name.getText().toString().trim();
        String quest = task_quest.getText().toString().trim();
        String date_completed = task_date_completed.getText().toString().trim();

        if(!(name.isEmpty() || quest.isEmpty())){
            LinearLayout task_layout = findViewById(R.id.task_list);
            TextView view = new TextView(MainActivity.this);
            Task task = new Task(name, quest, date_completed);

            task_list.add(task);
            view.setText( (task_list.indexOf(task) + 1) + " - "+ task.toString());
            task_layout.addView(view);

        }
    }
    public void printArray(){
        for (Task task:task_list) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button send_btn = findViewById(R.id.add_btn);
        send_btn.setOnClickListener(view -> {
            scanTask();
            printArray();
        });
    }
}