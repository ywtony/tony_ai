package com.tony.ced.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.tony.ced.BaseActivity;
import com.tony.ced.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @ProjectName: tony_ai
 * @Package: com.tony.ced.ui
 * @ClassName: BitmapOptionActivity
 * @Description: 像素级别操作Bitmap
 * @Author: wei.yang
 * @CreateDate: 2020/12/9 16:00
 * @UpdateUser: 更新者：wei.yang
 * @UpdateDate: 2020/12/9 16:00
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class BitmapOptionActivity extends BaseActivity {
    @BindView(R.id.btnRedAndWritePixelSingle)
    Button btnRedAndWritePixelSingle;
    @BindView(R.id.btnRedAndWritePixelAll)
    Button btnRedAndWritePixelAll;
    @BindView(R.id.ivShowImage)
    ImageView ivShowImage;
    @BindView(R.id.btnCropImage)
    Button btnCropImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap_option);
        ButterKnife.bind(this);
        initViews();
    }


    private void initViews() {
        btnRedAndWritePixelSingle.setOnClickListener(v -> singlePixel());
        btnRedAndWritePixelAll.setOnClickListener(v -> {
//            allPixel();
            cropImage();
        });
        btnCropImage.setOnClickListener(v->cropImage());
    }

    private void singlePixel() {
        //将mipmap中的图片加载为bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //
        int a = 0;
        int r = 0;
        int g = 0;
        int b = 0;
        //其实这个地方就是在遍历一个二维数组并且分别取出其中的每个像素值
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                //读取像素
                int pixel = bitmap.getPixel(col, row);
                //分别取出像素中的每个颜色通道
                a = Color.alpha(pixel);
                r = Color.red(pixel);
                g = Color.green(pixel);
                b = Color.blue(pixel);
                //取反修改像素值
                r = 255 - r;
                g = 255 - g;
                b = 255 - b;
                //将每个像素点重新保存到bitmap中
                newBitmap.setPixel(col, row, Color.argb(a, r, g, b));
            }
        }
        ivShowImage.setImageBitmap(newBitmap);
        bitmap.recycle();
    }

    private void allPixel() {
        try {
            //将mipmap中的图片加载为bitmap
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] pixels = new int[width * height];
            int a = 0, r = 0, g = 0, b = 0;
            int index = 0;
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    //读取像素
                    index = width * row + col;
                    a = (pixels[index] >> 24) & 0xff;
                    r = (pixels[index] >> 16) & 0xff;
                    g = (pixels[index] >> 8) & 0xff;
                    b = pixels[index] & 0xff;

                    a = 255 - a;
                    r = 255 - r;
                    g = 255 - g;
                    b = 255 - b;
                    //保存到新的bitmap中
                    pixels[index] = (a << 24) | (r << 16) | (g << 8) | b;

                }
            }
            Bitmap newBitmap = Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.ARGB_8888);
            ivShowImage.setImageBitmap(newBitmap);
            bitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     /**
      * @description 对Bitmap进行裁剪
      * @date: 2020/12/10 9:50
      * @author: wei.yang
      */
    private void cropImage(){
        //将mipmap中的图片加载为bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl5);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height/2];
        bitmap.getPixels(pixels,0,width,0,0,width,height/2);
        Bitmap newBitmap = Bitmap.createBitmap(pixels, 0, width, width, height/2, Bitmap.Config.ARGB_8888);
        ivShowImage.setImageBitmap(newBitmap);
        bitmap.recycle();
    }
}
