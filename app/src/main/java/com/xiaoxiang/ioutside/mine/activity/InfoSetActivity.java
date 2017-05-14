//package com.example.oubin6666.ioutside.mine.activity;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.oubin6666.api.PostEngine.ApiImpl.ApiInterImpl;
//import com.example.oubin6666.api.PostEngine.PostEngine.OkHttpManager;
//import com.example.oubin6666.api.PostEngine.Response.BaseResponse;
//import com.example.oubin6666.ioutside.R;
//import com.example.oubin6666.ioutside.widget.CircleImageView;
//import com.example.oubin6666.model.mine.GPhotoList;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import com.lzy.imagepicker.ImagePicker;
//import com.lzy.imagepicker.bean.ImageItem;
//import com.lzy.imagepicker.loader.GlideImageLoader;
//import com.lzy.imagepicker.ui.ImageGridActivity;
//import com.lzy.imagepicker.view.CropImageView;
//import com.nostra13.universalimageloader.core.ImageLoader;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//
//public class InfoSetActivity extends Activity {
//    @Bind(R.id.cirimg_defhead_info)
//    CircleImageView cirimghead;
//    private String TAG = getClass().getSimpleName();
//    private static final int IMAGE_PICKER = 10;
//
//    @Bind(R.id.lay_bindemail_info)
//    RelativeLayout layBindemailInfo;
//    @Bind(R.id.lay_bindphone_info)
//    RelativeLayout layBindphoneInfo;
//    @Bind(R.id.edt_bindemail)
//    TextView tvEmail;
//    @Bind(R.id.edt_bindphone)
//    TextView tvPhone;
//    @Bind(R.id.img_backbtn)
//    ImageView btnBack;
//    @Bind(R.id.tv_title_setting)
//    TextView tvTitleSetting;
//    @Bind(R.id.edt_name)
//    EditText tvName;
//    @Bind(R.id.edt_sex)
//    EditText tvSex;
//    @Bind(R.id.edt_addr)
//    EditText tvAddr;
//    @Bind(R.id.edt_certi)
//    EditText tvSkills;
//    @Bind(R.id.edt_experience)
//    EditText tvExperience;
//
//    String oldname;
//    String oldaddress;
//    String oldskills;
//    String oldexperience;
//    String oldphone;
//    String oldsex;
//    String sex;
//    String oldemail;
//    String oldphoto;
//    Intent infoIn;
//    String returnedPhotoPath;
//    private String token;
//    private ApiInterImpl apiImpl;
//    private OkHttpManager mOkHttpManager;
//    private Gson gson;
//    private BaseResponse modifyRe;
//    private ImagePicker imagePicker;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_info_set);
//        ButterKnife.bind(this);
//        infoIn = getIntent();
//        token = infoIn.getStringExtra("token");
//        Log.d(TAG, token);
//        gson = new Gson();
//        apiImpl = new ApiInterImpl();
//        mOkHttpManager = OkHttpManager.getInstance();
//        initPicker();
//        loadMySubjects();
//    }
//
//    public void initPicker() {
//        imagePicker = ImagePicker.getInstance();
//        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
//        imagePicker.setShowCamera(true);  //显示拍照按钮
//        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
//        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
//        imagePicker.setStyle(CropImageView.Style.CIRCLE);  //裁剪框的形状
//        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
//        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
//        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
//        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
//        imagePicker.setMultiMode(false);
//    }
//
//
//    public void loadMySubjects() {
//        oldname = infoIn.getStringExtra("name");
//        oldphoto = infoIn.getStringExtra("photo");
//        oldaddress = infoIn.getStringExtra("address");
//        oldskills = infoIn.getStringExtra("certi");
//        oldexperience = infoIn.getStringExtra("experience");
//        oldemail = infoIn.getStringExtra("email");
//        oldphone = infoIn.getStringExtra("phone");
//        oldsex = infoIn.getStringExtra("sex");
//        if (oldname != null) {
//            tvName.setText(oldname);
//        }
//        if (oldphoto != null) {
//            ImageLoader.getInstance().displayImage(oldphoto, cirimghead);
//        } else {
//            cirimghead.setImageResource(R.drawable.defoulthead);
//        }
//        if (oldaddress != null) {
//            tvAddr.setText(oldaddress);
//        }
//        if (oldskills != null) {
//            tvSkills.setText(oldskills);
//        }
//        if (oldexperience != null) {
//            tvExperience.setText(oldexperience);
//        }
//        if (oldemail != null) {
//            tvEmail.setText(oldemail);
//        }
//        if (oldphone != null) {
//            tvPhone.setText(oldphone);
//        }
//        if (oldsex != null) {
//            tvSex.setText(oldsex);
//        }
//    }
//
//
//    @OnClick({R.id.img_backbtn, R.id.cirimg_defhead_info, R.id.lay_bindemail_info, R.id.lay_bindphone_info})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.img_backbtn:
//                applyChange();
//                break;
//            case R.id.cirimg_defhead_info:
//                Intent intent = new Intent(this, ImageGridActivity.class);
//                startActivityForResult(intent, IMAGE_PICKER);
//                break;
//            case R.id.lay_bindemail_info:
//                Intent intent1 = new Intent(InfoSetActivity.this, ModifyEmailActivity.class);
//                intent1.putExtra("token", token);
//                startActivity(intent1);
//                break;
//            case R.id.lay_bindphone_info:
//                Intent in = new Intent(InfoSetActivity.this, ModifyPhoneActivity.class);
//                in.putExtra("token", token);
//                startActivity(in);
//
//                break;
//        }
//    }
//
//    private void postInfo(List<OkHttpManager.Param> params) {
//
//        String info = "http://www.ioutside.com/xiaoxiang-backend/user/modify-user-info.do";
//
//        mOkHttpManager.getStringAsyn(info, params, new OkHttpManager.ResultCallback<BaseResponse>() {
//
//            @Override
//            public void onError(Request request, Exception e) {
//                Toast.makeText(InfoSetActivity.this, "信息修改失败，请检查网络设置", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onResponse(BaseResponse response) {
//                if (response.isSuccess()) {
//                    Toast.makeText(InfoSetActivity.this, "信息修改成功！", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(InfoSetActivity.this, response.getErrorMessage(), Toast.LENGTH_SHORT).show();
//                }
//                finish();
//            }
//        });
//    }
//
//    @Override
//    public void onBackPressed() {
//        applyChange();
//    }
//
//
//    private void applyChange() {
//
//        String newname = tvName.getText().toString();
//        String newsex = tvSex.getText().toString();
//        String newaddr = tvAddr.getText().toString();
//        String newskills = tvSkills.getText().toString();
//        String newexper = tvExperience.getText().toString();
//
//        if (TextUtils.isEmpty(newname)) {
//            Toast.makeText(this, "昵称不能为空", Toast.LENGTH_SHORT).show();
//        } else if (TextUtils.isEmpty(newsex)) {
//            Toast.makeText(this, "请填写性别", Toast.LENGTH_SHORT).show();
//        } else {
//
//            List<OkHttpManager.Param> params = new ArrayList<>();
//
//            if ("男".equals(newsex)) {
//                newsex = "m";
//            } else if ("女".equals(newsex)) {
//                newsex = "w";
//            } else {
//                newsex = "u";
//            }
//
//            params.add(new OkHttpManager.Param("name", newname));
//            params.add(new OkHttpManager.Param("sex", newsex));
//            params.add(new OkHttpManager.Param("address", newaddr));
//            params.add(new OkHttpManager.Param("skills", newskills));
//            params.add(new OkHttpManager.Param("experiences", newexper));
//            params.add(new OkHttpManager.Param("token", token));
//            if (!TextUtils.isEmpty(returnedPhotoPath))
//                params.add(new OkHttpManager.Param("photo", returnedPhotoPath));
//
//            postInfo(params);
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
//            if (data != null && requestCode == IMAGE_PICKER) {
//                //     Log.d(TAG, "data.getData() --> " + data.getData().toString());
//                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
//                try {
//                    getContentResolver().openInputStream(Uri.parse(images.get(0).path));
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//                cirimghead.setImageURI(Uri.fromFile(new File(images.get(0).path)));
//                //     imagePicker.getImageLoader().displayImage(InfoSetActivity.this, images.get(0).path, cirimghead, 180, 180);
//                Log.d(TAG, "图片的路径是" + images.get(0).path);
//                postAvatar(images.get(0).path);
//                //图片的上传和处理
//            } else {
//                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//
//    }
//
//    public void postAvatar(final String avatarPath) {
//
//        OkHttpClient client = mOkHttpManager.getOkHttpClient();
//
//        MediaType type = MediaType.parse("image/png");
//
//        Log.d(TAG, "token -->" + token);
//
//        File file = new File(avatarPath);
//
//        MultipartBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("category", "user")
//                .addFormDataPart("token", token)
//                .addFormDataPart("photo", file.getName(), RequestBody.create(type, file))
//                .build();
//
//        Request request = new Request.Builder()
//                .url("http://ioutside.com/xiaoxiang-backend/assist/batch-upload-photo")
//                .post(requestBody)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//
//            @Override
//            public void onFailure(Call call, IOException e) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(InfoSetActivity.this, "头像上传失败,请检查网络设置", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//                String responseJson = response.body().string();
//
//                Type ob = new TypeToken<BaseResponse<GPhotoList>>() {
//                }.getType();
//
//                BaseResponse<GPhotoList> photoList = new Gson().fromJson(responseJson, ob);
//                GPhotoList list = photoList.getData();
//
//                Log.d(TAG, "response.body().string()--> " + responseJson);
//                Log.d("photo", list.getList().get(0));
//
//                returnedPhotoPath = list.getList().get(0);
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(InfoSetActivity.this, "头像上传成功", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
//    }
//
//}
package com.xiaoxiang.ioutside.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.api.Api;
import com.xiaoxiang.ioutside.api.ApiInterImpl;
import com.xiaoxiang.ioutside.common.CircleImageView;
import com.xiaoxiang.ioutside.mine.dialog.EditTextDialog;
import com.xiaoxiang.ioutside.mine.dialog.MultiEditTextDialog;
import com.xiaoxiang.ioutside.mine.dialog.SelectDialog;
import com.xiaoxiang.ioutside.mine.model.GPhotoList;
import com.xiaoxiang.ioutside.network.postengine.OkHttpManager;
import com.xiaoxiang.ioutside.network.response.BaseResponse;
import com.xiaoxiang.ioutside.util.FormatUtil;
import com.xiaoxiang.ioutside.util.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class InfoSetActivity extends Activity implements EditTextDialog.OnTextChangeListener {
    @Bind(R.id.cirimg_defhead_info)
    CircleImageView cirimghead;
    private String TAG = getClass().getSimpleName();
    private static final int IMAGE_PICKER = 10;
    @Bind(R.id.lay_bindemail_info)
    RelativeLayout layBindemailInfo;
    @Bind(R.id.lay_bindphone_info)
    RelativeLayout layBindphoneInfo;
    @Bind(R.id.edt_bindemail)
    TextView tvEmail;
    @Bind(R.id.edt_bindphone)
    TextView tvPhone;
    @Bind(R.id.iv_back)
    ImageView btnBack;
    @Bind(R.id.tv_title)
    TextView tvTitleSetting;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_sex)
    TextView tvSex;
    @Bind(R.id.tv_addr)
    TextView tvAddr;

    @Bind(R.id.tv_experience)
    TextView tvExperience;
    @Bind(R.id.tv_certificate_1)
            TextView tvCertificate1;
    @Bind(R.id.tv_certificate_2)
            TextView tvCertificate2;
    @Bind(R.id.tv_certificate_3)
            TextView tvCertificate3;
    String oldname;
    String oldaddress;
    String oldskills;
    String oldexperience;
    String oldphone;
    String oldsex;
    String sex;
    String oldemail;
    String oldphoto;
    Intent infoIn;
    String returnedPhotoPath;
    private String token;
    private ApiInterImpl apiImpl;
    private OkHttpManager mOkHttpManager;
    private Gson gson;
    private BaseResponse modifyRe;
    private ImagePicker imagePicker;
    private EditTextDialog editTextDialog;
    private SelectDialog selectDialog;
//    private TextView tvCurrentEdit;
    private MultiEditTextDialog multiEditTextDialog;
    private TextView[] tvCertificates;

    private final int ID_NICKNAME = 0;
    private final int ID_ADDRESS = 1;
    private final int ID_OUTDOOR_EXPERIENCE = 2;
    private final int ID_EMAIL = 3;
    private final int ID_PHONE = 4;
    private final int ID_CERTIFICATE = 5;
    private final int ID_SEX = 6;

    private Map<Integer, String> tempInfo = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_set);
        ButterKnife.bind(this);
        infoIn = getIntent();
        token = infoIn.getStringExtra("token");
        Log.d(TAG, token);
        gson = new Gson();
        apiImpl = new ApiInterImpl();
        mOkHttpManager = OkHttpManager.getInstance();
        tvCertificates = new TextView[]{tvCertificate1, tvCertificate2, tvCertificate3};
        initPicker();
        loadData();
    }


    public void initPicker() {
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new com.lzy.imagepicker.loader.ImageLoader() {
            @Override
            public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
                Glide.with(activity)
                        .load(new File(path))
                        .placeholder(R.mipmap.default_image)
                        .error(R.mipmap.default_image)
                        .override(width, height)
                        .centerCrop()
                        .skipMemoryCache(true)
                        .into(imageView);
            }

            @Override
            public void clearMemoryCache() {

            }
        });   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setStyle(CropImageView.Style.CIRCLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
        imagePicker.setMultiMode(false);
    }


    //这个方法要重写，不应该从 intent 获取，而应该从磁盘获取
    public void loadData() {

        oldname = infoIn.getStringExtra("name");
        oldphoto = infoIn.getStringExtra("photo");
        oldaddress = infoIn.getStringExtra("address");
        oldskills = infoIn.getStringExtra("certi");
        oldexperience = infoIn.getStringExtra("experience");
        oldemail = infoIn.getStringExtra("email");
        oldphone = infoIn.getStringExtra("phone");
        oldsex = infoIn.getStringExtra("sex");

        if (oldname != null) {
           // tvName.setText(oldname);
            tempInfo.put(ID_NICKNAME, oldname);
        }
        if (oldphoto != null) {
            ImageLoader.getInstance().displayImage(oldphoto, cirimghead);
        } else {
            cirimghead.setImageResource(R.drawable.defoulthead);
        }
        if (oldaddress != null) {
          //  tvAddr.setText(oldaddress);
            tempInfo.put(ID_ADDRESS, oldaddress);
        }
        if (oldskills != null) {
         //   tvSkills.setText(oldskills);
            tempInfo.put(ID_CERTIFICATE, oldskills);
        }
        if (oldexperience != null) {
          //  tvExperience.setText(oldexperience);
            tempInfo.put(ID_OUTDOOR_EXPERIENCE, oldexperience);
        }
        if (oldemail != null) {
          //  tvEmail.setText(oldemail);
            tempInfo.put(ID_EMAIL, oldemail);
        }
        if (oldphone != null) {
          //  tvPhone.setText(oldphone);
            tempInfo.put(ID_PHONE, oldphone);
        }
        if (oldsex != null) {
         //   tvSex.setText(oldsex);
            tempInfo.put(ID_SEX, oldsex);
        }

        updateInfo();
    }

    @Override
    public void onPreEdit(EditTextDialog dialog, int id) {

        EditText editText = dialog.getEditText();

        switch (id) {
            case ID_NICKNAME:
                editText.setLines(1);
                editText.setText(tvName.getText());
                dialog.setDialogTitle("昵称");
                editText.setHint("请填写您的你昵称");
                break;
            case ID_ADDRESS:
                editText.setLines(3);
                editText.setText(tvAddr.getText());
                dialog.setDialogTitle("地址");
                editText.setHint("请填写您的地址");
                break;
            case ID_OUTDOOR_EXPERIENCE:
                editText.setLines(4);
                editText.setText(tvExperience.getText());
                dialog.setDialogTitle("户外经历");
                editText.setHint("请填写您的户外经历");
                break;
            case ID_EMAIL:
                editText.setLines(1);
                editText.setText(tvEmail.getText());
                editText.setHint("请输入邮箱号");
                dialog.setDialogTitle("绑定邮箱");
                break;
            case ID_PHONE:
                editText.setLines(1);
                editText.setHint("请输入手机号");
                editText.setText(tvPhone.getText());
                dialog.setDialogTitle("绑定手机");
                break;
        }
    }

    @Override
    public void onSubmitText(EditTextDialog dialog, int id) {

        EditText editText = dialog.getEditText();
        String info = editText.getText().toString();

        switch (id) {
            case ID_NICKNAME:
                if (TextUtils.isEmpty(tvName.getText())) {
                    ToastUtils.show(this, "昵称不能为空");
                    return;
                }
                tempInfo.put(id, info);
                postInfo("name", info);
                break;
            case ID_ADDRESS:
                tempInfo.put(id, info);
                postInfo("address", info);
                break;
            case ID_OUTDOOR_EXPERIENCE:
                tempInfo.put(id, info);
                postInfo("experiences", info);
                break;
            case ID_EMAIL:
                tempInfo.put(id, info);
                bindEmail(info);
                break;
            case ID_PHONE:
                tempInfo.put(id, info);
                bindPhone(info);
                break;
        }
    }


    @OnClick({R.id.iv_back, R.id.cirimg_defhead_info, R.id.lay_bindemail_info, R.id.lay_bindphone_info,
    R.id.lay_setaddr_info, R.id.lay_setname_info, R.id.lay_setexper_info, R.id.lay_setsex_info, R.id.lay_setcert_info})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.cirimg_defhead_info:
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, IMAGE_PICKER);
                break;
            case R.id.lay_bindemail_info:
                ToastUtils.show(this, "绑定的邮箱暂时还不能修改哦~");
