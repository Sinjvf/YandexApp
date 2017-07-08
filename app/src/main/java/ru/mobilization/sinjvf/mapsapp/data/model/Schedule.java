
package ru.mobilization.sinjvf.mapsapp.data.model;

import android.support.annotation.NonNull;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ru.mobilization.sinjvf.mapsapp.Adapter.AdapterItem;

public class Schedule implements AdapterItem, Comparable<Schedule>{

    @SerializedName("duration")
    @Expose
    private Double duration;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("schools")
    @Expose
    private List<String> schools = null;
    @SerializedName("teacher")
    @Expose
    private List<String> teacher = null;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("title")
    @Expose
    private String title;

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getSchools() {
        return schools;
    }

    public void setSchools(List<String> schools) {
        this.schools = schools;
    }

    public List<String> getTeacher() {
        return teacher;
    }

    public void setTeacher(List<String> teacher) {
        this.teacher = teacher;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date1 = new Date();

        try {
            date1 = dateFormat.parse(this.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date1;
    }

    @Override
    public int compareTo(@NonNull Schedule schedule) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date1 ;
        Date date2 ;


        try {
            date1 = dateFormat.parse(this.getTime());
            date2 = dateFormat.parse(schedule.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }


        return date1.getTime() > date2.getTime() ? 1 : date1.getTime()==date2.getTime() ? 0 : -1;
    }
}
