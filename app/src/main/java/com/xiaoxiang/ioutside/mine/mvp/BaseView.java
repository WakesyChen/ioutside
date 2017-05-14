package com.xiaoxiang.ioutside.mine.mvp;

/**
 * Created by 15119 on 2016/6/28.
 */
public interface BaseView<T extends BasePresenter> {
   void setPresenter(T presenter);
}
