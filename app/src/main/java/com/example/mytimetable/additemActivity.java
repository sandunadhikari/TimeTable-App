package com.example.mytimetable;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import android.os.Bundle;

import com.example.mytimetable.constants.Constants;
import com.example.mytimetable.database.AppDatabase;
import com.example.mytimetable.database.AppExecutors;
import com.example.mytimetable.model.Timetable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class additemActivity extends AppCompatActivity {
    String[] users = { "To Do", "In Progress", "Completed" };
    EditText projectName, deliveryName, projectActivity, estimatedHours;
    Button button;
    Calendar myCalendar;
    EditText datetext;
    DatePickerDialog datePickerDialog;
    String statusValue;
    private AppDatabase mDb;
    Intent intent;
    int mTimetableId;
    Spinner spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);

        mDb = AppDatabase.getInstance(getApplicationContext());
        datetext= (EditText) findViewById(R.id.txtDate);
        spin = (Spinner) findViewById(R.id.status);
        initViews();
        setDateTimeField();
        showStatus();

        intent=getIntent();
        if (intent != null && intent.hasExtra(Constants.UPDATE_Timetable_Id)) {
            button.setText("Update");

            mTimetableId = intent.getIntExtra(Constants.UPDATE_Timetable_Id, -1);

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    Timetable timetable = mDb.timetableDao().loadTimetableById(mTimetableId);
                    populateUI(timetable);
                }
            });


        }

        datetext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                datePickerDialog.show();
                return false;
            }
        });
    }

    private void populateUI(Timetable timetable) {

        if (timetable == null) {
            return;
        }

        projectName.setText(timetable.getPROJECT_NAME());
        deliveryName.setText(timetable.getDELIVERY_NAME());
        projectActivity.setText(timetable.getPROJECT_ACTIVITY());
        estimatedHours.setText(timetable.getESTIMATED_HOURS());
        datetext.setText(timetable.getDATE().toString());

        String compareValue = timetable.getSTATUS().toString();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        if (compareValue != null) {
            int spinnerPosition = adapter.getPosition(compareValue);
            spin.setSelection(spinnerPosition);
        }
    }

    private void initViews() {
        projectName = findViewById(R.id.txtProjectName);
        deliveryName = findViewById(R.id.txtDeliveryName);
        projectActivity = findViewById(R.id.txtProjectActivity);
        estimatedHours = findViewById(R.id.txtEstimatedHours);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveButtonClicked();
            }
        });
    }

    public void onSaveButtonClicked() {

        String colorCode = null;
        if(statusValue.toString().equals("To Do"))
        {
            colorCode = "#6F6A6A";
        }
        else if(statusValue.toString().equals("In Progress"))
        {
            colorCode = "#13D3B4";
        }
        else if(statusValue.toString().equals("Completed"))
        {
            colorCode = "#FF991F";
        }

        final Timetable timetable = new Timetable(
                projectName.getText().toString(),
                deliveryName.getText().toString(),
                projectActivity.getText().toString(),
                statusValue.toString(),
                estimatedHours.getText().toString(),
                datetext.getText().toString(),
                colorCode);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (!intent.hasExtra(Constants.UPDATE_Timetable_Id)) {
                    mDb.timetableDao().insertTimetable(timetable);
                } else {
                    timetable.setID(mTimetableId);
                    mDb.timetableDao().updateTimetable(timetable);
                }
                finish();
            }
        });
    }

    //status dropdown
    private void showStatus() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statusValue=parent.getItemAtPosition(position).toString();
                //Toast.makeText(AddItemActivity.this,"selected: "+vv,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy");
                final Date startDate = newDate.getTime();
                String fdate = sd.format(startDate);

                datetext.setText(fdate);

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }
}
