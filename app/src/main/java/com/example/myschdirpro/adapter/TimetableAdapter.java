package com.example.myschdirpro.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myschdirpro.R;

import java.util.List;

public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.ViewHolder> {

    private final List<String> courseList;
    private final OnItemClickListener listener;
    private boolean isEditMode = false; // 是否處於編輯模式，預設為 false

    // 定義一個點擊事件的介面，用來把點擊事件傳回 MainActivity
    public interface OnItemClickListener {
        void onItemClick(int position, String courseName);
    }

    // 建構子 (Constructor)
    public TimetableAdapter(List<String> courseList, OnItemClickListener listener) {
        this.courseList = courseList;
        this.listener = listener;
    }

    // 提供方法讓 MainActivity 可以切換模式
    public void setEditMode(boolean editMode) {
        this.isEditMode = editMode;
    }

    public boolean isEditMode() {
        return isEditMode;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // 修正：移除 Kotlin 的 val，改用標準 Java 宣告（加上 final 更安全）
        public final TextView tvCourseName;
        public final EditText etCourseInput;

        public ViewHolder(@NonNull View view) {
            super(view);
            tvCourseName = view.findViewById(R.id.tvCourseName);
            etCourseInput = view.findViewById(R.id.etCourseInput);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String currentCourse = courseList.get(position);

        if (isEditMode) {
            // --- 編輯模式 ---
            holder.tvCourseName.setVisibility(View.GONE);
            holder.etCourseInput.setVisibility(View.VISIBLE);
            holder.etCourseInput.setText(currentCourse);

            // 即時監聽輸入的文字變更，同步到 List 中
            holder.etCourseInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    // 為了避免重複引發錯亂，確保抓到最新的 position
                    int currentPos = holder.getAdapterPosition();
                    if (currentPos != RecyclerView.NO_POSITION) {
                        courseList.set(currentPos, s.toString());
                    }
                }
            });
        } else {
            // --- 瀏覽 / 普通模式 ---
            holder.tvCourseName.setVisibility(View.VISIBLE);
            holder.etCourseInput.setVisibility(View.GONE);

            // 如果字串是空的，顯示「空堂」
            if (currentCourse == null || currentCourse.trim().isEmpty()) {
                holder.tvCourseName.setText("空堂");
                holder.itemView.setOnClickListener(null);
                holder.itemView.setClickable(false); // 空堂不可點擊
            } else {
                holder.tvCourseName.setText(currentCourse);

                // 只有「有儲存資料」的那一格才能點擊
                holder.itemView.setOnClickListener(v -> {
                    int currentPos = holder.getAdapterPosition();
                    if (currentPos != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(currentPos, currentCourse);
                    }
                });
                holder.itemView.setClickable(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return courseList != null ? courseList.size() : 0;
    }
}
