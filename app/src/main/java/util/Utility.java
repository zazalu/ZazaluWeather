package util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import app.zazaluweather.com.zazaluweather.R;
import model.City;
import model.County;
import model.Province;

/**
 * Created by zazalu on 7/19/16.
 *
 */
public class Utility {

    //analysis the Province data from Server
    public static boolean handleProvinceResponse(ZazaluWeatherDB zazaluWeatherDB,String response){
        if(!TextUtils.isEmpty(response)){
            String[] allProvinces = response.split(",");
            if(allProvinces.length > 0){
                for(String p: allProvinces){
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceName(array[1]);
                    province.setProvinceCode(array[0]);
                    zazaluWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }
    //analysis the City data from Server
    public static boolean handleCityResponse(ZazaluWeatherDB db,String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            String[] allProvinces = response.split(",");
            if(allProvinces.length > 0){
                for(String p: allProvinces){
                    String[] array = p.split("\\|");
                    City city = new City();
                    city.setCityName(array[1]);
                    city.setCityCode(array[0]);
                    city.setProvinceId(provinceId);
                    db.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    //analysis the County data from Server
    public static boolean handleCountiesResponse(ZazaluWeatherDB db,String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            String[] allProvinces = response.split(",");
            if(allProvinces.length > 0){
                for(String p: allProvinces){
                    String[] array = p.split("\\|");
                    County county = new County();
                    county.setCountyName(array[1]);
                    county.setCountyCode(array[0]);
                    county.setCityId(cityId);
                    db.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }

    //analysis JSON using JSONObject
    public static void handleWeatherResponse(Context context,String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
        String cityName = weatherInfo.getString("city");
        String weatherCode = weatherInfo.getString("cityid");
        String temp1 = weatherInfo.getString("temp");
        String temp2 = "null";
        String windDirect = weatherInfo.getString("WD");
        String windLevel = weatherInfo.getString("WS");
        String weatherDesp = weatherInfo.getString("njd");
        String publishTime = weatherInfo.getString("time");
        saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, windDirect, windLevel, weatherDesp, publishTime);


    }

    //save all JSON data into SharedPreference
    public static void saveWeatherInfo(Context context, String cityName, String weatherCode,
                                       String temp1, String temp2, String windDirect, String windLevel, String weatherDesp, String publishTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit(); //打开编辑文件开始写入数据
        editor.putBoolean("city_selected", true);
        editor.putString("city_name", cityName);
        editor.putString("weather_code", weatherCode);
        editor.putString("temp1", temp1);
        editor.putString("temp2", temp2);
        editor.putString("wind_direct", windDirect);
        editor.putString("wind_level", windLevel);
        editor.putString("weather_desp", weatherDesp);
        editor.putString("publish_time", publishTime);
        editor.putString("current_date", sdf.format(new Date()));
        editor.commit(); //提交写入内容
    }


}
