package com.example.calenderproject;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Calendar;

public class Event extends AppCompatActivity {
    private String eventName;
    private String eventInfo;
    private int day;
    private int month;
    private int year;
    private int notif_ID;
    private boolean notif;
    private String repeat;
    private double eventLatitute;
    private double eventLongitude;
    private boolean isLocationExist;

    private int nextDay;
    private int nextMonth;
    private int nextYear;
    private boolean repeatDate;
    private float timeInMilis;

    //static ArrayList<Event>  eventsList = new ArrayList<Event>();

    public Event(String name, int a, int b, int c, String r ){
        this.eventName = name;
        this.day = a;
        this.month=b;
        this.year=c;
        this.timeInMilis = getTimeInM();
        this.repeat = r;
        this.nextDay = a;
        this.nextMonth = b;
        this.nextYear = c;
        this.repeatDate = false;
    }

    public Event(String name, int a, int b, int c, String r , int d, int e, int f){
        this.eventName = name;
        this.day = a;
        this.month=b;
        this.year=c;
        this.repeat = r;
        this.nextDay = d;
        this.nextMonth = e;
        this.nextYear = f;
        this.repeatDate = true;
        this.timeInMilis = getTimeInMV2();
    }

    public Event(){
    }

    //public static ArrayList<Event>  getEventsList(){
    //    return eventsList;
   // }

    public long getTimeInM(){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        //cal.add(Calendar.SECOND,MINUTE,HOUR, int);
        //cal.add(Calendar.SECOND, 10);
        //Date date = cal.getTime();
        cal.set(Calendar.DATE,getDay());
        cal.set(Calendar.MONTH,getMonth()-1);
        cal.set(Calendar.YEAR,getYear());
        //cal.set(Calendar.HOUR_OF_DAY, 22);  //HOUR
        //cal.set(Calendar.MINUTE, 20);
        //cal.set(Calendar.SECOND,0);
        return cal.getTimeInMillis();
    }

    public long getTimeInMV2(){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        //cal.add(Calendar.SECOND,MINUTE,HOUR, int);
        //cal.add(Calendar.SECOND, 10);
        //Date date = cal.getTime();
        cal.set(Calendar.DATE,getNextDay());
        cal.set(Calendar.MONTH,getNextMonth()-1);
        cal.set(Calendar.YEAR,getNextYear());
        //cal.set(Calendar.HOUR_OF_DAY, 22);  //HOUR
        //cal.set(Calendar.MINUTE, 20);
        //cal.set(Calendar.SECOND,0);
        return cal.getTimeInMillis();
    }

    public String getEventName() {
        return eventName;
    }
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    public int getDay() {
        return day;
    }
    public int getMonth() {
        return month;
    }
    public int getYear() {
        return year;
    }
    public String getRepeat() {
        return repeat;
    }
    public void setDay(int day) {
        this.day = day;
    }
    public double getEventLatitute() { return eventLatitute; }
    public int getNextDay() {
        return nextDay;
    }
    public int getNextMonth() {
        return nextMonth;
    }
    public int getNextYear() {
        return nextYear;
    }
}
