package com.example.mytimetable.adaptors;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mytimetable.R;
import com.example.mytimetable.constants.Constants;
import com.example.mytimetable.database.AppDatabase;
import com.example.mytimetable.model.Timetable;
import com.example.mytimetable.additemActivity;

import java.util.List;

public class TimetableAdaptor extends RecyclerView.Adapter<TimetableAdaptor.MyViewHolder>{
    private Context context;
    LinearLayout statusColor;
    private List<Timetable> mTimetableList;

    public TimetableAdaptor(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public TimetableAdaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_popular_courses, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimetableAdaptor.MyViewHolder myViewHolder, int i) {
        myViewHolder.projectName.setText(mTimetableList.get(i).getPROJECT_NAME());
        myViewHolder.deliveryName.setText("Delivery Name"+":-"+mTimetableList.get(i).getDELIVERY_NAME());
        myViewHolder.projectActivity.setText(mTimetableList.get(i).getPROJECT_ACTIVITY());
        myViewHolder.status.setText(mTimetableList.get(i).getSTATUS());
        myViewHolder.estimatedHours.setText(mTimetableList.get(i).getESTIMATED_HOURS()+" "+"Hours");
        statusColor.getBackground().setColorFilter(Color.parseColor(mTimetableList.get(i).getCOLOR()), PorterDuff.Mode.SRC_ATOP);

    }

    @Override
    public int getItemCount() {
        if (mTimetableList == null) {
            return 0;
        }
        return mTimetableList.size();
    }

    public List<Timetable> getTasks() {

        return mTimetableList;
    }

    public void setTasks(List<Timetable> timetableList) {
        mTimetableList = timetableList;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView projectName, deliveryName, projectActivity, estimatedHours,status;
        CardView cv;
        AppDatabase mDb;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            try {


                mDb = AppDatabase.getInstance(context);
                projectName = itemView.findViewById(R.id.txtProjectName);
                deliveryName = itemView.findViewById(R.id.txtDeliveryName);
                projectActivity = itemView.findViewById(R.id.txtProjectActivity);
                estimatedHours = itemView.findViewById(R.id.txtEstimatedHours);
                status = itemView.findViewById(R.id.txtStatus);
                statusColor = itemView.findViewById(R.id.colorStatus);
                cv = itemView.findViewById(R.id.cardview);

                cv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int elementId = mTimetableList.get(getAdapterPosition()).getID();
                        Intent intent = new Intent(context,additemActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(Constants.UPDATE_Timetable_Id, elementId);
                        context.startActivity(intent);
                    }
                });

            }catch (Exception ex)
            {

            }
        }
    }
}
