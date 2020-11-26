package com.tony.ced.adapter.listener;

/**
 * Describe：RecycleView 点击事件监听
 * Created by 杨伟 on 2018/10/18.
 */
public interface OnListItemClickListener<T> {

    /**
     * Item点击事件
     *
     * @param data     Data
     * @param position position
     */
    void onItemClick(T data, int position);
}
