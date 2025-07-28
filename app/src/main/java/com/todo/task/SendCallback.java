package com.todo.task;

import com.todo.task.User;

// Trả về kết quả đăng nhập
public interface SendCallback {
    void onLoginSuccess(User user);
    void onLoginFailure(String errorMessage);
}
