package com.xiaoxiang.ioutside.mine.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.common.CircleImageView;
import com.xiaoxiang.ioutside.mine.model.MyEssayNews;

import java.util.ArrayList;

/**
 * Created by oubin6666 on 2016/5/17.
 */
public class EssayNewsAdapter extends RecyclerView.Adapter<EssayNewsAdapter.NewsHolder> {
    private final String TAG = getClass().getSimpleName();
    private int LIKED = 2;
    private int COMMENT = 1;
    private int OFFICIAL = 3;
    public ArrayList<MyEssayNews> mylist = new ArrayList<>();
    private View view;
    private NewsHolder newsHolder;


    public EssayNewsAdapter(ArrayList<MyEssayNews> mylist) {
        this.mylist = mylist;
    }


    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_essaynews_item, parent, false);
        newsHolder = new NewsHolder(view);
        return newsHolder;
    }


    /**
     * 追加单个数据
     *
     * @param item
     */
    public void addItem(MyEssayNews item) {
        mylist.add(item);
        notifyDataSetChanged();
    }


    /**
     * 追加数据集
     *
     * @param items
     */
    public void addItems(ArrayList<MyEssayNews> items) {
        mylist.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * 添加单个数据到列表头部
     *
     * @param item
     */
    public void addItemToHead(MyEssayNews item) {
        mylist.add(0, item);
        notifyDataSetChanged();
    }

    /**
     * 添加数据集到列表头部
     *
     * @param items
     */
    public void addItemsToHead(ArrayList<MyEssayNews> items) {
        mylist.addAll(0, items);
        notifyDataSetChanged();
    }

    /**
     * 移除某个数据
     *
     * @param position
     */
    public void remove(int position) {
        mylist.remove(position);
        notifyDataSetChanged();
    }

    /**
     * 移除某个数据项
     *
     * @param item
     */
    public void remove(MyEssayNews item) {
        mylist.remove(item);
        notifyDataSetChanged();
    }

    /**
     * clearData all data
     */
    public void clear() {
        mylist.clear();
        notifyDataSetChanged();
    }

    /**
     * 获取指定位置的数据项
     *
     * @param position
     * @return
     */
    public MyEssayNews getItem(int position) {
        return mylist.get(position);
    }

    @Override
    public int getItemCount() {
        return mylist.size();
    }


    @Override
    public void onBindViewHolder(final NewsHolder newsHolder, final int position) {
        MyEssayNews essayNews = mylist.get(position);
        newsHolder.userName.setText(essayNews.getUserName());
        newsHolder.msg.setText(essayNews.getMessage());
        newsHolder.title.setText(essayNews.getTitle());
        newsHolder.commentCount.setText(essayNews.getCommentCount() + "");
        newsHolder.likedCount.setText(essayNews.getLikedCount() + "");
        ImageLoader.getInstance().displayImage(essayNews.getPhoto(), newsHolder.essayPhoto);
        if (essayNews.getUserPhoto() == null) {
            newsHolder.userPhoto.setImageResource(R.drawable.defoulthead);
        }
        ImageLoader.getInstance().displayImage(essayNews.getUserPhoto(), newsHolder.userPhoto);
        if (essayNews.getMessageType() == COMMENT) {
            newsHolder.commentCon.setVisibility(View.VISIBLE);
            newsHolder.commentCon.setText(essayNews.getContent());
        }
        newsHolder.userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemPhotoClick(view, position);
                }
            }
        });

        newsHolder.essay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemEssayClick(view, position);
                }
            }
        });
    }

    public ArrayList<MyEssayNews> getMylist() {
        return mylist;
    }

    public void setMylist(ArrayList<MyEssayNews> mylist) {
        this.mylist = mylist;
    }

    public class NewsHolder extends RecyclerView.ViewHolder {
        public TextView userName;
        public TextView msg;
        public TextView title;
        public TextView likedCount;
        public TextView commentCount;
        public CircleImageView userPhoto;
        public RelativeLayout essay;
        public ImageView essayPhoto;
        public TextView commentCon;

        public NewsHolder(View view) {
            super(view);
            userPhoto = (CircleImageView) view.findViewById(R.id.cirimg_userphoto_news);
            userName = (TextView) view.findViewById(R.id.tv_username_news);
            msg = (TextView) view.findViewById(R.id.tv_msg_news);
            title = (TextView) view.findViewById(R.id.my_essaytitle_news);
            essayPhoto = (ImageView) view.findViewById(R.id.my_essayphoto_news);
            essay = (RelativeLayout) view.findViewById(R.id.item_lay_my_news);
            likedCount = (TextView) view.findViewById(R.id.tv_likedCount_news);
            commentCount = (TextView) view.findViewById(R.id.tv_commentCount_news);
            commentCon = (TextView) view.findViewById(R.id.tv_commentcon_news);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, getLayoutPosition());
                    }
                }
            });
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);

        void onItemPhotoClick(View view, int position);

        void onItemEssayClick(View view, int position);

    }

    public void setOnItemClickListener(ItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private ItemClickListener onItemClickListener;

}
