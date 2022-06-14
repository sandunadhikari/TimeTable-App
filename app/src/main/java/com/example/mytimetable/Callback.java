package com.example.mytimetable;

import com.example.mytimetable.model.Timetable;

import java.util.List;

public interface Callback {
    void onSuccess(List<Timetable> value);
}
