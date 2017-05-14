package com.xiaoxiang.ioutside.common;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 15119 on 2016/6/19.
 */
public class BaseActivity extends Activity {

    /**
     * 此类暂时为空，之后的重构所有 activity 都应该继承自该类以放置全局配置
     */


    private static final List<Activity> activityCollector = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCollector.add(this);
    }

    public void removeAll() {
        for (Activity activity : activityCollector) {
            activity.finish();
            activityCollector.remove(activity);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityCollector.remove(this);
    }
}
