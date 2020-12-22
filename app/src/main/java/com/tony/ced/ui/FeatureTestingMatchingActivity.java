package com.tony.ced.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import com.tony.ced.BaseActivity;
import com.tony.ced.R;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @ProjectName: tony_ai
 * @Package: com.tony.ced.ui
 * @ClassName: FeatureTestingMatchingActivity
 * @Description: 特征检测与匹配
 * @Author: wei.yang
 * @CreateDate: 2020/12/22 9:15
 * @UpdateUser: 更新者：wei.yang
 * @UpdateDate: 2020/12/22 9:15
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class FeatureTestingMatchingActivity  extends BaseActivity {
    @BindView(R.id.btnSelectImage)
    Button btnSelectImage;
    @BindView(R.id.ivImage)
    ImageView ivImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_features_testing);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        btnSelectImage.setOnClickListener(v -> {
            selectedImage();
        });
    }

    private void selectedImage() {
        List<String> datas = new ArrayList<>();
        show(datas);
    }

    @Override
    public void onMenuItemClick(int position) {
        super.onMenuItemClick(position);
        switch (position) {
            case 0://
                break;


        }
    }

}