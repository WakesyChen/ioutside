package com.xiaoxiang.ioutside.homepage.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.util.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wakesy on 2017/5/27.
 */
public class ChooseCityActivity extends Activity implements View.OnClickListener{
    ImageView choose_back;
    EditText cityName;
    Button check;
    private String cityname;
    private SimpleAdapter cityAdapter;
    private GridView gridView;
    private List<Map<String,String>>datalist;
    private String[] cities={"武汉","上海","杭州","南京","广州","深圳","北京","沈阳","苏州","齐齐哈尔","郑州","合肥"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_city);
        cityName= (EditText) findViewById(R.id.cityName);
        choose_back= (ImageView) findViewById(R.id.choose_back);
        gridView= (GridView) findViewById(R.id.city_GridView);
        check= (Button) findViewById(R.id.check);
        choose_back.setOnClickListener(this);
        check.setOnClickListener(this);
        datalist=new ArrayList<>();
        cityAdapter=new SimpleAdapter(this,getData(),R.layout.city_item,new String[]{"city"},new int[]{R.id.city_text});
        gridView.setAdapter(cityAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(ChooseCityActivity.this,WeatherActivity.class);
                TextView cityText= (TextView) view.findViewById(R.id.city_text);
                cityname=cityText.getText().toString();
                intent.putExtra("cityName",cityname);
                setResult(WeatherActivity.RESULT_CODE,intent);
                finish();
            }
        });
    }

    private List<Map<String ,String>> getData() {
        for (int i=0;i<cities.length;i++){
            Map<String,String>map=new HashMap<>();
            map.put("city",cities[i]);
            datalist.add(map);
        }
        return datalist;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.choose_back:
                finish();
                break;
            case R.id.check:
                Intent intent=new Intent(ChooseCityActivity.this,WeatherActivity.class);
                cityname=cityName.getText().toString().trim();
                if (cityname.equals("")) {
                    ToastUtils.show("请先输入城市名!");
                }else {
                    intent.putExtra("cityName",cityname);
                    setResult(WeatherActivity.RESULT_CODE,intent);
                    finish();
                    break;
                }
        }

    }
}
