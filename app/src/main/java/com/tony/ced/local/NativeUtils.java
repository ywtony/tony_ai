package com.tony.ced.local;

import android.graphics.Bitmap;

import org.opencv.core.Mat;

/**
 * @ProjectName: tony_ai
 * @Package: com.tony.ced.local
 * @ClassName: NativeUtils
 * @Description: 本地方法集中处理
 * @Author: wei.yang
 * @CreateDate: 2021/1/7 15:21
 * @UpdateUser: 更新者：wei.yang
 * @UpdateDate: 2021/1/7 15:21
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class NativeUtils {
    private NativeUtils() {
    }

    private static NativeUtils instance;

    public static NativeUtils getInstance() {
        if (instance == null) {
            instance = new NativeUtils();
        }
        return instance;
    }

    /**
     * @description 获取opencv的版本号
     * @date: 2021/1/7 15:26
     * @author: wei.yang
     */
    public native String getOpenCVVersion();

    /**
     * 返回漫画风格的图片
     * @param input 输入的原图 用bitmap表示
     * @param output 输出的图片，用bitmap表示
     */
    public native void getComicImage(Bitmap input, Bitmap output);
}
