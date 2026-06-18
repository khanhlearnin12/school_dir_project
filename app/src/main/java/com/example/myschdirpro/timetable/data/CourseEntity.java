package com.example.myschdirpro.timetable.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "course_table")
public class CourseEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String day;
    public String time;
    public String name;
    public String room;

    public CourseEntity(String day, String time, String name, String room) {
        this.day = day;
        this.time = time;
        this.name = name;
        this.room = room;
    }
}