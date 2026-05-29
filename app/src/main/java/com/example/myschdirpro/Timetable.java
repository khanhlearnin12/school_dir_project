package com.example.myschdirpro;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myschdirpro.adapter.TimetableAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class Timetable extends AppCompatActivity {

    // 修正 2：補上類別變數的宣告
    private TimetableAdapter adapter;
    private final List<String> courseList = new ArrayList<>();
    private final String sharedPrefsKey = "timetable_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_timetable);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 如果是從之前的 XML 修改而來，記得將 R.id.toolbar 改為你的 BottomAppBar ID 或是保留原樣
        Toolbar toolbar = findViewById(R.id.toolbar);
        Button btnEdit = findViewById(R.id.btn_edit);
        Button btnSave = findViewById(R.id.btn_save);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewTimetable);

        // 1. 初始化資料（預設 35 格空字串：5天 * 7節課 = 35）
        // 這裡先填滿 35 個空值，避免後續 load 或是 binding 時發生 IndexOutOfBoundsException
        for (int i = 0; i < 50; i++) {
            courseList.add("");
        }

        // 2. 讀取舊有的課表資料（如果有的話會覆蓋上面的空值）
        loadTimetableData();

        // 3. 初始化 RecyclerView (設定為 5 欄，代表週一到週五)
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));

        // 這裡使用我們之前寫好的 Java 版介面監聽器與 Lambda 語法
        adapter = new TimetableAdapter(courseList, (position, courseName) -> {
            // 處理「有資料格子」的點擊事件
            Toast.makeText(Timetable.this, "你點擊了第 " + (position + 1) + " 節的課：" + courseName, Toast.LENGTH_SHORT).show();
        });
        recyclerView.setAdapter(adapter);

        // 4. 點擊「編輯」按鈕
        btnEdit.setOnClickListener(v -> {
            adapter.setEditMode(true);
            adapter.notifyDataSetChanged(); // 刷新畫面切換成輸入框
            btnEdit.setVisibility(View.GONE);
            btnSave.setVisibility(View.VISIBLE);
        });

        // 5. 點擊「儲存」按鈕
        btnSave.setOnClickListener(v -> {
            adapter.setEditMode(false);
            adapter.notifyDataSetChanged(); // 刷新畫面切換回文字顯示
            btnEdit.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.GONE);

            saveTimetableData(); // 儲存到本地
            Toast.makeText(Timetable.this, "課表已儲存！", Toast.LENGTH_SHORT).show();
        });

        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> finish());
        }
    }

    // 使用 SharedPreferences 儲存 List（轉成 JSON 字串）
    // 使用 SharedPreferences 儲存 List
    private void saveTimetableData() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        JSONArray jsonArray = new JSONArray(courseList);
        editor.putString(sharedPrefsKey, jsonArray.toString());
        editor.apply();
    }

    // 讀取本地課表資料
    private void loadTimetableData() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String jsonString = sharedPreferences.getString(sharedPrefsKey, null);

        if (jsonString != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                int limit = Math.min(jsonArray.length(), courseList.size());
                for (int i = 0; i < limit; i++) {
                    courseList.set(i, jsonArray.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}