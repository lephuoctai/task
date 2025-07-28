package com.todo.task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.todo.task.ProjectListActivity;

public class User {
    private static User instance;

    private FirebaseUser firebaseUser;

    // Private constructor (singleton)
    private User() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    // Lấy instance duy nhất của lớp User
    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        } else {
            // Cập nhật lại user mới nhất mỗi khi gọi
            instance.firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        }
        return instance;
    }

    // Kiểm tra người dùng đã đăng nhập chưa
    public static boolean isExist() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null;
    }

    public static void logout(ProjectListActivity projectListActivity) {
    }

    // Lấy UID người dùng
    public String getUid() {
        if (firebaseUser != null) {
            return firebaseUser.getUid();
        } else {
            return null;
        }
    }

    // Lấy tên người dùng (display name)
    public String getName() {
        if (firebaseUser != null && firebaseUser.getDisplayName() != null) {
            return firebaseUser.getDisplayName();
        } else if (firebaseUser != null && firebaseUser.getEmail() != null) {
            return firebaseUser.getEmail(); // trả về email nếu không có display name
        } else {
            return "Unknown";
        }
    }

    // Lấy email người dùng
    public String getEmail() {
        if (firebaseUser != null) {
            return firebaseUser.getEmail();
        } else {
            return null;
        }
    }

    // Đăng xuất người dùng
    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        instance = null;
    }
}
