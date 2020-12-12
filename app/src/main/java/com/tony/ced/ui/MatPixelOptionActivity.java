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
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @ProjectName: tony_ai
 * @Package: com.tony.ced.ui
 * @ClassName: MatPixelOptionActivity
 * @Description: Mat像素操作
 * @Author: wei.yang
 * @CreateDate: 2020/12/11 19:27
 * @UpdateUser: 更新者：wei.yang
 * @UpdateDate: 2020/12/11 19:27
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class MatPixelOptionActivity extends BaseActivity {
    @BindView(R.id.btnSelectImage)
    Button btnSelectImage;
    @BindView(R.id.ivImage)
    ImageView ivImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mat_pixel_option);
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
        datas.add("操作单个像素，并将图像像素取反");
        datas.add("一次操作一列像素，并增加图片亮度");
        datas.add("一次操作所有像素，并减小图像亮度");
        show(datas);
    }

    @Override
    public void onMenuItemClick(int position) {
       switch (position){
           case 0:
               optionSinglePixel();
               break;
           case 1:
               optionRowPixels();
               break;
           case 2:
               optionAllPixels();
               break;
           case 3:
               break;
       }

    }
    /**
     * @description 一次更改一个像素，并对像素取反
     * @date: 2020/12/12 10:09
     * @author: wei.yang
     */
    private void optionSinglePixel() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        //获取通道数
        int channels = target.channels();
        byte[] data = new byte[channels];
        int b = 0;
        int g = 0;
        int r = 0;
        int width = target.width();
        int height = target.height();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                //读取
                target.get(row, col, data);
                b = data[0] & 0xff;
                g = data[1] & 0xff;
                r = data[2] & 0xff;
                //修改
                b = 255 - b;
                g = 255 - g;
                r = 255 - r;
                //写入
                data[0] = (byte) b;
                data[1] = (byte) g;
                data[2] = (byte) r;
                target.put(row, col, data);
            }
        }
        Utils.matToBitmap(target, bitmap);
        ivImage.setImageBitmap(bitmap);

    }

    /**
     * @description 一次更改一列像素，并调节图像亮度（增加亮度）
     * @date: 2020/12/12 10:09
     * @author: wei.yang
     */
    private void optionRowPixels() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        int width = target.width();
        int height = target.height();
        //获取通道数
        int channels = target.channels();
        byte[] data = new byte[channels * width];
        int pv = 0;
        for (int row = 0; row < height; row++) {
            target.get(row, 0, data);
            for (int col = 0; col < data.length; col++) {
                //读取
                pv = data[col] & 0xff;
                //修改
                pv += 50;
                if (pv > 255) {
                    pv = 255;
                }
//                pv =255-pv;
                data[col] = (byte) pv;
            }
            //写入
            target.put(row, 0, data);
        }
        Utils.matToBitmap(target, bitmap);
        ivImage.setImageBitmap(bitmap);
    }
     /**
      * @description 一次性更改所有像素，并调节图像亮度（减小亮度）
      * @date: 2020/12/12 10:09
      * @author: wei.yang
      */
    private void optionAllPixels() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        Mat target = new Mat();
        Utils.bitmapToMat(bitmap, target);
        int width = target.width();
        int height = target.height();
        //获取通道数
        int channels = target.channels();
        byte[] data = new byte[channels * width * height];
        target.get(0, 0, data);
        int pv = 0;
        for (int i = 0; i < data.length; i++) {
            pv = data[i] & 0xff;
            pv -= 50;
            if (pv < 0) {
                pv = 0;
            }
            data[i] = (byte) pv;
        }
        target.put(0, 0, data);
        Utils.matToBitmap(target, bitmap);
        ivImage.setImageBitmap(bitmap);
    }
     /**
      * @description 图像通道分离
      * @date: 2020/12/12 10:20
      * @author: wei.yang
      */

}
