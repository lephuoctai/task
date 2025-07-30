package com.todo.task;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.todo.task.SignInUp.SignIn;
import com.todo.task.databinding.ActivityResetPasswordBinding;

public class ResetPassword extends AppCompatActivity {
    ActivityResetPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.email.getText().toString();

                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener( task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ResetPassword.this, "Đã gửi link đến email của bạn!", Toast.LENGTH_SHORT).show();
                        Log.d("TM", "ResetPass: Đã gửi link đến email " + email);
                        Intent intent = new Intent(ResetPassword.this, SignIn.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }
}