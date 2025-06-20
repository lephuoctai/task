package com.todo.task;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TaskStorage {
    private static final String FILE_NAME = "tasks.json";
    private static final Gson gson = new Gson().newBuilder()
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
        .create();

    // TypeAdapter cho LocalDateTime
    public static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
        @Override
        public void write(JsonWriter out, LocalDateTime value) throws IOException {
            out.value(value != null ? value.format(formatter) : null);
        }
        @Override
        public LocalDateTime read(JsonReader in) throws IOException {
            String str = in.nextString();
            if (str == null || str.isEmpty()) return null;
            return LocalDateTime.parse(str, formatter);
        }
    }

    // Lưu danh sách Task vào file
    public static void saveTasks(Context context, List<Task> tasks) {
        try {
            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
            String json = gson.toJson(tasks);
            writer.write(json);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Đọc danh sách Task từ file
    public static List<Task> loadTasks(Context context) {
        List<Task> tasks = new ArrayList<>();
        try {
            FileInputStream fis = context.openFileInput(FILE_NAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            String json = sb.toString();
            Type type = new TypeToken<List<Task>>(){}.getType();
            tasks = gson.fromJson(json, type);
            if (tasks == null) tasks = new ArrayList<>();
        } catch (Exception e) {
            // Nếu file chưa tồn tại hoặc lỗi thì trả về danh sách rỗng
        }
        return tasks;
    }
}
