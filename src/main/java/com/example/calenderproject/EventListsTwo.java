package com.example.calenderproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class EventListsTwo extends AppCompatActivity {
    Button bt1,bt2,bt3,bt4;
    ListView lv;
    ArrayList<Event> aList, bList;
    MyDatabase db = new MyDatabase(this);
    ArrayList<String> eventsListAll;
    ArrayList<String> eventsListDates;
    long weekInMilis = (long)(1000 * 60*60*24*7);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(R.style.AppThemeSecond);
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String s1 = sh.getString("mode", "light");
        if(s1.equals("light")){
            setTheme(R.style.AppTheme);
            setContentView(R.layout.eventlists_layout);
            LinearLayout lL = (LinearLayout)findViewById(R.id.LinearLayoutx4);
            //lL.setBackgroundResource(R.color.blue);

        }else if (s1.equals("dark")){
            setTheme(R.style.ThemeOverlay_AppCompat_Dark);
            setContentView(R.layout.eventlists_layout);

        }

        bt1 = (Button) findViewById(R.id.bt1);
        bt2 = (Button) findViewById(R.id.bt2);
        bt3 = (Button) findViewById(R.id.bt3);
        bt4 = (Button) findViewById(R.id.bt4);
        lv = (ListView)findViewById(R.id.listview1);
        eventsListAll = db.getAllData();
        eventsListDates = db.getAllDate();

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventsListAll = db.getAllData();
                ArrayList<Event> eventsList = makeEventObject(eventsListAll);

                final ArrayList<Event> sortedList = new ArrayList<Event>(eventsList);
                Collections.sort(sortedList, new Comparator<Event>() {
                    public int compare(Event p1, Event p2) {
                        return Long.compare(p1.getTimeInM(), p2.getTimeInM());
                    }
                });

                EventAdapter adapter = new EventAdapter(EventListsTwo.this, sortedList, eventsListAll);
                lv.setAdapter(adapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        Intent in = new Intent(EventListsTwo.this, UpdateEvent.class);
                        //in.putExtra("key", tmp_index);
                        in.putExtra("key", sortedList.get(position).getEventName());
                        //in.putExtra("key", position);
                        startActivity(in);
                    }
                });

                bt2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar tmpCal = Calendar.getInstance();
                        LocalDateTime now = LocalDateTime.now();
                        int year = now.getYear();
                        int month = now.getMonthValue();
                        int day = now.getDayOfMonth();
                        aList = new ArrayList<>();
                        for (Event e_object: sortedList){
                            if (year == e_object.getYear() && month == e_object.getMonth() && e_object.getDay() == day){
                                aList.add(e_object);
                            }

                            if (e_object.getRepeat().equals("Daily")   &&   (e_object.getYear() < year  ||  (year == e_object.getYear() &&  e_object.getMonth() < month) ||
                                    (year == e_object.getYear() && month == e_object.getMonth() && e_object.getDay() < day))  ){
                                aList.add(e_object);
                            }

                            if (e_object.getRepeat().equals("Weekly")   &&   (e_object.getYear() < year  ||  (year == e_object.getYear() &&  e_object.getMonth() < month) ||
                                    ( year == e_object.getYear() && month == e_object.getMonth() && e_object.getDay() < day))  ){
                                Calendar cal01 = Calendar.getInstance();
                                cal01.set(Calendar.DATE,day);
                                cal01.set(Calendar.MONTH,month-1);
                                cal01.set(Calendar.YEAR,year);
                                long ct = cal01.getTimeInMillis();
                                long et = e_object.getTimeInM();
                                while (et < ct){
                                    et += weekInMilis;
                                    if (et==ct) {aList.add(e_object);  break;}
                                }

                            }

                            if (e_object.getRepeat().equals("Monthly")   &&  e_object.getDay() == day
                                    && (e_object.getYear() < year  ||  (year == e_object.getYear() &&  e_object.getMonth() < month))  ){
                                aList.add(e_object);
                            }

                            if (e_object.getRepeat().equals("Yearly")   &&   e_object.getYear() < year && (e_object.getDay() == day && e_object.getMonth()==month )){
                                aList.add(e_object);
                            }

                        }
                        EventAdapter adapter = new EventAdapter(EventListsTwo.this, aList, eventsListAll);
                        lv.setAdapter(adapter);
                    }
                });

                bt3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LocalDateTime now = LocalDateTime.now();
                        int year = now.getYear();
                        int month = now.getMonthValue();
                        int day = now.getDayOfMonth();
                        //aList = new ArrayList<>();
                        bList = new ArrayList<>();
                        for (Event e_object: sortedList){
                            if (year == e_object.getYear() && month == e_object.getMonth() && e_object.getDay() - day < 7 && e_object.getDay() - day > -1){
                                bList.add(e_object);
                            }

                            if (e_object.getRepeat().equals("Daily")   &&
                                    (e_object.getYear() < year  ||  (year == e_object.getYear() &&  e_object.getMonth() < month) ||
                                            ( year == e_object.getYear() &&  e_object.getMonth() == month && e_object.getDay() < day) ) ){
                                bList.add(e_object);
                            }

                            if (e_object.getRepeat().equals("Weekly")   &&   (e_object.getYear() < year  ||  year == e_object.getYear() &&  e_object.getMonth() < month ||
                                    year == e_object.getYear() && month == e_object.getMonth() && e_object.getDay() < day)  ){
                                Calendar cal103 = Calendar.getInstance();
                                cal103.set(Calendar.DATE,e_object.getDay());
                                cal103.set(Calendar.MONTH,e_object.getMonth()-1);
                                cal103.set(Calendar.YEAR,e_object.getYear());
                                Calendar cal104 = Calendar.getInstance();
                                cal104.set(Calendar.DATE,day);
                                cal104.set(Calendar.MONTH,month-1);
                                cal104.set(Calendar.YEAR,year);
                                long td = cal104.getTimeInMillis();
                                while (true){
                                    cal103.add(Calendar.MILLISECOND, 1000*60*60*24*7);
                                    long ed = cal103.getTimeInMillis();
                                    if (ed - td >= 0 && ed - td < weekInMilis){
                                        bList.add(new Event(e_object.getEventName(),day,month,year,e_object.getRepeat(),cal103.get(Calendar.DATE), cal103.get(Calendar.MONTH)+1, cal103.get(Calendar.YEAR)));
                                        //bList.add(e_object);
                                        break;
                                    }
                                    if (ed - td > weekInMilis) {break;}
                                }

                            }

                            if (e_object.getRepeat().equals("Monthly") && (e_object.getYear() < year || year == e_object.getYear() &&  e_object.getMonth() < month)){
                                Calendar cal102 = Calendar.getInstance();
                                cal102.set(Calendar.DATE,day);
                                cal102.set(Calendar.MONTH,month-1);
                                cal102.set(Calendar.YEAR,year);
                                long td = cal102.getTimeInMillis();

                                Calendar cal01 = Calendar.getInstance();
                                cal01.set(Calendar.DATE,e_object.getDay());
                                cal01.set(Calendar.MONTH, e_object.getMonth()-1);
                                cal01.set(Calendar.YEAR, e_object.getYear());
                                while (true){
                                    cal01.add(Calendar.MONTH, 1);
                                    long ct = cal01.getTimeInMillis();
                                    if (ct - td >= 0 && ct - td < weekInMilis) {
                                        bList.add(new Event(e_object.getEventName(),day,month,year,e_object.getRepeat(),cal01.get(Calendar.DATE), cal01.get(Calendar.MONTH)+1, cal01.get(Calendar.YEAR)));
                                        bList.add(e_object);
                                        break;
                                    }
                                    if (ct - td > weekInMilis) {break;}
                                }

                            }

                            if (e_object.getRepeat().equals("Yearly")   &&   e_object.getYear() < year && e_object.getMonth()==month){
                                int i = 0;
                                while (i<500){
                                    i++;
                                    if (e_object.getDay() - day < 8 && e_object.getDay() - day > -1){
                                        bList.add(e_object);
                                        break;
                                    }
                                }
                            }


                        }

                        /*
                        final ArrayList<Event> aList = new ArrayList<Event>(bList);
                        Collections.sort(sortedList, new Comparator<Event>() {
                            public int compare(Event p1, Event p2) {
                                return Long.compare(p1.getTimeInM(), p2.getTimeInM());
                            }
                        });

                         */
                        final ArrayList<Event> aList = new ArrayList<Event>(bList);
                        Collections.sort(aList, new Comparator<Event>() {
                            public int compare(Event p1, Event p2) {
                                return Integer.compare(p1.getNextDay(), p2.getNextDay());
                            }
                        });

                        EventAdapter adapter = new EventAdapter(EventListsTwo.this, aList, eventsListAll);
                        lv.setAdapter(adapter);
                    }
                });

                bt4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LocalDateTime now = LocalDateTime.now();
                        int year = now.getYear();
                        int month = now.getMonthValue();
                        int day = now.getDayOfMonth();
                        //aList = new ArrayList<>();
                        bList = new ArrayList<>();
                        for (Event e_object: sortedList){

                            if (year == e_object.getYear() && month == e_object.getMonth()){
                                bList.add(e_object);
                            }
                            if (e_object.getRepeat().equals("Daily")   &&   (e_object.getYear() < year  ||  (year == e_object.getYear() &&  e_object.getMonth() < month))){
                                bList.add(e_object);
                            }

                            if (e_object.getRepeat().equals("Weekly")   &&   (e_object.getYear() < year  ||  year == e_object.getYear() &&  e_object.getMonth() < month ||
                                    year == e_object.getYear() && month == e_object.getMonth() && e_object.getDay() < day)  ){
                                Calendar cal01 = Calendar.getInstance();
                                cal01.set(Calendar.DATE,day);
                                cal01.set(Calendar.MONTH,month-1);
                                cal01.set(Calendar.YEAR,year);
                                long ct = cal01.getTimeInMillis();
                                long et = e_object.getTimeInM();
                                while (et < ct) {
                                    et += weekInMilis;
                                }
                                //bList.add(e_object);
                                //Calendar cal02 = Calendar.getInstance();
                                cal01.set(Calendar.DATE,1);
                                cal01.set(Calendar.MONTH,month-1 + 1);
                                cal01.set(Calendar.YEAR,year);
                                long ct2 = cal01.getTimeInMillis();
                                while (et < ct2) {
                                    cal01.setTimeInMillis(et);
                                    bList.add(new Event(e_object.getEventName(),day,month,year,e_object.getRepeat(),cal01.get(Calendar.DATE), cal01.get(Calendar.MONTH)+1, cal01.get(Calendar.YEAR)));
                                    et += weekInMilis;
                                }
                            }

                            if (e_object.getRepeat().equals("Monthly") &&
                                    (e_object.getYear() < year || (year == e_object.getYear() &&  e_object.getMonth() < month) )){
                                bList.add(e_object);
                            }

                            if (e_object.getRepeat().equals("Yearly")   &&   e_object.getYear() < year && e_object.getMonth()==month){
                                bList.add(e_object);
                            }
                        }
                        /*
                        final ArrayList<Event> aList = new ArrayList<Event>(bList);
                        Collections.sort(sortedList, new Comparator<Event>() {
                            public int compare(Event p1, Event p2) {
                                return Long.compare(p1.getTimeInM(), p2.getTimeInM());
                            }
                        });
                        */
                        final ArrayList<Event> aList = new ArrayList<Event>(bList);
                        Collections.sort(aList, new Comparator<Event>() {
                            public int compare(Event p1, Event p2) {
                                return Integer.compare(p1.getNextDay(), p2.getNextDay());
                            }
                        });
                        EventAdapter adapter = new EventAdapter(EventListsTwo.this, aList, eventsListAll);
                        lv.setAdapter(adapter);
                    }
                });
            }
        });

    }

    public ArrayList<Event> makeEventObject(ArrayList<String> eList1){
        ArrayList<Event> eList2 = new ArrayList<>();
        for(String s: eList1){
            String[] arrOfStr = s.split("-");
            //String e_name=arrOfStr[0];
            eList2.add(new Event(arrOfStr[0],Integer.valueOf(arrOfStr[2]), Integer.valueOf(arrOfStr[3]), Integer.valueOf(arrOfStr[4]), arrOfStr[5] ));
        }
        return eList2;
    }

}
