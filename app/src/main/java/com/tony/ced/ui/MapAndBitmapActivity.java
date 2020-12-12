package com.tony.ced.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.tony.ced.BaseActivity;
import com.tony.ced.R;
import com.tony.ced.utils.BitmapUtil;
import com.tony.ced.utils.ToastUtil;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @ProjectName: tony_ai
 * @Package: com.tony.ced.ui
 * @ClassName: MapAndBitmapActivity
 * @Description: 测试Mat与Bitmap
 * @Author: wei.yang
 * @CreateDate: 2020/11/27 10:12
 * @UpdateUser: 更新者：wei.yang
 * @UpdateDate: 2020/11/27 10:12
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class MapAndBitmapActivity extends BaseActivity {
    @BindView(R.id.btnClickMenu)
    Button btnClickMenu;
    @BindView(R.id.btnLoadImage)
    Button btnLoadImage;
    @BindView(R.id.ivOrigin)
    ImageView ivOrigin;
    @BindView(R.id.ivChange)
    ImageView ivChange;
    private List<String> datas = new ArrayList<>();
    private List<String> images = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_and_bitmap);
        ButterKnife.bind(this);
        btnClickMenu.setOnClickListener(v -> {
            datas.clear();
            datas.add("从相册中加载Mat并显示到页面上");
            datas.add("创建Mat");
            show(datas);
        });
        btnLoadImage.setOnClickListener(v -> {
            openPhoto();
        });


    }

    /**
     * @description 选择图像后的回调
     * @date: 2020/11/27 10:13
     * @author: wei.yang
     */
    @Override
    public void onPhotoDataCallback(List<String> images) {
        this.images = images;
        if (images != null && images.size() > 0) {
            ivOrigin.setImageBitmap(BitmapFactory.decodeFile(images.get(0)));
        }
    }

    @Override
    public void onMenuItemClick(int position) {

//        if (images != null && images.size() > 0) {
            switch (position) {
                case 0:
                    getMatBaseInfo(images.get(0));
                    break;
                case 1:
                    ToastUtil.show(position+"sss");
                    createMat();
                    break;
            }
//        }

    }

    /**
     * @description 获取Mat对象的基本信息
     * @date: 2020/11/27 11:29
     * @author: wei.yang
     * Imgcodecs.IMREAD_GRAYSCALE 加载灰度图片
     * Imgcodes.IMREAD_COLOR 加载彩色图片
     * Imgcodes.ImREAD_UNCHANGED 不改变加载图片的类型
     */
    private void getMatBaseInfo(String path) {
        //从系统相册中选中一张图片并生成一个Bitmap
        Mat mat = Imgcodecs.imread(path, Imgcodecs.IMREAD_UNCHANGED);
        //初始化一个Bitmap
        Bitmap bitmap = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888);
        //将Mat对象转换为Bitmap
        Utils.matToBitmap(mat, bitmap);
        ivOrigin.setImageBitmap(bitmap);
        mat.release();
    }

    /**
     * @description 创建一个mat对象
     * @date: 2020/11/30 11:27
     * @author: wei.yang
     */
    private void createMat() {
        Mat mat = new Mat();
        mat.create(new Size(300, 300), CvType.CV_8UC3);
        //设置Mat的颜色
        mat.setTo(new Scalar(0, 198, 255));
        //创建一个Bitmap
        Bitmap bitmap = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888);
        //将Mat转换为Bitmap
        Utils.matToBitmap(mat, bitmap);
        ivOrigin.setImageBitmap(bitmap);
        mat.release();

    }
}
