package com.xiaoxiang.ioutside.homepage.activity;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.common.CachedInfo;
import com.xiaoxiang.ioutside.common.CircleImageView;
import com.xiaoxiang.ioutside.common.Constants;
import com.xiaoxiang.ioutside.common.MyApplication;
import com.xiaoxiang.ioutside.common.methods.UmengShare;
import com.xiaoxiang.ioutside.homepage.model.ArticleDetail;
import com.xiaoxiang.ioutside.mine.activity.LoginActivity;
import com.xiaoxiang.ioutside.mine.activity.OtherPersonActivity;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GArticleDetail;
import com.xiaoxiang.ioutside.util.NetworkUtil;

import java.lang.reflect.Type;

import okhttp3.Request;

public class ArticleDetailActivity extends AppCompatActivity implements Constants,OkHttpManager.ResultCallback.CommonErrorListener {
    private static final String TAG = "ArticleDetailActivity";
    public static final int RESULT_CODE=11;
    private static final int REQUEST_CODE=2;
    private RelativeLayout article_detail_relativeLayout;
    private TextView article_detail_articleType;
    private ImageView article_detail_back;//返回
    private ImageView article_detail_collect;//收藏文章
    private CircleImageView article_detail_userPhoto;//文章的推荐人
    private TextView article_detail_userName;//用户昵称
    private TextView article_detail_original;
    private ImageView article_detail_focus;//加关注
    private WebView article_detail_webView;
    private ImageView article_detail_share;//分享文章
    private ImageView article_detail_function_zan;//文章的点赞
    private TextView article_detail_likedCount;//文章的点赞数
    private EditText article_detail_comment;
    private TextView article_detail_commentCount;//文章的评论量
    private ImageView article_detail_function_comment;
    private ArticleDetail articleDetail;
    private int id;
    private String token;
    private String fileToken;
    private PopupWindow popupWindow;
    private int index=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        id = Integer.parseInt(intent.getStringExtra("id"));
        token = intent.getStringExtra("token");
        CachedInfo  cachedInfo = MyApplication.getInstance().getCachedInfo();
        if (cachedInfo != null) {
             fileToken = cachedInfo.getToken();
            Log.i(TAG, "fileToken=" + fileToken);
        }
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_article_detail);
        article_detail_relativeLayout=(RelativeLayout)findViewById(R.id.article_detail_relativeLayout);
        article_detail_back = (ImageView) findViewById(R.id.article_detail_back);
        article_detail_articleType=(TextView)findViewById(R.id.article_detail_articleType);
        article_detail_collect = (ImageView) findViewById(R.id.article_detail_collect);
        article_detail_userPhoto = (CircleImageView) findViewById(R.id.article_detail_userPhoto);
        article_detail_userName=(TextView)findViewById(R.id.article_detail_userName);
        article_detail_original=(TextView)findViewById(R.id.article_detail_original);
        article_detail_focus = (ImageView) findViewById(R.id.article_detail_focus);
        article_detail_webView = (WebView) findViewById(R.id.article_detail_webView);
        article_detail_function_comment = (ImageView) findViewById(R.id.article_detail_function_comment);
        article_detail_function_zan = (ImageView) findViewById(R.id.article_detail_function_zan);
        article_detail_likedCount = (TextView) findViewById(R.id.article_detail_likedCount);
        article_detail_comment=(EditText)findViewById(R.id.article_detail_comment);
        article_detail_commentCount = (TextView) findViewById(R.id.article_detail_commentCount);
        article_detail_share = (ImageView) findViewById(R.id.article_detail_share);
        if (token != null) {
            initData();
        } else if (token == null && fileToken == null) {
            initData();
        } else {
            token = fileToken;
            initData();
        }
        initEvent();

    }

    private void initData() {
        if (NetworkUtil.isNetworkConnected(this)){
            ApiInterImpl api=new ApiInterImpl();
            OkHttpManager okHttpManager=OkHttpManager.getInstance();
            okHttpManager.getStringAsyn(api.getArticleDetail(id,token), new OkHttpManager.ResultCallback<String>(ArticleDetailActivity.this) {
                @Override
                public void onError(Request request, Exception e) {
                    Log.i(TAG, "error");
                }

                @Override
                public void onResponse(String response) {
                    super.onResponse(response);
                    Type objectType = new TypeToken<BaseResponse<GArticleDetail>>() {
                    }.getType();
                    Gson gson=new Gson();
                    BaseResponse detailResponse = gson.fromJson(response, objectType);
                    GArticleDetail gArticleDetail = (GArticleDetail) detailResponse.getData();
                    articleDetail = gArticleDetail.getArticle();
                    if (articleDetail != null) {
                        String subjectName=articleDetail.getSubjectName();
                        String url=articleDetail.getUrl();
                        String userName=articleDetail.getUserName();
                        String userPhoto=articleDetail.getUserPhoto();
                        boolean isOriginal=articleDetail.isOriginal();
                        int likeCount = articleDetail.getLikedCount();//点赞数
                        int commentCount=articleDetail.getCommentCount();
                        boolean isLike=articleDetail.isLiked();
                        if (articleDetail.isObserved()== false)
                            article_detail_focus.setImageResource(R.drawable.add_focus);
                        else
                            article_detail_focus.setImageResource(R.drawable.has_focus);

                        if (isLike == false)
                            article_detail_function_zan.setImageResource(R.drawable.zan_normal);
                        else
                            article_detail_function_zan.setImageResource(R.drawable.zan_pressed);

                        if (isOriginal==false)
                            article_detail_original.setText("推荐");
                        else
                            article_detail_original.setText("原创");
                        article_detail_articleType.setText(subjectName);
                        article_detail_likedCount.setText(likeCount + "");
                        article_detail_commentCount.setText(commentCount + "");
                        article_detail_userName.setText(userName);
//                        ImageLoader.getInstance().displayImage(userPhoto, article_detail_userPhoto);
                        Glide.with(getApplicationContext()).load(userPhoto).into(article_detail_userPhoto);
                        //加载网页
                        WebSettings webSettings=article_detail_webView.getSettings();
                        webSettings.setJavaScriptEnabled(true);
                        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//使用缓存
                        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
                            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);//实现混合加载
                        }
                        article_detail_webView.loadUrl(url);
                        article_detail_webView.setWebViewClient(new WebViewClient() {
                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                view.loadUrl(url);
                                return true;
                            }
                        });

                    }
                }
            });
        }
        else {
            article_detail_relativeLayout.setVisibility(View.GONE);
            Toast.makeText(this,"网络有点问题!",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(popupWindow!=null){
            popupWindow.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (articleDetail!=null){
            int likeCount=articleDetail.getLikedCount();
            int commentCount=articleDetail.getCommentCount();
            Bundle bundle=new Bundle();
            bundle.putInt("likeCount", likeCount);//是否点赞
            bundle.putInt("commentCount", commentCount);//是否评论
            bundle.putInt("id", id);
            Intent mIntent=new Intent();
            mIntent.putExtras(bundle);
            setResult(RESULT_CODE, mIntent);
            finish();
        }
        else
            finish();
    }

    private void initEvent() {
        final ApiInterImpl api = new ApiInterImpl();
        article_detail_articleType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int subjectId=articleDetail.getSubjectID();
                Intent intentSubject=new Intent(ArticleDetailActivity.this, SubjectDetailActivity.class);
                intentSubject.putExtra("subjectID",subjectId);
                intentSubject.putExtra("token",token);

                startActivity(intentSubject);
            }
        });

        article_detail_focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userId = articleDetail.getUserID();
                boolean isObserved = articleDetail.isObserved();
                OkHttpManager okHttpManager = OkHttpManager.getInstance();
                if (token == null) {
                    noLogin();
                } else {
                    if (isObserved == false) {
                        okHttpManager.getStringAsyn(api.observeUser(userId, token), new OkHttpManager.ResultCallback<String>(ArticleDetailActivity.this) {
                            @Override
                            public void onError(Request request, Exception e) {
                                Toast.makeText(ArticleDetailActivity.this, "网络有点问题！", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "关注失败");
                            }

                            @Override
                            public void onResponse(String response) {
                                super.onResponse(response);
                                Log.d(TAG, response);
                                articleDetail.setObserved(true);
                                article_detail_focus.setImageResource(R.drawable.has_focus);//没有效果
                            }
                        });
                    } else {
                        okHttpManager.getStringAsyn(api.cancelObserveUser(userId, token), new OkHttpManager.ResultCallback<String>(ArticleDetailActivity.this) {
                            @Override
                            public void onError(Request request, Exception e) {
                                Toast.makeText(ArticleDetailActivity.this, "网络有点问题！", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "取消关注失败");
                            }

                            @Override
                            public void onResponse(String response) {
                                super.onResponse(response);
                                Log.d(TAG, response);
                                articleDetail.setObserved(false);
                                article_detail_focus.setImageResource(R.drawable.add_focus);
                            }
                        });
                    }

                }
            }
        });


        article_detail_function_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int commentCount = articleDetail.getCommentCount();
                Intent intent = new Intent(ArticleDetailActivity.this, ArticleCommentActivity.class);
                intent.putExtra("commentCount", "" + commentCount);
                intent.putExtra("id", "" + id);
                intent.putExtra("token", token);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        article_detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        article_detail_userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userId = articleDetail.getUserID();
                boolean isObserved = articleDetail.isObserved();
                Intent intentInfo = new Intent();
                intentInfo.setClass(ArticleDetailActivity.this, OtherPersonActivity.class);
                intentInfo.putExtra("userID", userId);//要传入什么信息
                intentInfo.putExtra("observed", isObserved);
                intentInfo.putExtra("token", token);
                startActivity(intentInfo);
            }
        });

        article_detail_userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userId = articleDetail.getUserID();
                boolean isObserved = articleDetail.isObserved();
                Intent intentInfo = new Intent();
                intentInfo.setClass(ArticleDetailActivity.this, OtherPersonActivity.class);
                intentInfo.putExtra("userID", userId);//要传入什么信息
                intentInfo.putExtra("observed", isObserved);
                intentInfo.putExtra("token", token);
                startActivity(intentInfo);
            }
        });


        //点赞的接口
        article_detail_function_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (token == null) {
                    noLogin();
                } else {//使用全局变量可以
                    OkHttpManager okHttpManager = OkHttpManager.getInstance();
                    boolean isLike = articleDetail.isLiked();
                    if (isLike == false) {
                        okHttpManager.getStringAsyn(api.likeArticle(id, token), new OkHttpManager.ResultCallback<String>(ArticleDetailActivity.this) {
                            @Override
                            public void onError(Request request, Exception e) {
                                Log.i(TAG, "onError");
                                Toast.makeText(ArticleDetailActivity.this, "网络有点问题", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String response) {
                                super.onResponse(response);
                                Log.i(TAG, "onResponse");
                                article_detail_function_zan.setImageResource(R.drawable.zan_pressed);
                                articleDetail.setLiked(true);
                                int likeCount = articleDetail.getLikedCount();
                                likeCount++;
                                articleDetail.setLikedCount(likeCount);
                                article_detail_likedCount.setText("" + likeCount);
                            }
                        });

                    } else {
                        okHttpManager.getStringAsyn(api.cancelLikeArticle(id, token),
                                new OkHttpManager.ResultCallback<String>(ArticleDetailActivity.this) {
                                    @Override
                                    public void onError(Request request, Exception e) {
                                        Log.i(TAG, "onError");
                                        Toast.makeText(ArticleDetailActivity.this, "网络有点问题", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onResponse(String response) {
                                        super.onResponse(response);
                                        Log.i(TAG, "onResponse:id"+id);
                                        article_detail_function_zan.setImageResource(R.drawable.zan_normal);
//                                        articleDetail.setLiked(true);  //bug点
                                        articleDetail.setLiked(false);
                                        int likeCount = articleDetail.getLikedCount();
                                        likeCount--;
                                        articleDetail.setLikedCount(likeCount);
                                        article_detail_likedCount.setText("" + likeCount);
                                    }
                                });
                    }
                }
            }
        });

        article_detail_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (token == null) {
                    noLogin();
                } else {
                    showPopupComment();
                }
            }
        });


        //        点击两次收藏会退出出现的bug

        article_detail_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ArticleDetailActivity.this);
                builder.setItems(new String[]{"收藏", "举报"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                if (token == null) {
                                    noLogin();
                                } else {
                                    boolean isCollected = articleDetail.isCollected();
                                    if (isCollected == false) {
                                        OkHttpManager.Param tokenParam = new OkHttpManager.Param("token", token);
                                        OkHttpManager.Param idParam = new OkHttpManager.Param("id", String.valueOf(id));
                                        OkHttpManager mOkHttpManager = OkHttpManager.getInstance();
                                        mOkHttpManager.postAsyn(api.collectArticle(id, token), new OkHttpManager.ResultCallback<String>(ArticleDetailActivity.this) {
                                            @Override
                                            public void onError(Request request, Exception e) {
                                                Toast.makeText(ArticleDetailActivity.this, "网络有问题！", Toast.LENGTH_SHORT).show();
                                                Log.d(TAG, "收藏状态1"+articleDetail.isCollected());
                                            }

                                            @Override
                                            public void onResponse(String response) {
                                                super.onResponse(response);
                                                Toast.makeText(ArticleDetailActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                                                Log.d(TAG, response);
                                                articleDetail.setCollected(true);//点击后收藏成功，状态改变
                                                Log.d(TAG, "收藏状态2"+articleDetail.isCollected());

                                            }
                                        }, new OkHttpManager.Param[]{idParam, tokenParam});
                                    } else {
                                        Log.d(TAG, "收藏状态3"+articleDetail.isCollected());

                                        Toast.makeText(ArticleDetailActivity.this, "已经收藏过了!请不要重复收藏", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                break;
                            case 1:
                                if (token == null) {
                                    noLogin();
                                } else {
                                    //举报
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ArticleDetailActivity.this);
                                    builder1.setTitle("举报类型");
                                    builder1.setSingleChoiceItems(new String[]{"广告推销", "色情暴力", "其他"}, 0, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            index = which;
                                        }
                                    });
                                    builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            int postID = id;//文章的id
                                            int postType = 1;//表示是文章
                                            int reportUserID = articleDetail.getUserID();//用户的id
                                            int reportType;
                                            OkHttpManager mOkHttpManager = OkHttpManager.getInstance();
                                            switch (index) {
                                                case 0:
                                                    reportType = 1;
                                                    mOkHttpManager.postAsyn(api.addReport(postID, postType, reportUserID, reportType, token), new OkHttpManager.ResultCallback<String>(ArticleDetailActivity.this) {
                                                        @Override
                                                        public void onError(Request request, Exception e) {
                                                            Toast.makeText(ArticleDetailActivity.this, "网络有问题！", Toast.LENGTH_SHORT).show();
                                                            Log.d(TAG, "举报失败");
                                                        }

                                                        @Override
                                                        public void onResponse(String response) {
                                                            super.onResponse(response);
                                                            Toast.makeText(ArticleDetailActivity.this, "举报文章广告推销成功", Toast.LENGTH_SHORT).show();
                                                            Log.d(TAG, response);
                                                        }
                                                    });
                                                    break;
                                                case 1:
                                                    reportType = 2;
                                                    mOkHttpManager.postAsyn(api.addReport(postID, postType, reportUserID, reportType, token), new OkHttpManager.ResultCallback<String>(ArticleDetailActivity.this) {
                                                        @Override
                                                        public void onError(Request request, Exception e) {
                                                            Toast.makeText(ArticleDetailActivity.this, "网络有问题！", Toast.LENGTH_SHORT).show();
                                                            Log.d(TAG, "举报失败");
                                                        }

                                                        @Override
                                                        public void onResponse(String response) {
                                                            super.onResponse(response);
                                                            Toast.makeText(ArticleDetailActivity.this, "举报文章色情暴力成功", Toast.LENGTH_SHORT).show();
                                                            Log.d(TAG, response);
                                                        }
                                                    });
                                                    break;
                                                case 2:
                                                    reportType = 3;
                                                    mOkHttpManager.postAsyn(api.addReport(postID, postType, reportUserID, reportType, token), new OkHttpManager.ResultCallback<String>(ArticleDetailActivity.this) {
                                                        @Override
                                                        public void onError(Request request, Exception e) {
                                                            Toast.makeText(ArticleDetailActivity.this, "网络有问题！", Toast.LENGTH_SHORT).show();
                                                            Log.d(TAG, "举报失败");
                                                        }

                                                        @Override
                                                        public void onResponse(String response) {
                                                            super.onResponse(response);
                                                            Toast.makeText(ArticleDetailActivity.this, "举报文章其他成功", Toast.LENGTH_SHORT).show();
                                                            Log.d(TAG, response);
                                                        }
                                                    });
                                                    break;
                                                case 3:
                                                    dialog.dismiss();
                                                    break;
                                            }
                                        }
                                    });
                                    builder1.show();
                                }
                                break;
                        }
                    }
                });
                AlertDialog dialog=builder.create();
                Window window=dialog.getWindow();
                WindowManager.LayoutParams params=new WindowManager.LayoutParams();
                params.x=500;
                params.y=500;
                window.setAttributes(params);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.show();
            }
        });

        article_detail_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String photo=articleDetail.getPhoto();
                String title = articleDetail.getTitle();
                String content=articleDetail.getContent();
                String url = "http://ioutside.com/xiaoxiang-backend/article-share.html?articleID=" + id;
