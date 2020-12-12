package com.tony.ced.ui;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.tony.ced.BaseActivity;
import com.tony.ced.R;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @ProjectName: tony_ai
 * @Package: com.tony.ced.ui
 * @ClassName: MatPixelOptionActivity
 * @Description: Mat像素操作
 * @Author: wei.yang
 * @CreateDate: 2020/12/11 19:27
 * @UpdateUser: 更新者：wei.yang
 * @UpdateDate: 2020/12/11 19:27
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class MatPixelOptionActivity extends BaseActivity {
    private static final String TAG = "MatPixelOptionActivityTAG";
    @BindView(R.id.btnSelectImage)
    Button btnSelectImage;
    @BindView(R.id.ivImage)
    ImageView ivImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mat_pixel_option);
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
        datas.add("操作单个像素，并将图像像素取反");
        datas.add("一次操作一列像素，并增加图片亮度");
        datas.add("一次操作所有像素，并减小图像亮度");
        datas.add("图像通道分离与合并");
        datas.add("显示通道一的数据");
        datas.add("显示通道二的数据");
        datas.add("显示通道三的数据");
        datas.add("均值与标准方差");
        show(datas);
    }

    @Override
    public void onMenuItemClick(int position) {
        switch (position) {
            case 0:
                optionSinglePixel();
                break;
            case 1:
                optionRowPixels();
                break;
            case 2:
                optionAllPixels();
                break;
            case 3:
                splitMerge();
                break;
            case 4:
                showSingleChannels(0);
                break;
            case 5:
                showSingleChannels(1);
                break;
            case 6:
                showSingleChannels(2);
                break;
            case 7:
                optionMeanStd();
                break;
        }

    }

    /**
     * @description 一次更改一个像素，并对像素取反
     * @date: 2020/12/12 10:09
     * @author: wei.yang
     */
    private void optionSinglePixel() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        //获取通道数
        int channels = target.channels();
        byte[] data = new byte[channels];
        int b = 0;
        int g = 0;
        int r = 0;
        int width = target.width();
        int height = target.height();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                //读取
                target.get(row, col, data);
                b = data[0] & 0xff;
                g = data[1] & 0xff;
                r = data[2] & 0xff;
                //修改
                b = 255 - b;
                g = 255 - g;
                r = 255 - r;
                //写入
                data[0] = (byte) b;
                data[1] = (byte) g;
                data[2] = (byte) r;
                target.put(row, col, data);
            }
        }
        Utils.matToBitmap(target, bitmap);
        ivImage.setImageBitmap(bitmap);

    }

    /**
     * @description 一次更改一列像素，并调节图像亮度（增加亮度）
     * @date: 2020/12/12 10:09
     * @author: wei.yang
     */
    private void optionRowPixels() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        int width = target.width();
        int height = target.height();
        //获取通道数
        int channels = target.channels();
        byte[] data = new byte[channels * width];
        int pv = 0;
        for (int row = 0; row < height; row++) {
            target.get(row, 0, data);
            for (int col = 0; col < data.length; col++) {
                //读取
                pv = data[col] & 0xff;
                //修改
                pv += 50;
                if (pv > 255) {
                    pv = 255;
                }
//                pv =255-pv;
                data[col] = (byte) pv;
            }
            //写入
            target.put(row, 0, data);
        }
        Utils.matToBitmap(target, bitmap);
        ivImage.setImageBitmap(bitmap);
    }

    /**
     * @description 一次性更改所有像素，并调节图像亮度（减小亮度）
     * @date: 2020/12/12 10:09
     * @author: wei.yang
     */
    private void optionAllPixels() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        int width = target.width();
        int height = target.height();
        //获取通道数
        int channels = target.channels();
        byte[] data = new byte[channels * width * height];
        target.get(0, 0, data);
        int pv = 0;
        for (int i = 0; i < data.length; i++) {
            pv = data[i] & 0xff;
            pv -= 50;
            if (pv < 0) {
                pv = 0;
            }
            data[i] = (byte) pv;
        }
        target.put(0, 0, data);
        Utils.matToBitmap(target, bitmap);
        ivImage.setImageBitmap(bitmap);
    }

    /**
     * @description 图像通道分离与合并，并减少图像的亮度
     * @date: 2020/12/12 10:20
     * @author: wei.yang
     */
    private void splitMerge() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        List<Mat> mats = new ArrayList<>();
        Core.split(target, mats);
        for (Mat mat : mats) {
            int pv = 0;
            int channels = mat.channels();
            int width = mat.width();
            int height = mat.height();
            byte[] data = new byte[channels * width * height];
            mat.get(0, 0, data);
            for (int i = 0; i < data.length; i++) {
                pv = data[i] & 0xff;
                pv = pv - 100;
                if (pv < 0) {
                    pv = 0;
                }
                data[i] = (byte) pv;

            }
            mat.put(0, 0, data);
        }
        Core.merge(mats, target);

        Utils.matToBitmap(mats.get(1), bitmap);
        ivImage.setImageBitmap(bitmap);
    }

    /**
     * @description 取单独的一个通道的图像数据并显示出来
     * @date: 2020/12/12 10:36
     * @author: wei.yang
     */
    private void showSingleChannels(int index) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        List<Mat> mats = new ArrayList<>();
        Core.split(target, mats);
        Utils.matToBitmap(mats.get(index), bitmap);
        ivImage.setImageBitmap(bitmap);
    }

    /**
     * @description 均值与标准方差 ：根据平均值可以实现基于平均值的图像二值分割，根据标准方差可以找到空白图像或者无效图像
     * ps:如果stddevsArray[0]<5 那么图像基本上可以看成是无效图像或空白图像了，因为标准方差越小说明图像各个像素的差异越小，图像携带的有效信息就越小。我们可以通过这个结论
     * 过滤质量不高的打印或扫描图像。
     * @date: 2020/12/12 10:45
     * @author: wei.yang
     */
    @SuppressLint("LongLogTag")
    private void optionMeanStd() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        //转为灰度图像
        Mat grayMat = new Mat();
        Imgproc.cvtColor(target, grayMat, Imgproc.COLOR_BGR2GRAY);
        //计算均值与标准方差
        MatOfDouble means = new MatOfDouble();
        MatOfDouble stddevs = new MatOfDouble();
        Core.meanStdDev(grayMat, means, stddevs);
        //显示均值与标准方差
        double[] meansArray = means.toArray();
        double[] stddevsArray = means.toArray();
        Log.e(TAG, "均值：" + meansArray.toString());
        Log.e(TAG, "标准方差：" + stddevsArray.toString());


        //读取像素数组
        int width = grayMat.cols();
        int height = grayMat.rows();
        byte[] data = new byte[width * height];
        grayMat.get(0, 0, data);
        int pv = 0;
        //根据均值进行二值分割
        int t = (int) meansArray[0];
        for (int i = 0; i < data.length; i++) {
            pv = data[i] & 0xff;
            if (pv > t) {
                data[i] = (byte) 255;
            } else {
                data[i] = (byte) 0;
            }
        }
        grayMat.put(0,0,data);


        Utils.matToBitmap(grayMat, bitmap);
        ivImage.setImageBitmap(bitmap);

    }
}
