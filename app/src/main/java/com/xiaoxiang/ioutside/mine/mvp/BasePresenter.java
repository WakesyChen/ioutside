package com.xiaoxiang.ioutside.mine.mvp;

/**
 * Created by 15119 on 2016/6/28.
 */
public interface BasePresenter<T extends BaseView> {
    void start();
    void setView(T view);
    T getView();
    boolean isViewAttached();
}
