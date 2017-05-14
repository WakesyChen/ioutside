package com.xiaoxiang.ioutside.common.methods;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.homepage.activity.ArticleDetailActivity;

/**
 * Created by Wakesy on 2016/8/10.
 */
public class UmengShare {

    //友盟分享的创建

    public static void setShareContent(Activity activity, String title, String url, String photo, String content) {

        UMImage localImage = new UMImage(activity, R.mipmap.head_ele);
        if (!TextUtils.isEmpty(photo)&&localImage!=null) {
            localImage = new UMImage(activity,photo);//photo链接
        }

        final SHARE_MEDIA[] displayList = new SHARE_MEDIA[]
                {
                        SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                        SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE
                };


        new ShareAction(activity).setDisplayList( displayList )
                .withText("  "+content)//分享的标题
                .withTitle(title)//分享的内容
                .withTargetUrl(url)
                .withMedia(localImage)
                //.setListenerList(umShareListener)
                .open();
    }

}
