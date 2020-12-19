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
import org.opencv.core.Point;
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
 * @ClassName: ImageOptionActivity
 * @Description: 图像操作
 * @Author: wei.yang
 * @CreateDate: 2020/12/16 10:40
 * @UpdateUser: 更新者：wei.yang
 * @UpdateDate: 2020/12/16 10:40
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class ImageOptionActivity extends BaseActivity {
    @BindView(R.id.btnSelectImage)
    Button btnSelectImage;
    @BindView(R.id.ivImage)
    ImageView ivImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_option);
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
        datas.add("均值模糊");
        datas.add("高斯模糊");
        datas.add("中值滤波");
        datas.add("最大值滤波");
        datas.add("最小值滤波");
        datas.add("高斯双边滤波");
        datas.add("金字塔均值迁移滤波");
        datas.add("自定义均值模糊卷积核");
        datas.add("自定义近似高斯模糊卷积核");
        datas.add("自定义锐化算子");
        datas.add("自定义x方向梯度算子");
        datas.add("自定义y方向的梯度算子");
        datas.add("膨胀");
        datas.add("腐蚀");
        datas.add("开操作");
        datas.add("闭操作");
        datas.add("黑帽");
        datas.add("顶帽");
        datas.add("形态学梯度");
        datas.add("自定义阈值分割");
        datas.add("自适应阈值分割");
        show(datas);
    }

    @Override
    public void onMenuItemClick(int position) {
        super.onMenuItemClick(position);
        switch (position) {
            case 0://均值模糊
                toBlur();
                break;
            case 1://高斯模糊
                toGaussianBlur();
                break;
            case 2://中值滤波
                toMedianBlur();
                break;
            case 3://最大值滤波
                toDilateBlur();
                break;
            case 4://最小值滤波
                toErodeBlur();
                break;
            case 5://高斯双边滤波
                toBilateralFilter();
                break;
            case 6://金字塔均值迁移滤波
                toPyrMeanShiftFiltering();
                break;
            case 7://自定义均值模糊卷积核
                toFilter2DBlur();
                break;
            case 8://自定义近似高斯模糊卷积核
                toFilter2DMeansBlur();
                break;
            case 9://自定义锐化算子
                toFilter2DRuiHua();
                break;
            case 10://自定义x方向梯度算子
                toFilter2DRobert(0);
                break;
            case 11://自定义y方向梯度算子
                toFilter2DRobert(1);
                break;
            case 12://膨胀
                toMorphologyDemo(0);
                break;
            case 13://腐蚀
                toMorphologyDemo(1);
                break;
            case 14://开操作
                toMorphologyDemo(2);
                break;
            case 15://闭操作
                toMorphologyDemo(3);
                break;
            case 16://黑帽
                toMorphologyDemo(4);
                break;
            case 17://顶帽
                toMorphologyDemo(5);
                break;
            case 18://形态学梯度
                toMorphologyDemo(6);
                break;
            case 19://自定义阈值分割
                toThresholdBinnary(0);
                break;
            case 20://自适应阈值分割
                toAdaptiveThreshold(0);
                break;
        }
    }

    /**
     * 均值模糊：基于相同系数的卷积核完成的卷积操作称为均值模糊。均值模糊的主要作用是：1.降低图片噪声 2.模糊图像 3.减低图像的对比度
     * blur(Mat src,Mat dst,Size ksize,Point anchor,int borderType)函数参数解释:
     * src:输入图像
     * dst:卷积模糊后的输出图像
     * ksize:图像卷积核大小
     * anchor:卷积核的中心位置
     * borderType:边缘填充类型，默认情况下是BORDER_DEFAULT
     */
    private void toBlur() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat dst = new Mat();
        Imgproc.blur(target, dst, new Size(5, 5), new Point(-1, -1), Core.BORDER_DEFAULT);
        Utils.matToBitmap(dst, bitmap);
        ivImage.setImageBitmap(bitmap);
        target.release();
        dst.release();
    }

    /**
     * 高斯模糊：根据空间相对位置的不同，，卷积核中每个系数具有不同的系数（权重），因为我们经常使用高斯正态分布图的方式生成权重系数，所以这种模糊又称为高斯模糊
     * 对于整个卷积核来说，越是在卷积核的中心地方，其系数应该越高，越接近边缘的地方，其系数应该越低，由此即可得知基于高斯的卷积核权重与空间位置之间的关系。
     * GuassianBlur(Mat src,Mat dst,Size ksize,double sigmaX,double sigmaY,int borderType)
     * src:表示输入图像
     * dst:高斯模糊后的输出图像
     * ksize：表示卷积核的大小，当ksize=new Size(0,0) 的时候表示从sigmaY计算得到
     * sigmaX:  x方向的模糊程度
     * sigmaY:y方向的模糊程度,当啊sigmaY不填写的时候表示从sigmaX计算得到
     * borderType:表示边缘填充方式，默认为BORDER_DEFAULT
     * <p>
     * 高斯滤波常用于图像的预处理，能够起到很好的抑制噪声的作用，使用他们选择的卷积核大小通常为，3x3 ,5x5
     * <p>
     * 卷积核系数越大，模糊程度越高
     */
    private void toGaussianBlur() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat dst = new Mat();
        Imgproc.GaussianBlur(target, dst, new Size(5, 5), 0);
        Utils.matToBitmap(dst, bitmap);
        ivImage.setImageBitmap(bitmap);
        target.release();
        dst.release();
    }

    /**
     * 统计排序滤波：中值滤波、最大值与最小值滤波
     * 经常用来消除图像噪声或者抑制图像像素极小值与极大值
     */
    /**
     * @description 中值滤波：同样也需要一个卷积核，与卷积滤波不同的是，其不会用卷积核的每个系数与对应像素值做算数运算，而是把对应的像素做排序，取中间值作为输出。
     * @date: 2020/12/18 9:15
     * @author: wei.yang
     * <p>
     * medianBlur(Mat src,Mat dst,int ksize)
     * src：表示输入图像，当ksize为3、5的时候输入图像可以为浮点数或者整数，当ksize大于5时只能为字节类型，及CV_8UC
     * dst:表示中值滤波后的输出图像，其类型与输入图像保持一致。
     * ksize:表示模块大小，常见的是3、5，模板大小必须为奇数，而且必须大于1
     * <p>
     * <p>
     * 特点：中值滤波对图像的椒盐噪声有很好的抑制作用，是一个很好的图像降噪滤波器
     * 椒盐噪声：图片中随机出现的白点或者黑点
     */
    private void toMedianBlur() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat dst = new Mat();
        Imgproc.medianBlur(target, dst, 5);
        Utils.matToBitmap(dst, bitmap);
        ivImage.setImageBitmap(bitmap);
        target.release();
        dst.release();
    }

    /**
     * 最大值与最小值滤波：其和中值滤波非常相似，唯一不同的一点是对于排序后的像素数组，中值滤波使用中间值代替中心像素点输出。而最大值和最小值滤波采用最大值或者最小值代替中心点像素输出。
     */
    /**
     * @description 最大值滤波
     * @date: 2020/12/18 9:31
     * @author: wei.yang
     * dilate(Mat src,Mat dst,Mat kernel)//膨胀（最大值滤波）用最大值替换中心像素
     * src：输入图像
     * dst：输出图像
     * kernel：表示结构元素或卷积核，它可以是任意形状
     */
    private void toDilateBlur() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat dst = new Mat();
        //创建一个大小为3的矩形
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
        //膨胀操作
        Imgproc.dilate(target, dst, kernel);
        Utils.matToBitmap(dst, bitmap);
        ivImage.setImageBitmap(bitmap);
        target.release();
        dst.release();
    }

    /**
     * @description 最小值滤波
     * @date: 2020/12/18 9:32
     * @author: wei.yang
     * erode(Mat src,Mat dst,Mat kernel)//腐蚀（最小值滤波）用最小值替换中心像素
     * src：输入图像
     * dst：输出图像
     * kernel：表示结构元素或卷积核，它可以是任意形状
     */
    private void toErodeBlur() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat dst = new Mat();
        //创建一个3x3的矩形结构体
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
        //腐蚀操作
        Imgproc.erode(target, dst, kernel);
        Utils.matToBitmap(dst, bitmap);
        ivImage.setImageBitmap(bitmap);
        target.release();
        dst.release();
    }

    /**
     * 高斯双边滤波、金字塔均值迁移滤波：拥有人脸美化及图片美化效果
     */
    /**
     * @description 高斯双边滤波：高斯双边滤波是在高斯滤波的基础上进一步拓展与延伸出来的图像滤波方法，blur是图像均值模糊，会导致图像轮廓与边缘消失的现象，而高斯模糊会产生类似毛玻璃的效果
     * 导致边缘扩展效应明显，图像边缘细节丢失现象。双边滤波器可以很好的保留边缘的同时抑制平坦区域图像的噪声。
     * 双边滤波器能做到这些的原因在于它不想普通的高斯/卷积滤波，其不仅考虑了位置对中心像素的影响，还考虑了卷积核中像素与中心店像素之间相似程度的影响。
     * @date: 2020/12/18 11:12
     * @author: wei.yang
     * <p>
     * bilateral(Mat src,Mat dst,int d,double sigmaColor,double sigmaSpace)
     * src:输入图像
     * dst:高斯双边滤波后的输出图像
     * d:表示过滤的卷积核直径大小,一般取0意思是从sigmaColor中计算得到
     * sigmaColor:颜色权重计算时需要的参数
     * sigmaSpace:控件权重计算时需要的参数
     * <p>
     * 通常情况下：sigmaColor的取值范围再100~150左右，sigmaSpace的取值范围再10~25之间的时候，双边滤波的效果比较好，计算速度也比较快
     */
    private void toBilateralFilter() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);

        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat dst = new Mat();
        //这行代码必须要加上，Bitmap默认的颜色通道时RGBA，需要转换为OpenCv可以识别的BGR。不然就会报通道数异常
        Imgproc.cvtColor(target, dst, Imgproc.COLOR_RGBA2BGR);

        Mat dst2 = new Mat();
        //高斯双边滤波，用于给图片美化
        Imgproc.bilateralFilter(dst, dst2, 0, 150d, 15d);
        Utils.matToBitmap(dst2, bitmap);
        ivImage.setImageBitmap(bitmap);
        target.release();
        dst.release();
        dst2.release();
    }

    /**
     * 金字塔均值迁移滤波：主要是通过概率密度估算与中心点迁移的方式来实现图像的边缘保留滤波，其基本原理是通过创建大小指定的卷积核窗口，搜索
     * 并计算该窗口中心像素P(x，y)范围内所有满足条件的像素，计算他们的中心位置，然后基于新的中心位置再次计算更新，直到中心点不再变化或者两次变化
     * 的距离满足指定的收敛精度为止。
     * <p>
     * 其除了可以做图像美化外还可以作为图像自动分割的方法之一
     * pyrMeanShiftFiltering(Mat src,Mat dst,double sp,double sr,int maxLevel,TermCriteria termCrit）
     * src：输入图像
     * dst：输出图像
     * sp：色彩空间，也是窗口大小
     * sr：图像色彩像素范围，也是像素差范围
     * maxLevel：金字塔层数，当maxLevel大于0时，金字塔层数为Level+1
     * termCrit：表示循环或者迭代停止条件
     */
    private void toPyrMeanShiftFiltering() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);

        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat dst = new Mat();
        //这行代码必须要加上，Bitmap默认的颜色通道时RGBA，需要转换为OpenCv可以识别的BGR。不然就会报通道数异常
        Imgproc.cvtColor(target, dst, Imgproc.COLOR_RGBA2BGR);

        Mat dst2 = new Mat();
        //金字塔均值迁移滤波
        Imgproc.pyrMeanShiftFiltering(dst, dst2, 10, 50);
        Utils.matToBitmap(dst2, bitmap);
        ivImage.setImageBitmap(bitmap);
        target.release();
        dst.release();
        dst2.release();
    }


    /**
     * 什么是图像锐化？：图像锐化是指补偿图像轮廓，增强图像边缘及灰度跳变的部分，使图像变的更加清晰。
     * 什么是图像梯度？：图像梯度其实就是二维离散函数求导。其作用
     *
     *
     */
    /**
     * @description 自定义均值模糊：
     * @date: 2020/12/19 10:38
     * @author: wei.yang
     * filter2D(Mat src,Mat dst,int ddepth,Mat kernel)
     * src:输入图像
     * dst:输出图像
     * ddepth:图像深度，-1表示与输入图像一致即可
     * kernel:表示自定义卷积核
     */
    private void toFilter2DBlur() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat dst = new Mat();
        //这行代码必须要加上，Bitmap默认的颜色通道时RGBA，需要转换为OpenCv可以识别的BGR。不然就会报通道数异常
        Imgproc.cvtColor(target, dst, Imgproc.COLOR_RGBA2BGR);
        Mat dst2 = new Mat();
        //自定义均值模糊卷积核
        Mat k = new Mat(3, 3, CvType.CV_32FC1);
        float[] data = new float[]{
                1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f,
                1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f,
                1.0f / 9.0f, 1.0f / 9.0f, 1.0f / 9.0f,
        };
        k.put(0, 0, data);
        Imgproc.filter2D(dst, dst2, -1, k);
        Utils.matToBitmap(dst2, bitmap);
        ivImage.setImageBitmap(bitmap);
        target.release();
        dst.release();
        dst2.release();
    }

    /**
     * @description 自定近似高斯模糊卷积核
     * @date: 2020/12/19 10:47
     * @author: wei.yang
     */
    private void toFilter2DMeansBlur() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat dst = new Mat();
        //这行代码必须要加上，Bitmap默认的颜色通道时RGBA，需要转换为OpenCv可以识别的BGR。不然就会报通道数异常
        Imgproc.cvtColor(target, dst, Imgproc.COLOR_RGBA2BGR);
        Mat dst2 = new Mat();
        //自定义均值模糊卷积核
        Mat k = new Mat(3, 3, CvType.CV_32FC1);
        float[] data = new float[]{
                0, 1.0f / 8.0f, 0,
                1.0f / 8.0f, 0.5f, 1.0f / 8.0f,
                0, 1.0f / 8.0f, 0,
        };
        k.put(0, 0, data);
        Imgproc.filter2D(dst, dst2, -1, k);
        Utils.matToBitmap(dst2, bitmap);
        ivImage.setImageBitmap(bitmap);
        target.release();
        dst.release();
        dst2.release();
    }

    /**
     * 图像的锐化可以提高图像的对比度，轻微去模糊，提升图片质量
     * 下面通过自定义实现锐化算子
     */
    private void toFilter2DRuiHua() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat dst = new Mat();
        //这行代码必须要加上，Bitmap默认的颜色通道时RGBA，需要转换为OpenCv可以识别的BGR。不然就会报通道数异常
        Imgproc.cvtColor(target, dst, Imgproc.COLOR_RGBA2BGR);
        Mat dst2 = new Mat();
        //自定义均值模糊卷积核
        Mat k = new Mat(3, 3, CvType.CV_32FC1);
