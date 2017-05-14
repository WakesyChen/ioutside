package com.xiaoxiang.ioutside.circle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.circle.model.QAdataList;
import com.xiaoxiang.ioutside.common.CircleImageView;
import com.xiaoxiang.ioutside.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Wakesy on 2016/9/19.
 */
public class QAofVAdapter extends RecyclerView.Adapter {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_NORMAL = 1;
    public final static int TYPE_FOOTER = 2;
    private static final String TAG = "QAofVAdapter";
    private List<QAdataList.QAdata> datalist;
    private Context context;
    public QAofVAdapter() {
        datalist=new ArrayList<>();
    }

    public void addData(List<QAdataList.QAdata> datas){
       addData(0,datas);
    }

    public void addData(int position,List<QAdataList.QAdata> datas) {
        if (datas!=null&&datas.size()>0) {
            datalist.addAll(position,datas);
//            Log.i(TAG, "addData: "+datalist.size());
           notifyDataSetChanged();
        }

    }
    public void setData(List<QAdataList.QAdata> datalist){
       this.datalist=datalist;
        notifyDataSetChanged();

    }
    public List<QAdataList.QAdata> getData(){
        return datalist;
    }
    public void clearData(){
        datalist.clear();
        notifyItemRangeChanged(0,datalist.size());

    }
    @Override
    public int getItemCount() {

        return datalist!=null&&datalist.size()>0 ?datalist.size() + 2:1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position+1== getItemCount()) {

            return TYPE_FOOTER;
        } else {
            return TYPE_NORMAL;
        }


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.circle_bigv_recy_header, parent, false);
            return new HeaderViewHolder(view);
        } else if (viewType == TYPE_NORMAL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.circle_bigv_recy_item, parent, false);
            return new NormalViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.circle_bigv_recy_footer, parent, false);
            return new FooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (datalist!=null&&datalist.size()>0) {
            if (holder instanceof HeaderViewHolder) {
                ((HeaderViewHolder) holder).expand_text_view.setText("当你孤单你会想起谁，你想不想找个人来陪，你的快乐上别，只有我能体会，让我再陪你走一回，" +
                        " 你的快乐伤悲，只有我能体会,你真的不爱了你不是真正的快乐，微笑是你穿的保护色，");
            }else if (holder instanceof NormalViewHolder){
               NormalViewHolder viewHolder=((NormalViewHolder) holder);
//                Glide.with(context).load(datalist.get(position-1).getQuestionUserPhoto()).into(viewHolder. circle_item_QPhoto);
//                viewHolder.circle_item_QName.setText(datalist.get(position - 1).getQuestionUserName());
//                viewHolder.circle_item_time.setText(datalist.get(position-1).getQuestionTime());

                viewHolder.expand_textview_Question.setText(datalist.get(position - 1).getQuestion()+"");
                String answer=datalist.get(position - 1).getAnswer();
                if (!TextUtils.isEmpty(answer)) {
                    viewHolder.circle_item_AArea.setVisibility(View.VISIBLE);
//                    大V头像，名字
//                    Glide.with(context).load(datalist.get(position-1).getAnswer()UserPhoto()).into(viewHolder.circle_item_APhoto);
//                    viewHolder.circle_item_QName.setText(datalist.get(position - 1).getQuestionUserName());
                    viewHolder.expand_textview_Answer.setText(answer);

                } else {
                    viewHolder.circle_item_AArea.setVisibility(View.GONE);
                }

//                viewHolder.circle_item_time.setText(datalist.get(position-1).getAnswerTime());

            } else {

            }
        }


    }

    class HeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ExpandableTextView expand_text_view;
        private ImageView circle_bigV_back;
        private TextView circle_bigV_title;
        private ImageView circle_bigV_share;
        private TextView circle_bigV_focusNum;
        private ToggleButton circle_bigV_focus;
        private CircleImageView circle_bigV_photo;
        private TextView circle_bigV_QuestionNum;
        private TextView circle_bigV_AnswerNum;
        private ToggleButton circle_bigV_hotOrNew;

        public HeaderViewHolder(View view) {
            super(view);
            expand_text_view = (ExpandableTextView) view.findViewById(R.id.expand_text_view);
            circle_bigV_back= (ImageView) view.findViewById(R.id.circle_bigV_back);
            circle_bigV_title= (TextView) view.findViewById(R.id.circle_bigV_title);
            circle_bigV_share= (ImageView) view.findViewById(R.id.circle_bigV_share);
            circle_bigV_focusNum= (TextView) view.findViewById(R.id.circle_bigV_focusNum);
            circle_bigV_focus= (ToggleButton) view.findViewById(R.id.circle_bigV_focus);
            circle_bigV_photo= (CircleImageView) view.findViewById(R.id.circle_bigV_photo);
            circle_bigV_QuestionNum= (TextView) view.findViewById(R.id.circle_bigV_QuestionNum);
            circle_bigV_AnswerNum= (TextView) view.findViewById(R.id.circle_bigV_AnswerNum);
            circle_bigV_hotOrNew= (ToggleButton) view.findViewById(R.id.circle_bigV_hotOrNew);

            circle_bigV_back.setOnClickListener(this);
            circle_bigV_share.setOnClickListener(this);
            circle_bigV_focus.setOnClickListener(this);
            circle_bigV_hotOrNew.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case  R.id.circle_bigV_back:
                    if (onClickListener!=null) {
                        onClickListener.backClick();
                    }

                    break;
                case  R.id.circle_bigV_share:
                    if (onClickListener!=null) {
                        onClickListener.shareClick();
                    }
                    break;
                case  R.id.circle_bigV_focus:
                    if (onClickListener!=null) {
                        onClickListener.focusClick(circle_bigV_focus);
                    }
                    break;
                case  R.id.circle_bigV_hotOrNew:
                    if (onClickListener!=null) {
                        onClickListener.ChooseTypeClick(circle_bigV_hotOrNew);
                    }
                    break;

            }


        }
    }

    class NormalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ExpandableTextView expand_textview_Question;
        private ExpandableTextView expand_textview_Answer;
        private CircleImageView circle_item_QPhoto;
        private TextView circle_item_QName;
        private CircleImageView circle_item_APhoto;
        private TextView circle_item_AName;
        private TextView circle_item_time;
        private TextView bigV_item_questionNum;
        private ImageView bigV_item_question;
        private RelativeLayout circle_item_AArea;
        private TextView bigV_item_likeNum;
        private ToggleButton bigV_item_like;


        public NormalViewHolder(View itemView) {
            super(itemView);
            expand_textview_Question = (ExpandableTextView) itemView.findViewById(R.id.expand_textview_Question);
            expand_textview_Answer = (ExpandableTextView) itemView.findViewById(R.id.expand_textview_Answer);
            circle_item_QPhoto = (CircleImageView) itemView.findViewById(R.id.circle_item_QPhoto);
            circle_item_QName = (TextView) itemView.findViewById(R.id.circle_item_QName);
            circle_item_AArea = (RelativeLayout) itemView.findViewById(R.id.circle_item_AArea);
            circle_item_APhoto = (CircleImageView) itemView.findViewById(R.id.circle_item_APhoto);
            circle_item_AName = (TextView) itemView.findViewById(R.id.circle_item_AName);
            circle_item_time = (TextView) itemView.findViewById(R.id.circle_item_time);
            bigV_item_questionNum = (TextView) itemView.findViewById(R.id.bigV_item_questionNum);
            bigV_item_question = (ImageView) itemView.findViewById(R.id.bigV_item_question);
            bigV_item_likeNum = (TextView) itemView.findViewById(R.id.bigV_item_likeNum);
            bigV_item_like = (ToggleButton) itemView.findViewById(R.id.bigV_item_like);

            bigV_item_question.setOnClickListener(this);
            bigV_item_like.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bigV_item_question:
                    if (onClickListener!=null) {
                        onClickListener.commentClick();
                    }

                    break;
                case R.id.bigV_item_like:
                    if (onClickListener!=null) {
                        onClickListener.likeClick(bigV_item_like);
                    }
                    break;

            }
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnClickListener {

        void backClick();
        void shareClick();
        void focusClick(View v);
        void ChooseTypeClick(View v);

        void commentClick();
        void likeClick(View v);
    }
    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener=onClickListener;

    }


}
