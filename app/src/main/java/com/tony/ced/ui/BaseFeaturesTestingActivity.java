package com.tony.ced.ui;

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
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
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
        datas.add("霍夫直线检测方式1");
        datas.add("霍夫直线检测方式2");
        datas.add("霍夫圆检测");
        datas.add("发现轮廓，并绘制轮廓");
        datas.add("轮廓分析-过滤较小的轮廓，保留大的轮廓");
        datas.add("绘制直方图");
        datas.add("直方图均衡化灰度图像");
        datas.add("直方图均衡化彩色图像");
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
            case 5://霍夫直线检测方式1

                toHoughLines();
                break;
            case 6://霍夫直线检测方式2
                toHoughLines2();
                break;
            case 7://霍夫圆检测
                houghCircle();
                break;
            case 8://发现轮廓并绘制轮廓
                toFindContours();
                break;
            case 9://轮廓分析，保留大的轮廓，去掉小的轮廓
                toContoursAnalysis();
                break;
            case 10://绘制直方图
                toCalcHist();
                break;
            case 11://直方图均衡化灰度图像
                toEqualizeHistGray();
                break;
            case 12://直方图均衡化彩色图像
                toEqualizeHistSrc();
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
     *
     *
     * 霍夫直线检测：
     * 在取得图像边缘的基础上，对一些特定的几何形状边缘，如直线、圆，通过图像霍夫变换把图像从平面坐标空间变换到霍夫空间坐标，
     * 就可以通过求取霍夫空间的局部极大值方法得到空间坐标对应参数方程中直线的两个参数，从而计算得到图像平面坐标中直线的数目与位置
     * 检测霍夫直线的API使用方法如下：
     * HoughLines(Mat src,Mat lines,double rho,double theta,int threshold)
     * src:输入图像,8位单通道图像，一般为二值图像
     * lines:表示输出的每个直线的极坐标参数方程的两个参数
     * rho:表示极坐标空间r值每次的步长，一般设置为1
     * theta:表示角度θ，每次移动1°即可
     * threshold:表示极坐标中该点的累计数，该累计数越大，则得到的直线就可能越长，取值范围通常是30~50，单位是像素，假设为30的话，则表示大于30个像素长度的线段才会被检测到
     *
     */
    private void toHoughLines(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.gaosugonglu);
        //将Bitmap转换为mat
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat src = new Mat();
        Imgproc.cvtColor(target, src, Imgproc.COLOR_RGBA2BGR);
        //转换成灰度图像
        Mat src2 = new Mat();
        Imgproc.cvtColor(src,src2,Imgproc.COLOR_BGR2GRAY);
        //边缘检测
        Mat dst = new Mat();
        //线进行边缘检测
        Imgproc.Canny(src2,dst,50,150,3,true);
        //霍夫直线检测
        Mat lines = new Mat();
        Imgproc.HoughLines(dst,lines,1,Math.PI/180.0,200);
        Mat out = Mat.zeros(src.size(),src.type());
        float[] data = new float[2];
        for(int i=0;i<lines.rows();i++){
            lines.get(i,0,data);
            float rho = data[0];
            float theta = data[1];
            double a = Math.cos(theta);
            double b = Math.sin(theta);
            double x0= a*rho;
            double y0 = b*rho;
            Point pt1 = new Point();
            Point pt2 = new Point();
            pt1.x  = Math.round(x0+1000*(-b));
            pt1.y = Math.round(y0+1000*a);
            pt2.x = Math.round(x0-1000*(-b));
            pt2.y = Math.round(y0-1000*a);
            Imgproc.line(out,pt1,pt2,new Scalar(0,0,255),3,Imgproc.LINE_AA,0);

        }
        Mat dst2 = new Mat();
        out.copyTo(dst2);
        target.release();
        src.release();
        src2.release();
        //将Mat转换为Bitmap
        Bitmap bm = Bitmap.createBitmap(dst2.cols(),dst2.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst2,bm);
        ivImage.setImageBitmap(bm);
        dst.release();

    }

    /**
     *
     * HoughLinesP(Mat src,Mat lines,double rho,double theta,int threshold,double minLieLength,double maxLineGap)
     *  src:输入图像，8位单通道图像，一般为二值图像
     *  lines：表示输出的每个直线的极坐标参数方程的两个参数。
     *  rho:表示极坐标空间r值每次的步长，一般设置为1
     *  theta：表示角度θ，每次移动1°即可
     *  threshold：表示极坐标中该点的累计数，该累计数越大，则得到的直线可能就越长，取值范围通常在30~50，单位是像素，假设取值为30，则表示大于30个像素长度的线段才会被检测到
     *  minLineLength:表示可以检测到的最小线段长度，根据实际需要进行设置
     *  maxLineGap：表示线段之间的最大间隔像素，假设5表示5个像素的两个相邻线段可以连接起来。
     *
     */
    private void toHoughLines2(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.gaosugonglu);
        //将Bitmap转换为mat
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat src = new Mat();
        Imgproc.cvtColor(target, src, Imgproc.COLOR_RGBA2BGR);
        //转换成灰度图像
        Mat src2 = new Mat();
        Imgproc.cvtColor(src,src2,Imgproc.COLOR_BGR2GRAY);
        //边缘检测
        Mat dst = new Mat();
        //线进行边缘检测
        Imgproc.Canny(src2,dst,50,150,3,true);
        //霍夫直线检测
        Mat lines = new Mat();
        Imgproc.HoughLinesP(dst,lines,1,Math.PI/180.0,100,50,10);
        Mat out = Mat.zeros(src.size(),src.type());
        for(int i=0;i<lines.rows();i++){
          int[] oneLine = new int[4];
          lines.get(i,0,oneLine);
          Imgproc.line(out,new Point(oneLine[0],oneLine[1]),new Point(oneLine[2],oneLine[3]),new Scalar(255,0,0),2,8,0);
        }
        Mat dst2 = new Mat();
        out.copyTo(dst2);
        target.release();
        src.release();
        src2.release();
        //将Mat转换为Bitmap
        Bitmap bm = Bitmap.createBitmap(dst2.cols(),dst2.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst2,bm);
        ivImage.setImageBitmap(bm);
        dst.release();

    }

    /**
     *
     *
     * HoughCircles(Mat src,Mat circles,int method,double dp,double minDist,double param1,double param2,int minRadius,int maxRadius)
     * src:8位单通道灰度图像
     * circles：输出的三个向量的数组，圆心与半径（x, y, r）
     * method：唯一支持的方法就是基于梯度霍夫变换——HOUGH_GRADIENT。
     * dp：图像分辨率，注意dp越大，图像就会相应减小分辨率；当dp等于1时，其跟原图的大小一致；当dp=2时，其为原图的一半。
     * minDist：表示区分两个圆的圆心之间最小的距离，如果两个圆之间的距离小于给定的minDist，则认为是同一个圆，这个参数对霍夫圆检测来说非常有用，可以帮助降低噪声影响。
     * param1：边缘检测Canny算法中使用的高阈值
     * param2：累加器阈值，值越大，说明越有可能是圆
     * param2：累加器阈值，值越大，说明越有可能是圆
     * minRadius：检测的最小圆半径，单位为像素
     * maxRadius：检测的最大圆半径，单位为像素
     *
     */
    private void houghCircle(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.yingbi);
        //将Bitmap转换为mat
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat src = new Mat();
        Imgproc.cvtColor(target, src, Imgproc.COLOR_RGBA2BGR);
        Imgproc.pyrMeanShiftFiltering(src,src,15,80);


        //转换成灰度图像
        Mat src2 = new Mat();
        Imgproc.cvtColor(src,src2,Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(src2,src2,new Size(3,3),0);
        //
        Mat circles = new Mat();




        //边缘检测
        Mat dst = new Mat();
        dst.create(src.size(),src.type());
        Imgproc.HoughCircles(src2,circles,Imgproc.HOUGH_GRADIENT,1,20,100,30,10,200);
        for(int i=0;i<circles.cols();i++){
            float[] info = new float[3];
            circles.get(0,i,info);
            Imgproc.circle(dst,new Point((int)info[0],(int)info[1]),(int)info[2],new Scalar(0,255,0),2,8,0);
        }
        circles.release();
        src2.release();

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
     * ps:此方法的运行速度超级慢
     *
     * 轮廓发现的API
     * findContours(Mat src, List<MatOfPoint> contours,Mat hierarchy,int mode,int method,Point offset)
     * src:输入图像，必须是8位单通道的图像，轮廓发现的时候该图像会被修改
     * contours：是List类型，List里面的每个元素都是一个轮廓对应的所有像素点集合。
     * hierarchy：拓扑信息，可以不填写这个参数
     * mode：返回的轮廓拓扑模式，一共有四种
     * method:描述轮廓的方法，一般是基于链式的编码
     * offset：是否有位移，默认都是各像素点没有相对原因的位置移动，所以位移默认是（0，0）
     *
     * 返回轮廓拓扑的四种模式：
     * 1.RETR_EXTERNAL = 0：表示获取最外层最大的轮廓
     * 2.RETR_LIST = 1：表示获取所有的轮廓，轮廓是按LIST队列顺序组织的。
     * 3.表示获取所有轮廓呈现的双层结构组织，第一层是外部边界，第二层是孔边界。
     * 4.RETR_TREE = 3：表示对获取的轮廓按照树形结构进行组织，显示出归属与嵌套层次。
     *
     * 链式编码的四种方式：
     * 1.CHAIN_APPROX_NONE = 1：将链式编码中的所有点都转换为点输出
     * 2.CHAIN_APPROX_SIMPLE = 2：压缩水平、垂直、倾斜部分的轮廓点输出。
     * 3.CHAIN_APPROX_TC89_L1 = 3：使用Teh-Chin链式逼近算法中的一种
     * 4.CHAIN_APPROX_TC89_KCOS = 4：使用Teh-Chin链式逼近算法中的一种。
     * 创建的上述两个参数常为RETR_EXTERNAL和CHAIN_APPROX_SIMPLE或者RETR_TREE和CHAIN_APPROX_SIMPLE。
     *
     * 轮廓绘制的API：
     * drawContours(Mat src,List<MatOfPoint> contours,int contourIdx,Scalar color,int thickness)
     * src:要绘制轮廓的图像，通常可以创建一张空白的黑色背景图像
     * contours：轮廓数据来自轮廓发现函数输出
     * contourIdx：声明绘制第几个轮廓
     * color：绘制轮廓时使用的颜色
     * thickness：声明绘制轮廓时使用的线宽,当线宽参数thickness＜0的时候表示填充该轮廓
     *
     *
     *
     */
    private void toFindContours(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl4);
        //将Bitmap转换为mat
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat src = new Mat();
        Imgproc.cvtColor(target, src, Imgproc.COLOR_RGBA2BGR);
        //转换成灰度图像
        Mat gray = new Mat();
        Imgproc.cvtColor(src,gray,Imgproc.COLOR_BGR2GRAY);
        //二值化
        Mat binary = new Mat();
        Imgproc.threshold(gray,binary,0,255,Imgproc.THRESH_BINARY|Imgproc.THRESH_OTSU);
        //轮廓发现
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(binary,contours,hierarchy,Imgproc.RETR_TREE,Imgproc.CHAIN_APPROX_SIMPLE,new Point(0,0));
        //绘制轮廓
        Mat dst = new Mat();
        dst.create(src.size(),src.type());
        for(int i=0;i<contours.size();i++){
            Imgproc.drawContours(dst,contours,i,new Scalar(255,0,0),2);
        }

        //将Mat转换为Bitmap
        Bitmap bm = Bitmap.createBitmap(dst.width(),dst.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst,bm);
        ivImage.setImageBitmap(bm);
        target.release();
        src.release();
        gray.release();
        dst.release();
    }

    /**
     * 轮廓分析
     * 边界框API说明：
     * boundingRect(MatOfPoint points)
     * points:轮廓所有点的集合对象
     *
     * 最小边界框API说明：
     * RotateRect minAreaRect(MatOfPoint2d points)
     * points:轮廓的所有点的集合
     *
     * 面积API说明：
     * contourArea(Mat contour,boolean oriented)
     * contour:轮廓所有点的集合对象
     * oriented:表示轮廓方向、当oriented=true时返回的面积是一个有符号值，默认是false返回的hi绝对值
     *
     * 周长API说明：
     * arcLength(MatOfPoint2f curve ,boolean closed)
     * curve:轮廓的所有点的集合
     * closed:表示是否是闭合曲线，默认是true
     *
     *
     */
    private void toContoursAnalysis(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl4);
        //将Bitmap转换为mat
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat src = new Mat();
        Imgproc.cvtColor(target, src, Imgproc.COLOR_RGBA2BGR);
        //转换成灰度图像
        Mat gray = new Mat();
        Imgproc.cvtColor(src,gray,Imgproc.COLOR_BGR2GRAY);
        //二值化
        Mat binary = new Mat();
        Imgproc.threshold(gray,binary,0,255,Imgproc.THRESH_BINARY|Imgproc.THRESH_OTSU);
        //轮廓发现
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(binary,contours,hierarchy,Imgproc.RETR_TREE,Imgproc.CHAIN_APPROX_SIMPLE,new Point(0,0));
        //测量轮廓
        //绘制轮廓
        Mat dst = new Mat();
        dst.create(src.size(),src.type());
        for(int i=0;i<contours.size();i++){
            Rect rect = Imgproc.boundingRect(contours.get(i));
            double w = rect.width;
            double h = rect.height;
            double rate = Math.min(w,h)/Math.max(w,h);
            Log.e("BoundRect ","rate:"+rate);
            RotatedRect minRect = Imgproc.minAreaRect(new MatOfPoint2f(contours.get(i).toArray()));
            w = minRect.size.width;
            h  = minRect.size.height;
            rate = Math.min(w,h)/Math.max(w,h);
            Log.e("Min Bound Rect","rate:"+rate);

            double area = Imgproc.contourArea(contours.get(i),false);
            double arcLength = Imgproc.arcLength(new MatOfPoint2f(contours.get(i).toArray()),true);
            Log.e("contourArea:","area:"+area);
            Log.e("arcLength","arcLength:"+arcLength);
            if(area>50){//过滤的越多，绘制的越快
                Imgproc.drawContours(dst,contours,i,new Scalar(255,0,0),2);
            }

        }

        //将Mat转换为Bitmap
        Bitmap bm = Bitmap.createBitmap(dst.width(),dst.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst,bm);
        ivImage.setImageBitmap(bm);
        target.release();
        src.release();
        gray.release();
        dst.release();
    }

    /**
     * 图像直方图：图像直方图是图像的统计学特征，是图像处理中的重要概念之一
     * 直方图数据计算的API函数如下：
     * calcHist(List<Mat> images ,MatOfInt channels,Mat mask,Mat hist,MatOfInt histSize,MatOfFloat ranges)
     * images:输入图像，类型必须相同，每个图像都拥有任意通道数据
     * channels:通道索引列表
     * mask:表示遮罩，遮罩层主要是针对输入images，如果要使用遮罩则要求输入图像大小最好相同
     * hist：计算得到直方图数据，是一维/二维稀疏矩阵
     * histSize：直方图的大小，一般指BIN个数的多少
     * rangs:直方图的取值范围，这个与输入图像有关，如果是RGB色彩空间则取值范围在0~255之间。
     *
     *
     *
     */
    private void toCalcHist(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl4);
        //将Bitmap转换为mat
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat src = new Mat();
        Imgproc.cvtColor(target, src, Imgproc.COLOR_RGBA2BGR);
        //转换成灰度图像
        Mat gray = new Mat();
        Imgproc.cvtColor(src,gray,Imgproc.COLOR_BGR2GRAY);
        //计算直方图数据并归一化
        List<Mat> images = new ArrayList<>();
        images.add(gray);
        //创建遮罩
        Mat mask=Mat.ones(src.size(),CvType.CV_8UC1);
        Mat hist = new Mat();
        //计算直方图数据
        Imgproc.calcHist(images,new MatOfInt(0),mask,hist,new MatOfInt(256),new MatOfFloat(0,255));
        Core.normalize(hist,hist,0,255,Core.NORM_MINMAX);
        int height = hist.rows();
        Mat dst = new Mat();
        dst.create(400,400,src.type());
        dst.setTo(new Scalar(200,200,200));
        float[] histData = new float[256];
        hist.get(0,0,histData);
        int offsetX = 50;
        int offsetY = 350;
        //绘制直方图
        Imgproc.line(dst,new Point(offsetX,0),new Point(offsetX,offsetY),new Scalar(0,0,0));
        Imgproc.line(dst,new Point(offsetX,offsetY),new Point(400,offsetY),new Scalar(0,0,0));
        for(int i=0;i<height-1;i++){
            int y1 = (int)histData[i];
            int y2 = (int)histData[i+1];
            Rect rect = new Rect();
            rect.x = offsetX+i;
            rect.y = offsetY-y1;
            rect.width = 1;
            rect.height = y1;
            Imgproc.rectangle(dst,rect.tl(),rect.br(),new Scalar(15,15,15));

        }



        //将Mat转换为Bitmap
        Bitmap bm = Bitmap.createBitmap(dst.width(),dst.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst,bm);
        ivImage.setImageBitmap(bm);
        target.release();
        src.release();
        gray.release();
        dst.release();
    }
    /**
     * 直方图均衡化：
     * 直方图均衡化技术常应用于摄影后期的修图中，对于室外拍摄的图像，因为光线较亮，所以需要通过直方图均值化以避免图像饱和度过高，
     * 对于在室内相对较暗的场景下拍摄的照片则需要调整亮度、对比度，以提升图像质量，直方图均衡化对它们都可以起到一定的调节作用。
     * 直方图均衡化主要是针对单通道的8位灰度图像，其灰度值范围为0～255，直方图均衡化的本质是改变图像的灰度分布，或者说改变图像直方图的灰度分布，
     * 通过累积灰度级别与相关的数学变换公式，来改变原有的图像直方图灰度分布，然后用改变之后的灰度值LUT查找方式重建图像，从而达到调整图像亮度与对比度的目的
     *
     * 直方图均衡化的API函数描述：
     * equalizeHist(Mat src,Mat dst)
     * src:输入图像，8位单通道图像
     * dst：输出图像，大小、类型与输入图像一致
     *
     *
     * 以下是直方图均衡化后的灰度图像
     */
    private void toEqualizeHistGray(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl4);
        //将Bitmap转换为mat
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat src = new Mat();
        Imgproc.cvtColor(target, src, Imgproc.COLOR_RGBA2BGR);
        //转换成灰度图像
        Mat gray = new Mat();
        Imgproc.cvtColor(src,gray,Imgproc.COLOR_BGR2GRAY);
        Mat dst = new Mat();
        //直方图均衡化函数
        Imgproc.equalizeHist(gray,dst);
        //将Mat转换为Bitmap
        Bitmap bm = Bitmap.createBitmap(dst.width(),dst.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst,bm);
        ivImage.setImageBitmap(bm);
        target.release();
        src.release();
        gray.release();
        dst.release();
    }

    /**
     * 直方图均衡化后的彩色图像.
     * ps:均衡后色彩更加鲜艳，而且此算法的运行速度比较高。完全可以作为一个单独的滤镜。
     */
    private void toEqualizeHistSrc(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl4);
        //将Bitmap转换为mat
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat src = new Mat();
        Imgproc.cvtColor(target, src, Imgproc.COLOR_RGBA2BGR);
        //彩色图像拆分通道
        List<Mat> mv = new ArrayList<>();
        Core.split(src,mv);
        //循环均衡每个单通道图像
        for(int i=0;i<mv.size();i++){
            Imgproc.equalizeHist(mv.get(i),mv.get(i));
        }
        //合并所有通道图像
        Mat dst = new Mat();
        Core.merge(mv,dst);


        //将Mat转换为Bitmap
        Bitmap bm = Bitmap.createBitmap(dst.width(),dst.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst,bm);
        ivImage.setImageBitmap(bm);
        target.release();
        src.release();
        dst.release();
    }

    /**
     * 直方图比较：
     * 方图数据是图像的基本属性之一，其归一化之后受图像大小、尺度变化的影响很小，
     * 所以可以通过比较两幅图像的直方图来反映两幅图像灰度分布的相似程度，而得到图像本身的相似程度
     *
     * 在图像辐照度或者光线稳定与分辨率不变的情况下，
     * 直方图比较是很好的相似图像判别工具之一，而且表5-1所述的7种直方图比较方法中，对两个相同的直方图数据使用不同的计算方法得到的匹配数据也会不一样，
     * 最常选择的计算方法是相关性与巴氏距离。
     *
     *
     *
     * 还有两个查找API没有练习：1.直方图方向投影 2.模板匹配
     *
     */

}
