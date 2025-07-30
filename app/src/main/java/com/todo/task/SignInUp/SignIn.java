package com.todo.task.SignInUp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.todo.task.R;
import com.todo.task.WelcomeScreen;
import com.todo.task.SendCallback;
import com.todo.task.ResetPassword;
import com.todo.task.user.User;

public class SignIn extends AppCompatActivity {
    private Button registerBtn;
    private Button resetPasswordBtn;
    private ImageButton loginBtn;
    private EditText inputName, inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);

        Intent intent = new Intent(this, SignUp.class);
        inputName = findViewById(R.id.name);
        inputPassword = findViewById(R.id.password);
        registerBtn = findViewById(R.id.register_btn);
        loginBtn = findViewById(R.id.btn_signIn);
        resetPasswordBtn = findViewById(R.id.register_btn);

// Chuyển sang màn hình đăng ký
        registerBtn.setOnClickListener(v -> startActivity(intent));
// Chuyển sang
        resetPasswordBtn.setOnClickListener(v -> {
            Intent resetIntent = new Intent(SignIn.this, ResetPassword.class);
            startActivity(resetIntent);
        });

// Xử lý đăng nhập với email/password
        loginBtn.setOnClickListener(v -> {
            String name = inputName.getText().toString();
            String password = inputPassword.getText().toString();

            // Kiểm tra thông tin đăng nhập được điền đủ
            if (name.isEmpty() || password.isEmpty()) {
                Toast.makeText(SignIn.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Gọi phương thức đăng nhập của User
            User.getInstance().Login(name, password, new SendCallback() {
                @Override
                // Gọi khi đăng nhập thành công
                public void onLoginSuccess(User user) {
                    Toast.makeText(SignIn.this, "Chào mừng " + user.getName() + " đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignIn.this, WelcomeScreen.class);
                    intent.putExtra("userName", user.getName());
                    startActivity(intent);

                    // Kết thúc
                    finish();
                }

                // Gọi khi đăng nhập thất bại
                public void onLoginFailure(String errorMessage) {
                    Toast.makeText(SignIn.this, "Đăng nhập thất bại:\n" + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}