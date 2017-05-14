package com.xiaoxiang.ioutside.common;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.umeng.socialize.PlatformConfig;

import java.io.File;

/**
 * Created by zhang on 2016/6/2,0002.
 */
public class MyApplication extends Application {

    private String TAG = getClass().getSimpleName();
    private CachedInfo cachedInfo;
    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    public CachedInfo getCachedInfo() {
        return cachedInfo;
    }



//    /**
//     * 获取默认的 RefWatch 对象
//     * @param context
//     * @return
//     */
//    public static RefWatcher getRefWatcher(Context context) {
//        MyApplication application = (MyApplication) context.getApplicationContext();
//        return application.refWatcher;
//    }

  //  private RefWatcher refWatcher;


    @Override
    public void onCreate() {
        super.onCreate();


        //内存泄漏的检测工具 LeakCanary
//        refWatcher = LeakCanary.install(this);

        instance = this;

        //配置参数
        DisplayImageOptions options=new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoaderConfiguration configuration=new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(options)
                .threadPoolSize(5)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCacheFileCount(100)
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000))
                .build();
        //初始化ImageLoader
        ImageLoader.getInstance().init(configuration);


        Log.d(TAG, "运行到这里23了" + this.getFilesDir());
        //进行cachedinfo的处理
        cachedInfo = new CachedInfo(this);
        File tokenFile = new File(getFilesDir(), "cachedInfo");
        if (tokenFile.exists()) {
            cachedInfo.load();
        }

        //umeng qq: 100424468 c7394704798a158208a74ab60104f0ba
        //ioutside : 1105319552 Hoi7YisSaKrrRpVc
        PlatformConfig.setQQZone("1105319552", "Hoi7YisSaKrrRpVc");

        PlatformConfig.setWeixin("wx583526182896dcf2", "535c1b32a51e769d851a4e40b778fd50");

    }

}


