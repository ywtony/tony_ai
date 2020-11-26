package com.tony.ced;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

/**
 * @ProjectName: tony_ai
 * @Package: com.tony.ced
 * @ClassName: BaseActivity
 * @Description: demo中的基类
 * @Author: wei.yang
 * @CreateDate: 2020/11/26 11:14
 * @UpdateUser: 更新者：wei.yang
 * @UpdateDate: 2020/11/26 11:14
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class BaseActivity extends FragmentActivity {
    protected Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
    }
}
