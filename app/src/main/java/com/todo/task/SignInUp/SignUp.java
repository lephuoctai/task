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
import com.todo.task.SendCallback;
import com.todo.task.user.User;

public class SignUp extends AppCompatActivity {
    private ImageButton registerBtn;
    private Button loginBtn;
    private EditText inputName, inputPassword, inputPasswordAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        inputName = findViewById(R.id.name);
        inputPassword = findViewById(R.id.password);
        inputPasswordAgain = findViewById(R.id.passwordAgain);
        registerBtn = findViewById(R.id.btn_signUp);
        loginBtn = findViewById(R.id.login_btn);

        // Chuyển sang trang đăng nhập
        loginBtn.setOnClickListener(v -> startActivity(new Intent(SignUp.this, SignIn.class)));

        // Xử lý đăng nhập với email/password
        registerBtn.setOnClickListener(v -> {
            String name = inputName.getText().toString();
            String password = inputPassword.getText().toString();
            String passwordAgain = inputPasswordAgain.getText().toString();

            // Kiểm tra thông tin đăng ký
            if (name.isEmpty() || password.isEmpty() || passwordAgain.isEmpty()) { // Kiểm tra không để trống thông tin
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }else if(password.equals(passwordAgain) == false){ // Kiểm tra mật khẩu
                Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            // Gọi phương thức đăng ký của User
            User.getInstance().register(name, password, new SendCallback() {
                @Override
                // Gọi khi đăng ký thành công
                public void onLoginSuccess(User user) {
                    Toast.makeText(SignUp.this, User.getInstance().getEmail() + " đăng ký thành công\n" + "Vui lòng đăng nhập!", Toast.LENGTH_SHORT).show();

                    // Trường hợp User còn đăng nhập thì đăng xuất
                    if(User.isExist()){
                        User.logout();
                    }

                    // Chuyển sang trang đăng nhập
                    Intent intent = new Intent(SignUp.this, SignIn.class);
                    startActivity(intent);

                    // Kết thúc
                    finish();
                }

                // Gọi khi đăng ký thất bại
                public void onLoginFailure(String errorMessage) {
                    Toast.makeText(SignUp.this, "Đăng ký thất bại:\n" + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });

    }
}