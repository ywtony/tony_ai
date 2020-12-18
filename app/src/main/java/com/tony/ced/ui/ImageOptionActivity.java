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
      *
      * bilateral(Mat src,Mat dst,int d,double sigmaColor,double sigmaSpace)
      * src:输入图像
      * dst:高斯双边滤波后的输出图像
      * d:表示过滤的卷积核直径大小,一般取0意思是从sigmaColor中计算得到
      * sigmaColor:颜色权重计算时需要的参数
      * sigmaSpace:控件权重计算时需要的参数
      *
      * 通常情况下：sigmaColor的取值范围再100~150左右，sigmaSpace的取值范围再10~25之间的时候，双边滤波的效果比较好，计算速度也比较快
      *
      */
    private void toBilateralFilter(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);

        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat dst = new Mat();
        //这行代码必须要加上，Bitmap默认的颜色通道时RGBA，需要转换为OpenCv可以识别的BGR。不然就会报通道数异常
        Imgproc.cvtColor(target,dst,Imgproc.COLOR_RGBA2BGR);

        Mat dst2 = new Mat();
        //高斯双边滤波，用于给图片美化
        Imgproc.bilateralFilter(dst, dst2, 0,150d,15d);
        Utils.matToBitmap(dst2, bitmap);
        ivImage.setImageBitmap(bitmap);
        target.release();
        dst.release();
        dst2.release();
    }
}
