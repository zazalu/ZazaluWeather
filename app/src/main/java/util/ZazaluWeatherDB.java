package util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import db.ZazaluWeatherOpenHelper;
import model.City;
import model.County;
import model.Province;

/**
 * Created by zazalu on 7/19/16.
 * can help me do some database operations
 */
public class ZazaluWeatherDB {
    public static final String DB_NAME = "zazalu_weather";
    private SQLiteDatabase db;
    public static final int VERSION = 1;
    //single mode
    private ZazaluWeatherDB zazaluWeatherDB;

    //init
    private ZazaluWeatherDB(Context context){
        ZazaluWeatherOpenHelper dbHelper = new ZazaluWeatherOpenHelper(context,DB_NAME,null,VERSION);
        db = dbHelper.getWritableDatabase();
    }

    //getInstance
    public ZazaluWeatherDB getInstance(Context context){
        if(zazaluWeatherDB == null){
            zazaluWeatherDB = new ZazaluWeatherDB(context);
        }
        return zazaluWeatherDB;
    }

    //save Province data to db
    public boolean saveProvince(Province province){
        if(province != null){
            ContentValues values = new ContentValues();
            values.put("province_name",province.getProvinceName());
            values.put("province_code",province.getProvinceCode());
            db.insert("province",null,values);
            return true;
        }
        return false;
    }

    //load Province data from table province
    public List<Province> loadProvince(){
        List<Province> list = new ArrayList<>();
        //query
        Cursor cursor =  db.query("province",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(province);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    //save City data to db
    public boolean saveCity(City city){
        if(city != null){
            ContentValues values = new ContentValues();
            values.put("city_name",city.getCityName());
            values.put("city_code",city.getCityCode());
            values.put("province_id",city.getProvinceId());
            db.insert("city",null,values);
            return true;
        }
        return false;
    }

    //load City data from table city
    public List<City> loadCity(int provinceId){
        List<City> list = new ArrayList<>();
        Cursor cursor = db.query("City",null,"province_id = ?",new String[]{ String.valueOf(provinceId)},null,null,null);
        if(cursor.moveToFirst()){
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(provinceId);
                list.add(city);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    //save County data to db
    public boolean saveCounty(County county){
        if(county != null){
            ContentValues values = new ContentValues();
            values.put("county_name",county.getCountyName());
            values.put("county_code",county.getCountyCode());
            values.put("city_id",county.getCityId());
            db.insert("county",null,values);
            return true;
        }
        return false;
    }

    //load County data from table county
    public List<County> loadCounty(int cityId){
        List<County> list = new ArrayList<>();
        Cursor cursor = db.query("County",null,"city_id = ?",new String[]{String.valueOf(cityId)},null,null,null);
        if(cursor.moveToFirst()){
            do {
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityId(cityId);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
