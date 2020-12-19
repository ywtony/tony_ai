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
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @ProjectName: tony_ai
 * @Package: com.tony.ced.ui
 * @ClassName: BaseFeaturesTestingActivity
 * @Description: 图像基本特征检测：包含计算图像梯度、提取边缘、检测图像中的几何图像（如：直线、圆等），查找图像中各个元素的轮廓、计算各个轮廓的周长与面积，最佳逼近外形匹配等。
 * @Author: wei.yang
 * @CreateDate: 2020/12/19 13:56
 * @UpdateUser: 更新者：wei.yang
 * @UpdateDate: 2020/12/19 13:56
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class BaseFeaturesTestingActivity extends BaseActivity {
    @BindView(R.id.btnSelectImage)
    Button btnSelectImage;
    @BindView(R.id.ivImage)
    ImageView ivImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_features_testing);
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
        datas.add("Sobel梯度算子");
        datas.add("Scharr梯度算子");
        show(datas);
    }

    @Override
    public void onMenuItemClick(int position) {
        super.onMenuItemClick(position);
        switch (position) {
            case 0://Sobel算子计算梯度
                toSobel();
                break;
            case 1://Scharr梯度算子
                toScharr();
                break;

        }
    }

    /**
     * 梯度计算：梯度可以反映出图像的像素差异，对于图像的边缘部分，梯度值会比较大，对于图像的平坦区域，梯度值一般比较小。
     * 更多的时候图像的梯度计算是基于数学离散一阶导数概念拓展与延伸的。
     * <p>
     * <p>
     * Sobel梯度算子分为X方向与Y方向，可以分别计算X方向与Y方向的梯度图像，假设有图像I，计算它的X方向与Y方向的梯度之后，再对结果进行平均权重相加就可以得到梯度图像
     * <p>
     * sobel梯度Api如下所示：
     * Sobel(Mat src,Mat dst,int ddepth,int dx,int dy)
     * src:输入图像
     * dst：输出图像
     * ddepth:表示图像的深度，常见为CV_32SC或者CV_32F
     * dx:表示计算x方向的梯度，1表示是，0表示否
     * dy:表示计算y向上的梯度，1表示计算，0表示不计算
     */
    private void toSobel() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl4);
        //将Bitmap转换为mat
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat src = new Mat();
        Imgproc.cvtColor(target, src, Imgproc.COLOR_RGBA2BGR);
        //转换成灰度图像
        Mat src2 = new Mat();
        Imgproc.cvtColor(src,src2,Imgproc.COLOR_BGR2GRAY);
        //x方向上的梯度
        Mat gradX = new Mat();
        Imgproc.Sobel(src2,gradX, CvType.CV_32F,1,0);
        Core.convertScaleAbs(gradX,gradX);
        //计算y方向上的梯度
        Mat gradY = new Mat();
        Imgproc.Sobel(src2,gradY,CvType.CV_32F,0,1);
        Core.convertScaleAbs(gradY,gradY);

        Mat dst = new Mat();
        //对结果进行平均权重相加
        Core.addWeighted(gradX,0.5,gradY,0.5,0,dst);
        target.release();
        src.release();
        src2.release();
        gradX.release();
        gradY.release();
        //将Mat转换为Bitmap
        Bitmap bm = Bitmap.createBitmap(dst.cols(),dst.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst,bm);
        ivImage.setImageBitmap(bm);
        dst.release();


    }


    /**
     * Scharr梯度：Scharr梯度是Sobel算子的升级加强版，其作用和Sobel算子类似，只是它是一种更强的梯度算子
     *
     */
    private void toScharr(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl4);
        //将Bitmap转换为mat
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat src = new Mat();
        Imgproc.cvtColor(target, src, Imgproc.COLOR_RGBA2BGR);
        //转换成灰度图像
        Mat src2 = new Mat();
        Imgproc.cvtColor(src,src2,Imgproc.COLOR_BGR2GRAY);
        //x方向上的梯度
        Mat gradX = new Mat();
        Imgproc.Scharr(src2,gradX, CvType.CV_32F,1,0);
        Core.convertScaleAbs(gradX,gradX);
        //计算y方向上的梯度
        Mat gradY = new Mat();
        Imgproc.Scharr(src2,gradY,CvType.CV_32F,0,1);
        Core.convertScaleAbs(gradY,gradY);

        Mat dst = new Mat();
        //对结果进行平均权重相加
        Core.addWeighted(gradX,0.5,gradY,0.5,0,dst);
        target.release();
        src.release();
        src2.release();
        gradX.release();
        gradY.release();
        //将Mat转换为Bitmap
        Bitmap bm = Bitmap.createBitmap(dst.cols(),dst.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst,bm);
        ivImage.setImageBitmap(bm);
        dst.release();

    }
}
