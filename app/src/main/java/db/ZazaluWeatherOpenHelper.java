package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zazalu on 7/19/16.
 */
public class ZazaluWeatherOpenHelper extends SQLiteOpenHelper{
    //Province table sqlString
    private final static String CREATE_PROVINCE = "create table province(" +
            "id integer primary key autoincrement," +
            "province_name text," +
            "province_code text " +
            ")";
    //City table sqlString
    private final static String CREATE_CITY = "create table city(" +
            "id integer primary key autoincrement," +
            "city_name text," +
            "city_code text," +
            "province_id integer " +
            ")";
    //County table sqlString
    private final static String CREATE_COUNTRY = "create table county(" +
            "id integer primary key autoincrement," +
            "county_name text," +
            "county_code text," +
            "city_id integer " +
            ")";

    public ZazaluWeatherOpenHelper(Context context, String dbname, SQLiteDatabase.CursorFactory factory,int version){
        super(context,dbname,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTRY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }

}
