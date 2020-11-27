package com.tony.ced.utils;

import android.app.Application;
import android.content.Context;


/**
 * Author: yw-tony
 * Description：获取全局上下文工具类
 * 注：需要在application页面初始化
 */
public final class ContextUtils {

    private static Context context;
    private static Application mApplication;

    private ContextUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        ContextUtils.context = context.getApplicationContext();
        if (context instanceof Application) {
            mApplication = ((Application) context.getApplicationContext());
        }

    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init first");
    }


    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Application getApplication() {
        if (mApplication != null) return mApplication;
        throw new NullPointerException("u should init first");
    }
}