package com.tony.ced.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tony.ced.BaseActivity;
import com.tony.ced.R;
import com.tony.ced.local.NativeUtils;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
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
 * @ClassName: MaskCoveringActivity
 * @Description: 面具覆盖
 * @Author: wei.yang
 * @CreateDate: 2020/12/29 16:56
 * @UpdateUser: 更新者：wei.yang
 * @UpdateDate: 2020/12/29 16:56
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class MaskCoveringActivity extends BaseActivity {
    @BindView(R.id.btnSelectMenu)
    Button btnMenu;
    @BindView(R.id.ivImage)
    ImageView ivImage;
    @BindView(R.id.tvOpenCvVersion)
    TextView tvVersion;
    static {
        System.loadLibrary("native-lib");
    }
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
        String version = NativeUtils.getInstance().getOpenCVVersion();
        tvVersion.setText("OpenCV的版本号："+version);
    }

    private void selectMenu() {
        List<String> datas = new ArrayList<>();
        datas.add("面具覆盖");
        show(datas);
    }

    @Override
    public void onMenuItemClick(int position) {
        super.onMenuItemClick(position);
        switch (position) {
            case 0://卡通画滤镜
//                showImage(getMaskCovering());
                showGray();
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
     * @description 给脸部覆盖面具
     * @date: 2020/12/29 16:58
     * @author: wei.yang
     */
    private Mat getMaskCovering() {
        Mat src = getSrc();//输入图像

        Mat dst = new Mat();//输出图像
        return dst;
    }

    private void showGray(){
        Bitmap inputBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.lena);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap.getWidth(),inputBitmap.getWidth(), Bitmap.Config.ARGB_8888);
        NativeUtils.getInstance().getComicImage(inputBitmap,outputBitmap);
        ivImage.setImageBitmap(outputBitmap);
    }

}
