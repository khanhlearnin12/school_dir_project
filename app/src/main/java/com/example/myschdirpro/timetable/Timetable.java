package com.example.myschdirpro.timetable;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myschdirpro.R;
import com.example.myschdirpro.timetable.adapter.TimetableAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Timetable extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TimetableAdapter adapter;

    private Map<String, List<Course>> timetableMap = new HashMap<>();

    private String currentDay = "Mon";

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

        // 1️⃣ RecyclerView 初始化
        recyclerView = findViewById(R.id.rvSchedule);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TimetableAdapter();
        recyclerView.setAdapter(adapter);

        // 2️⃣ 建立課表資料
        initData();

        // 3️⃣ 預設顯示 Monday
        switchDay("Mon");

        // 4️⃣ 綁定星期按鈕
        setupDayButtons();
    }

}