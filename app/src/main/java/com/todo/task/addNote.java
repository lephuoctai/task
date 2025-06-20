package com.todo.task;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class addNote extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Nút quay lại
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        // Nút thông báo
        findViewById(R.id.btnNoti).setOnClickListener(v ->
            Toast.makeText(this, "Bạn đã nhấn vào thông báo", Toast.LENGTH_SHORT).show()
        );
        // Nút thêm ghi chú
        Button btnAddNote = findViewById(R.id.button);
        EditText contentNote = findViewById(R.id.contentNote);
        btnAddNote.setOnClickListener(v -> {
            String content = contentNote.getText().toString().trim();
            if (content.isEmpty()) {
                contentNote.setError("Nội dung ghi chú không được để trống!");
            } else {
                MainActivity.noteList.add(new Note(content));
                Toast.makeText(this, "Đã thêm ghi chú!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}