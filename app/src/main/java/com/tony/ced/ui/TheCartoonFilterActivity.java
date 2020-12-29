package com.tony.ced.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.tony.ced.BaseActivity;
import com.tony.ced.R;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @ProjectName: tony_ai
 * @Package: com.tony.ced.ui
 * @ClassName: TheCartoonFilterActivity
 * @Description: 实现卡通画滤镜，此案例并不能应用
 * @Author: wei.yang
 * @CreateDate: 2020/12/26 15:31
 * @UpdateUser: 更新者：wei.yang
 * @UpdateDate: 2020/12/26 15:31
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class TheCartoonFilterActivity extends BaseActivity {
    @BindView(R.id.btnSelectMenu)
    Button btnMenu;
    @BindView(R.id.ivImage)
    ImageView ivImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_cartoon_filter);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        btnMenu.setOnClickListener(v -> {
            selectMenu();
        });
    }

    private void selectMenu() {
        List<String> datas = new ArrayList<>();
        datas.add("卡通画滤镜");
        show(datas);
    }

    @Override
    public void onMenuItemClick(int position) {
        super.onMenuItemClick(position);
        switch (position) {
            case 0://卡通画滤镜
                showImage(getScaleEdges());
                break;
        }
    }

    /**
     * @description 获取输入图
     * @date: 2020/12/26 14:25
     * @author: wei.yang
     */
    private Mat getSrc() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.lena);
        //将bitmap转换为mat对象
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap, dst);
        //将RGBA色彩空间转换为BGR色彩控件
        Mat dst2 = new Mat();
        Imgproc.cvtColor(dst, dst2, Imgproc.COLOR_RGBA2BGR);
        dst.release();
        return dst2;
    }

    /**
     * @description 展示图片
     * @date: 2020/12/26 14:28
     * @author: wei.yang
     */
    private void showImage(Mat src) {
        Mat dst = new Mat(src.size(), src.type());
        //在getSrc方法中把RGBA色彩空间转换为了BGR，这里需要再转换回来，即把BGR色彩空间转换为RGBA色彩空间，图片才能够正常的显示
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGR2RGBA);
        Bitmap bitmap = Bitmap.createBitmap(src.width(), src.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst, bitmap);
        src.release();
        dst.release();
        ivImage.setImageBitmap(bitmap);
    }

    /**
     * @description 获取均值滤波后的mat
     * @date: 2020/12/26 15:35
     * @author: wei.yang
     */
    private Mat getBlurMat() {
        Mat src = getSrc();
        Mat dst = new Mat();
        Imgproc.blur(src, dst, new Size(7, 7));
        src.release();
        return dst;
    }

    /**
     * @description 获取边缘检测后的Mat
     * @date: 2020/12/26 15:36
     * @author: wei.yang
     */
    private Mat getCannyMat() {
        Mat src = getBlurMat();
        Mat dst = new Mat();
        Imgproc.Canny(src, dst, 50, 150);
        src.release();
        return dst;
    }

    /**
     * @description 获取膨胀后的Mat对象
     * @date: 2020/12/26 15:38
     * @author: wei.yang
     */
    private Mat getDilateMat() {
        Mat src = getCannyMat();
        Mat dst = new Mat();
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 2));
        Imgproc.dilate(src, dst, kernel);
        src.release();
        kernel.release();
        return dst;
    }

    private Mat getScaleEdges() {
        Mat imgCanny = getDilateMat();
        //对imgCanny先除法再减法
        Mat src2 = Mat.zeros(imgCanny.size(), imgCanny.type());
        src2.setTo(new Scalar(255, 255, 255));
        Core.divide(imgCanny, src2, imgCanny);
        //做减法
        Mat src3 = Mat.zeros(imgCanny.size(), imgCanny.type());
        src3.setTo(new Scalar(1, 1, 1));
        Core.subtract(src3, imgCanny, imgCanny);
        //将Canny8位无符号像素格式转换为浮点矩阵
        Mat imgCannyF = new Mat();
        imgCanny.convertTo(imgCannyF, CvType.CV_32FC3);
        //均值模糊滤镜
        Imgproc.blur(imgCannyF, imgCannyF, new Size(5, 5));
        Mat imgBF = new Mat(imgCannyF.size(), imgCannyF.type());
        Imgproc.bilateralFilter(getSrc(), imgBF, 9, 150, 150);
        //
        Mat result = Mat.zeros(imgBF.size(), imgBF.type());
        result.setTo(new Scalar(25, 25, 25));
        Core.divide(imgBF, result, result);
        //
        Mat result2 = Mat.zeros(imgBF.size(), imgBF.type());
        result2.setTo(new Scalar(25, 25, 25));
        Core.multiply(result, result2, result2);
        //合并颜色和边缘结果
        Mat imgCanny3c = new Mat();
        List<Mat> mv = new ArrayList<>();
        mv.add(imgCannyF);
        mv.add(imgCannyF);
        mv.add(imgCannyF);
        Core.merge(mv, imgCanny3c);
        //把颜色图像转换为32位结果图像
        Mat resultF = new Mat();
        result2.convertTo(resultF, CvType.CV_32FC3);
        //
        Core.multiply(resultF, imgCanny3c, resultF);
        //将图像转换为8位
        Mat result3 = new Mat();
        resultF.convertTo(result3, CvType.CV_8UC3);
        return result3;
    }

}
