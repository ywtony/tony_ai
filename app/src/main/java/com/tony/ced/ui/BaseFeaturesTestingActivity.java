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
import org.opencv.core.Size;
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
        datas.add("拉普拉斯算子");
        datas.add("使用Canny进行边缘检测");
        datas.add("基于两个已计算出来的梯度进行边缘检测");
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
            case 2://拉普拉斯算子
                toLaplacian();
                break;
            case 3://使用Canny进行边缘检测
                toCanny();
                break;
            case 4://基于两个已计算出来的梯度进行边缘检测
                toCanny2();
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

    /**
     * 二阶导数算子：拉普拉斯算子
     * 图像边缘再二阶导数的作用下，其周围具有极大值或极小值。
     * 特点：拉普拉斯算子，既可以用于图像增强，同时也可以用于边缘检测，当它作用于边缘检测的时候稍微有点麻烦，首先是边缘的二阶导数会过零点，但是平坦区域也是0，因此
     * 会导致无法区分边缘与平坦区域，这个时候往往首先通过一阶导数把平坦区域过滤掉，之后再来做，这样就会得到边缘区域。
     *
     * 拉普拉斯算子API定义如下：
     * Laplacian(Mat src,Mat dst,int ddepth,int ksize,double scale,double delta)
     * src:输入图像
     * dst：输出图像
     * ddepth：图像深度，常见的是CV_32F
     * ksize：最常见的3*3，ksize=3
     * scale:是否缩放，默认scale=1
     * delta：是否调整像素，默认delta=0
     * ps：最后三个参数可以忽略，不用填写
     */
    private void toLaplacian(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl4);
        //将Bitmap转换为mat
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat src = new Mat();
        Imgproc.cvtColor(target, src, Imgproc.COLOR_RGBA2BGR);
        //转换成灰度图像
        Mat src2 = new Mat();
        Imgproc.cvtColor(src,src2,Imgproc.COLOR_BGR2GRAY);
        Mat dst = new Mat();
        //拉普拉斯算子计算梯度
        Imgproc.Laplacian(src2,dst, CvType.CV_32F,3,1.0,0);
        Core.convertScaleAbs(dst,dst);

        target.release();
        src.release();
        src2.release();
        //将Mat转换为Bitmap
        Bitmap bm = Bitmap.createBitmap(dst.cols(),dst.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst,bm);
        ivImage.setImageBitmap(bm);
        dst.release();
    }

    /**
     * 边缘检测：Canny边缘检测法是一种对噪声比较敏感的边缘检测方法，所以在Canny边缘检测之前，首先对图像进行降噪。
     * 图像降噪方法有：均值滤波、高斯滤波、中值滤波、最大值最小值滤波。一般情况下会首先选用高斯滤波来完成噪声抑制，因为多数噪声都是来源于自然界的随机噪声，高斯模糊对他们有不同程度的抑制作用
     * Canny边缘检测最大的一个创新来源于其使用两个阈值尝试把所有的边缘像素链接起来，形成边缘曲线或者线段。
     *
     * 完成边缘检测需要如下步骤：
     * 1.高斯模糊：完成噪声抑制
     * 2.灰度转换：在灰度图像上计算梯度值
     * 3.计算梯度：使用Sobel或Scharr
     * 4.非最大信号抑制：在梯度图像上寻找局部最大值
     * 5.高低阈值链接：把边缘像素链接成线段，形成完整边缘轮廓
     *
     * Canny推荐的高低阈值比在2:1到3:1之间，首先使用低阈值，把低于阈值边缘的像素点都去掉，然后保留所有高于阈值的像素点，对于高阈值于低阈值之间的像素点，如果从高阈值
     * 像素点出发，经过的所有像素点都高于低阈值，则保留这些像素，否则丢弃。
     *
     * OpenCV中的Canny边缘检测函数已经包含了上述5步骤。相关的API函数如下所示：
     * Canny(Mat src,Mat dst,double threshold1,double threshold2,int apertureSize,boolean L2gradient)
     * src:输入图像
     * dst:表示输出的二值边缘图像
     * threshold1:表示低阈值T1
     * threshold2:表示高阈值T2
     * apertureSize:用于计算内部梯度Sobel
     * l2gradient:计算图像梯度的计算方法
     */
    private void toCanny(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl4);
        //将Bitmap转换为mat
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat src = new Mat();
        Imgproc.cvtColor(target, src, Imgproc.COLOR_RGBA2BGR);
        //使用高斯滤波进行过滤
        Imgproc.GaussianBlur(src,src,new Size(3,3),0);
        //转换成灰度图像
        Mat src2 = new Mat();
        Imgproc.cvtColor(src,src2,Imgproc.COLOR_BGR2GRAY);
        Mat dst = new Mat();
        //进行边缘检测
        Imgproc.Canny(src2,dst,50,150,3,true);
//        Core.bitwise_and(src,src,src2,dst);//不用关注这一行
        target.release();
        src.release();
        src2.release();
        //将Mat转换为Bitmap
        Bitmap bm = Bitmap.createBitmap(dst.cols(),dst.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst,bm);
        ivImage.setImageBitmap(bm);
        dst.release();
    }

    /**
     * OpenCV还支持从两个已经计算出来的X方向梯度图像与Y方向梯度图像去检测图像边缘
     * Canny(Mat dx,Mat dy,Mat deges,double threshold1,double threshold2)
     * dx:表示x方向的梯度图像
     * dy：表示y方向上的梯度图像
     * edges:表示输出的二值边缘图像
     * threshold1:表示低阈值T1
     * threshold2：表示高阈值T2
     */
    private void toCanny2(){
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
        Imgproc.Sobel(src2,gradX, CvType.CV_16S,1,0);
        //计算y方向上的梯度
        Mat gradY = new Mat();
        Imgproc.Sobel(src2,gradY,CvType.CV_16S,0,1);
        //边缘检测
        Mat dst = new Mat();
        Imgproc.Canny(gradX,gradY,dst,50,150);
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
     * 霍夫直线检测：
     */
    private void toHoughLines(){

    }
}
