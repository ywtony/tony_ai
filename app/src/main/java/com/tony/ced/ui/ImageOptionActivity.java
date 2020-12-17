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
        show(datas);
    }

    @Override
    public void onMenuItemClick(int position) {
        super.onMenuItemClick(position);
        switch (position){
            case 0://均值模糊
                toBlur();
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
     *
     */
    private void toBlur(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        Mat dst = new Mat();
        Imgproc.blur(target,dst,new Size(5,5),new Point(-1,-1),Core.BORDER_DEFAULT);
        Utils.matToBitmap(dst, bitmap);
        ivImage.setImageBitmap(bitmap);
        target.release();
        dst.release();
    }
}
