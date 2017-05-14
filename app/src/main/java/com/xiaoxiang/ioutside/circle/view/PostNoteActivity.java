package com.xiaoxiang.ioutside.circle.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoxiang.ioutside.R;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.iwf.photopicker.widget.MultiPickResultView;

/**
 * Created by Wakesy on 2016/10/10.
 */
public class PostNoteActivity extends Activity {
    @Bind(R.id.circle_pickPhoto)
    MultiPickResultView circle_pickPhoto;
    @Bind(R.id.circle_notePost)
    TextView circle_notePost;
    @Bind(R.id.circle_photoSelected)
    TextView circle_photoSelected;

    private  ArrayList<String> pathslook ;
    private  ArrayList<String> photo_chooesed ;
    public final static int REQUEST_CODE=101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circle_postnote);
        ButterKnife.bind(this);
        pathslook=new ArrayList<>();
        circle_pickPhoto.init(this,MultiPickResultView.ACTION_SELECT,null);

        circle_notePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showData();
            }
        });
    }


    private void showData() {
        photo_chooesed=new ArrayList<>();
        StringBuilder sb=new StringBuilder();
        if (circle_pickPhoto.getPhotos()!=null&&circle_pickPhoto.getPhotos().size()>0) {
            photo_chooesed.addAll(circle_pickPhoto.getPhotos());
            Iterator<String> paths=circle_pickPhoto.getPhotos().iterator();
            sb.append("选中"+photo_chooesed.size()+"张\n");
            while (paths.hasNext()) {
                sb.append(paths.next()+"\n");

            }
            circle_photoSelected.setText(sb.toString());

        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            // permission was granted, yay!
//            onClick(photo_pick);

        } else {
            // permission denied, boo! Disable the
            // functionality that depends on this permission.
            Toast.makeText(this, "No read storage permission! Cannot perform the action.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        switch (permission) {
            case Manifest.permission.READ_EXTERNAL_STORAGE:
            case Manifest.permission.CAMERA:
                // No need to explain to user as it is obvious
                return false;
            default:
                return true;
        }
    }


    //    检测是否有权限
    private void checkPermission() {

        int readStoragePermissionState = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int cameraPermissionState = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        boolean readStoragePermissionGranted = readStoragePermissionState != PackageManager.PERMISSION_GRANTED;
        boolean cameraPermissionGranted = cameraPermissionState != PackageManager.PERMISSION_GRANTED;

        if (readStoragePermissionGranted || cameraPermissionGranted) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                String[] permissions;
                if (readStoragePermissionGranted && cameraPermissionGranted) {
                    permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA };
                } else {
                    permissions = new String[] {
                            readStoragePermissionGranted ? Manifest.permission.READ_EXTERNAL_STORAGE
                                    : Manifest.permission.CAMERA
                    };
                }
                ActivityCompat.requestPermissions(this,
                        permissions,
                        REQUEST_CODE);
            }

        } else {
            // Permission granted
//            onClick(photo_pick);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        circle_pickPhoto.onActivityResult(requestCode,resultCode,data);



    }

}
