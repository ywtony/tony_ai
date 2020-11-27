package com.tony.ced.ui;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.tony.ced.BaseActivity;
import com.tony.ced.R;
import com.tony.ced.utils.BitmapUtil;
import com.tony.ced.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @ProjectName: tony_ai
 * @Package: com.tony.ced.ui
 * @ClassName: MapAndBitmapActivity
 * @Description: 测试Mat与Bitmap
 * @Author: wei.yang
 * @CreateDate: 2020/11/27 10:12
 * @UpdateUser: 更新者：wei.yang
 * @UpdateDate: 2020/11/27 10:12
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class MapAndBitmapActivity extends BaseActivity {
    @BindView(R.id.btnClickMenu)
    Button btnClickMenu;
    @BindView(R.id.btnLoadImage)
    Button btnLoadImage;
    @BindView(R.id.ivOrigin)
    ImageView ivOrigin;
    @BindView(R.id.ivChange)
    ImageView ivChange;
    private List<String> datas = new ArrayList<>();
    private List<String> images = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_and_bitmap);
        ButterKnife.bind(this);
        btnClickMenu.setOnClickListener(v -> {
            datas.clear();
            datas.add("加载图像与读取图像基本信息");
            show(datas);
        });
        btnLoadImage.setOnClickListener(v -> {
            openPhoto();
        });


    }

    /**
     * @description 选择图像后的回调
     * @date: 2020/11/27 10:13
     * @author: wei.yang
     */
    @Override
    public void onPhotoDataCallback(List<String> images) {
        this.images = images;
        if (images != null && images.size() > 0) {
            ivOrigin.setImageBitmap(BitmapFactory.decodeFile(images.get(0)));
        }
    }

    @Override
    public void onMenuItemClick(int position) {
        if (images != null && images.size() > 0) {
            ivOrigin.setImageBitmap(BitmapUtil.getInstance().getGrayBitmap(BitmapFactory.decodeFile(images.get(0))));
        }

    }
}
