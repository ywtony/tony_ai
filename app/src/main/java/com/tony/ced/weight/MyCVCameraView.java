package com.tony.ced.weight;


import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;

import com.tony.ced.utils.LogUtil;

import org.opencv.android.JavaCameraView;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @ProjectName: tony_ai
 * @Package: com.tony.ced.weight
 * @ClassName: MyCVCameraView
 * @Description: opencv中相机预览类
 * @Author: wei.yang
 * @CreateDate: 2020/12/24 9:22
 * @UpdateUser: 更新者：wei.yang
 * @UpdateDate: 2020/12/24 9:22
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class MyCVCameraView extends JavaCameraView implements Camera.PictureCallback {
    private String imageFileName;

    public MyCVCameraView(Context context, int cameraId) {
        super(context, cameraId);
    }

    public MyCVCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        LogUtil.log("Saving a bitmap to file");
        mCamera.startPreview();
        mCamera.setPreviewCallback(this);
        try {
            FileOutputStream fos = new FileOutputStream(imageFileName);
            fos.write(data);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void takePicture(final String fileName) {
        LogUtil.log("Taking picture");
        this.imageFileName = fileName;
        System.gc();
        mCamera.setPreviewCallback(null);
        mCamera.takePicture(null, null, this);
    }
}
