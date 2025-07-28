package com.todo.task;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.todo.task.User;



public class WelcomeScreen extends AppCompatActivity {
    private TextView welcomeText;
    private ImageButton btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome_screen);

        welcomeText = findViewById(R.id.welcome_text);
        btn_next = findViewById(R.id.btn_bg);
        User user = User.getInstance();

        // Gán tên người dùng vào màn hình chào mừng
        String welcomePerson = user.getName() + "," + welcomeText.getText().toString();
        welcomeText.setText(welcomePerson);

        // Chuyển màng hình tới workspace
        btn_next.setOnClickListener(v -> {
            startActivity(new Intent(this, ProjectListActivity.class));
            Log.d("TM", "-WelcomeScreen: Chuyển sang projectList");
            finish();
        });
    }
}