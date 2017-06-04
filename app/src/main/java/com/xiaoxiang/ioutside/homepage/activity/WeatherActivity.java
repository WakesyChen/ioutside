package com.xiaoxiang.ioutside.homepage.activity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.homepage.model.WeatherData;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.util.ToastUtils;
import java.lang.reflect.Type;
import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;

/**
 * Created by Wakesy on 2017/5/25.
 */
public class WeatherActivity extends Activity implements View.OnClickListener {
    @Bind(R.id.weather_back)
    ImageView weather_back;
    @Bind(R.id.weather_city)
    TextView weather_city;
    @Bind(R.id.weather_currentTemp)
    TextView weather_currentTemp;
    @Bind(R.id.weather_state)
    TextView weather_state;
    @Bind(R.id.weather_time)
    TextView weather_time;
    @Bind(R.id.weather_todayTemp)
    TextView weather_todayTemp;
    @Bind(R.id.weather_stateImg)
    ImageView weather_stateImg;
    @Bind(R.id.weather_refresh)
    ImageView weather_refresh;

    private String cityName="武汉";
    private String currentTemp;
    private String state;
    private String time;
    private String todayTemp;
    private static final String TAG = "WeatherActivity";
    public static int REQUEST_CODE=100;
    public static int RESULT_CODE=200;
    private int[] weatherImgs={R.drawable.weather_sunny,R.drawable.weather_cloud,
            R.drawable.weather_rainny2,R.drawable.weather_snow1,R.drawable.weather_smoke};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_main);
        ButterKnife.bind(this);
        weather_back.setOnClickListener(this);
        weather_refresh.setOnClickListener(this);
        weather_city.setOnClickListener(this);
        weather_city.setText(cityName);
        refreshData(cityName);


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weather_back:
                finish();
                break;
            case R.id.weather_refresh:
                refreshData(cityName);
                break;
            case R.id.weather_city:
                Intent intent=new Intent(WeatherActivity.this,ChooseCityActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
                break;
        }

    }

    //    刷新数据
    private void refreshData(String cityName) {
        ApiInterImpl api=new ApiInterImpl();
        OkHttpManager okHttpManager=OkHttpManager.getInstance();
        okHttpManager.getStringAsyn(api.getWeatherData(cityName),
                                    new OkHttpManager.ResultCallback<String>(){
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                ToastUtils.show("网络错误");
            }
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Log.i(TAG,response);
                Type objectType= new TypeToken<WeatherData>(){}.getType();
                Gson gson=new Gson();
                WeatherData weatherData=gson.fromJson(response,objectType);
                if(weatherData!=null) {
                    if (weatherData.getResultcode() ==200) {
                        refreshUI(weatherData);//更新界面
                        ToastUtils.show("更新成功！");
                    } else {

                        ToastUtils.show(weatherData.getReason() + "");
                    }
                }

            }

        });

    }

    private void refreshUI(WeatherData weatherData) {
        currentTemp=weatherData.getResult().getSk().getTemp();
        state=weatherData.getResult().getToday().getWeather();
        time=weatherData.getResult().getToday().getWeek();
        todayTemp=weatherData.getResult().getToday().getTemperature();
        Log.i(TAG,currentTemp+"---"+state+"---"+time+"---"+todayTemp);
//        stateImg;
        weather_currentTemp.setText(currentTemp+"℃");
        weather_state.setText(state);
        weather_time.setText(time);
        weather_todayTemp.setText(todayTemp);
        setWeatherImgByState(state);
    }

    private void setWeatherImgByState(String state) {
            if(state.contains("云")){
                weather_stateImg.setImageResource(R.drawable.weather_cloud);
            }else if (state.contains("雨")){
                weather_stateImg.setImageResource(R.drawable.weather_rainny2);
            }else  if (state.contains("晴")){
                weather_stateImg.setImageResource(R.drawable.weather_sunny);
            }else if (state.contains("雪")){
                weather_stateImg.setImageResource(R.drawable.weather_snow1);
            }else if(state.contains("雾")){
                weather_stateImg.setImageResource(R.drawable.weather_smoke);
            }else {
                weather_stateImg.setImageResource(R.drawable.weather_cloud);
            }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            if(requestCode==REQUEST_CODE&&resultCode==RESULT_CODE){
                cityName=data.getStringExtra("cityName");
                weather_city.setText(cityName);
                refreshData(cityName);
            }

        }
    }
}

