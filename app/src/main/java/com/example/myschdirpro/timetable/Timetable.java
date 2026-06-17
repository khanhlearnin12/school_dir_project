package com.example.myschdirpro.timetable;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myschdirpro.R;
import com.example.myschdirpro.timetable.adapter.TimetableAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Timetable extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TimetableAdapter adapter;
    private Map<String, List<Course>> timetableMap = new HashMap<>();
    private String currentDay = "Mon";

    private void initData() {

        timetableMap.put("Mon", Arrays.asList(
                new Course("08:00", "MATH 101", "Room 302"),
                new Course("10:00", "ECON 50", "Room 101"),
                new Course("14:00", "GEOG 10", "Annex 2")
        ));

        timetableMap.put("Tue", Arrays.asList(
                new Course("09:00", "CS 101", "Room 201"),
                new Course("13:00", "Physics", "Lab 1")
        ));

        timetableMap.put("Wed", Arrays.asList(
                new Course("08:00", "English", "Room 105")
        ));
    }

    private void switchDay(String day) {

        currentDay = day;

        List<Course> list = timetableMap.get(day);

        if (list == null) {
            list = new ArrayList<>();
        }

        adapter.updateData(list);
    }

    private void setupDayButtons() {

        Button mon = findViewById(R.id.btnMon);
        Button tue = findViewById(R.id.btnTue);
        Button wed = findViewById(R.id.btnWed);
        Button thu = findViewById(R.id.btnThu);
        Button fri = findViewById(R.id.btnFri);

        mon.setOnClickListener(v -> switchDay("Mon"));
        tue.setOnClickListener(v -> switchDay("Tue"));
        wed.setOnClickListener(v -> switchDay("Wed"));
        thu.setOnClickListener(v -> switchDay("Thu"));
        fri.setOnClickListener(v -> switchDay("Fri"));
    }

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