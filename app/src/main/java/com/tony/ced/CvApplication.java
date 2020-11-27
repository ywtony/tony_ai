package com.tony.ced;

import android.app.Application;
import android.content.Context;

import com.tony.ced.utils.ContextUtils;

/**
 * @ProjectName: tony_ai
 * @Package: com.tony.ced
 * @ClassName: CvApplication
 * @Description: java类作用描述
 * @Author: wei.yang
 * @CreateDate: 2020/11/26 11:15
 * @UpdateUser: 更新者：wei.yang
 * @UpdateDate: 2020/11/26 11:15
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class CvApplication extends Application {
    private static CvApplication instance;

    public static CvApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ContextUtils.init(this);
    }
}
