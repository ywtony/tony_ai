package com.tony.ced.ui;

import android.content.Context;
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
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @ProjectName: tony_ai
 * @Package: com.tony.ced.ui
 * @ClassName: FeatureTestingMatchingActivity
 * @Description: 特征检测与匹配
 * @Author: wei.yang
 * @CreateDate: 2020/12/22 9:15
 * @UpdateUser: 更新者：wei.yang
 * @UpdateDate: 2020/12/22 9:15
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class FeatureTestingMatchingActivity extends BaseActivity {
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
        datas.add("角点检测并绘制角点");
        datas.add("角点加检测与绘制2");
        datas.add("测试一下人脸检测");
        show(datas);
    }

    @Override
    public void onMenuItemClick(int position) {
        super.onMenuItemClick(position);
        switch (position) {
            case 0://角点检测与绘制角点
                toHarrisCorner();
                break;
            case 1://焦点检测与绘制2
                toShiTomasi();
                break;
            case 2://测试一下人脸检测
                toFaceMatching();
                break;


        }
    }

    /**
     * 角点：即极值点，在某方面属性特别突出的点
     * 角点检测的基本原理：是对图像求导，对每个像素点生成二阶梯度图像，只是在卷积核使用的时候要使用高斯核
     * <p>
     * 角点检测API函数解释如下：
     * cornerHarris(Mat src,Mat dst,int blockSize,int ksize,double k)
     * src:输入图像，单通道的8位或浮点数图像
     * dst: 输入每个像素点的响应值，是CV_32F类型，大小与输入图像一致
     * blockSize:根据特征值与特征向量计算矩阵M的大小，常见取值为2
     * ksize Sobel：算子梯度计算常见取值为3
     * k：系数大小，取值范围0.02~0.04
     */
    private void toHarrisCorner() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl4);
        //将Bitmap转换为mat
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat src = new Mat();
        Imgproc.cvtColor(target, src, Imgproc.COLOR_RGBA2BGR);
        //转换成灰度图像
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        //定义阈值
        int threshold = 120;
        Mat response = new Mat();
        Mat response_norm = new Mat();
        //角点检测
        Imgproc.cornerHarris(gray, response, 2, 3, 0.04);
        Core.normalize(response, response_norm, 0, 255, Core.NORM_MINMAX, CvType.CV_32F);
        Mat dst = new Mat();
        //绘制角点
        dst.create(src.size(), src.type());
        src.copyTo(dst);
        float[] data = new float[1];
        for (int i = 0; i < response_norm.rows(); i++) {
            for (int j = 0; j < response_norm.cols(); j++) {
                response_norm.get(i, j, data);
                if ((int) data[0] > threshold) {
                    Imgproc.circle(dst, new Point(j, i), 5, new Scalar(0, 0, 255), 2, 8, 0);
                }
            }
        }
        //将Mat转换为Bitmap
        Bitmap bm = Bitmap.createBitmap(dst.width(), dst.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst, bm);
        ivImage.setImageBitmap(bm);
        target.release();
        src.release();
        gray.release();
        dst.release();
    }

    /**
     * shi-tomasi角点检测
     * 相关调用API如下：
     * goodFeaturesToTrack(Mat src,MatOfPoint corners,int maxCorners,double qualityLevel,double minDistance,Mat mask,int blockSize,boolean useHarrisDetector,double k)
     * src:表示输入图像，类型为单通道8位或浮点数
     * corners:输出得到的角点数组
     * maxCorners:表示获取前N个最强响应R值得角点
     * qualityLevel:其取值范围为0～1，这里取它与最大R值相乘，得到的值作为阈值T，低于它的都要被丢弃，假设Rmax= 1500, qualityLevel = 0.01，则阈值T = 15，小于15的都会被丢弃。
     * minDistance：最终返回的角点之间的最小距离，小于这个距离则被丢弃
     * mask：默认全部为零
     * blockSize：计算矩阵M时需要的，常取值为3
     * useHarrisDetector：是否使用Harris角点检测，true表示使用，若为false则使用Shi-Tomasi角点检测。
     * k：当使用Harris角点检测的时候才使用
     */
    private void toShiTomasi() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl4);
        //将Bitmap转换为mat
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat src = new Mat();
        Imgproc.cvtColor(target, src, Imgproc.COLOR_RGBA2BGR);
        //转换成灰度图像
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        //
        double k = 0.04;
        int blockSize = 3;
        double qualityLevel = 0.01;
        boolean useHarrisCorner = false;
        //角点检测
        MatOfPoint corners = new MatOfPoint();
        Imgproc.goodFeaturesToTrack(gray, corners, 120, qualityLevel, 10, new Mat(), blockSize, useHarrisCorner, k);
        Mat dst = new Mat();
        //绘制角点
        dst.create(src.size(), src.type());
        src.copyTo(dst);
        Point[] points = corners.toArray();
        for (int i = 0; i < points.length; i++) {
            Imgproc.circle(dst, points[i], 5, new Scalar(0, 0, 255), 2, 8, 0);
        }
        //将Mat转换为Bitmap
        Bitmap bm = Bitmap.createBitmap(dst.width(), dst.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst, bm);
        ivImage.setImageBitmap(bm);
        target.release();
        src.release();
        gray.release();
        dst.release();
    }

    /**
     * ps:只有正面照才能被检测出来，其他的都是检测不出来的。
     *
     * OpenCV中的人脸检测，其是基于训练好的LBP与HAAR特征级联检测器完成的。
     * 相关调用API如下所示：
     * detectMultiScale(Mat src,MatOfRect objects,double scaleFactor,int minNeighbors,int flags,Size minSize,Size maxSize)
     * src:输入图像
     * objects:表示检测到的对象个数，返回每个对象的矩形Box坐标
     * scaleFactor:尺度变换比率，基本在1.05~1.2之间比较好
     * minNeighbors:领域范围内符合条件的对象个数，它是输出检测最终BOX的重要阈值，太大，则条件比较苛刻，容易丢失检测对象，太小，则容易导致错误检测
     * flags:OpenCV 2.x版本使用的参数，这里不需要，设置为0即可。
     * minSize：对象检测的最小范围。
     * maxSize：对象检测的最大范围。
     */
    private void toFaceMatching() {
        try {
            initFaceDetector();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.face);
        //将Bitmap转换为mat
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat src = new Mat();
        Imgproc.cvtColor(target, src, Imgproc.COLOR_RGBA2BGR);
        //转换成灰度图像
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        Mat dst = new Mat();
        MatOfRect faces = new MatOfRect();
        //人脸检测
        faceDetector.detectMultiScale(gray, faces, 1.1, 3, 0, new Size(50, 50), new Size(src.width(),src.height()));
        //绘制Box
        List<Rect> faceList = faces.toList();
        src.copyTo(dst);
        if (faceList != null && faceList.size() > 0) {
            for (Rect rect : faceList) {
                Imgproc.rectangle(dst, rect.tl(), rect.br(), new Scalar(255, 0, 0), 2, 8, 0);
            }
        }


        //将Mat转换为Bitmap
        Bitmap bm = Bitmap.createBitmap(dst.width(), dst.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst, bm);
        ivImage.setImageBitmap(bm);
        target.release();
        src.release();
        gray.release();
        dst.release();

    }

    private CascadeClassifier faceDetector = null;

    private void initFaceDetector() throws IOException {
        InputStream input = getResources().openRawResource(R.raw.lbpcascade_frontalface);
        File cascadeDir = this.getDir("cascade", Context.MODE_PRIVATE);
        File file = new File(cascadeDir.getAbsoluteFile(), "lbpcascade_frontalface.xml");
        FileOutputStream output = new FileOutputStream(file);
        byte[] buff = new byte[1024];
        int len = 0;
        while ((len = input.read(buff)) != -1) {
            output.write(buff, 0, len);
        }
        input.close();
        output.close();
        faceDetector = new CascadeClassifier(file.getAbsolutePath());
        file.delete();
        cascadeDir.delete();
    }

}