package com.tony.ced.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import androidx.fragment.app.Fragment;
import android.util.DisplayMetrics;

import java.lang.reflect.Field;


/**
 * Author: yw-tony
 * Description：获取全局上下文工具类
 * 注：需要在application页面初始化
 */
public class PixelMethod {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     *
     * @param activity
     * @return 单位px
     */
    public static int getScreenWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕宽度
     *
     * @param fragment
     * @return
     */
    public static int getScreenWidth(Fragment fragment) {
        DisplayMetrics dm = new DisplayMetrics();
        fragment.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        if (context instanceof Activity) {
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        }
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param activity
     * @return
     */
    public static int getScreenHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }


    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }


    /**
     * 获取屏幕高度
     *
     * @param fragment
     * @return
     */
    public static int getScreenHeight(Fragment fragment) {
        DisplayMetrics dm = new DisplayMetrics();
        fragment.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    // 获取图片
    public static Drawable getDrawable(Context context, int id) {
        return context.getResources().getDrawable(id);
    }

    // 获取颜色
    public static int getColor(Context context, int id) {
        return context.getResources().getColor(id);
    }

    //根据id获取颜色的状态选择器
    public static ColorStateList getColorStateList(Context context, int id) {
        return context.getResources().getColorStateList(id);
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 针对网络图片 如果为0,返回0
     * 确保图片的宽高比
     * 单位px
     *
     * @param imgOrgWidth  接口中提供图片的宽度
     * @param imgOrgHeight 接口中提供图片的高度
     * @param width
     * @return
     */
    public static int getImageHeight(String imgOrgWidth, String imgOrgHeight,
                                     int width) {
        int widthSize = Integer.parseInt(imgOrgWidth);
        if (widthSize <= 0)
            return 0;
        return Integer.parseInt(imgOrgHeight) * width / widthSize;
    }

    /**
     * 网络或者设计提供的宽高
     * 确保图片的宽高比
     * 单位px
     *
     * @param imgOrgWidth
     * @param imgOrgHeight
     * @param width
     * @return
     */
    public static float getImageHeight(float imgOrgWidth, float imgOrgHeight,
                                       int width) {
        if (imgOrgWidth <= 0)
            return 0;
        return imgOrgHeight * width / imgOrgWidth;
    }

    /**
     * 针对本地图片
     *
     * @param imgOrgWidth
     * @param imgOrgHeight
     * @param width
     * @return
     */
    public static int getImageHeight(int imgOrgWidth, int imgOrgHeight,
                                     int width) {
        if (imgOrgWidth <= 0)
            return 0;
        return imgOrgHeight * width / imgOrgWidth;
    }

    /**
     * @param imgOrgWidth
     * @param imgOrgHeight
     * @param height
     * @return
     */
    public static int getImageWidth(int imgOrgWidth, int imgOrgHeight,
                                    int height) {
        if (imgOrgHeight <= 0)
            return 0;
        return imgOrgWidth * height / imgOrgHeight;
    }

    /**
     * 确保图片的宽高比
     *
     * @param imgOrgHeight
     * @param imgOrgHeightIn
     * @param height
     * @return
     */
    public static int getImageHeightWithHeight(int imgOrgHeight, int imgOrgHeightIn,
                                               int height) {
        if (imgOrgHeight <= 0)
            return 0;
        return imgOrgHeightIn * height / imgOrgHeight;
    }
}
