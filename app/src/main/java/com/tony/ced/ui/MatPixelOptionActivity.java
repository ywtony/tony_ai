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
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        datas.add("对两个图片执行叠加操作——加法");
        datas.add("对两个图片执行叠加操作——减法");
        datas.add("对两个图片执行叠加操作——乘法");
        datas.add("对两个图片执行叠加操作——除法");
        datas.add("调整图像亮度");
        datas.add("调整图像对比度");
        datas.add("基于权重的图像混合");
        datas.add("图像取反操作");
        datas.add("图像的与操作");
        datas.add("图像或操作");
        datas.add("异或操作");
        datas.add("浮点型图像归一化处理");
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
            case 8:
                addMat();
                break;
            case 9:
                subtractMat();
                break;
            case 10:
                multiplyMat();//可能是参数配置有误，变空白页了
                break;
            case 11:
                divideMat();//可能是参数配置有误，变空白页了
                break;
            case 12://调节亮度
                brightness();
                break;
            case 13://调节对比度
                contrastRatio();
                break;
            case 14://基于权重的图像混合
                addWidget();
                break;
            case 15://图像取反操作
                bitwiseNot();
                break;
            case 16://图像与操作
                bitwiseAnd();
                break;
            case 17://图像的或操作
                bitwiseOr();
                break;
            case 18://异或操作
                bitwiseXOr();
                break;
            case 19://浮点型图像归一化处理
                normalize();
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
        grayMat.put(0, 0, data);


        Utils.matToBitmap(grayMat, bitmap);
        ivImage.setImageBitmap(bitmap);

    }

    /**
     * @description 基于图像的加法运算
     * @date: 2020/12/12 11:13
     * @author: wei.yang
     */
    private void addMat() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        //输入图像二
        Mat moon = Mat.zeros(target.rows(), target.cols(), target.type());
        int cx = target.cols() - 700;
        int cy = 150;
        Imgproc.circle(moon, new Point(cx, cy), 120, new Scalar(90, 95, 234), -1, 8, 0);
        //执行加法运算
        Mat dst = new Mat();
        Core.add(target, moon, dst);

        Utils.matToBitmap(dst, bitmap);
        ivImage.setImageBitmap(bitmap);

    }

    /**
     * @description 基于图像的减法运算
     * @date: 2020/12/12 11:13
     * @author: wei.yang
     */
    private void subtractMat() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        //输入图像二
        Mat moon = Mat.zeros(target.rows(), target.cols(), target.type());
        int cx = target.cols() - 700;
        int cy = 150;
        Imgproc.circle(moon, new Point(cx, cy), 120, new Scalar(90, 95, 234), -1, 8, 0);
        //执行加法运算
        Mat dst = new Mat();
        Core.subtract(target, moon, dst);

        Utils.matToBitmap(dst, bitmap);
        ivImage.setImageBitmap(bitmap);

    }

    /**
     * @description 乘法
     * @date: 2020/12/12 11:13
     * @author: wei.yang
     */
    private void multiplyMat() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        //输入图像二
        Mat moon = Mat.zeros(target.rows(), target.cols(), target.type());
        int cx = target.cols() - 700;
        int cy = 150;
        Imgproc.circle(moon, new Point(cx, cy), 120, new Scalar(90, 95, 234), -1, 8, 0);
        //执行加法运算
        Mat dst = new Mat();
        Core.multiply(target, moon, dst);

        Utils.matToBitmap(dst, bitmap);
        ivImage.setImageBitmap(bitmap);

    }

    /**
     * @description 除法
     * @date: 2020/12/12 11:13
     * @author: wei.yang
     */
    private void divideMat() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        //输入图像二
        Mat moon = Mat.zeros(target.rows(), target.cols(), target.type());
        int cx = target.cols() - 700;
        int cy = 150;
        Imgproc.circle(moon, new Point(cx, cy), 120, new Scalar(90, 95, 234), -1, 8, 0);
        //执行加法运算
        Mat dst = new Mat();
        Core.divide(target, moon, dst);

        Utils.matToBitmap(dst, bitmap);
        ivImage.setImageBitmap(bitmap);
    }

    /**
     * @description 调整图像的亮度：图像的亮度和对比度是图像的两个基本属性，对RGB色彩图片来说，亮度越高图像对应的像素点的RGB值应该越大，越接近255，反之越小，越接近0.所以可以通过简单
     * 的加法或者减法来起到调节图像亮度的目的。
     * @date: 2020/12/12 11:30
     * @author: wei.yang
     */
    private void brightness() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat dst = new Mat();
        Core.add(target, new Scalar(50, 50, 50), dst);

        Utils.matToBitmap(dst, bitmap);
        ivImage.setImageBitmap(bitmap);

    }

    /**
     * @description 调整图像的对比度，图像的对比度是图像的基本属性之一。其主要用来描述图像颜色与亮度之间的差异感知，对比度越大，图像每个像素与周围的差异性也就越大，整个图像的细节也就
     * 越显著，反之亦然。通过对图像进行乘法或者除法操作来扩大或者缩小图像像素之间的差值，这样达到了调节图像对比度的目的。
     * @date: 2020/12/12 11:33
     * @author: wei.yang
     */
    private void contrastRatio() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat dst = new Mat();
        Core.multiply(target, new Scalar(1.5, 1.5, 1.5), dst);


        //转换为Bitmap显示
        Bitmap bm = Bitmap.createBitmap(target.cols(),target.rows(), Bitmap.Config.ARGB_8888);
        Mat result =new Mat();
        Imgproc.cvtColor(dst,result,Imgproc.COLOR_BGR2RGBA);
        Utils.matToBitmap(result, bm);
        ivImage.setImageBitmap(bm);
    }
     /**
      * @description 基于权重的图像调节方法
      * @date: 2020/12/15 9:15
      * @author: wei.yang
      */
    private void addWidget(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat dst = new Mat();
        //创建一个纯黑色的图片
        Mat blackMat=Mat.zeros(target.size(),target.type());
        //像素混合-基于权重
        Core.addWeighted(target,1.5,blackMat,-0.5,30,dst);
        //转换为Bitmap显示
        Bitmap bm = Bitmap.createBitmap(target.cols(),target.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst, bm);
        ivImage.setImageBitmap(bm);
    }

    /**
     * 图像取反操作
     */
    private void bitwiseNot(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat dst = new Mat();
        Core.bitwise_not(target,dst);
        //转换为Bitmap显示
        Bitmap bm = Bitmap.createBitmap(target.cols(),target.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst, bm);
        ivImage.setImageBitmap(bm);
    }

    /**
     * 图像的与操作：操作两张图像让其混合，输出图像有降低混合图像亮度的效果
     */
    private void bitwiseAnd(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat target2 = Mat.zeros(target.size(),target.type());
        target2.setTo(new Scalar(255,255,255));
        Mat dst = new Mat();
        Core.bitwise_and(target,target2,dst);
        //转换为Bitmap显示
        Bitmap bm = Bitmap.createBitmap(target.cols(),target.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst, bm);
        ivImage.setImageBitmap(bm);
    }

    /**
     * 或操作，对两张混合后的输出图像， 有强化混合图像亮度的效果
     */
    private void bitwiseOr(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat target2 = Mat.zeros(target.size(),target.type());
        target2.setTo(new Scalar(255,255,255));
        Mat dst = new Mat();
        Core.bitwise_or(target,target2,dst);
        //转换为Bitmap显示
        Bitmap bm = Bitmap.createBitmap(target.cols(),target.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst, bm);
        ivImage.setImageBitmap(bm);
    }
    /**
     * 异或操作，可以看做是对输入图像的叠加取反效果
     */
    private void bitwiseXOr(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat target2 = Mat.zeros(target.size(),target.type());
        target2.setTo(new Scalar(255,255,255));
        Mat dst = new Mat();
        Core.bitwise_xor(target,target2,dst);
        //转换为Bitmap显示
        Bitmap bm = Bitmap.createBitmap(target.cols(),target.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst, bm);
        ivImage.setImageBitmap(bm);
    }

    /**
     * 将0~1的浮点数归一化处理到0~255的图像并输出
     */
    private void normalize(){
        Mat src = Mat.zeros(400,400, CvType.CV_32FC3);
        float[] data = new float[400*400*3];
        Random random = new Random();
        for(int i=0;i<data.length;i++){
            data[i] = (float)random.nextGaussian();
        }
        src.put(0,0,data);
        //将其值归一化到0~255之间
        Mat dst = new Mat();
        Mat newMat = new Mat();
        Core.normalize(src,dst,0,255,Core.NORM_MINMAX,-1,newMat);
        //类型转换
        Mat dst8u = new Mat();
        dst.convertTo(dst8u,CvType.CV_8UC3);

        Bitmap bitmap = Bitmap.createBitmap(dst8u.cols(),dst8u.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst8u,bitmap);
        ivImage.setImageBitmap(bitmap);
    }
}
