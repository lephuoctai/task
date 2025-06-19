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

    // Layout chứa danh sách task hiển thị ra màn hình
    private LinearLayout taskLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Gắn lại phần padding tự động nếu thiết bị có thanh điều hướng ảo
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Gán sự kiện cho nút "Tạo mới" để mở màn hình thêm dự án
        Button openProjectBtn = findViewById(R.id.btn_open_project);
        openProjectBtn.setOnClickListener(v -> {
            // Chuyển sang màn hình addProjectActivity
            Intent intent = new Intent(MainActivity.this, addProjectActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Hàm onResume được gọi mỗi lần màn hình này được hiển thị lại (kể cả sau khi quay lại từ AddProject)
     * → Dùng để render lại danh sách task
     */
    @Override
    protected void onResume() {
        super.onResume();
        renderTasks(); // Cập nhật lại giao diện danh sách công việc
    }

    /**
     * Hàm hiển thị danh sách các công việc (task) ra màn hình chính
     * - Xóa danh sách cũ
     * - Lặp qua từng Task trong `taskList`, tạo TextView để hiển thị
     */
    private void renderTasks() {
        // Lấy lại đối tượng layout chứa danh sách công việc
        taskLayout = findViewById(R.id.task_list);

        // Xóa toàn bộ các view con (task cũ)
        taskLayout.removeAllViews();

        // Tạo TextView cho mỗi task và thêm vào layout
        for (int i = 0; i < taskList.size(); i++) {
            Task task = taskList.get(i);

            // Tạo view hiển thị task
            TextView view = new TextView(MainActivity.this);
            view.setText((i + 1) + " - " + task.getName() + " | " + task.getQuest() + " | " + task.getStatus());

            // Thêm vào danh sách
            taskLayout.addView(view);
        }
    }
}
