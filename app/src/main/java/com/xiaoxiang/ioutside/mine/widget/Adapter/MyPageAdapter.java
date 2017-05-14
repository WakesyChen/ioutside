package com.xiaoxiang.ioutside.mine.widget.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoxiang.ioutside.homepage.model.Subject;

import java.util.ArrayList;

/**
 * Created by oubin6666 on 2016/5/8.
 */
public class MyPageAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<Subject> subjects;




    public MyPageAdapter(Context context,ArrayList<Subject> subjects){
        this.context=context;
        if(subjects==null){
            this.subjects=new ArrayList<>();
        }else{
            this.subjects=subjects;
        }

        for(int i=0;i<subjects.size();i++){

        }

    }




    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }
}
