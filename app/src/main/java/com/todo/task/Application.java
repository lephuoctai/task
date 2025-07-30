package com.todo.task;

import com.google.firebase.database.FirebaseDatabase;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Bật tính năng lưu dữ liệu offline Firebase Realtime Database
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}