package util;

import android.text.TextUtils;

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
}
