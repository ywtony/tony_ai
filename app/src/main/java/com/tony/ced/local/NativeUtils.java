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
     /**
      * @description 测试imread
      * @date: 2021/1/9 13:29
      * @author: wei.yang
      */
    public native void getGrayImage(String path,Bitmap ouput);


    /*************************************************第一章***************************************/
    /**
     * Mat是OpenCV中用来存储图像信息的内存对象
     * Mat中存储的有：像素、宽、高、类型、维度、大小、深度
     * 图像深度：是指存储每个像素所用到的位数，也用于度量图像的彩色分辨率.图像深度确定彩色图像每个像素可能有的颜色数  ，或者确定灰度图像每个像素可能有的灰度级数。
     * 它决定了每个彩色图像中可能出现的的最多颜色数或者灰度图像中最大的灰度等级。比如一个单色图像每个像素占8位，则最大灰度数目为2的8次方，即256。一幅彩色图像RGB3个分量
     * 像素位数分别是4,4,2，则最大颜色数目是2的4+4+2次方，即1024，也就睡说像素深度是10位，每个像素可以是1024中颜色中的一种。
     * 例如：一幅图的尺寸是1024*768，深度为16，则它的数据量为1.5M
     * 计算公式如下：1024*768*16bit=(1024*768*16)/8字节=[(1024*768*16)/8]/1024KB=={[(1024*768*16)/8]/1024}/1024MB
     *
     *
     */
     /**
      * @description 执行像素取反
      * @date: 2021/1/11 19:23
      * @author: wei.yang
      */
    public native void pixelNegation(Bitmap input ,Bitmap output);
}
