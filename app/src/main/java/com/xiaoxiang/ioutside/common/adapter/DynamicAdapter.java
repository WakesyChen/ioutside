package com.xiaoxiang.ioutside.common.adapter;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xiaoxiang.ioutside.mine.adapter.CollectionAdapter;
import com.xiaoxiang.ioutside.mine.model.Dynamic;
import com.xiaoxiang.ioutside.util.BitmapUtil;

import java.util.List;

/**
 * Created by 15119 on 2016/7/14.
 */
public class DynamicAdapter extends CollectionAdapter<Dynamic> {

    @Override
    public void bindViewHolder(NormalViewHolder viewHolder, Dynamic data) {

        CollectionAdapter.ViewHolder holder = (CollectionAdapter.ViewHolder) viewHolder;

        holder.layHeader.setVisibility(View.GONE);

        if (data.getPhotoList() != null) {
            Glide.with(holder.itemView.getContext()).load(data.getPhotoList().get(0)).into(holder.ivPhoto);
        } else if (data.getVideo() != null) {
            new RetrieveVideoThumbnailTask(holder.ivPhoto).execute(data.getVideo());
        }

        holder.tvTitle.setText(data.getThoughts());
        holder.tvTime.setText(data.getPublishTime());
        holder.tvCountLiked.setText(String.valueOf(data.getLikedCount()));
        holder.tvCountComment.setText(String.valueOf(data.getCommentCount()));
    }

    public DynamicAdapter(List<Dynamic> dataSet) {
        super(dataSet);
    }


    public class RetrieveVideoThumbnailTask extends AsyncTask<String, Void, Bitmap> {

        private ImageView iv;

        private int width;
        private int height;

        public RetrieveVideoThumbnailTask(ImageView iv) {
            this.iv = iv;
        }

        @Override
        protected void onPreExecute() {
            width = iv.getWidth();
            height = iv.getHeight();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return BitmapUtil.createVideoThumbnail(params[0],
                    width, height);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            iv.setImageBitmap(bitmap);
        }
    }
}
