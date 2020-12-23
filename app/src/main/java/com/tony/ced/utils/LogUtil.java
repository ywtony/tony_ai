package com.tony.ced.utils;

import android.util.Log;

/**
 * @ProjectName: tony_ai
 * @Package: com.tony.ced.utils
 * @ClassName: LogUtil
 * @Description: java类作用描述
 * @Author: wei.yang
 * @CreateDate: 2020/12/23 9:38
 * @UpdateUser: 更新者：wei.yang
 * @UpdateDate: 2020/12/23 9:38
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class LogUtil {
    private static final String TAG = "OpenCV_Tony";
    public static void log(String str){
        Log.e(TAG,str);
    }
}