//                setShareContent(title, url,photo,content);
                UmengShare.setShareContent(ArticleDetailActivity.this,title,url,photo,content);

            }
        });
    }

    private void showPopupComment() {
        View view = LayoutInflater.from(ArticleDetailActivity.this).inflate(R.layout.comment_popupwindow, null);
        final EditText inputComment=(EditText)view.findViewById(R.id.comment);
        ImageView submit_comment=(ImageView)view.findViewById(R.id.submit_comment);
        TextView quit_comment=(TextView)view.findViewById(R.id.quit_comment);
        popupWindow=new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);//设置点击窗口外边窗口消失
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        ColorDrawable cd = new ColorDrawable(0x000000);
        popupWindow.setBackgroundDrawable(cd);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.4f;
        getWindow().setAttributes(params);
        popupWindow.update();
        popupInputMethodWindow();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            }
        });
        quit_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        submit_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //向服务器发送post请求
                String comment = inputComment.getText().toString().trim();
                if (TextUtils.isEmpty(comment)) {
                    Toast.makeText(ArticleDetailActivity.this, "评论不能为空!请重新输入", Toast.LENGTH_SHORT).show();
                } else {
                    //直接评论文章
                    int postID = id;
                    OkHttpManager mOkHttpManager = OkHttpManager.getInstance();
                    ApiInterImpl api = new ApiInterImpl();
                    mOkHttpManager.postAsyn(api.addCommentForArticle(postID, comment, token), new OkHttpManager.ResultCallback<String>(ArticleDetailActivity.this) {
                        @Override
                        public void onError(Request request, Exception e) {
                            Toast.makeText(ArticleDetailActivity.this, "网络有问题！", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "评论失败");
                        }

                        @Override
                        public void onResponse(String response) {
                            super.onResponse(response);
                            Toast.makeText(ArticleDetailActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, response);
                            int commentCount=articleDetail.getCommentCount();
                            commentCount++;
                            articleDetail.setCommentCount(commentCount);
                            article_detail_commentCount.setText("" + commentCount);
                            popupWindow.dismiss();
                        }
                    });
                }
            }
        });
    }

    private void popupInputMethodWindow() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }.start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==ArticleCommentActivity.RESULT_CODE){
            Bundle bundle=data.getExtras();
            int _commentCount=bundle.getInt("commentCount");
            int commentCount=articleDetail.getCommentCount();
            if (commentCount!=_commentCount){
                commentCount=_commentCount;
                articleDetail.setCommentCount(commentCount);
                article_detail_commentCount.setText(commentCount+"");
            }
        }
    }

    public void noLogin(){
        String[] items=new String[]{"登录","取消"};
        AlertDialog.Builder builder=new AlertDialog.Builder(ArticleDetailActivity.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        Intent intent=new Intent(ArticleDetailActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        dialog.dismiss();
                        break;
                }
            }
        }).show();
    }

    @Override
    public void onCommonError(int errorCode) {
        // 这里处理通用逻辑
        if (errorCode == USER_OUTLINE) {
            Toast.makeText(this,"你已在别的地方登录，你被迫下线，请重新登录！",Toast.LENGTH_SHORT).show();
            Intent in=new Intent(this,LoginActivity.class);
            startActivity(in);
        }else if(errorCode==TOKEN_OVERTIME){
            Toast.makeText(this,"你的登录信息已过期，请重新登录",Toast.LENGTH_SHORT).show();
            Intent in=new Intent(this,LoginActivity.class);
            startActivity(in);
        }else if(errorCode==SERVER_ERROR){
            Toast.makeText(this,"服务器内部错误，请重新登录",Toast.LENGTH_SHORT).show();
            Intent in=new Intent(this,LoginActivity.class);
            startActivity(in);
        }
    }

    private void setShareContent(String title,String url,String photo,String content) {

//        UMImage localImage = new UMImage(ArticleDetailActivity.this, R.mipmap.head_ele);
        UMImage localImage = new UMImage(ArticleDetailActivity.this,photo);

        final SHARE_MEDIA[] displayList = new SHARE_MEDIA[]
                {
                        SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                        SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE
                };


        new ShareAction(this).setDisplayList( displayList )
                .withTitle(title)//分享的标题
                .withText("  "+content)//分享的内容
                .withTargetUrl(url)
                .withMedia(localImage)
                //.setListenerList(umShareListener)
                .open();
        Log.i(TAG, "setShareContent: url"+url);
    }


}



