package com.todo.task;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;

public class ProjectListActivity extends AppCompatActivity {
    private LinearLayout listRecent, listOld;
    private ArrayList<Task> taskList = MainActivity.taskList;
    private enum SortType { NAME, DATE }
    private SortType sortType = SortType.NAME;
    private boolean isSortAZ = true;
    private TextView tvSortType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);
        listRecent = findViewById(R.id.list_recent);
        listOld = findViewById(R.id.list_old);
        tvSortType = findViewById(R.id.tvSortType);

        // Nút quay lại
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Nút thông báo: Hiển thị tên các project còn dưới 3 ngày
        findViewById(R.id.btnNoti).setOnClickListener(v -> {
            StringBuilder sb = new StringBuilder();
            for (Task t : taskList) {
                if (t.dayLeft() > 0 && t.dayLeft() < 3) {
                    sb.append("- ").append(t.getName()).append(" (còn ").append(t.dayLeft()).append(" ngày)\n");
                }
            }
            String msg = sb.length() > 0 ? sb.toString() : "Không có project nào sắp hết hạn!";
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle("Project sắp hết hạn (<3 ngày)")
                .setMessage(msg)
                .setPositiveButton("Đóng", null)
                .show();
        });

        // Nút thêm mới
        findViewById(R.id.btnNewTask).setOnClickListener(v ->
            startActivity(new android.content.Intent(this, addProjectActivity.class))
        );

        // Sắp xếp khi nhấn vào icon sort
        ImageView sortBtn = findViewById(R.id.ic_sort);
        if (sortBtn != null) {
            sortBtn.setOnClickListener(v -> {
                String[] options = {"Tên (A-Z)", "Tên (Z-A)", "Ngày (Gần nhất)", "Ngày (Xa nhất)"};
                new android.app.AlertDialog.Builder(this)
                    .setTitle("Sắp xếp dự án")
                    .setItems(options, (dialog, which) -> {
                        switch (which) {
                            case 0:
                                sortType = SortType.NAME;
                                isSortAZ = true;
                                break;
                            case 1:
                                sortType = SortType.NAME;
                                isSortAZ = false;
                                break;
                            case 2:
                                sortType = SortType.DATE;
                                isSortAZ = true;
                                break;
                            case 3:
                                sortType = SortType.DATE;
                                isSortAZ = false;
                                break;
                        }
                        updateSortTypeText();
                        renderTasks();
                    })
                    .show();
            });
        }

        // Nút dropdown ở phần sort: ví dụ hiển thị menu chọn kiểu sort
        ImageView sortDropdownBtn = findViewById(R.id.sort_ic_dropdown);
        if (sortDropdownBtn != null) {
            sortDropdownBtn.setOnClickListener(v -> {
                String[] options = {"Tên (A-Z)", "Tên (Z-A)", "Ngày (Gần nhất)", "Ngày (Xa nhất)"};
                new android.app.AlertDialog.Builder(this)
                    .setTitle("Sắp xếp dự án")
                    .setItems(options, (dialog, which) -> {
                        switch (which) {
                            case 0:
                                sortType = SortType.NAME;
                                isSortAZ = true;
                                break;
                            case 1:
                                sortType = SortType.NAME;
                                isSortAZ = false;
                                break;
                            case 2:
                                sortType = SortType.DATE;
                                isSortAZ = true;
                                break;
                            case 3:
                                sortType = SortType.DATE;
                                isSortAZ = false;
                                break;
                        }
                        updateSortTypeText();
                        renderTasks();
                    })
                    .show();
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateSortTypeText();
        renderTasks();
    }

    @Override
    protected void onPause() {
        super.onPause();
        TaskStorage.saveTasks(this, taskList);
    }

    private void updateSortTypeText() {
        if (tvSortType == null) return;
        if (sortType == SortType.NAME) {
            tvSortType.setText(isSortAZ ? "A-Z" : "Z-A");
        } else {
            tvSortType.setText(isSortAZ ? "Ngày gần nhất" : "Ngày xa nhất");
        }
    }

    private void renderTasks() {
        listRecent.removeAllViews();
        listOld.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        // Sắp xếp theo thời gian chỉnh sửa gần nhất (updatedAt), xử lý null
        Collections.sort(taskList, (t1, t2) -> {
            if (t1.getUpdatedAt() == null && t2.getUpdatedAt() == null) return 0;
            if (t1.getUpdatedAt() == null) return 1;
            if (t2.getUpdatedAt() == null) return -1;
            return t2.getUpdatedAt().compareTo(t1.getUpdatedAt());
        });

        // Hiển thị dự án gần nhất ở listRecent với layout item_task_recent
        if (!taskList.isEmpty()) {
            Task recentTask = taskList.get(0);
            View recentView = inflater.inflate(R.layout.item_task_recent, listRecent, false);
            LinearLayout infoLayout = recentView.findViewById(R.id.infoLayout);
            ImageView icDropdown = recentView.findViewById(R.id.ic_dropdown);

            if (infoLayout != null && infoLayout.getChildCount() >= 2) {
                TextView tvName = (TextView) infoLayout.getChildAt(0);
                TextView tvStatus = (TextView) infoLayout.getChildAt(1);
                if (tvName != null) tvName.setText(recentTask.getName());
                if (tvStatus != null) tvStatus.setText(recentTask.getStatus());
            }

            // Xử lý sự kiện click vào mũi tên dropdown
            if (icDropdown != null) {
                icDropdown.setOnClickListener(v -> {
                    // Tạo AlertDialog với layout tùy chỉnh
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                    builder.setTitle(recentTask.getName());

                    // Hiển thị mô tả của dự án
                    builder.setMessage(recentTask.getQuest());

                    // Thêm các nút tùy chọn
                    builder.setPositiveButton("Chỉnh sửa", (dialog, which) -> {
                        // Mở EditProjectActivity để chỉnh sửa
                        android.content.Intent intent = new android.content.Intent(this, EditProjectActivity.class);
                        intent.putExtra("name", recentTask.getName());
                        intent.putExtra("desc", recentTask.getQuest());
                        intent.putExtra("group", recentTask.getGroup());
                        intent.putExtra("start", recentTask.getDateBegin());
                        intent.putExtra("end", recentTask.getDateCompleted());
                        intent.putExtra("position", 0);
                        startActivity(intent);
                    });

                    builder.setNegativeButton("Xóa", (dialog, which) -> {
                        // Xác nhận xóa
                        new android.app.AlertDialog.Builder(this)
                            .setTitle("Xác nhận xóa")
                            .setMessage("Bạn có chắc chắn muốn xóa dự án này không?")
                            .setPositiveButton("Xóa", (d, w) -> {
                                MainActivity.taskList.remove(0);
                                TaskStorage.saveTasks(this, MainActivity.taskList);
                                renderTasks();
                                Toast.makeText(this, "Đã xóa dự án", Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("Hủy", null)
                            .show();
                    });

                    builder.show();
                });
            }

            listRecent.addView(recentView);
        }

        // Hiển thị các dự án còn lại ở listOld với layout item_task_old
        for (int i = 1; i < taskList.size(); i++) {
            Task task = taskList.get(i);
            View oldView = inflater.inflate(R.layout.item_task_old, listOld, false);
            LinearLayout infoLayout = oldView.findViewById(R.id.infoLayout);
            ImageView icDropdown = oldView.findViewById(R.id.ic_dropdown);

            if (infoLayout != null && infoLayout.getChildCount() >= 2) {
                TextView tvName = (TextView) infoLayout.getChildAt(0);
                TextView tvStatus = (TextView) infoLayout.getChildAt(1);
                if (tvName != null) tvName.setText(task.getName());
                if (tvStatus != null) tvStatus.setText(task.getStatus());
            }

            // Xử lý sự kiện click vào mũi tên dropdown cho các dự án cũ
            if (icDropdown != null) {
                final int position = i;
                icDropdown.setOnClickListener(v -> {
                    // Tạo AlertDialog với layout tùy chỉnh
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                    builder.setTitle(task.getName());

                    // Hiển thị mô tả của dự án
                    builder.setMessage(task.getQuest());

                    // Thêm các nút tùy chọn
                    builder.setPositiveButton("Chỉnh sửa", (dialog, which) -> {
                        // Mở EditProjectActivity để chỉnh sửa
                        android.content.Intent intent = new android.content.Intent(this, EditProjectActivity.class);
                        intent.putExtra("name", task.getName());
                        intent.putExtra("desc", task.getQuest());
                        intent.putExtra("group", task.getGroup());
                        intent.putExtra("start", task.getDateBegin());
                        intent.putExtra("end", task.getDateCompleted());
                        intent.putExtra("position", position);
                        startActivity(intent);
                    });

                    builder.setNegativeButton("Xóa", (dialog, which) -> {
                        // Xác nhận xóa
                        new android.app.AlertDialog.Builder(this)
                            .setTitle("Xác nhận xóa")
                            .setMessage("Bạn có chắc chắn muốn xóa dự án này không?")
                            .setPositiveButton("Xóa", (d, w) -> {
                                MainActivity.taskList.remove(position);
                                TaskStorage.saveTasks(this, MainActivity.taskList);
                                renderTasks();
                                Toast.makeText(this, "Đã xóa dự án", Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("Hủy", null)
                            .show();
                    });

                    builder.show();
                });
            }

            listOld.addView(oldView);
        }
    }
}
