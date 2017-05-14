package com.xiaoxiang.ioutside.common.imagepicker;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhang on 2016/5/31,0031.
 */
public class AlbumController {

    private ContentResolver resolver;
    private static final int IMAGE_SIZE=1024*10;
    public static final String RECENT_PHOTO="最近照片";

    public AlbumController(Context context) {
        resolver = context.getContentResolver();
    }

    /** 获取最近照片列表 */
    public List<String> getCurrent() {
        List<String> photos = new ArrayList<String>();
        Cursor cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.DATE_ADDED, MediaStore.Images.ImageColumns.SIZE }, MediaStore.Images.ImageColumns.SIZE+" > "+IMAGE_SIZE , null, MediaStore.Images.ImageColumns.DATE_ADDED+"  DESC");
        if (cursor == null) {
            return photos;
        }

        while (cursor.moveToNext()){
            photos.add(cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)));
        }

        if(!cursor.isClosed()){
            cursor.close();
            cursor=null;
        }

        return photos;
    }


    /**
     * 获取所有相册列表
     * 主要给文件夹计数
     * */
    public List<AlbumModel> getAlbums() {
        List<AlbumModel> albums = new ArrayList<AlbumModel>();
        Map<String, AlbumModel> map = new HashMap<String,AlbumModel>();
        Cursor cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, MediaStore.Images.ImageColumns.SIZE }, null, null, null);
        if (cursor == null || !cursor.moveToNext())
            return albums;
        cursor.moveToLast();
        AlbumModel current = new AlbumModel(RECENT_PHOTO, 0, cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA))); // "最近照片"相册
        albums.add(current);

        do {
            if (cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.SIZE)) < IMAGE_SIZE)
                continue;
            current.increaseCount();//给最近照片+1
            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME));//获取文件夹名字
            if (map.keySet().contains(name))
                map.get(name).increaseCount();
            else {
                AlbumModel album = new AlbumModel(name, 1, cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)));
                map.put(name, album);
                albums.add(album);
            }
        } while (cursor.moveToPrevious());



        if(!cursor.isClosed()){
            cursor.close();
            cursor=null;
        }

        return albums;
    }

    /** 获取对应相册下的照片 */
    public List<String> getAlbum(String name) {
        List<String> photos = new ArrayList<String>();
        Cursor cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                        MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.DATE_ADDED, MediaStore.Images.ImageColumns.SIZE }, "bucket_display_name = ? AND _size > ? ",
                new String[] { name,IMAGE_SIZE+"" }, MediaStore.Images.ImageColumns.DATE_ADDED+" DESC");
        if (cursor == null )
            return photos;

        while(cursor.moveToNext()){
            photos.add(cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)));
        }
        if(!cursor.isClosed()){
            cursor.close();
            cursor=null;
        }

        return photos;
    }


}

