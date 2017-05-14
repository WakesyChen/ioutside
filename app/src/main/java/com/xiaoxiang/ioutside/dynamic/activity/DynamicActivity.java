package com.xiaoxiang.ioutside.dynamic.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.common.CachedInfo;
import com.xiaoxiang.ioutside.common.Constants;
import com.xiaoxiang.ioutside.common.MyApplication;
import com.xiaoxiang.ioutside.dynamic.adapter.DynamicPublishAdapter;
import com.xiaoxiang.ioutside.mine.activity.LoginActivity;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

public class DynamicActivity extends AppCompatActivity implements Constants,OkHttpManager.ResultCallback.CommonErrorListener{
    private static final String TAG=DynamicActivity.class.getSimpleName();
    private ImageView dynamic_back;
    private ImageView dynamic_publish;
    private EditText dynamic_thoughts;
    private RecyclerView dynamic_recyclerView;
    private TextView dynamic_tag1;
    private TextView dynamic_tag2;
    private TextView dynamic_tag3;
    private ImageView dynamic_add_tag;
   // private ImageView dynamic_qq;
   // private ImageView dynamic_weixin;
   // private ImageView dynamic_weibo;
    private DynamicPublishAdapter mAdapter;
    private String token;
    private String fileToken;
    private ArrayList<String> list=new ArrayList<>();//存放上传图片的路径
    private ArrayList<String> localList=new ArrayList<>();//存放本地图片的路径
    private StringBuilder photoBuilder=new StringBuilder();//取图片
    private StringBuilder tagBuilder=new StringBuilder();
    private List<String> tags=new ArrayList<>(3);//存标签
    private int number=0;//控制标签的个数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        token=getIntent().getStringExtra("token");
        list=getIntent().getStringArrayListExtra("select_photo_list");
        localList=getIntent().getStringArrayListExtra("local_list");
        CachedInfo cachedInfo= MyApplication.getInstance().getCachedInfo();
        if (cachedInfo!=null){
            fileToken=cachedInfo.getToken();
            Log.d(TAG,"fileToken="+fileToken);
        }
        if (token!=null){
            initView();
        }
        else if (token==null&&fileToken==null){
            initView();
        }else {
            token=fileToken;
            initView();
        }
        initEvent();

    }

    private void initView() {
        setContentView(R.layout.activity_dynamic);
        dynamic_back = (ImageView) findViewById(R.id.dynamic_back);
        dynamic_publish = (ImageView) findViewById(R.id.dynamic_publish);
        dynamic_thoughts=(EditText)findViewById(R.id.dynamic_thoughts);
        dynamic_recyclerView = (RecyclerView) findViewById(R.id.dynamic_recyclerView);
        dynamic_tag1=(TextView)findViewById(R.id.dynamic_tag1);
        dynamic_tag2=(TextView)findViewById(R.id.dynamic_tag2);
        dynamic_tag3=(TextView)findViewById(R.id.dynamic_tag3);
        dynamic_add_tag=(ImageView)findViewById(R.id.dynamic_add_tag);
       // dynamic_qq=(ImageView)findViewById(R.id.dynamic_qq);
       // dynamic_weibo=(ImageView)findViewById(R.id.dynamic_weibo);
       // dynamic_weixin=(ImageView)findViewById(R.id.dynamic_weixin);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 4);
        dynamic_recyclerView.setLayoutManager(layoutManager);
        mAdapter=new DynamicPublishAdapter(localList);
        dynamic_recyclerView.setAdapter(mAdapter);
    }

    private void initEvent() {
        dynamic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder= new AlertDialog.Builder(DynamicActivity.this);
                builder.setTitle("是否退出本次编辑？");
                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.show();
            }
        });

        dynamic_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (number==0){
                        Toast.makeText(DynamicActivity.this, "标签不能为空!", Toast.LENGTH_LONG).show();
                    } else {
                        for (int i = 0; i < list.size(); i++) {//取出图片的地址，并转换为string
                            if (i != list.size() - 1)
                                photoBuilder.append(list.get(i) + ",");
                            else
                                photoBuilder.append(list.get(i));
                        }
                        Log.e(TAG, photoBuilder.toString());
                        final String thoughts = dynamic_thoughts.getText().toString();

                        for (int i = 0; i < tags.size(); i++) {//取出图片的地址，并转换为string
                            if (i != tags.size() - 1)
                                tagBuilder.append(tags.get(i) + ",");
                            else
                                tagBuilder.append(tags.get(i));
                        }
                        OkHttpManager mOkHttpManager = OkHttpManager.getInstance();
                        ApiInterImpl api=new ApiInterImpl();
                        mOkHttpManager.postAsyn(api.publish(thoughts, token, tagBuilder.toString(), photoBuilder.toString()), new OkHttpManager.ResultCallback<BaseResponse>(DynamicActivity.this) {
                            @Override
                            public void onError(Request request, Exception e) {
                                Toast.makeText(DynamicActivity.this, "网络有问题！", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "发布失败");
                            }

                            @Override
                            public void onResponse(BaseResponse response) {
                                super.onResponse(response);
                                String data=response.getData().toString();
                                int footprintID=Integer.parseInt(data.substring(13, data.length()-3));
                                Log.i(TAG, footprintID + "");
                                Toast.makeText(DynamicActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
/*
                                if (checkedWechat==true){
                                    String title=thoughts;
                                    String url="http://ioutside.com/xiaoxiang-backend/footprint-share.html?footprintID="+footprintID;
                                    setShareContentWeChat(title, url);
                                }
                                if (checkedQQ==true){
                                    String title=thoughts;
                                    String url="http://ioutside.com/xiaoxiang-backend/footprint-share.html?footprintID="+footprintID;
                                    setShareContent(title, url);
                                }
*/
                                finish();
                            }
                        });

                    }
                }
        });

        dynamic_add_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(DynamicActivity.this, DynamicTagActivity.class);
                startActivityForResult(intent, 8);
            }
        });
