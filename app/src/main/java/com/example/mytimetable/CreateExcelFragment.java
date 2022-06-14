package com.example.mytimetable;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mytimetable.adaptors.TimetableAdaptor;
import com.example.mytimetable.database.AppDatabase;
import com.example.mytimetable.database.AppExecutors;
import com.example.mytimetable.model.Timetable;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CreateExcelFragment extends Fragment {
    EditText startdate,enddate;
    DatePickerDialog datePickerDialog1,datePickerDialog2;
    Button create;
    private AppDatabase mDb;
    HashMap<String, String> Leave = new HashMap<String, String>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.create_excel_fragment_layout, container, false);
        startdate = v.findViewById(R.id.txtStartDate);
        enddate = v.findViewById(R.id.txtEndDate);
        create = v.findViewById(R.id.buttonCreate);
        setStartDateTimeField();
        setEndDateTimeField();

        startdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                datePickerDialog1.show();
                return false;
            }
        });

        enddate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                datePickerDialog2.show();
                return false;
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateButtonClicked();
            }


        });
        mDb = AppDatabase.getInstance(getActivity().getApplicationContext());

        Leave.put("Annual Leave","N/A");
        Leave.put("Casual Leave","N/A");
        Leave.put("Sick Leave","N/A");
        Leave.put("Short Leave","N/A");
        Leave.put("JKCS - Misc","N/A");
        Leave.put("Out of Office","N/A");
        Leave.put("Public/Mecantile/JKH Holiday","N/A");

        return v;
    }

    private void retrieveTasks(final Callback callback,final String startDate,final String endDate) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Timetable> list = mDb.timetableDao().loadData(startDate,endDate);
                callback.onSuccess(list);

            }
        });
    }

    private void onCreateButtonClicked()
    {
        final List<Timetable>[] mList = new List[]{null};
        retrieveTasks(new Callback() {
            @Override
            public void onSuccess(List<Timetable> value) {
                mList[0] =value;
            }
        },startdate.getText().toString(),enddate.getText().toString());
        int days = dateCount(startdate.getText().toString(),enddate.getText().toString());
        Workbook wb=new HSSFWorkbook();

        CellStyle cellStyle=wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setWrapText(true);

        //Now we are creating sheet
        Sheet sheet=null;
        sheet = wb.createSheet("Name of sheet");
        //Now column and row
        Row row0 =sheet.createRow(0);
        Cell cell0=row0.createCell(2);
        CellStyle cellStyle1=wb.createCellStyle();
        cell0.setCellValue("Sandun-December 2020");
        cellStyle1.setAlignment(HorizontalAlignment.CENTER);
        cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle1.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cell0.setCellStyle(cellStyle1);


        Row row1 =sheet.createRow(1);
        Cell cell1=row1.createCell(3);
        cell1.setCellValue("Status");
        CellStyle cellStyle3=wb.createCellStyle();
        cellStyle3.setAlignment(HorizontalAlignment.CENTER);
        cellStyle3.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle3.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        cellStyle3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cell1.setCellStyle(cellStyle3);


        Cell cell2=row1.createCell(4);
        cell2.setCellValue("Estimated Hours");
        CellStyle cellStyle4=wb.createCellStyle();
        cellStyle4.setAlignment(HorizontalAlignment.CENTER);
        cellStyle4.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle4.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
        cellStyle4.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cell2.setCellStyle(cellStyle4);

        Cell cell3=row1.createCell(5);
        cell3.setCellValue("Worked Days");
        cell3.setCellStyle(cellStyle4);

        Row row2 =sheet.createRow(2);
        CellStyle cellStyle2=wb.createCellStyle();
        cellStyle2.setAlignment(HorizontalAlignment.CENTER);
        cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle2.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        cellStyle2.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Cell cell9=row2.createCell(3);
        Cell cell10=row2.createCell(4);
        Cell cell11=row2.createCell(5);
        cell9.setCellStyle(cellStyle3);
        cell10.setCellStyle(cellStyle4);
        cell11.setCellStyle(cellStyle4);


        for (int i = 0; i <= days; i++)
        {
            Cell cell4 = row1.createCell(6+i);
            String dd = Daycreate(i);
            cell4.setCellValue(dd);
            cell4.setCellStyle(cellStyle2);

            Cell cell5 = row2.createCell(6+i);
            String d = DD(i);
            cell5.setCellValue(d);
            cell5.setCellStyle(cellStyle2);
        }

        Row row3 =sheet.createRow(3);

        for(int i = 0; i <= 6+days; i++)
        {
            Cell cell12=row3.createCell(i);
            if(i == 0){
                cell12.setCellValue("Project Name");
            }else if(i == 1){
                cell12.setCellValue("Delivery Name");
            }else if(i == 2){
                cell12.setCellValue("Project Activity");
            }
            cell12.setCellStyle(cellStyle1);
        }


        if(!mList[0].isEmpty())
        {
            for(int k = 0; k < mList[0].size(); k++)
            {
                Row row =sheet.createRow(4+k);
                int kk = dateCount(startdate.getText().toString(),mList[0].get(k).getDATE());
                for(int j = 0; j <= 6; j++)
                {
                    Cell cell=row.createCell(j);
                    if(j == 0)
                    {
                        cell.setCellValue(mList[0].get(k).getPROJECT_NAME());
                    }else if(j == 1)
                    {
                        cell.setCellValue(mList[0].get(k).getDELIVERY_NAME());
                    }else if(j == 2)
                    {
                        cell.setCellValue(mList[0].get(k).getPROJECT_ACTIVITY());
                    }else if(j == 3)
                    {
                        cell.setCellValue(mList[0].get(k).getSTATUS());
                    }else if(j == 4)
                    {
                        cell.setCellValue(mList[0].get(k).getESTIMATED_HOURS());
                    }else  if(j == 5)
                    {
                        double workDay = Double.parseDouble(mList[0].get(k).getESTIMATED_HOURS());
                        double wd = workDay/8;
                        cell.setCellValue(wd);
                    }else {
                        for (int i = 0; i <= kk; i++)
                        {
                            Cell cell12 = row.createCell(6+kk);
                            cell12.setCellValue(mList[0].get(k).getESTIMATED_HOURS());
                            cell12.setCellStyle(cellStyle);
                        }
                    }
                    cell.setCellStyle(cellStyle);
                }

            }
        }

        int r = mList[0].size()+9;
        Row row4 =sheet.createRow(r);
        String ff= "hhh";

        for(int i = 0; i <= 6+days; i++)
        {
            Cell cell13=row4.createCell(i);
            if(i == 2){
                cell13.setCellValue("Other");
            }
            cell13.setCellStyle(cellStyle1);
        }

        int index = 0;


        for (String i : Leave.keySet()) {
            int rr = mList[0].size()+10+index;
            Row row5 =sheet.createRow(rr);
            Cell cell14=row5.createCell(2);
            cell14.setCellValue(i);
            cell14.setCellStyle(cellStyle);

            Cell cell15=row5.createCell(3);
            cell15.setCellValue(Leave.get(i));
            cell15.setCellStyle(cellStyle);
            ++index;
        }

        sheet.setColumnWidth(0,(10*500));
        sheet.setColumnWidth(1,(10*500));
        sheet.setColumnWidth(2,(10*1000));
        sheet.setColumnWidth(3,(10*500));
        sheet.setColumnWidth(4,(10*500));
        sheet.setColumnWidth(5,(10*500));
//        sheet.setColumnWidth(1,(10*200));
//        sheet.setColumnWidth(2,(10*200));
//        sheet.setColumnWidth(3,(10*200));
//        sheet.setColumnWidth(4,(10*200));
//        sheet.setColumnWidth(5,(10*200));

        File file = new File(getActivity().getExternalFilesDir(null),"plik.xls");
        FileOutputStream outputStream =null;

        try {
            outputStream=new FileOutputStream(file);
            wb.write(outputStream);
            Toast.makeText(getActivity().getApplicationContext(),"OK",Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();

            Toast.makeText(getActivity().getApplicationContext(),"NO OK",Toast.LENGTH_LONG).show();
            try {
                outputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


    }

    private String Daycreate(int i){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(startdate.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, i);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        String output = sdf.format(c.getTime());
        SimpleDateFormat sd=new SimpleDateFormat("EEEE");
        String dayofweek=sd.format(c.getTime());
        return DayName(dayofweek);
    }

    private String DD(int i){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(startdate.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, i);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        String output = sdf.format(c.getTime());
        return output.split("-")[0];
    }

    private String DayName(String dName)
    {
        switch (dName)
        {
            case "Monday": return "M";
            case "Tuesday": return "T";
            case "Wednesday": return "W";
            case "Thursday": return "T";
            case "Friday": return "F";
            case "Saturday": return "S";
            case "Sunday": return "S";
            default: return null;
        }
    }

    private int dateCount(String startDate,String endDate){
        SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            Date date1 = myFormat.parse(startDate);
            Date date2 = myFormat.parse(endDate);
            long diff = date2.getTime() - date1.getTime();
            return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    private void setStartDateTimeField()
    {

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog1 = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy");
                final Date startDate = newDate.getTime();
                String fdate = sd.format(startDate);

                startdate.setText(fdate);

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog1.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private void setEndDateTimeField()
    {

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog2 = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy");
                final Date startDate = newDate.getTime();
                String fdate = sd.format(startDate);

                enddate.setText(fdate);

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog2.getDatePicker().setMaxDate(System.currentTimeMillis());

    }
}
