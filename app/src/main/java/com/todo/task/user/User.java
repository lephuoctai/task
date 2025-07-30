package com.todo.task.user;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.todo.task.MainActivity;
import com.todo.task.SendCallback;

public final class User {
    private FirebaseUser firebaseUser = null;
    private String uid = null;
    private String name = null;
    private String email = null;

    // Singleton pattern
    private static User instance = null;

    /// Constructor tổ chức theo singleton pattern
    private User() {
        this.Update();
    }
    public static synchronized User getInstance() {
        if(instance == null) {
            instance = new User();
        }
        else {
            // Cập nhật thông tin User
            instance.Update();
        }
        return instance;
    }

    /// Cập nhật thông tin User
    public void Update(){
        this.firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // kiểm tra người dùng có tồn tại hay không
        if(this.firebaseUser == null) {
            this.uid = null;
            this.name = null;
            this.email = null;
            Log.d("TM", " -Update: Thông tin người dùng không tồn tại");
            return;
        };

        // Cập nhật uid
        String newUid = firebaseUser.getUid();
        // kiểm tra có thay đổi người đăng nhập hay không
        if(this.uid != newUid) {
            this.uid = firebaseUser.getUid();

            // lấy tên người dùng
            this.name = firebaseUser.getEmail().substring(0, firebaseUser.getEmail().indexOf("@"));
            // lấy email người dùng
            this.email = firebaseUser.getEmail();
        }
        Log.d("TM", " -Update: Thông tin người dùng đã được cập nhật");
    }
    // kiểm tra người dùng có tồn tại hay không
    public static boolean isExist() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
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
                   instance.Update();
                   callBack.onLoginSuccess(User.this);
                     Log.d("TM"," -Login: Đăng nhập thành công");
               } else {
                   callBack.onLoginFailure(task.getException().getMessage());
                     Log.d("TM"," -Login: Đăng nhập thất bại: " + task.getException().getMessage());
               }
            });
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
        instance.Update();
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


    // Getter methods
    public String getUid() {
        return uid;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }
}
