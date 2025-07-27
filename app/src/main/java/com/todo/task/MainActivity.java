package com.todo.task;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.todo.task.SignInUp.SignIn;
import com.todo.task.user.User;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Danh sách các Task dùng chung toàn app (dùng static để truy cập từ activity khác)
    static ArrayList<Task> taskList = new ArrayList<>();
    // Danh sách ghi chú dùng chung toàn app
    public static ArrayList<Note> noteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Đọc danh sách Task từ file khi khởi động app
        taskList = new ArrayList<>(TaskStorage.loadTasks(this));
        // Nếu người dùng đã đăng nhập thì chuyển hướng đến welcome_screen
        if(User.isExist()) {
            Log.d("TM", "onCreate: Fbase mail:" + FirebaseAuth.getInstance().getCurrentUser().getEmail());
            Log.d("TM", "onCreate: User uid:" + User.getInstance().getUid());
            Log.d("TM", "onCreate: User name:" + User.getInstance().getName());
            Log.d("TM", "onCreate: Fbase uid:" + FirebaseAuth.getInstance().getCurrentUser().getUid());
            Log.d("TM", "onCreate: Fbase name:" + FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

            // Chuyển hướng đến welcome_screen
            Intent intent = new Intent(this, WelcomeScreen.class);
            startActivity(intent);
            finish(); // Kết thúc activity này để không quay lại truy cập login
        }

        // Chuyển hướng đến sign_in khi nhấn nút "Bắt đầu"
        ImageButton startBtn = findViewById(R.id.button2);
        if (startBtn != null) {
            startBtn.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, SignIn.class);
                startActivity(intent);
                finish();
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