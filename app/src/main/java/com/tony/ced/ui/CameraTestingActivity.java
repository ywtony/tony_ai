package com.tony.ced.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.tony.ced.BaseActivity;
import com.tony.ced.R;
import com.tony.ced.utils.LogUtil;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.core.Core;
import org.opencv.core.Mat;

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
    private int cameraIndex = 0;//默认开启后置摄像头

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
                    return frame;
                } else {
                    Core.flip(frame, frame, 1);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.enableView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraView.disableView();
    }
}
