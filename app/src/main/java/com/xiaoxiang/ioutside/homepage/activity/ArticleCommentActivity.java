package com.xiaoxiang.ioutside.homepage.activity;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.common.CachedInfo;
import com.xiaoxiang.ioutside.common.Constants;
import com.xiaoxiang.ioutside.common.MyApplication;
import com.xiaoxiang.ioutside.common.alipaydemo.PayDemoActivity;
import com.xiaoxiang.ioutside.homepage.NoAlphaItemAnimator;
import com.xiaoxiang.ioutside.homepage.adapter.CommentAdapter;
import com.xiaoxiang.ioutside.mine.activity.LoginActivity;
import com.xiaoxiang.ioutside.mine.activity.OtherPersonActivity;
import com.xiaoxiang.ioutside.model.CommentList;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.network.response.gsonresponse.GCommentList;
import com.xiaoxiang.ioutside.util.ToastUtils;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Request;


public class ArticleCommentActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,Constants,OkHttpManager.ResultCallback.CommonErrorListener{
    private static final String TAG=ArticleCommentActivity.class.getSimpleName();
    public static final int RESULT_CODE=22;
    public static final int REQUEST_CODE=33;
    private RelativeLayout article_comment_relativeLayout;
    private ImageView article_comment_back;
    private ImageView article_comment;
    private SwipeRefreshLayout swipeRefresh_comment;
    private RecyclerView recyclerView_comment;
    private CommentAdapter mAdapter;
    private int commentCount;
    private int id;
    private int pageSize=5;
    private int pageNo=1;
    private String token;
    private String fileToken;
    private CachedInfo cachedInfo;//为了得到登录人的userName
    private PopupWindow popupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        commentCount=Integer.parseInt(intent.getStringExtra("commentCount"));
        id=Integer.parseInt(intent.getStringExtra("id"));
        token=getIntent().getStringExtra("token");
        cachedInfo= MyApplication.getInstance().getCachedInfo();
        if (cachedInfo != null) {
            fileToken = cachedInfo.getToken();
            Log.d(TAG, "fileToken=" + fileToken);
        }
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_article_comment);
        article_comment_relativeLayout=(RelativeLayout)findViewById(R.id.article_comment_relativeLayout);
        article_comment_back=(ImageView)findViewById(R.id.article_comment_back);
        article_comment=(ImageView)findViewById(R.id.article_comment);
        swipeRefresh_comment=(SwipeRefreshLayout)findViewById(R.id.swipeRefresh_comment);
        swipeRefresh_comment.setOnRefreshListener(this);
        recyclerView_comment=(RecyclerView)findViewById(R.id.recyclerView_comment);
        recyclerView_comment.addOnScrollListener(listener);
        recyclerView_comment.setHasFixedSize(true);
        recyclerView_comment.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_comment.setItemAnimator(new NoAlphaItemAnimator());
        mAdapter=new CommentAdapter();
        recyclerView_comment.setAdapter(mAdapter);
        if (token!=null){
           onRefresh();
        }
        else if (token==null&&fileToken==null){
           onRefresh();
        }
        else {
            token=fileToken;
            onRefresh();
        }
        initEvent();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(popupWindow!=null){
            popupWindow.dismiss();
        }
    }

    @Override
    public void onRefresh() {
        int pageNo = 1;
        OkHttpManager okHttpManager=OkHttpManager.getInstance();
        ApiInterImpl api=new ApiInterImpl();
        okHttpManager.getStringAsyn(api.getCommentListForArticle(id, pageSize, pageNo), new OkHttpManager.ResultCallback<String>(ArticleCommentActivity.this) {
            @Override
            public void onError(Request request, Exception e) {
                Log.i(TAG, "onError");
            }

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Type objectType = new TypeToken<BaseResponse<GCommentList>>() {
                }.getType();
                Gson gson = new Gson();
                BaseResponse commentResponse = gson.fromJson(response, objectType);
                GCommentList gCommentList = (GCommentList) commentResponse.getData();
                List<CommentList> list = gCommentList.getList();
                if (list != null && list.size() != 0) {
                    mAdapter.addItemToHead(pageSize,list);
                }
                swipeRefresh_comment.setRefreshing(false);
            }
        });
    }

    private RecyclerView.OnScrollListener listener=new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (recyclerView_comment.getLayoutManager().getChildCount() > 0) {
                View lastChildView = recyclerView_comment.getLayoutManager().getChildAt(recyclerView_comment.getLayoutManager().getChildCount() - 1);
                int lastPosition = recyclerView_comment.getLayoutManager().getPosition(lastChildView);
                pageNo++;
                //进行判断
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastPosition == recyclerView_comment.getLayoutManager().getItemCount() - 1) {
                    OkHttpManager okHttpManager=OkHttpManager.getInstance();
                    ApiInterImpl api=new ApiInterImpl();
                    okHttpManager.getStringAsyn(api.getCommentListForArticle(id, pageSize, pageNo), new OkHttpManager.ResultCallback<String>(ArticleCommentActivity.this) {
                        @Override
                        public void onError(Request request, Exception e) {
                            Log.i(TAG, "onError");
                        }

                        @Override
                        public void onResponse(String response) {
                            super.onResponse(response);
                            Type objectType = new TypeToken<BaseResponse<GCommentList>>() {
                            }.getType();
                            Gson gson=new Gson();
                            BaseResponse commentResponse = gson.fromJson(response, objectType);
                            GCommentList gCommentList = (GCommentList) commentResponse.getData();
                            List<CommentList> list = gCommentList.getList();
                            if (list!=null&&list.size()!=0){
                                for (CommentList commentList : list) {
                                    if (!mAdapter.getDataSet().contains(commentList)) {
                                        mAdapter.addItem(commentList);
                                    }
                                }
                            }
                            swipeRefresh_comment.setRefreshing(false);
                        }
                    });
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    private void initEvent() {
        article_comment_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        article_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupComment();
            }
        });

        mAdapter.setOnItemClickListener(new CommentAdapter.OnItemClickListener() {
            @Override
            public void onUserInfoClick(View view, int position) {
                int userId = mAdapter.getDataSet().get(position).getUserID();
                boolean isObserved = false;
                Intent intent = new Intent(ArticleCommentActivity.this, OtherPersonActivity.class);
                intent.putExtra("userID", userId);
                intent.putExtra("observed", isObserved);
                intent.putExtra("token", token);
                startActivity(intent);
            }

            @Override
            public void onItemClick(View view,final int position) {
                if (token==null){
                    noLogin();
                }
                else {
                    String userName=mAdapter.getDataSet().get(position).getUserName();//评论列表得到的昵称
                    String name=cachedInfo.getPersonalInfo().getName();
                    if (userName.equals(name)) {//如果是自己评论的评论
                        AlertDialog.Builder builder = new AlertDialog.Builder(ArticleCommentActivity.this);
                        builder.setTitle("是否删除评论？");
                        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int commentId = mAdapter.getDataSet().get(position).getId();
                                OkHttpManager okHttpManager=OkHttpManager.getInstance();
                                ApiInterImpl api=new ApiInterImpl();
                                okHttpManager.getStringAsyn(api.deleteArticleComment(commentId, id, token), new OkHttpManager.ResultCallback<String>(ArticleCommentActivity.this) {
                                    @Override
                                    public void onError(Request request, Exception e) {
                                        super.onError(request, e);
                                        Toast.makeText(ArticleCommentActivity.this, "网络有点问题!", Toast.LENGTH_SHORT).show();
                                        Log.e(TAG, "onError");
                                    }

                                    @Override
                                    public void onResponse(String response) {
                                        super.onResponse(response);
                                        Toast.makeText(ArticleCommentActivity.this, "删除成功!", Toast.LENGTH_SHORT).show();
                                        commentCount--;
                                        mAdapter.removeItem(position);
                                    }
                                });
                            }
                        });
                        builder.show();
                    }
                    else {//如果是别人的评论，就提示回复
                        int otherId=mAdapter.getDataSet().get(position).getId();//回复了哪条评论
                        int otherUserId=mAdapter.getDataSet().get(position).getUserID();//回复了谁
                        showPopupComment(otherId,otherUserId);
                    }
                }
            }

            @Override
            public void onCommentDialogClick(View view, int position) {
                //跳转到评论列表界面
                int userId=mAdapter.getDataSet().get(position).getUserID();//评论人id
                int receiverId=mAdapter.getDataSet().get(position).getReceiverID();//被评论人的id
                int referCommentId=mAdapter.getDataSet().get(position).getReferCommentID();//一级评论的id
                int _commentCount=commentCount;
                Intent intent=new Intent(ArticleCommentActivity.this,ArticleDialogActivity.class);
                intent.putExtra("id", id+"");
                intent.putExtra("userId", userId+"");
                intent.putExtra("receiverId",receiverId+"");
                intent.putExtra("referCommentId", referCommentId+"");
                intent.putExtra("token", token);
                intent.putExtra("commentCount",""+_commentCount);
                startActivityForResult(intent, REQUEST_CODE);
            }

            @Override
            public void onOtherUserNameClick(View view, int position) {
                int userId = mAdapter.getDataSet().get(position).getReceiverID();
                boolean isObserved = false;
                Intent intent = new Intent(ArticleCommentActivity.this, OtherPersonActivity.class);
                intent.putExtra("userID", userId);
                intent.putExtra("observed", isObserved);
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });
    }


    //填写评论的弹出框
    private void showPopupComment() {
        View view = LayoutInflater.from(ArticleCommentActivity.this).inflate(R.layout.comment_popupwindow, null);
        final EditText inputComment=(EditText)view.findViewById(R.id.comment);
        ImageView submit_comment=(ImageView) view.findViewById(R.id.submit_comment);
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
        popupWindow.setBackgroundDrawable(new BitmapDrawable());//bug出现点，响应返回键必须的语句
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
        submit_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //向服务器发送post请求
                String comment = inputComment.getText().toString().trim();
                if (TextUtils.isEmpty(comment)) {
                    Toast.makeText(ArticleCommentActivity.this, "评论不能为空!请重新输入", Toast.LENGTH_SHORT).show();
                } else {
                    OkHttpManager mOkHttpManager = OkHttpManager.getInstance();
                    ApiInterImpl api = new ApiInterImpl();
                    mOkHttpManager.postAsyn(api.addCommentForArticle(id, comment, token), new OkHttpManager.ResultCallback<String>(ArticleCommentActivity.this) {
                        @Override
                        public void onError(Request request, Exception e) {
                            Toast.makeText(ArticleCommentActivity.this, "网络有问题！", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "评论失败");
                        }

                        @Override
                        public void onResponse(String response) {
                            super.onResponse(response);
                            Toast.makeText(ArticleCommentActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, response);
                            commentCount++;
                            popupWindow.dismiss();
                            onRefresh();
                        }
                    });
                }
            }
        });

        //取消评论
        quit_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

            }
        });
    }

    private void showPopupComment(final int otherId, final int otherUserId){
        View view = LayoutInflater.from(ArticleCommentActivity.this).inflate(R.layout.comment_popupwindow, null);
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
        popupWindow.setBackgroundDrawable(new BitmapDrawable());//bug出现点，响应返回键必须的语句
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
                    Toast.makeText(ArticleCommentActivity.this, "评论不能为空!请重新输入", Toast.LENGTH_SHORT).show();
                } else {
                    OkHttpManager mOkHttpManager = OkHttpManager.getInstance();
                    ApiInterImpl api = new ApiInterImpl();
                    mOkHttpManager.postAsyn(api.addCommentForArticle(id,otherUserId,otherId, comment, token), new OkHttpManager.ResultCallback<String>(ArticleCommentActivity.this) {
                        @Override
                        public void onError(Request request, Exception e) {
                            Toast.makeText(ArticleCommentActivity.this, "网络有问题！", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "评论失败");
                        }

                        @Override
                        public void onResponse(String response) {
                            super.onResponse(response);
                            Toast.makeText(ArticleCommentActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                            commentCount++;
                            popupWindow.dismiss();
                            onRefresh();
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
    public void onBackPressed() {
        Bundle bundle=new Bundle();
        bundle.putInt("id", id);
        bundle.putInt("commentCount", commentCount);
        Intent intent=new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_CODE, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==ArticleDialogActivity.RESULT_CODE) {
            Bundle bundle = data.getExtras();
            int _commentCount=bundle.getInt("commentCount");
            if (_commentCount!=commentCount){
                commentCount=_commentCount;
                onRefresh();
            }
        }
    }

    public void noLogin(){
        String[] items=new String[]{"登录","取消"};
        AlertDialog.Builder builder=new AlertDialog.Builder(ArticleCommentActivity.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        Intent intent=new Intent(ArticleCommentActivity.this, LoginActivity.class);
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
            Toast.makeText(this, "你已在别的地方登录，你被迫下线，请重新登录！", Toast.LENGTH_SHORT).show();
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
}

