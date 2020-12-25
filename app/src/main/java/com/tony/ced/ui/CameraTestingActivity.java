package com.tony.ced.ui;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.tony.ced.BaseActivity;
import com.tony.ced.R;
import com.tony.ced.utils.LogUtil;
import com.tony.ced.weight.MyCVCameraView;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @ProjectName: tony_ai
 * @Package: com.tony.ced.ui
 * @ClassName: CameraTestingActivity
 * @Description: OpenCV相机的使用
 * @Author: wei.yang
 * @CreateDate: 2020/12/23 9:19
 * @UpdateUser: 更新者：wei.yang
 * @UpdateDate: 2020/12/23 9:19
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class CameraTestingActivity extends BaseActivity {
    @BindView(R.id.cameraView)
    JavaCameraView cameraView;
    @BindView(R.id.btnStart)
    Button btnStart;
    @BindView(R.id.btnEnd)
    Button btnEnd;
    @BindView(R.id.btnPicture)
    Button btnPicture;
    @BindView(R.id.btnSelectFilter)
    Button btnSelectFilter;
    private int cameraIndex = 0;//默认开启后置摄像头
    private int option = 0;//预览特效的索引

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_testing_layout);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        cameraView.setVisibility(View.VISIBLE);
        cameraView.setCvCameraViewListener(new CameraBridgeViewBase.CvCameraViewListener2() {
            @Override
            public void onCameraViewStarted(int width, int height) {

            }

            @Override
            public void onCameraViewStopped() {

            }

            @Override
            public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {//预览的时候完成对每一帧的数据返回
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    LogUtil.log("竖屏显示");
                }
                Mat frame = inputFrame.rgba();
                if (cameraIndex == 0) {
                    //加上滤镜特效
                    process(frame);
                    return frame;
                } else {

                    Core.flip(frame, frame, 1);
                    //加上滤镜特效
                    process(frame);
                    return frame;
                }

            }
        });

//        setCameraIndex(int index)//0开启后置摄像头 1.开启前置摄像头
        //开启前置摄像头
        btnStart.setOnClickListener(v -> {
            cameraIndex = 0;
            cameraView.setCameraIndex(cameraIndex);
            cameraView.disableView();
            cameraView.enableView();//表示开始显示预览
        });
        //开启后置摄像头
        btnEnd.setOnClickListener(v -> {
            cameraIndex = 1;
            cameraView.setCameraIndex(cameraIndex);
            cameraView.disableView();
            cameraView.enableView();//表示开始显示预览
        });
        //拍照
        btnPicture.setOnClickListener(v -> {
            takePicture();
        });
        initCamera();
        btnSelectFilter.setOnClickListener(v->{
            selectFilter();
        });
    }

    private void initCamera() {
        cameraView.enableFpsMeter();
    }

    /**
     * 拍照
     */
    private void takePicture() {
        try {
            File fileDir = new File(Environment.getRootDirectory().getAbsolutePath() + "opencv/" + System.currentTimeMillis() + ".jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //默认后置摄像头开始预览
        cameraView.enableView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraView.disableView();
    }

    /**
     * @description 用于给图像的每一帧设置滤镜特效
     * @date: 2020/12/24 9:37
     * @author: wei.yang
     */
    private void process(Mat frame) {
        switch (option) {
            case 0://像素取反
                Core.bitwise_not(frame, frame);
                break;
            case 1://边缘检测
                Mat edges = new Mat();
                Imgproc.Canny(frame, edges, 100, 200, 3, false);
                Mat result = Mat.zeros(frame.size(), frame.type());
                frame.copyTo(result, edges);
                result.copyTo(frame);
                edges.release();
                result.release();
                break;
            case 2://计算图像梯度
                Mat gradx = new Mat();
                Imgproc.Sobel(frame, gradx, CvType.CV_32F, 1, 0);
                Core.convertScaleAbs(gradx, gradx);
                gradx.copyTo(frame);
                gradx.release();
                break;
            case 3://均值模糊
                Mat temp = new Mat();
                Imgproc.blur(frame, temp, new Size(15, 15));
                temp.copyTo(frame);
                temp.release();
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
        }
    }

    /**
     * @param
     * @return
     * @method 选择滤镜
     * @description
     * @date: 2020/12/24 9:43
     * @author: wei.yang
     */
    private void selectFilter() {
        List<String> datas = new ArrayList<>();
        datas.add("像素取反");
        datas.add("边缘检测");
        datas.add("计算图像梯度");
        datas.add("均值模糊");
        show(datas);
    }

    @Override
    public void onMenuItemClick(int position) {
        super.onMenuItemClick(position);
        this.option = position;
    }
}
