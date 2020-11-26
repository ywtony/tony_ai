package com.tony.ced.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.tony.ced.BaseActivity;
import com.tony.ced.R;
import com.tony.ced.utils.BitmapUtil;

/**
 * @ProjectName: tony_ai
 * @Package: com.tony.ced.ui
 * @ClassName: GrayImageActivity
 * @Description: 将任意图像转换为灰度图像
 * @Author: wei.yang
 * @CreateDate: 2020/11/26 11:13
 * @UpdateUser: 更新者：wei.yang
 * @UpdateDate: 2020/11/26 11:13
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class GrayImageActivity extends BaseActivity {
    private Button btnGray;
    private ImageView ivGray;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gray_image);
        initViews();
    }

    private void initViews() {
        btnGray = findViewById(R.id.btnToGray);
        btnGray.setOnClickListener(v -> {
            ivGray.setImageBitmap(BitmapUtil.getInstance().getGrayBitmap(R.mipmap.girl3));
        });
        ivGray = findViewById(R.id.ivGray);
    }
}
