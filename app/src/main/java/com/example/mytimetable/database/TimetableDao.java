package com.example.mytimetable.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.mytimetable.model.Timetable;

import java.util.List;

@Dao
public interface TimetableDao {
    @Query("SELECT * FROM TIMETABLE ORDER BY ID")
    List<Timetable> loadAllTimetables();

    @Query("SELECT * FROM TIMETABLE WHERE DATE = :date ORDER BY ID")
    List<Timetable> loadTimetablesData(String date);

    @Query("SELECT * FROM TIMETABLE WHERE DATE BETWEEN :date1 AND :date2 ORDER BY ID")
    List<Timetable> loadData(String date1,String date2);

    @Insert
    void insertTimetable(Timetable timetable);

    @Update
    void updateTimetable(Timetable timetable);

    @Delete
    void delete(Timetable timetable);

    @Query("SELECT * FROM TIMETABLE WHERE ID = :id")
    Timetable loadTimetableById(int id);
}