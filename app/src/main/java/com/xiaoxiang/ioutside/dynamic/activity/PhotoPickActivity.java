package com.xiaoxiang.ioutside.dynamic.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.common.CachedInfo;
import com.xiaoxiang.ioutside.common.MyApplication;
import com.xiaoxiang.ioutside.common.imagepicker.AlbumController;
import com.xiaoxiang.ioutside.common.imagepicker.AlbumPickActivity;
import com.xiaoxiang.ioutside.common.imagepicker.DisplayImageViewAdapter;
import com.xiaoxiang.ioutside.common.imagepicker.ImageLoader;
import com.xiaoxiang.ioutside.common.imagepicker.PhotoGalleyAdapter;
import com.xiaoxiang.ioutside.common.imagepicker.PhotoPreviewActivity;
import com.xiaoxiang.ioutside.common.imagepicker.PhotoSelectorHelper;
import com.xiaoxiang.ioutside.common.imagepicker.PictureUtil;
import com.xiaoxiang.ioutside.common.imagepicker.UriUtil;
import com.xiaoxiang.ioutside.mine.model.GPhotoList;
import com.xiaoxiang.ioutside.network.response.BaseResponse;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class PhotoPickActivity extends AppCompatActivity implements PhotoSelectorHelper.OnLoadPhotoListener, AdapterView.OnItemClickListener {
//    private static final String TAG=PhotoPickActivity.class.getSimpleName();
    private static final String TAG = "PhotoPickActivity";
    private PhotoSelectorHelper mHelper;
    private GridView mGridView;
    private PhotoGalleyAdapter mGalleyAdapter;
    //private View mPickAlbumView;
    private TextView mCountText;
    private ImageView photo_back;
    public static final String MAX_PICK_COUNT="max_pick_count";
    public static final String IS_SHOW_CAMERA="is_show_camera";
    public static final String SELECT_PHOTO_LIST="select_photo_list";
    private static final int TO_PICK_ALBUM=1;
    private static final int TO_PRIVIEW_PHOTO=2;
    private static final int TO_TAKE_PHOTO=3;

    private boolean isShowCamera;
    private int maxPickCount;
    private String mLastAlbumName;
    private String token;
    private String fileToken;
    private CachedInfo cachedInfo;
    private ArrayList<String> myList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_pick);
        token=getIntent().getStringExtra("token");
        cachedInfo= MyApplication.getInstance().getCachedInfo();
        if (cachedInfo!=null){
            fileToken=cachedInfo.getToken();
            Log.d(TAG,"fileToken="+fileToken);
        }
        if (token==null){
            token=fileToken;
        }
        photo_back=(ImageView)findViewById(R.id.photo_back);
        photo_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        List<String> list=getIntent().getStringArrayListExtra(SELECT_PHOTO_LIST);
        if(list!=null){
            for(String s:list){
                PhotoGalleyAdapter.mSelectedImage.add(s);
            }
        }
        maxPickCount=getIntent().getIntExtra(MAX_PICK_COUNT,1);
        isShowCamera=getIntent().getBooleanExtra(IS_SHOW_CAMERA,false);
        mGridView= (GridView) this.findViewById(R.id.mp_galley_gridView);
        mGridView.setOnItemClickListener(this);
        mCountText= (TextView) this.findViewById(R.id.tv_to_confirm);
       // mPickAlbumView=this.findViewById(R.id.tv_to_album);
        mLastAlbumName= AlbumController.RECENT_PHOTO;
        mHelper=new PhotoSelectorHelper(this);
        mHelper.getReccentPhotoList(this);
        mGridView.setAdapter(mGalleyAdapter=new PhotoGalleyAdapter(this,null,isShowCamera,maxPickCount));

        if(maxPickCount>1){
            mCountText.setVisibility(View.VISIBLE);
        }else{
            mCountText.setVisibility(View.GONE);
        }

        /*
        mPickAlbumView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=  new Intent(PhotoPickActivity.this,AlbumPickActivity.class);
                startActivityForResult(intent,TO_PICK_ALBUM);
            }
        });
        */

        mGalleyAdapter.setOnDisplayImageAdapter(new DisplayImageViewAdapter<String>() {
            @Override
            public void onDisplayImage(Context context, ImageView imageView, String path) {
                ImageLoader.getInstance().loadImage(path,imageView);
            }

            @Override
            public void onItemImageClick(Context context, int index, List<String> list) {
                Intent intent=new Intent(PhotoPickActivity.this,PhotoPreviewActivity.class);
                intent.putExtra(PhotoPreviewActivity.PHOTO_INDEX_IN_ALBUM,index);
                intent.putExtra(MAX_PICK_COUNT,maxPickCount);
                intent.putExtra(AlbumPickActivity.ALBUM_NAME,mLastAlbumName);
                startActivityForResult(intent,TO_PRIVIEW_PHOTO);
            }

            @Override
            public void onImageCheckL(String path, boolean isChecked) {
                updateCountView();
            }
        });


        mCountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDone();
            }
        });
        updateCountView();

    }

    /**
     * 图片加载完成
     * @param photos
     */
    @Override
    public void onPhotoLoaded(List<String> photos) {
        mGalleyAdapter.notifyDataSetChanged(photos,true);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==TO_PRIVIEW_PHOTO){
            updateCountView();
            mGalleyAdapter.notifyDataSetChanged();
        }
        if(resultCode!=RESULT_OK){
            return;
        }

        switch (requestCode){
            case TO_PICK_ALBUM:
                String name=data.getStringExtra(AlbumPickActivity.ALBUM_NAME);
                if(mLastAlbumName.equals(name)){
                    return;
                }
                if(getSupportActionBar()!=null){
                    getSupportActionBar().setTitle(name);
                }
                mLastAlbumName=name;
                if(name.equals(AlbumController.RECENT_PHOTO)){
                    mHelper.getReccentPhotoList(this);
                }else{
                    mHelper.getAlbumPhotoList(name,this);
                }
                break;

            case TO_PRIVIEW_PHOTO:
                selectDone();
                break;

            case TO_TAKE_PHOTO:
                String url= UriUtil.getAbsolutePathFromUri(this, mUri);
                PictureUtil.notifyGallery(this, url);
                PhotoGalleyAdapter.mSelectedImage.add(url);
                selectDone();
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id ==android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Uri mUri;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String path=mGalleyAdapter.getItem(position);
        if(PhotoGalleyAdapter.mSelectedImage.size()>=maxPickCount){
            Toast.makeText(getApplicationContext(), "已经选满" + maxPickCount + "张", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(path)){

            mUri= PictureUtil.takePhoto(this, TO_TAKE_PHOTO);
        }else{
            PhotoGalleyAdapter.mSelectedImage.add(path);
            selectDone();
        }
    }


    private void selectDone(){
        ArrayList<String> list=new ArrayList<>();
        for(String s:PhotoGalleyAdapter.mSelectedImage){
            list.add(s);
        }
        PhotoGalleyAdapter.mSelectedImage.clear();
        Toast.makeText(this,"正在上传图片!",Toast.LENGTH_SHORT).show();
        postAvatar(list);
            /*
            Timer timer=new Timer();
            TimerTask timerTask=new TimerTask() {
                @Override
                public void run() {
                    Log.e(TAG, "mylist=======" + myList.toString());
                    Intent intent=new Intent(PhotoPickActivity.this, DynamicActivity.class);
                    intent.putExtra("token", token);
                    intent.putStringArrayListExtra(SELECT_PHOTO_LIST,myList);
                    startActivity(intent);//这里是跳转的地方
                    setResult(2);
                    finish();
                }
            };
            timer.schedule(timerTask,2*1000);
            */

    }

    public void postAvatar(final ArrayList<String> avatarPath) {
        ArrayList<File> fileArrayList=new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        MediaType type = MediaType.parse("image/png");
        for (int i=0;i<avatarPath.size();i++){
            File file = new File(avatarPath.get(i));//创建多个文件
            fileArrayList.add(file);
        }
        MultipartBody.Builder builder= new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("category", "footprint");
        builder.addFormDataPart("token", token);

        for (File file:fileArrayList){
            builder.addFormDataPart("photo", file.getName(), RequestBody.create(type,file));
        }

        MultipartBody requestBody=builder.build();
        Request request = new Request.Builder()
                .url("http://ioutside.com/xiaoxiang-backend/assist/batch-upload-photo")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure");
                Toast.makeText(PhotoPickActivity.this, "网络有点问题", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseJson = response.body().string();
                Type ob = new TypeToken<BaseResponse<GPhotoList>>(){}.getType();
                BaseResponse<GPhotoList> photos = new Gson().fromJson(responseJson, ob);
                GPhotoList gPhotoList = photos.getData();
                List<String> list = gPhotoList.getList();
                if (list.size()!=0){
                    for (int i=0;i<list.size();i++){
                        String photo=list.get(i);
                        myList.add("http://ioutside.com/xiaoxiang-backend" +photo);
                    }
                }
                Log.e(TAG, "mylist=======" + myList.toString());
                Intent intent=new Intent(PhotoPickActivity.this, DynamicActivity.class);
                intent.putExtra("token", token);
                intent.putStringArrayListExtra(SELECT_PHOTO_LIST, myList);
                intent.putStringArrayListExtra("local_list",avatarPath);
                startActivity(intent);//这里是跳转的地方
                finish();
            }
        });

    }



    private void updateCountView(){
        if(PhotoGalleyAdapter.mSelectedImage.size()==0){
            mCountText.setEnabled(false);
        }else{
            mCountText.setEnabled(true);
        }
       // mCountText.setText("下一步("+PhotoGalleyAdapter.mSelectedImage.size()+"/"+maxPickCount+")");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PhotoGalleyAdapter.mSelectedImage.clear();
    }
}

