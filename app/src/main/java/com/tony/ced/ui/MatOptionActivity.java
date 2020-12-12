package com.tony.ced.ui;

import android.graphics.Bitmap;
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
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @ProjectName: tony_ai
 * @Package: com.tony.ced.ui
 * @ClassName: MatOptionActivity
 * @Description: OpenCV 绘制基本几何形状
 * @Author: wei.yang
 * @CreateDate: 2020/12/10 9:55
 * @UpdateUser: 更新者：wei.yang
 * @UpdateDate: 2020/12/10 9:55
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class MatOptionActivity extends BaseActivity {
    @BindView(R.id.btnLine)
    Button btnLine;
    @BindView(R.id.btnRectangle)
    Button btnRectangle;
    @BindView(R.id.btnCircle)
    Button btnCircle;
    @BindView(R.id.btnEllipse)
    Button btnEllipse;
    @BindView(R.id.ivMatContainer)
    ImageView ivMatContainer;
    @BindView(R.id.btnText)
    Button btnText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_option);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        btnLine.setOnClickListener(v -> {
            drawLine();
        });
        btnRectangle.setOnClickListener(v -> {
            drawRectangle();
        });
        btnCircle.setOnClickListener(v -> {
            drawCircle();
        });
        btnEllipse.setOnClickListener(v -> {
            drawEllipse();
        });
        btnText.setOnClickListener(v -> drawText());
    }

    /**
     * 绘制线
     */
    private void drawLine() {
        Mat src = Mat.zeros(new Size(500, 500), CvType.CV_8UC3);
        //绘制线
        Imgproc.line(src, new Point(10, 10), new Point(490, 490), new Scalar(0, 255, 0), 280);
        Imgproc.line(src, new Point(10, 490), new Point(490, 10), new Scalar(255, 0, 0), 2, 8, 0);
        Bitmap bitmap = Bitmap.createBitmap(src.width(), src.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(src, bitmap);
        ivMatContainer.setImageBitmap(bitmap);
        src.release();

    }

    private void drawRectangle() {
        Mat src = Mat.zeros(new Size(500, 500), CvType.CV_8UC3);
        //绘制矩形
        Rect rect = new Rect();
        rect.x = 50;
        rect.y = 50;
        rect.width = 100;
        rect.height = 100;
        Imgproc.rectangle(src, rect.tl(), rect.br(), new Scalar(255, 0, 0), 2, 8, 0);
        Bitmap bitmap = Bitmap.createBitmap(src.width(), src.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(src, bitmap);
        ivMatContainer.setImageBitmap(bitmap);
        src.release();
    }

    private void drawCircle() {
        Mat src = Mat.zeros(new Size(500, 500), CvType.CV_8UC3);
        //绘制圆形
        Imgproc.circle(src, new Point(400, 400), 50, new Scalar(0, 255, 0), 2, 8, 0);
        Bitmap bitmap = Bitmap.createBitmap(src.width(), src.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(src, bitmap);
        ivMatContainer.setImageBitmap(bitmap);
        src.release();
    }

    private void drawEllipse() {
        Mat src = Mat.zeros(new Size(500, 500), CvType.CV_8UC3);
        //绘制椭圆或者弧长
        Imgproc.ellipse(src, new Point(250, 250), new Size(100, 50), 360, 0, 360, new Scalar(0, 0, 255), 2, 8, 0);
        Bitmap bitmap = Bitmap.createBitmap(src.width(), src.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(src, bitmap);
        ivMatContainer.setImageBitmap(bitmap);
        src.release();
    }

    private void drawText() {
        Mat src = Mat.zeros(new Size(500, 500), CvType.CV_8UC3);
        //绘制文本
        Imgproc.putText(src, "Open CV Draw Demo", new Point(20, 20), Core.FONT_HERSHEY_PLAIN, 1.0, new Scalar(255, 0, 0), 2);
        Bitmap bitmap = Bitmap.createBitmap(src.width(), src.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(src, bitmap);
        ivMatContainer.setImageBitmap(bitmap);
        src.release();
    }
}
