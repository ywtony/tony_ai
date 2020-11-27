package com.tony.ced.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tony.ced.CvApplication;
import com.tony.ced.R;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * @ProjectName: tony_ai
 * @Package: com.tony.ced.utils
 * @ClassName: BitmapUtil
 * @Description: bitmap工具类
 * @Author: wei.yang
 * @CreateDate: 2020/11/26 11:38
 * @UpdateUser: 更新者：wei.yang
 * @UpdateDate: 2020/11/26 11:38
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class BitmapUtil {
    private BitmapUtil() {

    }

    private static BitmapUtil instance;

    public static BitmapUtil getInstance() {
        if (instance == null) {
            return new BitmapUtil();
        }
        return instance;
    }

    /**
     * @param resource 资源图片ID
     * @description 传入一张资源图片，生成一个灰度Bitmap并返回
     * @date: 2020/11/26 11:43
     * @author: wei.yang
     */
    public Bitmap getGrayBitmap(int resource) {
        Bitmap bitmap = BitmapFactory.decodeResource(CvApplication.getInstance().getResources(), resource);
        Mat src = new Mat();
        Mat dst = new Mat();
        //把bitmap转换为mat
        Utils.bitmapToMat(bitmap, src);
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGRA2GRAY);
        Utils.matToBitmap(dst, bitmap);
        //释放资源
        src.release();
        dst.release();
        return bitmap;
    }

    public Bitmap getGrayBitmap(Bitmap bitmap) {
        Mat src = new Mat();
        Mat dst = new Mat();
        //把bitmap转换为mat
        Utils.bitmapToMat(bitmap, src);
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGRA2GRAY);
        Utils.matToBitmap(dst, bitmap);
        //释放资源
        src.release();
        dst.release();
        return bitmap;
    }
}