/*
        dynamic_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedQQ==false) {
                    checkedQQ=true;
                    dynamic_qq.setImageResource(R.drawable.dynamic_qq_pressed);
                }
                else {
                    checkedQQ=false;
                    dynamic_qq.setImageResource(R.drawable.dynamic_qq);
                }
            }
        });

        dynamic_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedWechat==false) {
                    checkedWechat=true;
                    dynamic_weixin.setImageResource(R.drawable.dynamic_weixin_pressed);
                }
                else {
                    checkedWechat=false;
                    dynamic_weixin.setImageResource(R.drawable.dynamic_weixin);
                }
            }
        });

        dynamic_weibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //同步到微博
            }
        });
*/
        dynamic_tag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(DynamicActivity.this);
                builder.setTitle("是否删除此标签？");
                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dynamic_tag1.setVisibility(View.GONE);
                        tags.remove(0);
                        number--;
                    }
                });
                builder.show();
            }
        });

        dynamic_tag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(DynamicActivity.this);
                builder.setTitle("是否删除此标签？");
                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dynamic_tag2.setVisibility(View.GONE);
                        tags.remove(1);
                        number--;
                    }
                });
                builder.show();
            }
        });

        dynamic_tag3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(DynamicActivity.this);
                builder.setTitle("是否删除此标签？");
                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dynamic_tag3.setVisibility(View.GONE);
                        tags.remove(2);
                        number--;
                    }
                });
                builder.show();
            }
        });

    }
/*
    private void setShareContentWeChat(String title, String url) {
        UMImage localImage = new UMImage(this, list.get(0));

        new ShareAction(this).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                .withText(title)
                .withTitle("来自爱户外的分享")
                .withTargetUrl(url)
                .withMedia(localImage)
                .open();
        finish();
    }

    private void setShareContent(String title, String url) {
        UMImage localImage = new UMImage(this, list.get(0));

        new ShareAction(this).setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                .withText(title)
                .withTitle("来自爱户外的分享")
                .withTargetUrl(url)
                .withMedia(localImage)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {

                    }
                })
                .open();
    }
*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==DynamicTagActivity.RESULT_CODE){
            String tag=data.getExtras().getString("tag");
            if (number==3){
                Toast.makeText(DynamicActivity.this,"最多只能有三个标签!",Toast.LENGTH_SHORT).show();
            }
            if (tag!=null&&number!=3) {
                tags.add(tag);
                if (number == 0) {
                    dynamic_tag1.setVisibility(View.VISIBLE);
                    dynamic_tag1.setText(tags.get(number));
                    number++;
                } else if (number == 1) {
                    dynamic_tag2.setVisibility(View.VISIBLE);
                    dynamic_tag2.setText(tags.get(number));
                    number++;
                } else {
                    dynamic_tag3.setVisibility(View.VISIBLE);
                    dynamic_tag3.setText(tags.get(number));
                    number++;
                }
            }
        }
    }

    @Override
    public void onCommonError(int errorCode) {
        // 这里处理通用逻辑
        if (errorCode == USER_OUTLINE) {
            Toast.makeText(this,"你已在别的地方登录，你被迫下线，请重新登录！",Toast.LENGTH_SHORT).show();
            Intent in=new Intent(this, LoginActivity.class);
            startActivity(in);
        }else if(errorCode==TOKEN_OVERTIME){
            Toast.makeText(this,"你的登录信息已过期，请重新登录",Toast.LENGTH_SHORT).show();
            Intent in=new Intent(this, LoginActivity.class);
            startActivity(in);
        }else if(errorCode==SERVER_ERROR){
            Toast.makeText(this,"服务器内部错误，请重新登录",Toast.LENGTH_SHORT).show();
            Intent in=new Intent(this, LoginActivity.class);
            startActivity(in);
        }
    }
}
