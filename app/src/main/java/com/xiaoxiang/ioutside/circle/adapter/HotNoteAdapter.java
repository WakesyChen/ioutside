package com.xiaoxiang.ioutside.circle.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.circle.model.HotNotes;
import com.xiaoxiang.ioutside.circle.widge.MyGridItemDivider;
import com.xiaoxiang.ioutside.common.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wakesy on 2016/9/25.
 */
public class HotNoteAdapter extends RecyclerView.Adapter {
    private List<HotNotes.Notes> datalist;
    public final static int TYPE_NORMAL=0;
    public final static int TYPE_FOOTER=1;
    private Context context;
    private static final String TAG = "HotNoteAdapter";
    public HotNoteAdapter() {
        datalist=new ArrayList<>();
    }

    public List<HotNotes.Notes> getData() {
        return datalist;
    }

    public void setData(List<HotNotes.Notes> datas) {
        datalist=datas;
        notifyDataSetChanged();
    }
    public void clearData(){
        datalist.clear();
        notifyDataSetChanged();
    }

    public void addData(List<HotNotes.Notes> datas) {
            addData(0,datas);

    }
    public void addData(int position,List<HotNotes.Notes> datas) {
        if (datas!=null&&datas.size()>0) {
            datalist.addAll(position,datas);
            notifyDataSetChanged();
        }

    }



    @Override
    public int getItemCount() {

        return (datalist != null &&datalist.size()>0)? datalist.size()+1:0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position+1== getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return  TYPE_NORMAL;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL) {

            context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.hotnote_recy_item_normal, parent, false);
            return new NormalViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.circle_bigv_recy_footer, parent, false);
            return new FooterViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NormalViewHolder) {
            if (datalist != null && datalist.size() > 0) {
                HotNotes.Notes note = datalist.get(position);
                NormalViewHolder viewHolder = (NormalViewHolder) holder;
                Glide.with(context).load(note.getPublishUserPhoto()).into(viewHolder.hotnote_postPhoto);
                viewHolder.hotnote_postName.setText(note.getPublishUserName()+"");
                viewHolder.hotnote_time.setText(note.getPublishDate()+"");
                viewHolder.hotnote_title.setText(note.getTitle()+"");
                viewHolder.hotnote_content.setText(note.getContent()+"");

//            设置gridView适配器
                HotNoteGridAdapter gridAdapter=new HotNoteGridAdapter(note.getPhotoList());
                viewHolder.hotnote_photolist.setAdapter(gridAdapter);
                setListViewHeightBasedOnChildren(viewHolder.hotnote_photolist);
                gridAdapter.notifyDataSetChanged();
                Log.i(TAG, "onBindViewHolder:getItemCount "+gridAdapter.getCount());

//
                viewHolder.hotnote_item_questionNum.setText(note.getViewCount()+"");
                viewHolder.hotnote_item_likeNum.setText(10+"");

            }
        } else {


        }



    }



    public static class NormalViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView hotnote_postPhoto;
        private TextView hotnote_postName;
        private TextView hotnote_time;
        private TextView hotnote_title;
        private TextView hotnote_content;
        private GridView hotnote_photolist;
        private TextView hotnote_item_questionNum;
        private ImageView hotnote_item_question;
        private TextView hotnote_item_likeNum;
        private ToggleButton hotnote_item_like;


        public NormalViewHolder(View itemView) {
            super(itemView);
            hotnote_postPhoto = (CircleImageView) itemView.findViewById(R.id.hotnote_postPhoto);
            hotnote_postName = (TextView) itemView.findViewById(R.id.hotnote_postName);
            hotnote_time = (TextView) itemView.findViewById(R.id.hotnote_time);
            hotnote_title = (TextView) itemView.findViewById(R.id.hotnote_title);
            hotnote_content = (TextView) itemView.findViewById(R.id.hotnote_content);
            hotnote_photolist = (GridView) itemView.findViewById(R.id.hotnote_photolist);
            hotnote_item_questionNum = (TextView) itemView.findViewById(R.id.hotnote_item_questionNum);
            hotnote_item_question = (ImageView) itemView.findViewById(R.id.hotnote_item_question);
            hotnote_item_likeNum = (TextView) itemView.findViewById(R.id.hotnote_item_likeNum);
            hotnote_item_like = (ToggleButton) itemView.findViewById(R.id.hotnote_item_like);


        }
    }
   public class FooterViewHolder extends RecyclerView.ViewHolder {

       public FooterViewHolder(View itemView) {
           super(itemView);

       }
   }

//    动态设置grdiview高度
    public static void setListViewHeightBasedOnChildren(GridView listView) {
        // 获取listview的adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        // 固定列宽，有多少列
        int col = 3;// listView.getNumColumns();
        int totalHeight = 0;
        // i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
        // listAdapter.getCount()小于等于8时计算两次高度相加
        for (int i = 0; i < listAdapter.getCount(); i += col) {
            // 获取listview的每一个item
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            // 获取item的高度和
            totalHeight += listItem.getMeasuredHeight();
        }

        // 获取listview的布局参数
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // 设置高度
        params.height = totalHeight;
        // 设置margin
        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        // 设置参数
        listView.setLayoutParams(params);
    }
}
