package com.example.calenderproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import androidx.annotation.Nullable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class  MyDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "eventsDatabase";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "events";
    private static final String EVENT_NAME = "eventName";
    private static final String EVENT_INFO = "eventInfo";
    private static final String EVENT_DAY = "day";
    private static final String EVENT_MONTH = "month";
    private static final String EVENT_YEAR = "year";
    private static final String EVENT_REPEAT = "repeat";
    private static final String EVENT_NOTIF= "eventNotif";
    private static final String EVENT_NOTIFID = "eventNofifID";
    private static final String EVENT_LATITUTE = "eventLatitute";
    private static final String EVENT_LONGITUDE = "eventLongitude";
    private static final String EVENT_LOCATIONINFO = "eventLocationInfo";

    public MyDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "("
                //+ EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EVENT_NAME + " TEXT PRIMARY KEY NOT NULL, "
                + EVENT_INFO + " TEXT , "
                + EVENT_DAY + " INTEGER NOT NULL, "
                + EVENT_MONTH + " INTEGER NOT NULL, "
                + EVENT_YEAR + " INTEGER NOT NULL, "
                + EVENT_REPEAT + " TEXT NOT NULL, "
                + EVENT_NOTIF + " INTEGER NOT NULL, "
                + EVENT_NOTIFID + " INTEGER , "
                + EVENT_LATITUTE+ " REAL, "
                + EVENT_LONGITUDE+ " REAL, "
                + EVENT_LOCATIONINFO +  " INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addToDatabase(String ad, String bilgi, int day, int month, int year, String repeat, int notif, int locationinfo){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(EVENT_NAME, ad);
            cv.put(EVENT_INFO, bilgi);
            cv.put(EVENT_DAY, day);
            cv.put(EVENT_MONTH, month);
            cv.put(EVENT_YEAR, year);
            cv.put(EVENT_DAY, day);
            cv.put(EVENT_REPEAT, repeat);
            cv.put(EVENT_NOTIF, notif);
            //cv.put(EVENT_NOTIFID, notifID);
            //cv.put(EVENT_LATITUTE, );
            //cv.put(EVENT_LONGITUDE, );
            cv.put(EVENT_LOCATIONINFO, locationinfo);

            db.insert(TABLE_NAME, null,cv);
        }catch (Exception e){

        }
        db.close();
    }

    public void deleteFromDatabase (String nm){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME,EVENT_NAME + "=?",new String[]{String.valueOf(nm)});
        db.close();
    }


    public void updateEventInDatabase(String name, String new_info) {
        SQLiteDatabase db = this.getWritableDatabase();
        //Bu methodda ise var olan veriyi güncelliyoruz(update)
        ContentValues values = new ContentValues();
        values.put(EVENT_INFO, new_info);
        //values.put(KITAP_BASIM_YILI, kitap_basim_yili);
        //values.put(KITAP_FIYATI, kitap_fiyat);

        db.update(TABLE_NAME, values, EVENT_NAME + " = ?",
                new String[] { String.valueOf(name) });
        db.close();
    }

    public void updateEventNotifsInDatabase(String name, int a, int b) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EVENT_NOTIF, a);
        values.put(EVENT_NOTIFID, b);

        db.update(TABLE_NAME, values, EVENT_NAME + " = ?",
                new String[] { String.valueOf(name) });
        db.close();
    }

    public void updateLocationDatabase(String name, double a, double b) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EVENT_LATITUTE, a);
        values.put(EVENT_LONGITUDE, b);
        values.put(EVENT_LOCATIONINFO, 1);

        db.update(TABLE_NAME, values, EVENT_NAME + " = ?",
                new String[] { String.valueOf(name) });
        db.close();
    }


    public ArrayList<String> getAllData(){
        ArrayList<String> veriler=new ArrayList<String>();//String türünde bir liste oluşturduk.
        SQLiteDatabase db= this.getWritableDatabase();//SQLiteDatabase sınıfında yazılabilir bağlantı açıyoruz.
        String[] sutunlar={EVENT_NAME, EVENT_INFO, EVENT_DAY, EVENT_MONTH,EVENT_YEAR, EVENT_REPEAT, EVENT_NOTIF, EVENT_LOCATIONINFO};
        Cursor cr=db.query(TABLE_NAME,sutunlar,null,null,null,null,null);//query fonksiyonu ile aldığımız parametreler yoluyla komutu kendi içerisinde yapılandırıyoruz.
        while(cr.moveToNext()){//sırasıyla verileri listelememizi sağlıyor.
            veriler.add(cr.getString(0)+"-"+cr.getString(1)+"-"+cr.getString(2)+"-"+cr.getString(3)
                    +"-"+cr.getString(4)+"-"+cr.getString(5)+"-"+cr.getString(6)+"-"+cr.getString(7));

        }
        db.close();
        return veriler;
    }

    public ArrayList<String> getAllDate(){
        ArrayList<String> veriler=new ArrayList<String>();//String türünde bir liste oluşturduk.
        SQLiteDatabase db= this.getWritableDatabase();//SQLiteDatabase sınıfında yazılabilir bağlantı açıyoruz.
        String[] sutunlar={EVENT_NAME, EVENT_DAY, EVENT_MONTH,EVENT_YEAR };
        Cursor cr=db.query(TABLE_NAME,sutunlar,null,null,null,null,null);//query fonksiyonu ile aldığımız parametreler yoluyla komutu kendi içerisinde yapılandırıyoruz.
        while(cr.moveToNext()){//sırasıyla verileri listelememizi sağlıyor.
            veriler.add(cr.getString(0)+"-"+cr.getString(1)+"-"+cr.getString(2)+"-"+cr.getString(3));
        }
        db.close();
        return veriler;
    }



    public String getEventInfoDatabase(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] sutunlar={EVENT_NAME, EVENT_INFO};
        Cursor cr=db.query(TABLE_NAME,sutunlar,null,null,null,null,null);
        while(cr.moveToNext()){
            if (cr.getString(0).equals(name))  {db.close();     return cr.getString(1);}
        }
        return null;
    }

    public String getEventRepeatDatabase(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] sutunlar={EVENT_NAME, EVENT_REPEAT};
        Cursor cr=db.query(TABLE_NAME,sutunlar,null,null,null,null,null);
        while(cr.moveToNext()){
            if (cr.getString(0).equals(name))  {db.close();     return cr.getString(1);}
        }
        return null;
    }

    public float[] getLatituteLongitude(String name, float[] arr){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] sutunlar={EVENT_NAME, EVENT_LATITUTE, EVENT_LONGITUDE};
        Cursor cr=db.query(TABLE_NAME,sutunlar,null,null,null,null,null);
        while(cr.moveToNext()){
            if (cr.getString(0).equals(name))  {
                db.close();
                arr[0] = cr.getFloat(1);
                arr[1] = cr.getFloat(2);
                 //arr[0] = cr.getDouble(1);
                 //arr[1] = cr.getDouble(2);
                return arr;
                //return cr.getLong(1)
                //return cr.getString(1) + "+" + cr.getString(2);
            }
        }
        return null;
    }

    public Integer getNotID(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] sutunlar={EVENT_NAME, EVENT_NOTIFID, };
        Cursor cr=db.query(TABLE_NAME,sutunlar,null,null,null,null,null);
        //long arr[] = new long[2];
        while(cr.moveToNext()){
            if (cr.getString(0).equals(name))  {
                db.close();
                return cr.getInt(1);
            }
        }
        return null;
    }

    public String getDateAsString(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] sutunlar={EVENT_NAME, EVENT_DAY, EVENT_MONTH, EVENT_YEAR};
        Cursor cr=db.query(TABLE_NAME,sutunlar,null,null,null,null,null);
        String s = "";
        while(cr.moveToNext()){
            if (cr.getString(0).equals(name))  {
                db.close();
                s += cr.getString(1) +"/"+ cr.getString(2) +"/"+ cr.getString(3);
                return s;
            }
        }
        return s;
    }

    public int isNotificationDatabase(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] sutunlar={EVENT_NAME, EVENT_NOTIF};
        Cursor cr=db.query(TABLE_NAME,sutunlar,null,null,null,null,null);
        while(cr.moveToNext()){
            if (cr.getString(0).equals(name))  {db.close();     return cr.getInt(1);}
        }
        return 0;
    }

    public int checkLocationDatabase(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] sutunlar={EVENT_NAME, EVENT_LOCATIONINFO};
        Cursor cr=db.query(TABLE_NAME,sutunlar,null,null,null,null,null);
        while(cr.moveToNext()){
            if (cr.getString(0).equals(name))  {db.close();  return cr.getInt(1);}
        }
        return 0;
    }


    public boolean isExist(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] sutunlar={EVENT_NAME};
        Cursor cr=db.query(TABLE_NAME,sutunlar,null,null,null,null,null);
        while(cr.moveToNext()){
            if (cr.getString(0).equals(name)){return true;}
        }
        db.close();
        return false;
    }
}
