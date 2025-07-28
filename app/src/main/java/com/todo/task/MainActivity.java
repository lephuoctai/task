package com.todo.task;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

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

        // Kiểm tra người dùng đăng nhập qua lớp User đã tạo
        if (User.isExist()) {
            // Lấy người dùng Firebase hiện tại
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                Log.d("TM", "onCreate: Fbase mail: " + FirebaseAuth.getInstance().getCurrentUser().getEmail());
                Log.d("TM", "onCreate: Fbase uid: " + FirebaseAuth.getInstance().getCurrentUser().getUid());
                Log.d("TM", "onCreate: Fbase name: " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            } else {
                Log.d("TM", "onCreate: Firebase User hiện tại null");
            }

            Log.d("TM", "onCreate: User uid: " + User.getInstance().getUid());
            Log.d("TM", "onCreate: User name: " + User.getInstance().getName());

            // Điều hướng sang welcome_screen
            Intent intent = new Intent(this, WelcomeScreen.class);
            startActivity(intent);
            finish(); // Kết thúc activity này để không quay lại truy cập login
        }

        // Nút "Bắt đầu" chuyển sang SignIn
        ImageButton startBtn = findViewById(R.id.button2);
        if (startBtn != null) {
            startBtn.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, SignIn.class);
                startActivity(intent);
                finish(); // Kết thúc main để không quay lại
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Nếu cần cập nhật lại danh sách task khi quay lại activity này,
        // bạn có thể gọi load lại hoặc renderTasks() ở đây
        // Hiện tại đã chuyển render sang ProjectListActivity nên không cần ở đây.
    }
}
