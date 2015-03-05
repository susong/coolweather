package com.dream.coolweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dream.coolweather.R;
import com.dream.coolweather.db.CoolWeatherDB;
import com.dream.coolweather.model.City;
import com.dream.coolweather.model.County;
import com.dream.coolweather.model.Province;
import com.dream.coolweather.model.State;
import com.dream.coolweather.util.AreaXmlParse;
import com.dream.coolweather.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SuSong on 2015/3/4 0004.
 */
public class ChooseAreaActivity extends Activity {

    public static final int LEVEL_STATE = 0;
    public static final int LEVEL_PROVINCE = 1;
    public static final int LEVEL_CITY = 2;
    public static final int LEVEL_COUNTY = 3;

    private ProgressDialog progressDialog;

    private ListView listView;
    private TextView titleText;

    private List<String> dataList = new ArrayList<String>();
    private ArrayAdapter adapter;
    private CoolWeatherDB coolWeatherDB;

    private List<State> stateList;
    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;

    private State selectedState;
    private Province selectedProvince;
    private City selectedCity;

    private int currentLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);
        listView = (ListView) findViewById(R.id.list_view);
        titleText = (TextView) findViewById(R.id.title_text);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        coolWeatherDB = CoolWeatherDB.getInstance(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_STATE) {
                    selectedState = stateList.get(position);
                    queryProvinces();
                } else if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(position);
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(position);
                    queryCounties();
                }
            }

        });
        queryStates();
    }

    private void queryStates() {
        stateList = coolWeatherDB.loadStates();
        if (stateList.size() > 0) {
            dataList.clear();
            for (State state : stateList) {
                dataList.add(state.getStateName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText("全球");
            currentLevel = LEVEL_STATE;
        } else {
            queryFromServer("state");
        }
    }

    private void queryProvinces() {
        provinceList = coolWeatherDB.loadProvinces(selectedState.getStateCode());
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedState.getStateName());
            currentLevel = LEVEL_PROVINCE;
        } else {
            queryFromServer("province");
        }
    }

    private void queryCities() {
        cityList = coolWeatherDB.loadCity(selectedProvince.getProvinceCode());
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedProvince.getProvinceName());
            currentLevel = LEVEL_CITY;
        } else {
            queryFromServer("city");
        }
    }

    private void queryCounties() {
        countyList = coolWeatherDB.loadCounties(selectedCity.getCityCode());
        if (countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedCity.getCityName());
            currentLevel = LEVEL_COUNTY;
        } else {
            queryFromServer("county");
        }
    }

    private void queryFromServer(final String type) {
        showProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result = false;
                result = Utility.handleResponce(coolWeatherDB, AreaXmlParse.parseCityId(ChooseAreaActivity.this));
                if (result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("state".equals(type)) {
                                queryStates();
                            } else if ("province".equals(type)) {
                                queryProvinces();
                            } else if ("city".equals(type)) {
                                queryCities();
                            } else if ("county".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();

    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if (currentLevel == LEVEL_COUNTY) {
            queryCities();
        } else if (currentLevel == LEVEL_CITY) {
            queryProvinces();
        } else if (currentLevel == LEVEL_PROVINCE) {
            queryStates();
        } else {
            finish();
        }
    }
}
