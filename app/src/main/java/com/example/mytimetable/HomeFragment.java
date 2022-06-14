package com.example.mytimetable;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytimetable.adaptors.TimetableAdaptor;
import com.example.mytimetable.database.AppDatabase;
import com.example.mytimetable.database.AppExecutors;
import com.example.mytimetable.messages.messagePopup;
import com.example.mytimetable.model.Timetable;
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements DatePickerListener {
    FloatingActionButton fabButton;
    private AppDatabase mDb;
    private RecyclerView mRecyclerView;
    private TimetableAdaptor mAdapter;
    String SelectDate;
    messagePopup messagePopup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_fragment_layout, container, false);

        fabButton = (FloatingActionButton) v.findViewById(R.id.fabButton);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),additemActivity.class);
                startActivity(intent);
            }
        });

        mRecyclerView = v.findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new TimetableAdaptor(getActivity().getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);
        mDb = AppDatabase.getInstance(getActivity().getApplicationContext());

        messagePopup = messagePopup.getInstance();
        messagePopup.init(getActivity().getApplicationContext());
        deleteTasks();
        HorizontalPicker picker= (HorizontalPicker) v.findViewById(R.id.datePicker);
        picker.setListener(this)
                .setDays(120)
                .setOffset(7)
                .setDateSelectedColor(Color.DKGRAY)
                .setDateSelectedTextColor(Color.WHITE)
                .setMonthAndYearTextColor(Color.DKGRAY)
                .setTodayButtonTextColor(getResources().getColor(R.color.colorPrimary))
                .setTodayDateTextColor(getResources().getColor(R.color.colorPrimary))
                .setTodayDateBackgroundColor(Color.GRAY)
                .setUnselectedDayTextColor(Color.DKGRAY)
                .setDayOfWeekTextColor(Color.DKGRAY )
                .setUnselectedDayTextColor(getResources().getColor(R.color.primaryTextColor))
                .showTodayButton(false)
                .init();
        picker.setBackgroundColor(Color.LTGRAY);
        picker.setDate(new DateTime());

        return v;
    }

    @Override
    public void onDateSelected(DateTime dateSelected) {
        SelectDate = dateSelected.toString("dd-MM-yyyy");
        Log.i("HorizontalPicker","Fecha seleccionada="+dateSelected.toString("dd-MM-yyyy"));
        retrieveTasks(SelectDate);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if(SelectDate != null)
            {
                retrieveTasks(SelectDate);
            }else
            {
                String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                retrieveTasks(date);
            }
        }catch (Exception ex)
        {

        }
    }


    private void deleteTasks(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                boolean isYes= messagePopup.getDialogValueBack(getActivity(),"Are you want to deletete ?");
                if(isYes){
                    // Here is where you'll implement swipe to delete
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            int position = viewHolder.getAdapterPosition();
                            List<Timetable> tasks = mAdapter.getTasks();
                            mDb.timetableDao().delete(tasks.get(position));
                            retrieveTasks(SelectDate);
                        }
                    });
                }
                else
                {
                    retrieveTasks(SelectDate);
                }
            }

        }).attachToRecyclerView(mRecyclerView);
    }

    private void retrieveTasks(final String selectDate) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<Timetable> list = mDb.timetableDao().loadTimetablesData(selectDate);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mAdapter.setTasks(list);
                    }
                });
            }
        });


    }
}
