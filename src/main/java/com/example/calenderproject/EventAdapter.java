package com.example.calenderproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.ArrayList;

public class EventAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<Event> events;
    private ArrayList<String> eventsString;

    public EventAdapter(Activity activity, ArrayList<Event> events, ArrayList<String> eventsString) {

        this.mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.events = events;
        this.eventsString = eventsString;

    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //@SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.eventview_layout, null);
        TextView t1 = (TextView) convertView.findViewById(R.id.ev_textview11);
        TextView t2 = (TextView) convertView.findViewById(R.id.ev_textview22);
        TextView t3 = (TextView) convertView.findViewById(R.id.ev_textview33);

        Event e = events.get(position);
        String ename = e.getEventName();
        String string_element = func(eventsString, ename);
        if (string_element == null){
            return null;
        }
        String[] arrOfStr = string_element.split("-");
        String e_name=arrOfStr[0];
        String e_info=arrOfStr[1];
        String e_day=arrOfStr[2];
        String e_month=arrOfStr[3];
        String e_year=arrOfStr[4];
        String e_repeat=arrOfStr[5];
        String e_notif = arrOfStr[6];
        String e_locationinfo=arrOfStr[7];

        if (!e_repeat.equals("None")){
            //t1.setBackgroundColor(R.color.colorPrimary);
            if (e_repeat.equals("Daily")) {
                t2.setBackgroundResource(R.color.c1);
                t1.setBackgroundResource(R.color.c1);
                t3.setBackgroundResource(R.color.c1);
            }
            if (e_repeat.equals("Weekly")) {
                t2.setBackgroundResource(R.color.c2);
                t1.setBackgroundResource(R.color.c2);
                t3.setBackgroundResource(R.color.c2);
            }
            if (e_repeat.equals("Monthly")) {
                t2.setBackgroundResource(R.color.c3);
                t1.setBackgroundResource(R.color.c3);
                t3.setBackgroundResource(R.color.c3);
            }
            if (e_repeat.equals("Yearly")) {
                t2.setBackgroundResource(R.color.c4);
                t1.setBackgroundResource(R.color.c4);
                t3.setBackgroundResource(R.color.c4);
            }
        }
        t1.setText(e_name);
        String s2 = e_day + "/" + e_month + "/" + e_year + "  Repeat : " +  e_repeat;
        if (!e_repeat.equals("None")) {s2 += " - " +   e.getNextDay() + "/" + e.getMonth() + "/" + e.getYear();}
        t2.setText(s2);
        String ek = "No";
        if(e_notif.equals("1")){
            ek = "Yes" ;
        }
        String ek2 = "No";
        if(e_locationinfo.equals("1")){ek2 = "Yes";}
        String s3 = "Notification : " + ek + "\n" + "Location : " + ek2 + "\n" +  e_info;
        t3.setText(s3);
        return convertView;
    }

    public String func(ArrayList<String> liste1, String isim){
        for (String s: liste1){
            String[] arrOfStr = s.split("-",2);
            if(arrOfStr[0].equals(isim)){
                return s;
            }

        }
        return null;
    }



}
