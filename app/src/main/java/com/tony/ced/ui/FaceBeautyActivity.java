package com.tony.ced.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.tony.ced.BaseActivity;
import com.tony.ced.R;
import com.tony.ced.utils.DefaultSkinFinder;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @ProjectName: tony_ai
 * @Package: com.tony.ced.ui
 * @ClassName: FaceBeautyActivity
 * @Description: 人脸美颜
 * @Author: wei.yang
 * @CreateDate: 2020/12/28 17:48
 * @UpdateUser: 更新者：wei.yang
 * @UpdateDate: 2020/12/28 17:48
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class FaceBeautyActivity extends BaseActivity {
    private float sigma = 30.0f;
    @BindView(R.id.btnSelectMenu)
    Button btnMenu;
    @BindView(R.id.ivImage)
    ImageView ivImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebeauty);
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
        datas.add("使用积分图实现图像快速模糊");
        datas.add("展示遮罩");
        datas.add("局部均方差滤波");
        datas.add("图像权重融合");
        datas.add("美颜最终效果");
        show(datas);
    }

    @Override
    public void onMenuItemClick(int position) {
        super.onMenuItemClick(position);
        switch (position) {
            case 0://使用积分图实现图像快速模糊
                showImage(userIntegralImageRealizationBlur());
                break;
            case 1://展示遮罩层
                showImage(showMask());
                break;
            case 2://局部均方差滤波
                showImage(showFastEPFilter());
                break;
            case 3://图像权重融合
                showImage(showBlendImage());
                break;
            case 4://美颜最终效果
//                showImage(showFaceBeauty());
                test();
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
        Mat dst = new Mat();
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGR2RGBA);
        Bitmap bitmap = Bitmap.createBitmap(src.width(), src.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst, bitmap);
        src.release();
        dst.release();
        ivImage.setImageBitmap(bitmap);
    }

    /**
     * 这个模糊效果的运算速度还是比较慢的。
     *
     * @description 使用积分图快速实现模糊
     * @date: 2020/12/28 17:56
     * @author: wei.yang
     */
    private Mat userIntegralImageRealizationBlur() {
        Mat src = getSrc();
        Mat dst = new Mat(src.size(), src.type());
        //积分图（和表）
        Mat sum = new Mat();
        Imgproc.integral(src, sum, CvType.CV_32S);

        int w = src.cols();
        int h = src.rows();
        int x1 = 0, y1 = 0;
        int x2 = 0, y2 = 0;
        int kSize = 15;
        int radius = kSize / 2;
        int ch = src.channels();
        //存储图像数据
        byte[] data = new byte[w * h * ch];
        //
        int[] tl = new int[3];
        int[] tr = new int[3];
        int[] bl = new int[3];
        int[] br = new int[3];
        //
        int cx = 0;
        int cy = 0;
        for (int row = 0; row < h + radius; row++) {
            y2 = (row + 1) > h ? h : (row + 1);
            y1 = (row - kSize) < 0 ? 0 : (row - kSize);
            for (int col = 0; col < w + radius; col++) {
                x2 = (col + 1) > w ? w : (col + 1);
                x1 = (col - kSize) < 0 ? 0 : (col - kSize);
                sum.get(y1, x1, tl);
                sum.get(y2, x1, tr);
                sum.get(y1, x2, bl);
                sum.get(y2, x2, br);
                cx = (col - radius) < 0 ? 0 : (col - radius);
                cy = (row - radius) < 0 ? 0 : (row - radius);
                for (int i = 0; i < ch; i++) {
                    int num = (x2 - x1) * (y2 - y1);
                    int x = (br[i] - bl[i] - tr[i] + tl[i]) / num;
                    data[cy * ch * w + cx * ch + i] = (byte) x;
                }

            }
        }
        dst.put(0, 0, data);
        src.release();
        return dst;

    }

    /**
     * @description 生成遮罩
     * @date: 2020/12/28 19:19
     * @author: wei.yang
     */
    private void getMask(Mat src, Mat mask) {
        int w = src.cols();
        int h = src.rows();
        byte[] data = new byte[3];
        Mat ycrcb = new Mat();
        DefaultSkinFinder skinFinder = new DefaultSkinFinder();
        Imgproc.cvtColor(src, ycrcb, Imgproc.COLOR_BGR2YCrCb);
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                ycrcb.get(row, col, data);
                int y = data[0] & 0xff;
                int cr = data[1] & 0xff;
                int cb = data[2] & 0xff;
                if (skinFinder.yCrCbSkin(y, cr, cb)) {
                    mask.put(row, col, new byte[]{(byte) 255});
                }
            }
        }
        ycrcb.release();
    }

    /**
     * @description 局部均方差滤波
     * @date: 2020/12/28 19:21
     * @author: wei.yang
     */
    private void FastEPFilter(Mat src, int[] sum, float[] sqsum, Mat dst) {
        int w = src.cols();
        int h = src.rows();
        int x2 = 0, y2 = 0;
        int x1 = 0, y1 = 0;
        int ksize = 15;
        int radius = ksize / 2;
        int ch = src.channels();
        byte[] data = new byte[ch * w * h];
        src.get(0, 0, data);
        int cx = 0, cy = 0;
        float sigma2 = sigma * sigma;
        for (int row = radius; row < h + radius; row++) {
            y2 = (row + 1) > h ? h : (row + 1);
            y1 = (row - ksize) < 0 ? 0 : (row - ksize);
            for (int col = 0; col < w + radius; col++) {
                x2 = (col + 1) > w ? w : (col + 1);
                x1 = (col - ksize) < 0 ? 0 : (col - ksize);
                cx = (col - radius) < 0 ? 0 : col - radius;
                cy = (row - radius) < 0 ? 0 : row - radius;
                int num = (x2 - x1) * (y2 - y1);
                for (int i = 0; i < ch; i++) {
                    int s = getblockMean(sum, x1, y1, x2, y2, i, w + 1);
                    float var = getblockSqrt(sqsum, x1, y1, x2, y2, i, w + 1);

                    // 计算系数K
                    float dr = (var - (s * s) / num) / num;
                    float mean = s / num;
                    float kr = dr / (dr + sigma2);

                    // 得到滤波后的像素值
                    int r = data[cy * ch * w + cx * ch + i] & 0xff;
                    r = (int) ((1 - kr) * mean + kr * r);
                    data[cy * ch * w + cx * ch + i] = (byte) r;
                }
            }
        }
        dst.put(0, 0, data);
    }

    private int getblockMean(int[] sum, int x1, int y1, int x2, int y2, int i, int w) {
        int tl = sum[y1 * 3 * w + x1 * 3 + i];
        int tr = sum[y2 * 3 * w + x1 * 3 + i];
        int bl = sum[y1 * 3 * w + x2 * 3 + i];
        int br = sum[y2 * 3 * w + x2 * 3 + i];
        int s = (br - bl - tr + tl);
        return s;
    }

    private float getblockSqrt(float[] sum, int x1, int y1, int x2, int y2, int i, int w) {
        float tl = sum[y1 * 3 * w + x1 * 3 + i];
        float tr = sum[y2 * 3 * w + x1 * 3 + i];
        float bl = sum[y1 * 3 * w + x2 * 3 + i];
        float br = sum[y2 * 3 * w + x2 * 3 + i];
        float var = (br - bl - tr + tl);
        return var;
    }

    /**
     * @description 图像权重融合
     * @date: 2020/12/28 19:23
     * @author: wei.yang
     */
    private void blendImage(Mat src, Mat dst, Mat mask) {
        Mat blur_mask = new Mat();
        Mat blur_mask_f = new Mat();

        // 高斯模糊
        Imgproc.GaussianBlur(mask, blur_mask, new Size(3, 3), 0.0);
        blur_mask.convertTo(blur_mask_f, CvType.CV_32F);
        Core.normalize(blur_mask_f, blur_mask_f, 1.0, 0, Core.NORM_MINMAX);

        // 获取数据
        int w = src.cols();
        int h = src.rows();
        int ch = src.channels();
        byte[] data1 = new byte[w * h * ch];
        byte[] data2 = new byte[w * h * ch];
        float[] mdata = new float[w * h];
        blur_mask_f.get(0, 0, mdata);
        src.get(0, 0, data1);
        dst.get(0, 0, data2);

        // 高斯权重混合
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                int b1 = data1[row * ch * w + col * ch] & 0xff;
                int g1 = data1[row * ch * w + col * ch + 1] & 0xff;
                int r1 = data1[row * ch * w + col * ch + 2] & 0xff;

                int b2 = data2[row * ch * w + col * ch] & 0xff;
                int g2 = data2[row * ch * w + col * ch + 1] & 0xff;
                int r2 = data2[row * ch * w + col * ch + 2] & 0xff;

                float w2 = mdata[row * w + col];
                float w1 = 1.0f - w2;

                b2 = (int) (b2 * w2 + w1 * b1);
                g2 = (int) (g2 * w2 + w1 * g1);
                r2 = (int) (r2 * w2 + w1 * r1);

                data2[row * ch * w + col * ch] = (byte) b2;
                data2[row * ch * w + col * ch + 1] = (byte) g2;
                data2[row * ch * w + col * ch + 2] = (byte) r2;
            }
        }
        dst.put(0, 0, data2);

        // 释放内存
        blur_mask.release();
        blur_mask_f.release();
        data1 = null;
        data2 = null;
        mdata = null;
    }

    /**
     * @method 边缘提升
     * @description
     * @date: 2020/12/28 19:24
     * @author: wei.yang
     */
    private void enhanceEdge(Mat src, Mat dst, Mat mask) {
        Imgproc.Canny(src, mask, 150, 300, 3, true);
        Core.bitwise_and(src, src, dst, mask);
        Imgproc.GaussianBlur(dst, dst, new Size(3, 3), 0.0);
    }

    /**
     * @description 执行美颜的最终效果呈现
     * @date: 2020/12/28 19:25
     * @author: wei.yang
     */
    private Mat showFaceBeauty() {
        Mat src = getSrc();
        Mat dst = new Mat(src.size(), src.type());
        Mat mask = new Mat(src.size(), CvType.CV_8UC1);//单通道图像
        int w = src.cols();
        int h = src.rows();
        int ch = src.channels();
        int[] data1 = new int[(w + 1) * (h + 1) * ch];
        float[] data2 = new float[(w + 1) * (h + 1) * ch];
        //生成遮罩
        getMask(src, mask);
        //局部均值方差滤波
        FastEPFilter(src, data1, data2, dst);
        //权重融合
        blendImage(src, dst, mask);
        //边缘提升
        enhanceEdge(src, dst, mask);
        return dst;
    }

    /**
     * @description 展示遮罩
     * @date: 2020/12/28 19:29
     * @author: wei.yang
     */
    private Mat showMask() {
        Mat src = getSrc();
        Mat mask = new Mat(src.size(), CvType.CV_8UC1);//单通道图像
        //生成遮罩
        getMask(src, mask);
        return mask;
    }

    /**
     * @description 局部均方差滤波
     * @date: 2020/12/28 19:30
     * @author: wei.yang
     */
    private Mat showFastEPFilter() {
        Mat src = getSrc();
        Mat dst = new Mat(src.size(), src.type());
        int w = src.cols();
        int h = src.rows();
        int ch = src.channels();
        int[] data1 = new int[(w + 1) * (h + 1) * ch];
        float[] data2 = new float[(w + 1) * (h + 1) * ch];
        //局部均值方差滤波
        FastEPFilter(src, data1, data2, dst);
        return dst;
    }

    /**
     * @description 图像权重融合
     * @date: 2020/12/28 19:31
     * @author: wei.yang
     */
    private Mat showBlendImage() {
        Mat src = getSrc();
        Mat dst = new Mat(src.size(), src.type());
        Mat mask = new Mat(src.size(), CvType.CV_8UC1);//单通道图像
        int w = src.cols();
        int h = src.rows();
        int ch = src.channels();
        int[] data1 = new int[(w + 1) * (h + 1) * ch];
        float[] data2 = new float[(w + 1) * (h + 1) * ch];
        //生成遮罩
        getMask(src, mask);
        //局部均值方差滤波
        FastEPFilter(src, data1, data2, dst);
        //权重融合
        blendImage(src, dst, mask);
        return dst;
    }


    private void test() {
        Mat src = getSrc();
        if (src.empty()) {
            return;
        }
        Mat dst = new Mat(src.size(), src.type());
        Mat mask = new Mat(src.size(), CvType.CV_8UC1);
        Mat sum = new Mat();
        Mat sqsum = new Mat();
        int w = src.cols();
        int h = src.rows();
        int ch = src.channels();
        int[] data1 = new int[(w + 1) * (h + 1) * ch];
        float[] data2 = new float[(w + 1) * (h + 1) * ch];
        Imgproc.integral2(src, sum, sqsum, CvType.CV_32S, CvType.CV_32F);
        sum.get(0, 0, data1);
        sqsum.get(0, 0, data2);
            getMask(src, mask);
            FastEPFilter(src, data1, data2, dst);
            blendImage(src, dst, mask);
            enhanceEdge(src, dst, mask);

        // 转换为Bitmap，显示
        Bitmap bm = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
        Mat result = new Mat();
        Imgproc.cvtColor(dst, result, Imgproc.COLOR_BGR2RGBA);
        Utils.matToBitmap(result, bm);

        // show
        ivImage.setImageBitmap(bm);

        // release memory
        src.release();
        dst.release();
        sum.release();
        sqsum.release();
        data1 = null;
        data2 = null;
        mask.release();
        result.release();
    }
}
