package com.xiaoxiang.ioutside.common;

import android.content.Context;

import com.xiaoxiang.ioutside.mine.model.Nums;
import com.xiaoxiang.ioutside.mine.model.PersonalInfo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


/**
 * Created by oubin6666 on 2016/5/3.
 */
public class CachedInfo implements Serializable {
    private static final long serialVersionUID = -190734710746841476L;

    private String FILE_NAME="cachedInfo";

    private String token;
    private PersonalInfo personalInfo;
    private Nums nums;
    private int userId;
    private transient Context mContext;

    public CachedInfo(Context mContext) {
        this.mContext = mContext;
        load();
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }
    public void setPersonalInfo(PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }
    public Nums getNums() {
        return nums;
    }
    public void setNums(Nums nums) {
        this.nums = nums;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    //从文件中加载出cachedinfo类来
    public synchronized void load() {
        try{
            FileInputStream ins=mContext.openFileInput(FILE_NAME);
            byte data[]=new byte[ins.available()];
            ins.read(data);
            ins.close();
            ByteArrayInputStream in=new ByteArrayInputStream(data);
            ObjectInputStream objIn=new ObjectInputStream(in);
            CachedInfo ci=(CachedInfo)objIn.readObject();
            copy(ci);
            objIn.close();
            in.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //将这个类存到文件中去
    public synchronized void save(){
        try {
            ByteArrayOutputStream out=new ByteArrayOutputStream();
            ObjectOutputStream objOut=new ObjectOutputStream(out);
            objOut.writeObject(this);
            byte[] data=out.toByteArray();
            objOut.close();
            out.close();
            FileOutputStream outs=mContext.openFileOutput(FILE_NAME,Context.MODE_PRIVATE);
            outs.write(data);
            outs.getFD().sync();
            outs.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public synchronized void copy(CachedInfo info){
        this.personalInfo=info.getPersonalInfo();
        this.token=info.getToken();
        this.nums=info.getNums();
        this.userId = info.getUserId();
    }

    public synchronized void clear(){
        this.token=null;
        this.personalInfo=null;
        this.nums=null;
        this.userId  = -1;
    }

}

