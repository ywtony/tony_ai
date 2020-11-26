package com.tony.ced.adapter;

import android.content.Context;

import com.tony.ced.R;
import com.tony.ced.adapter.listener.OnListItemClickListener;
import com.tony.ced.bean.MainItem;

import org.byteam.superadapter.SuperViewHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @ProjectName: tony_ai
 * @Package: com.tony.ced.adapter
 * @ClassName: MainAdapter
 * @Description: 主页的适配器
 * @Author: wei.yang
 * @CreateDate: 2020/11/26 9:57
 * @UpdateUser: 更新者：wei.yang
 * @UpdateDate: 2020/11/26 9:57
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class MainAdapter extends BaseListAdapter<MainItem> {

    public MainAdapter(Context context, List<MainItem> mData, OnListItemClickListener<MainItem> listener) {
        super(context, mData, R.layout.item_main, listener);
    }

    @Override
    public void onBindData(@NotNull SuperViewHolder holder, int viewType, int layoutPosition, @NotNull MainItem data) {
        holder.setText(R.id.tvName,data.getTitle());
    }
}
