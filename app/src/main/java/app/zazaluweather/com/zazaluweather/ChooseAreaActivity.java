package app.zazaluweather.com.zazaluweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import model.City;
import model.County;
import model.Province;
import util.HttpCallbackListener;
import util.HttpUtil;
import util.ProvinceAdapter;
import util.Utility;
import util.ZazaluWeatherDB;

public class ChooseAreaActivity extends AppCompatActivity {

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    private ProgressDialog progressDialog;
    private TextView titleText;
    private ListView listVIew;
    private ProvinceAdapter adapter;
    private List<String> dataList = new ArrayList<>();
    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;
    private Province selectedProvince;
    private City selectedCity;
    private int currentLevel;
    private ZazaluWeatherDB db;
    private boolean isFromWeatherActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity",false);
        if(prefs.getBoolean("city_selected",false) && !isFromWeatherActivity){
            Intent intent = new Intent(this,WeatherActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        titleText = (TextView)findViewById(R.id.title_text);
        listVIew = (ListView)findViewById(R.id.list_view);
        adapter = new ProvinceAdapter(this,R.layout.list_view,dataList);
        listVIew.setAdapter(adapter);
        listVIew.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel == LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(position);
                    queryCity();
                }else if (currentLevel == LEVEL_CITY){
                    selectedCity = cityList.get(position);
                    queryCounty();
                }else if (currentLevel == LEVEL_COUNTY){
                    String countyCode = countyList.get(position).getCountyCode();
                    Intent intent = new Intent(ChooseAreaActivity.this,WeatherActivity.class);
                    intent.putExtra("county_code",countyCode);
                    startActivity(intent);
                    finish();
                }
            }
        });
        db = ZazaluWeatherDB.getInstance(this);
        //获取省信息并且加载到listView里
        queryProvinces();
    }

    public void queryProvinces(){
        provinceList =  db.loadProvince();
        if(provinceList.size() > 0){
            dataList.clear();
            for(Province p : provinceList){
                dataList.add(p.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listVIew.setSelection(0);
            titleText.setText("中国");
            currentLevel = LEVEL_PROVINCE;
        }else {
            //从服务器上查找
            queryFromServer(null,"province");
        }
    }

    public void queryCity(){
        cityList = db.loadCity(selectedProvince.getId());
        if(cityList.size() > 0){
            dataList.clear();
            for(City c : cityList){
                dataList.add(c.getCityName());
            }
            adapter.notifyDataSetChanged();
            listVIew.setSelection(0);
            titleText.setText(selectedProvince.getProvinceName());
            currentLevel = LEVEL_CITY;
        }else {
            queryFromServer(selectedProvince.getProvinceCode(),"city");
        }
    }

    public void queryCounty(){
        countyList = db.loadCounty(selectedCity.getId());
        if(countyList.size() > 0){
            dataList.clear();
            for(County c : countyList){
                dataList.add(c.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listVIew.setSelection(0);
            titleText.setText(selectedCity.getCityName());
            currentLevel = LEVEL_COUNTY;
        }else{
            queryFromServer(selectedCity.getCityCode(),"county");
        }

    }


    public void queryFromServer(String code, final String type){
        String address;
        if(!TextUtils.isEmpty(code)){
            address = "http://weather.com.cn/data/list3/city"+ code + ".xml";
        }else {
            address = "http://weather.com.cn/data/list3/city.xml";
        }
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                if("province".equals(type)){
                    result = Utility.handleProvinceResponse(db,response);
                }else if("city".equals(type)){
                    result = Utility.handleCityResponse(db,response,selectedProvince.getId());
                }else if ("county".equals(type)){
                    result = Utility.handleCountiesResponse(db,response,selectedCity.getId());
                }
                if(result){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if("province".equals(type)){
                                queryProvinces();
                            }else if ("city".equals(type)){
                                queryCity();
                            }else if ("county".equals(type)){
                                queryCounty();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this,"记载失败",Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

    private void showProgressDialog(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed(){
        if(currentLevel == LEVEL_COUNTY){
            queryCity();
        }else if (currentLevel == LEVEL_CITY){
            queryProvinces();
        }else {
            if(isFromWeatherActivity){
                Intent intent = new Intent(this,WeatherActivity.class);
                startActivity(intent);
            }
            finish();
        }
    }
}
