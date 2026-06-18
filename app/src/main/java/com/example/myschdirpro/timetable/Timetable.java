package com.example.myschdirpro.timetable;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myschdirpro.R;
import com.example.myschdirpro.timetable.adapter.TimetableAdapter;
import com.example.myschdirpro.timetable.data.AppDatabase;
import com.example.myschdirpro.timetable.data.CourseDao;
import com.example.myschdirpro.timetable.data.CourseEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Timetable extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TimetableAdapter adapter;
    private String currentDay = "Mon";
    private boolean isEditMode = false;
    private AppDatabase db;
    private FloatingActionButton fabAddCourse;

    private void switchDay(String day) {

        currentDay = day;

        Log.d("DB_TEST", "day = " + day + ", size = " + db.courseDao().getCoursesByDay(day).size());

        List<CourseEntity> entities =
                db.courseDao().getCoursesByDay(day);

        if (entities == null) {
            entities = new ArrayList<>();
        }

        List<Course> list = new ArrayList<>();

        for (CourseEntity e : entities) {
            list.add(new Course(e.time, e.name, e.room));
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

    private void saveCurrentDayData() {

        db.courseDao().deleteByDay(currentDay);

        List<Course> list = adapter.getData();

        for (Course c : list) {

            if (c.name.isEmpty() && c.room.isEmpty()) continue;

            db.courseDao().insertCourse(
                    new CourseEntity(
                            currentDay,
                            c.time,
                            c.name,
                            c.room
                    )
            );
        }
    }

    private void setTopBar(){

        ImageButton btnBack = findViewById(R.id.btn_timetable_back);
        ImageButton btnEdit = findViewById(R.id.btn_timetable_Edit);

        // 返回上一頁
        btnBack.setOnClickListener(v -> {
            finish();
        });

        btnEdit.setOnClickListener(v -> {

            isEditMode = !isEditMode;
            adapter.setEditMode(isEditMode);

            if (isEditMode) {
                fabAddCourse.setVisibility(View.VISIBLE);
                btnEdit.setImageResource(R.drawable.save);

            } else {

                btnEdit.setImageResource(R.drawable.edit);
                fabAddCourse.setVisibility(View.GONE);

                saveCurrentDayData();
            }
        });
    }

    private void setCourseBtn(){

        fabAddCourse = findViewById(R.id.fabAddCourse);

        fabAddCourse.setOnClickListener(v -> {
            adapter.addItem();
        });

        adapter.setOnCourseDeleteListener(position -> {
            adapter.removeItem(position);
        });
    }

    private void insertTestData() {

        CourseDao dao = db.courseDao();

        // 先清空（避免重複）
        dao.deleteByDay("Mon");
        dao.deleteByDay("Tue");
        dao.deleteByDay("Wed");

        // Mon
        dao.insertCourse(new CourseEntity("Mon", "08:00", "MATH 101", "Room 302"));
        dao.insertCourse(new CourseEntity("Mon", "10:00", "ECON 50", "Room 101"));
        dao.insertCourse(new CourseEntity("Mon", "14:00", "GEOG 10", "Annex 2"));

        // Tue
        dao.insertCourse(new CourseEntity("Tue", "09:00", "CS 101", "Room 201"));
        dao.insertCourse(new CourseEntity("Tue", "13:00", "Physics", "Lab 1"));

        // Wed
        dao.insertCourse(new CourseEntity("Wed", "08:00", "English", "Room 105"));
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



        db = AppDatabase.getInstance(this);

        if (db.courseDao().getCoursesByDay("Mon").isEmpty()) {
            insertTestData();
        }

        // RecyclerView 初始化
        recyclerView = findViewById(R.id.rvSchedule);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TimetableAdapter();
        recyclerView.setAdapter(adapter);

        // 預設顯示 Monday
        switchDay("Mon");

        // 綁定星期按鈕
        setupDayButtons();

        setTopBar();

        setCourseBtn();

    }

}