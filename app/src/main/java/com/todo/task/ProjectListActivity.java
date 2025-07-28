package com.todo.task;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.todo.task.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

        // Nút logout
        findViewById(R.id.logout).setOnClickListener(v -> {
            User.getInstance().logout(this);
            Log.d("TM", "--User logged out");
            finish();
        });

        // Nút thông báo: hiển thị project còn dưới 3 ngày
        findViewById(R.id.btnNoti).setOnClickListener(v -> {
            StringBuilder sb = new StringBuilder();
            for (Task t : taskList) {
                int daysLeft = t.dayLeft();
                if (daysLeft > 0 && daysLeft < 3) {
                    sb.append("- ").append(t.getName())
                            .append(" (còn ").append(daysLeft).append(" ngày)\n");
                }
            }
            String msg = sb.length() > 0 ? sb.toString() : "Không có project nào sắp hết hạn!";
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Project sắp hết hạn (<3 ngày)")
                    .setMessage(msg)
                    .setPositiveButton("Đóng", null)
                    .show();
        });

        // Nút thêm mới project
        findViewById(R.id.btnNewTask).setOnClickListener(v ->
                startActivity(new Intent(this, addProjectActivity.class))
        );

        ImageView sortBtn = findViewById(R.id.ic_sort);
        ImageView sortDropdownBtn = findViewById(R.id.sort_ic_dropdown);

        View.OnClickListener sortListener = v -> {
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
        };

        if (sortBtn != null) sortBtn.setOnClickListener(sortListener);
        if (sortDropdownBtn != null) sortDropdownBtn.setOnClickListener(sortListener);
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
        if (listRecent == null || listOld == null) return;

        listRecent.removeAllViews();
        listOld.removeAllViews();

        // Sắp xếp theo sortType và isSortAZ
        if (sortType == SortType.NAME) {
            Collections.sort(taskList, (t1, t2) -> {
                if (t1 == null || t1.getName() == null) return 1;
                if (t2 == null || t2.getName() == null) return -1;
                return isSortAZ ? t1.getName().compareToIgnoreCase(t2.getName())
                        : t2.getName().compareToIgnoreCase(t1.getName());
            });
        } else if (sortType == SortType.DATE) {
            Collections.sort(taskList, (t1, t2) -> {
                if (t1 == null || t1.getUpdatedAt() == null) return 1;
                if (t2 == null || t2.getUpdatedAt() == null) return -1;
                return isSortAZ ? t1.getUpdatedAt().compareTo(t2.getUpdatedAt())
                        : t2.getUpdatedAt().compareTo(t1.getUpdatedAt());
            });
        }

        LayoutInflater inflater = LayoutInflater.from(this);

        // Hiển thị project gần nhất (nằm đầu list sau sort) ở listRecent
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

            if (icDropdown != null) {
                icDropdown.setOnClickListener(v -> showTaskOptionsDialog(recentTask, 0));
            }

            listRecent.addView(recentView);
        }

        // Hiển thị các project còn lại ở listOld
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

            final int position = i;
            if (icDropdown != null) {
                icDropdown.setOnClickListener(v -> showTaskOptionsDialog(task, position));
            }

            listOld.addView(oldView);
        }
    }

    /**
     * Hiển thị AlertDialog chứa các tùy chọn chỉnh sửa/xóa task
     * @param task Task hiện tại
     * @param position vị trí task trong danh sách taskList
     */
    private void showTaskOptionsDialog(Task task, int position) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(task.getName());
        builder.setMessage(task.getQuest());

        builder.setPositiveButton("Chỉnh sửa", (dialog, which) -> {
            Intent intent = new Intent(this, EditProjectActivity.class);
            intent.putExtra("name", task.getName());
            intent.putExtra("desc", task.getQuest());
            intent.putExtra("group", task.getGroup());
            intent.putExtra("start", task.getDateBegin());
            intent.putExtra("end", task.getDateCompleted());
            intent.putExtra("position", position);
            startActivity(intent);
        });

        builder.setNegativeButton("Xóa", (dialog, which) -> {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa dự án này không?")
                    .setPositiveButton("Xóa", (d, w) -> {
                        if (position >= 0 && position < taskList.size()) {
                            taskList.remove(position);
                            TaskStorage.saveTasks(this, taskList);
                            renderTasks();
                            Toast.makeText(this, "Đã xóa dự án", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        builder.show();
    }
}
