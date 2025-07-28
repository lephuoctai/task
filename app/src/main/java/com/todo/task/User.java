package com.todo.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

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
        /// Lỗi không tồn tại tên người dùng nhưng điều kiện vẫn thoã!
        if (firebaseUser != null && firebaseUser.getDisplayName() != null) {
            return firebaseUser.getDisplayName();
        } else if (firebaseUser != null && firebaseUser.getEmail() != null) {
            return firebaseUser.getEmail().substring(0, firebaseUser.getEmail().indexOf("@"));
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
    /// logout method
    public static void logout() {
        // kiểm tra người dùng có tồn tại hay không
        if(User.isExist() == false) {
            Log.d("TM"," -logout: Người dùng chưa tồn tại");
            return;
        }

        // Đăng xuất
        FirebaseAuth.getInstance().signOut();
        // Cập nhật lại thông tin User
        getInstance();
        Log.d("TM"," -logout: Đã đăng xuất");
    }

    public static void logout(Context context) {
        logout();

        // Chuyển hướng về MainActivity
        changeActivity(context);
    }

    // Chuyển hướng
    private static void changeActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /// register method
    public void register(String email, String password, SendCallback callBack) {
        if(User.isExist() == false) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    callBack.onLoginSuccess(User.this);
                    Log.d("TM"," -register: Đăng ký thành công");
                } else {
                    callBack.onLoginFailure(task.getException().getMessage());
                    Log.d("TM"," -register: Đăng ký thất bại: " + task.getException().getMessage());
                }
            });
        } else {
            Log.d("TM"," -register: Người dùng đã tồn tại");
            callBack.onLoginFailure("Người dùng đã tồn tại");
        }
    }

    /// login method
    // đăng nhập bằng email và mật khẩu
    public void Login(String email, String password, SendCallback callBack) {
        // FirebaseUser nếu người dùng chưa tồn tại
        if(User.isExist() == false) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    getInstance();
                    callBack.onLoginSuccess(User.this);
                } else {
                    callBack.onLoginFailure(task.getException().getMessage());
                }
            });
        }
    }

}
