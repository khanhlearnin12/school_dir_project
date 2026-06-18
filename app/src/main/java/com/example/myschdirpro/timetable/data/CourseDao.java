package com.example.myschdirpro.timetable.data;

import androidx.room.*;
import java.util.List;

@Dao
public interface CourseDao {

    @Query("SELECT * FROM course_table WHERE day = :day")
    List<CourseEntity> getCoursesByDay(String day);

    @Insert
    void insertCourse(CourseEntity course);

    @Update
    void updateCourse(CourseEntity course);

    @Delete
    void deleteCourse(CourseEntity course);

    @Query("DELETE FROM course_table WHERE day = :day")
    void deleteByDay(String day);
}
