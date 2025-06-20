package com.todo.task;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
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
        findViewById(R.id.btnNewTask).setOnClickListener(v -> {
            startActivity(new android.content.Intent(this, addProjectActivity.class));
        });
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
        // Sắp xếp theo lựa chọn
        if (sortType == SortType.NAME) {
            if (isSortAZ) {
                Collections.sort(taskList, Comparator.comparing(Task::getName));
            } else {
                Collections.sort(taskList, Comparator.comparing(Task::getName).reversed());
            }
        } else if (sortType == SortType.DATE) {
            if (isSortAZ) {
                Collections.sort(taskList, Comparator.comparing(t -> t.getDateCompleted()));
            } else {
                Collections.sort(taskList, Comparator.comparing((Task t) -> t.getDateCompleted()).reversed());
            }
        }
        for (Task task : taskList) {
            int dayLeft = task.dayLeft();
            LinearLayout parentLayout;
            int layoutId;
            if (dayLeft > 5) {
                parentLayout = listRecent;
                layoutId = R.layout.item_task_recent;
            } else {
                parentLayout = listOld;
                layoutId = R.layout.item_task_old;
            }
            // Inflate card
            android.view.View card = getLayoutInflater().inflate(layoutId, parentLayout, false);
            // Set data
            LinearLayout infoLayout = card.findViewById(R.id.infoLayout);
            TextView tvName = infoLayout != null ? (TextView) infoLayout.getChildAt(0) : null;
            TextView tvStatus = infoLayout != null ? (TextView) infoLayout.getChildAt(1) : null;
            if (tvName != null) tvName.setText(task.getName() + " (" + task.getGroup() + ")");
            if (tvStatus != null) tvStatus.setText(task.getStatus());
            // Xử lý nút expand (ic_dropdown) để hiển thị mô tả và ngày
            android.widget.ImageView btnExpand = card.findViewById(R.id.ic_dropdown);
            if (btnExpand != null) {
                btnExpand.setOnClickListener(v -> {
                    // Hiển thị dialog chi tiết project
                    String msg = "Mô tả: " + task.getQuest() +
                            "\nNhóm: " + task.getGroup() +
                            "\nBắt đầu: " + task.getDateBegin() +
                            "\nKết thúc: " + task.getDateCompleted() +
                            "\n" + task.getStatus();
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                    builder.setTitle(task.getName())
                        .setMessage(msg)
                        .setPositiveButton("Đóng", null)
                        .setNegativeButton("Xóa", (dialog, which) -> {
                            taskList.remove(task);
                            renderTasks();
                        })
                        .setNeutralButton("Sửa", (dialog, which) -> {
                            android.content.Intent intent = new android.content.Intent(this, EditProjectActivity.class);
                            // Truyền dữ liệu project qua intent
                            intent.putExtra("name", task.getName());
                            intent.putExtra("desc", task.getQuest());
                            intent.putExtra("group", task.getGroup());
                            intent.putExtra("start", task.getDateBegin());
                            intent.putExtra("end", task.getDateCompleted());
                            intent.putExtra("status", task.getStatus());
                            intent.putExtra("position", taskList.indexOf(task));
                            startActivityForResult(intent, 1001);
                        })
                        .show();
                });
            }
            parentLayout.addView(card);
        }
    }
}
