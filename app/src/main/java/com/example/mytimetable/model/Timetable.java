package com.example.mytimetable.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "timetable")
public class Timetable {
    @PrimaryKey(autoGenerate = true)
    private int ID;
    private String PROJECT_NAME;
    private String DELIVERY_NAME;
    private String PROJECT_ACTIVITY;
    private String STATUS;
    private String ESTIMATED_HOURS;
    private String DATE;
    private String COLOR;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getPROJECT_NAME() {
        return PROJECT_NAME;
    }

    public void setPROJECT_NAME(String PROJECT_NAME) {
        this.PROJECT_NAME = PROJECT_NAME;
    }

    public String getDELIVERY_NAME() {
        return DELIVERY_NAME;
    }

    public void setDELIVERY_NAME(String DELIVERY_NAME) {
        this.DELIVERY_NAME = DELIVERY_NAME;
    }

    public String getPROJECT_ACTIVITY() {
        return PROJECT_ACTIVITY;
    }

    public void setPROJECT_ACTIVITY(String PROJECT_ACTIVITY) {
        this.PROJECT_ACTIVITY = PROJECT_ACTIVITY;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getESTIMATED_HOURS() {
        return ESTIMATED_HOURS;
    }

    public void setESTIMATED_HOURS(String ESTIMATED_HOURS) {
        this.ESTIMATED_HOURS = ESTIMATED_HOURS;
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    @Ignore
    public Timetable(String PROJECT_NAME, String DELIVERY_NAME, String PROJECT_ACTIVITY, String STATUS, String ESTIMATED_HOURS, String DATE, String COLOR){
        this.PROJECT_NAME = PROJECT_NAME;
        this.DELIVERY_NAME = DELIVERY_NAME;
        this.PROJECT_ACTIVITY = PROJECT_ACTIVITY;
        this.STATUS = STATUS;
        this.ESTIMATED_HOURS = ESTIMATED_HOURS;
        this.DATE = DATE;
        this.COLOR = COLOR;
    }

    public Timetable(int ID,String PROJECT_NAME, String DELIVERY_NAME, String PROJECT_ACTIVITY, String STATUS, String ESTIMATED_HOURS, String DATE, String COLOR){
        this.ID = ID;
        this.PROJECT_NAME = PROJECT_NAME;
        this.DELIVERY_NAME = DELIVERY_NAME;
        this.PROJECT_ACTIVITY = PROJECT_ACTIVITY;
        this.STATUS = STATUS;
        this.ESTIMATED_HOURS = ESTIMATED_HOURS;
        this.DATE = DATE;
        this.COLOR = COLOR;
    }


    public String getCOLOR() {
        return COLOR;
    }

    public void setCOLOR(String COLOR) {
        this.COLOR = COLOR;
    }
}