//        float[] data = new float[]{//锐化算子
//                0, -1, 0,
//                -1, 5, -1,
//                0, -1, 0,
//        };

        //强化锐化算子八邻域
        float[] data = new float[]{//锐化算子
                -1, -1, -1,
                -1, 9, -1,
                -1, -1, -1,
        };
        k.put(0, 0, data);
        Imgproc.filter2D(dst, dst2, -1, k);
        Utils.matToBitmap(dst2, bitmap);
        ivImage.setImageBitmap(bitmap);
        target.release();
        dst.release();
        dst2.release();
    }

    /**
     * 图像的边缘是图像像素变化比较大的区域，是图像特征表征的候选区之一，在图像特征提取，图像二值化等方面有很重的应用。
     * 通过自定义算子实现梯度图像是查找边缘的关键步骤之一
     */
    private void toFilter2DRobert(int type) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat dst = new Mat();
        //这行代码必须要加上，Bitmap默认的颜色通道时RGBA，需要转换为OpenCv可以识别的BGR。不然就会报通道数异常
        Imgproc.cvtColor(target, dst, Imgproc.COLOR_RGBA2BGR);
        Mat dst2 = new Mat();
        //x方向的梯度
        Mat kx = new Mat(3, 3, CvType.CV_32FC1);
        //y方向上的梯度
        Mat ky = new Mat(3, 3, CvType.CV_32FC1);
        //定义x方向上的梯度算子
        float[] robertX = new float[]{-1, 0, 0, 1};
        //定义y方向上的梯度算子
        float[] robertY = new float[]{0, -1, 1, 0};
        if (type == 0) {
            Imgproc.filter2D(dst, dst2, -1, kx);
        } else {
            Imgproc.filter2D(dst, dst2, -1, ky);
        }

        Utils.matToBitmap(dst2, bitmap);
        ivImage.setImageBitmap(bitmap);
        target.release();
        dst.release();
        dst2.release();
    }


    /**以下是形态学操作相关的内容
     *
     * OpenCv的形态学操作主要有：膨胀、腐蚀、开操作、闭操作、黑帽、顶帽、形态学梯度
     *
     *
     * ******/

    /**
     * 膨胀与腐蚀：膨胀与腐蚀是最基本的形态学操作，与卷积计算类似，其也需要一个类似卷积核的结构元素，与输入图像像素数据完成计算，腐蚀与膨胀常见的操作对象主要是二值图像
     * 或者灰度图像。
     * OpenCV中所有的形态学操作都可以扩展到彩色图像，而腐蚀与膨胀扩展到彩色图像，就像前面提到的最小值与最大值滤波
     *
     * 特点：图像噪声消除、分离出独立的图像形状与几何元素，断开或者连接相邻的元素。其中膨胀是使局部极大值替换中心点像素，腐蚀与它正好相反，是使用局部极小值替换中心像素，而
     * 局部就是指结构元素，结构元素的形状与大小，关于最终的输出结果存在内在的对应关于与联系。
     *
     * 生成结构元素的API如下
     * getStructuringElement(int shape,Size ksize,Point anchor)
     * shape:表示结构元素的形状类型
     * ksize：表示结构元素的大小
     * achor：表示结构元素中心点的位置
     *
     * 其中结构元素支持的类型如下：MORPH_RECT：矩形。MORPH_CROSS :十字交叉。MORPH_ELLIPSE：椭圆或者圆形
     *
     */

    /**
     * 开闭操作：开闭操作是基于膨胀与腐蚀组合形成的新的形态学操作，开操作有点像腐蚀操作，主要用来去除小的图像噪声或者图像元素对象黏连。开操作可以定义为一个腐蚀操作再加上一个膨胀操作
     * 两个操作采用相同的结构元素
     *
     * 闭操作：闭操作有点像膨胀操作，但是它与膨胀不同，它只会填充小的闭合区域，闭操作可以定义为一个膨胀操作再接一个腐蚀操作。
     *
     */


    /**
     * 顶帽与黑帽：是由形态学开闭操作之后的结果与原图运算得到的结果，用于在灰度图像或显微镜图像上分离比较暗或者明亮的斑点。顶帽操作表示的是输入图像与图像开操作之间的不同
     * 黑帽操作：表示的是图像闭操作于输入图像之间的不同
     *
     */

    /**
     * 梯度：图像的形态学梯度又称为基本梯度，是通过两个最基本的额形态学操作膨胀与腐蚀之间的差值得到的。其中腐蚀与膨胀操作使用的结构元素必须相同。
     */

    private void toMorphologyDemo(int option) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat dst = new Mat();
        //这行代码必须要加上，Bitmap默认的颜色通道时RGBA，需要转换为OpenCv可以识别的BGR。不然就会报通道数异常
        Imgproc.cvtColor(target, dst, Imgproc.COLOR_RGBA2BGR);
        Mat dst2 = new Mat();
        Mat k = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(15, 15), new Point(-1, -1));
        switch (option) {
            case 0://膨胀
                Imgproc.morphologyEx(dst, dst2, Imgproc.MORPH_DILATE, k);
                break;
            case 1://腐蚀
                Imgproc.morphologyEx(dst, dst2, Imgproc.MORPH_ERODE, k);
                break;
            case 2://开操作
                Imgproc.morphologyEx(dst, dst2, Imgproc.MORPH_OPEN, k);
                break;
            case 3://闭操作
                Imgproc.morphologyEx(dst, dst2, Imgproc.MORPH_CLOSE, k);
                break;
            case 4://黑帽
                Imgproc.morphologyEx(dst,dst2,Imgproc.MORPH_BLACKHAT,k);
                break;
            case 5://顶帽
                Imgproc.morphologyEx(dst, dst2, Imgproc.MORPH_TOPHAT, k);
                break;
            case 6://形态学梯度
                Imgproc.morphologyEx(dst, dst2, Imgproc.MORPH_GRADIENT, k);
                break;
        }

        Utils.matToBitmap(dst2, bitmap);
        ivImage.setImageBitmap(bitmap);
        target.release();
        dst.release();
        dst2.release();
    }


    /**
     *对于彩色图像或者灰度图像，可以设置一个或多个阈值，使用他们可以对图像像素数据进行分类。这在图像处理上有一个专门的术语叫：图像分割。
     *
     * 阈值二值化：对于图像阈值二值化，是令大于阈值T的值为最大灰度255，小于阈值T的值为最小灰度0
     *
     * 反阈值二值化：若大于阈值T则赋值等于最小灰度0，如果小于阈值T则等于最大灰度值255
     *
     * 阈值截断：如果大于阈值T则赋值等于阈值T，若小于阈值T则保持原来的不变。
     *
     * 阈值取零：如果大于阈值T，则T保持不变。若小于阈值T，则则T的像素值等于0
     *
     * 反阈值取零：如果大于阈值T，则取0。如果小于阈值T则保持不变。
     *
     * 阈值化的API及其参数解释：
     * threshold(Mat src,Mat dst,double thresh,double maxval,int typ)
     * src:输入图像
     * dst:输出图像
     * thresh：阈值T
     * maxval：最大灰度值一般为255
     * type：阈值化方法必须是上述五种方法之一，最常见的是阈值二值化
     *
     * 五种阈值化方法在OpenCV中枚举类型定义如下：
     * THRESH_BINARY = 0：阈值二值化
     * THRESH_BINARY_INV = 1：反阈值二值化
     * THRESH_TRUNC = 2：阈值截断。
     * THRESH_TOZERO = 3：阈值取零
     * THRESH_TOZERO_INV = 4：反阈值取零
     *
     *
     */
    /**
     * 阈值二值化
     */
    private void toThresholdBinnary(int option){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat dst = new Mat();
        //这行代码必须要加上，Bitmap默认的颜色通道时RGBA，需要转换为OpenCv可以识别的BGR。不然就会报通道数异常
        Imgproc.cvtColor(target, dst, Imgproc.COLOR_RGBA2BGR);
        Mat dst2 = new Mat();
        Imgproc.cvtColor(dst,dst2,Imgproc.COLOR_BGR2GRAY);
        Mat dst3 = new Mat();


        switch (option) {
            case 0://阈值二值化
                Imgproc.threshold(dst2,dst3,80,255,Imgproc.THRESH_BINARY);
                break;
            case 1://反阈值二值化
                Imgproc.threshold(dst2,dst3,150,255,Imgproc.THRESH_BINARY_INV);
                break;
            case 2://阈值截断
                Imgproc.threshold(dst2,dst3,150,255,Imgproc.THRESH_TRUNC);
                break;
            case 3://阈值取零
                Imgproc.threshold(dst2,dst3,150,255,Imgproc.THRESH_TOZERO);
                break;
            case 4://反阈值取零
                Imgproc.threshold(dst2,dst3,150,255,Imgproc.THRESH_TOZERO_INV);
                break;
        }

        Utils.matToBitmap(dst3, bitmap);
        ivImage.setImageBitmap(bitmap);
        target.release();
        dst.release();
        dst2.release();
        dst3.release();
    }

    /**
     * OpenCV支持的两种全局自动计算阈值方法分别为OTSU与Triangle，这两种方法都是以图像直方图统计数据为基础来自动计算阈值的。
     * 此外，OpenCV还有两种自适应阈值分割方法，它们是基于局部图像自动计算阈值的方法
     *
     * OTSU:假设阈值为T，将直方图数据分割为两个部分，计算它们的类内方差与类间方差，最终最小类内方差或者最大类间方差对应的灰度值就是要计算得到的阈值T
     *
     * Triangle:三角阈值法是对得到的直方图数据寻找最大峰值，从最大峰值得到垂直45°方向的三角形，计算最大斜边到直方图的距离d，对应的直方图灰度值即为图像阈值T
     *
     * 自适应阈值:自适应阈值计算方法有C均值与高斯C均值两种ADAPTIVE_THRESH_MEAN_C、ADAPTIVE_THRESH_GAUSSIAN_C
     * 相关API解释如下：
     * adaptiveThreshold(Mat src, Mat dst,double maxValue,int adaptiveMethod,int thresholdType,int blockSize,double c)
     * src:输入图像
     * dst：输出图像
     * maxValue：最大灰度值，通常为255
     * adaptiveMethod:自适应方法，C均值或高斯C均值
     * thresholdType:阈值化方法，五种阈值化分割之一，常见的阈值化方法为：THRESH_Binary
     * blockSize：分块大小，必须为奇数
     * C:常量数值，阈值化的时候使用计算得到阈值+C之后做阈值化分割
     *
     */
    private void toAdaptiveThreshold(int type){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat dst = new Mat();
        //这行代码必须要加上，Bitmap默认的颜色通道时RGBA，需要转换为OpenCv可以识别的BGR。不然就会报通道数异常
        Imgproc.cvtColor(target, dst, Imgproc.COLOR_RGBA2BGR);
        Mat dst2 = new Mat();
        Imgproc.cvtColor(dst,dst2,Imgproc.COLOR_BGR2GRAY);
        Mat dst3 = new Mat();


        switch (type) {
            case 0://自动阈值二值化
                Imgproc.adaptiveThreshold(dst2,dst3,255,Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,Imgproc.THRESH_BINARY,15,0);
                break;
            case 1://自动反阈值二值化
                Imgproc.adaptiveThreshold(dst2,dst3,255,Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,Imgproc.THRESH_BINARY_INV,15,0);
                break;
            case 2://自动阈值截断
                Imgproc.adaptiveThreshold(dst2,dst3,255,Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,Imgproc.THRESH_TRUNC,15,0);
                break;
            case 3://自动阈值取零
                Imgproc.adaptiveThreshold(dst2,dst3,255,Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,Imgproc.THRESH_TOZERO,15,0);
                break;
            case 4://自动反阈值取零
                Imgproc.adaptiveThreshold(dst2,dst3,255,Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,Imgproc.THRESH_TOZERO_INV,15,0);
                break;
        }

        Utils.matToBitmap(dst3, bitmap);
        ivImage.setImageBitmap(bitmap);
        target.release();
        dst.release();
        dst2.release();
        dst3.release();
    }

}