//                tvCurrentEdit = tvEmail;
//                openEditDialog(ID_EMAIL);
//                Intent intent1 = new Intent(InfoSetActivity.this, ModifyEmailActivity.class);
//                intent1.putExtra("token", token);
//                startActivity(intent1);
                break;
            case R.id.lay_bindphone_info:
                ToastUtils.show(this, "手机号暂时还不能修改哦~");
//                tvCurrentEdit = tvPhone;
//                openEditDialog(ID_PHONE);
//                Intent in = new Intent(InfoSetActivity.this, ModifyPhoneActivity.class);
//                in.putExtra("token", token);
//                startActivity(in);
                break;
            case R.id.lay_setname_info:
              //  tvCurrentEdit = tvName;
                openEditDialog(ID_NICKNAME);
                break;
            case R.id.lay_setaddr_info:
              //  tvCurrentEdit = tvAddr;
                openEditDialog(ID_ADDRESS);
                break;
            case R.id.lay_setcert_info:
                openMultiEditTextDialog();
                break;
            case R.id.lay_setexper_info:
               // tvCurrentEdit = tvExperience;
                openEditDialog(ID_OUTDOOR_EXPERIENCE);
                break;
            case R.id.lay_setsex_info:
               // tvCurrentEdit = tvSex;
                openSelectDialog();
                break;

        }
    }

    public void openEditDialog(int id) {
        if (editTextDialog == null) {
            editTextDialog = new EditTextDialog(this, this);
        }
        editTextDialog.show(id);
    }

    private void openSelectDialog() {
        if (selectDialog == null) {
            selectDialog = new SelectDialog(this, new SelectDialog.OnItemSelectListener() {
                @Override
                public void onItemSelect(int which) {
                    if (which == SelectDialog.ITEM_MALE)  {
                        tempInfo.put(ID_SEX, "男");
                        postInfo("sex", "m");
                    }

                    else if (which == SelectDialog.ITEM_FEMALE) {
                        tempInfo.put(ID_SEX, "女");
                        postInfo("sex", "w");
                    }

                }
            });
        }

        selectDialog.show();
    }

    private void openMultiEditTextDialog() {
        if (multiEditTextDialog == null) {
            multiEditTextDialog = new MultiEditTextDialog(this, new MultiEditTextDialog.OnTextChangeListener() {
                @Override
                public void onSubmitText(MultiEditTextDialog dialog, int id) {
                    EditText[] editTexts = dialog.getEditTexts();
                    String skills = "";
                    for (int i = 0; i < editTexts.length; i++) {
                        if (i == 0)
                            skills += editTexts[i].getText().toString();
                        else if (! TextUtils.isEmpty(editTexts[i].getText().toString()))
                            skills += "," + editTexts[i].getText().toString();
                    }
                    tempInfo.put(ID_CERTIFICATE, skills);
                    postInfo("skills", skills);
                }

                @Override
                public void onPreEdit(MultiEditTextDialog dialog, int id) {

                }
            });
        }
        multiEditTextDialog.show(0);
    }



    private void bindEmail(String email) {
        if (email.equals("")) {
            ToastUtils.show(this, "请填写邮箱");
            return;
        }
        if (FormatUtil.isEmailFormat(email)) {

            String url = "http://www.ioutside.com/xiaoxiang-backend/user/modify-user-info.do"
                    + "?" + "email=" + email + "&token=" + token;

               // String url = apiImpl.getModifyEmailIn(email, token);
                mOkHttpManager.getStringAsyn(url, new OkHttpManager.ResultCallback<BaseResponse>() {

                    @Override
                    public void onError(Request request, Exception e) {
                        Log.d(TAG, "error");
                    }

                    @Override
                    public void onResponse(BaseResponse response) {
                        super.onResponse(response);
                        if (response.isSuccess()) {
                            ToastUtils.show(InfoSetActivity.this, "验证邮件已发送，请登陆邮箱激活");
//                            Intent in = new Intent(ModifyEmailActivity.this, EmailVeri2Activity.class);
//                            startActivity(in);
                        } else {
                            //Toast.makeText(ModifyEmailActivity.this, response.getErrorMessage(), Toast.LENGTH_SHORT).show();
                            ToastUtils.show(InfoSetActivity.this, response.getErrorMessage());
                        }
                    }
                });
            } else {
            ToastUtils.show(this, "邮箱格式不正确");
        }
    }

    private void bindPhone(String phoneNum) {

        if (TextUtils.isEmpty(phoneNum)) {
            ToastUtils.show(this, "手机号不能为空");
            return;
        }

        if (! FormatUtil.isPhoneNum(phoneNum)) {
            ToastUtils.show(this, "请填入正确的手机号");
            return;
        }


    }



    private void postInfo(OkHttpManager.Param param) {

        String url = "http://www.ioutside.com/xiaoxiang-backend/user/modify-user-info.do"
                + "?" + param.key + "=" + param.value + "&token=" + token;

        mOkHttpManager.getStringAsyn(url, new OkHttpManager.ResultCallback<BaseResponse>() {

            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(InfoSetActivity.this, "更新失败，请检查网络设置", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(BaseResponse response) {
                if (response.isSuccess()) {
                    updateInfo();
                    Toast.makeText(InfoSetActivity.this, "已更新", Toast.LENGTH_SHORT).show();
//                    if (tvCurrentEdit.getId() == R.id.tv_setcert_info)
//                    tvCurrentEdit.setText(tempInfo.get(tvCurrentEdit.getId()));
                } else {
                    Toast.makeText(InfoSetActivity.this, response.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateInfo() {

        tvName.setText(tempInfo.get(ID_NICKNAME));
        tvAddr.setText(tempInfo.get(ID_ADDRESS));
        tvEmail.setText(tempInfo.get(ID_EMAIL));
        tvSex.setText(tempInfo.get(ID_SEX));
        tvPhone.setText(tempInfo.get(ID_PHONE));
        tvExperience.setText(tempInfo.get(ID_OUTDOOR_EXPERIENCE));

        for (TextView tv : tvCertificates) {
            tv.setVisibility(View.GONE);
        }

        if (! TextUtils.isEmpty(tempInfo.get(ID_CERTIFICATE))) {
            String[] certificates = tempInfo.get(ID_CERTIFICATE).split(",");
            for (int i = 0; i < certificates.length; i ++) {

                if (! TextUtils.isEmpty(certificates[i])) {
                    tvCertificates[i].setVisibility(View.VISIBLE);
                    tvCertificates[i].setText(certificates[i]);
                }
            }
        }
    }

    public void postInfo(String key, String value) {
        OkHttpManager.Param param = new OkHttpManager.Param(key, value);
        postInfo(param);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                //     Log.d(TAG, "data.getData() --> " + data.getData().toString());
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                try {
                    getContentResolver().openInputStream(Uri.parse(images.get(0).path));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
        cirimghead.setImageURI(Uri.fromFile(new File(images.get(0).path)));
        //     imagePicker.getImageLoader().displayImage(InfoSetActivity.this, images.get(0).path, cirimghead, 180, 180);
        Log.d(TAG, "图片的路径是" + images.get(0).path);
        postAvatar(images.get(0).path);
        //图片的上传和处理
    } else {
        Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
    }
}
}

public void postAvatar(final String avatarPath) {

        OkHttpClient client = mOkHttpManager.getOkHttpClient();
        MediaType type = MediaType.parse("image/png");
        Log.d(TAG, "token -->" + token);
        File file = new File(avatarPath);
        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("category", "user")
                .addFormDataPart("token", token)
                .addFormDataPart("photo", file.getName(), RequestBody.create(type, file))
                .build();
        Request request = new Request.Builder()
                .url("http://ioutside.com/xiaoxiang-backend/assist/batch-upload-photo")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(InfoSetActivity.this, "头像上传失败,请检查网络设置", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseJson = response.body().string();
                Type ob = new TypeToken<BaseResponse<GPhotoList>>() {
                }.getType();
                BaseResponse<GPhotoList> photoList = new Gson().fromJson(responseJson, ob);
                GPhotoList list = photoList.getData();
                Log.d(TAG, "response.body().string()--> " + responseJson);
                Log.d("photo", list.getList().get(0));
                returnedPhotoPath = list.getList().get(0);
                Api api = new ApiInterImpl();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        postInfo("photo", returnedPhotoPath);
                    }
                });
            }
        });
    }



}