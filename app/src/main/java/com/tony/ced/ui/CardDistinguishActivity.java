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
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
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
 * @ClassName: CardDistinguish
 * @Description: 身份证识别
 * @Author: wei.yang
 * @CreateDate: 2020/12/26 14:15
 * @UpdateUser: 更新者：wei.yang
 * @UpdateDate: 2020/12/26 14:15
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class CardDistinguishActivity extends BaseActivity {
    @BindView(R.id.btnSelectMenu)
    Button btnMenu;
    @BindView(R.id.ivImage)
    ImageView ivImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_distinguish);
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
        datas.add("转换为灰度图");
        datas.add("对图像进行二值分割");
        datas.add("对二值分割后的图像进行反转");
        datas.add("执行膨胀操作");
        datas.add("发现轮廓并绘制轮廓");
        show(datas);
    }

    @Override
    public void onMenuItemClick(int position) {
        super.onMenuItemClick(position);
        switch (position) {
            case 0://转换为灰度图
                showImage(getGrayMat());
                break;
            case 1://对图像进行二值分割
                showImage(getThresholdMat());
                break;
            case 2://对二值分割后的图像进行反转，得到白字黑色背景
                showImage(getNotMat());
                break;
            case 3://执行膨胀操作
                showImage(getOpenMat());
                break;
            case 4://发现轮廓并绘制轮廓
                showImage(getFindContours());
                break;
        }
    }

    /**
     * @description 获取输入图
     * @date: 2020/12/26 14:25
     * @author: wei.yang
     */
    private Mat getSrc() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.card_id_face);
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
        Bitmap bitmap = Bitmap.createBitmap(src.width(), src.height(), Bitmap.Config.ARGB_8888);
//        Mat dst = new Mat();
//        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGR2RGBA);
        Utils.matToBitmap(src, bitmap);
        src.release();
        ivImage.setImageBitmap(bitmap);
    }

    /**
     * @description 将输入图像转换为灰度图像
     * @date: 2020/12/26 14:26
     * @author: wei.yang
     */
    private Mat getGrayMat() {
        Mat src = getSrc();
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        src.release();
        return gray;
    }

    /**
     * @description 对图像进行二值分割
     * @date: 2020/12/26 14:34
     * @author: wei.yang
     */
    private Mat getThresholdMat() {
        Mat gray = getGrayMat();
        Mat dst = new Mat();
        Imgproc.threshold(gray, dst, 0, 255, Imgproc.THRESH_OTSU);
        gray.release();
        return dst;
    }

    /**
     * @description 对图像进行反转
     * @date: 2020/12/26 14:40
     * @author: wei.yang
     */
    private Mat getNotMat() {
        Mat thresholdMat = getThresholdMat();
        Mat dst = new Mat();
        Core.bitwise_not(thresholdMat, dst);
        thresholdMat.release();
        return dst;
    }

    /**
     * @description 使用膨胀操作使文本扩张黏连.因为膨胀操作可以使元素更厚重
     * @date: 2020/12/26 14:43
     * @author: wei.yang
     */
    private Mat getOpenMat() {
        Mat src = getNotMat();
        Mat dst = new Mat();
        //创建结构元素,此处结构元素的大小代表膨胀的程度，实验下来的结果是，结构元素越大则膨胀程度越大，对象黏连的约紧密（可使用7x7膨胀5此，也可以使用3x3膨胀12此，测试图片中的身份证号就会黏连）
        Mat kernal = Imgproc.getStructuringElement(Imgproc.MORPH_CROSS, new Size(3, 3));
        //执行膨胀操作
        Imgproc.dilate(src, dst, kernal, new Point(-1, -1), 12);
        src.release();
        kernal.release();
        return dst;
    }

    /**
     * @description 执行轮廓发现操作
     * @date: 2020/12/26 14:52
     * @author: wei.yang
     */
    private Mat getFindContours() {
        Mat src = getOpenMat();
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(src, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));
        //绘制轮廓
        Mat dst = getSrc();
//        Mat dst = new Mat();
//        dst.create(src.size(), src.type());
        for (int i = 0; i < contours.size(); i++) {
            Rect rect = Imgproc.boundingRect(contours.get(i));
            double w = rect.width;
            double h = rect.height;
            double rate = Math.min(w, h) / Math.max(w, h);
            Log.e("BoundRect ", "rate:" + rate);
            if (w / h > 5) {
//                Imgproc.drawContours(dst, contours, i, new Scalar(255, 0, 0), 2);
                //找到并截取身份证区域
                dst = new Mat(dst, rect);
            }

        }
        return dst;
    }

}
