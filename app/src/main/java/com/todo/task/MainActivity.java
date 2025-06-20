package com.todo.task;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Danh sách các Task dùng chung toàn app (dùng static để truy cập từ activity khác)
    static ArrayList<Task> taskList = new ArrayList<>();
    // Danh sách ghi chú dùng chung toàn app
    public static ArrayList<Note> noteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Đọc danh sách Task từ file khi khởi động app
        taskList = new ArrayList<>(TaskStorage.loadTasks(this));

        // Gắn lại phần padding tự động nếu thiết bị có thanh điều hướng ảo
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Gán sự kiện cho nút "Bắt đầu" để mở ProjectListActivity
        Button startBtn = findViewById(R.id.button2);
        if (startBtn != null) {
            startBtn.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, ProjectListActivity.class);
                startActivity(intent);
            });
        }
    }

    /**
     * Hàm onResume được gọi mỗi lần màn hình này được hiển thị lại (kể cả sau khi quay lại từ AddProject)
     * → Dùng để render lại danh sách task
     */
    @Override
    protected void onResume() {
        super.onResume();
        // renderTasks(); // Đã chuyển sang ProjectListActivity, không cần render ở đây nữa
    }
}