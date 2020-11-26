package com.tony.ced.utils;

import android.content.Context;
import android.content.Intent;

/**
 * @ProjectName: tony_ai
 * @Package: com.tony.ced.utils
 * @ClassName: ActivityUtils
 * @Description: java类作用描述
 * @Author: wei.yang
 * @CreateDate: 2020/11/26 11:08
 * @UpdateUser: 更新者：wei.yang
 * @UpdateDate: 2020/11/26 11:08
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class ActivityUtils {
    private ActivityUtils() {
    }

    private static ActivityUtils instance;

    public static ActivityUtils getInstance() {
        if (instance == null) {
            instance = new ActivityUtils();
        }
        return instance;
    }

    /**
     * @description 跳转到指定的Activity界面
     * @date: 2020/11/26 11:10
     * @author: wei.yang
     */
    public void startActivity(Context context, String className) {
        Intent intent = new Intent();
        intent.setClassName(context, className);
        context.startActivity(intent);
    }
}
