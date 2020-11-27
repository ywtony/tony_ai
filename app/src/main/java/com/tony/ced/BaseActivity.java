package com.tony.ced;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.donkingliang.imageselector.utils.ImageSelector;
import com.tony.ced.dialog.AlertSheetClickListener;
import com.tony.ced.dialog.AlertSheetDialog;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: tony_ai
 * @Package: com.tony.ced
 * @ClassName: BaseActivity
 * @Description: demo中的基类
 * @Author: wei.yang
 * @CreateDate: 2020/11/26 11:14
 * @UpdateUser: 更新者：wei.yang
 * @UpdateDate: 2020/11/26 11:14
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public abstract class BaseActivity extends FragmentActivity {
    private static int REQUEST_CODE = 100;
    protected Context context;
    private AlertSheetDialog dialog = null;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
    }

    private void initDialog(List<String> arrs) {
        dialog = new AlertSheetDialog(context, arrs)
                .setAlertSheetClickListener(new AlertSheetClickListener() {
                    @Override
                    public AlertSheetDialog onSheetItemClickListener(View view, int position) {
                        onMenuItemClick(position);
                        return null;
                    }

                    @Override
                    public void onSheetDialogDismiss() {

                    }
                });
    }

    /**
     * @description 展示弹框
     * @date: 2020/11/27 9:38
     * @author: wei.yang
     */
    public void show(List<String> choices) {
        initDialog(choices);
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    /**
     * @description 关闭弹框
     * @date: 2020/11/27 9:38
     * @author: wei.yang
     */
    public void close() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * @description 打开相册进行单选
     * @date: 2020/11/27 9:56
     * @author: wei.yang
     * @link https://github.com/donkingliang/ImageSelector
     */
    public void openPhoto() {
        //单选
        ImageSelector.builder()
                .useCamera(true) // 设置是否使用拍照
                .setSingle(true)  //设置是否单选
                .canPreview(true) //是否可以预览图片，默认为true
                .start(this, REQUEST_CODE); // 打开相册
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            //获取选择器返回的数据
            ArrayList<String> images = data.getStringArrayListExtra(
                    ImageSelector.SELECT_RESULT);
//            if (onPhotoCallback != null) {
//                onPhotoCallback.photoCallback(images);
//            }
            onPhotoDataCallback(images);
            /**
             * 是否是来自于相机拍照的图片，
             * 只有本次调用相机拍出来的照片，返回时才为true。
             * 当为true时，图片返回的结果有且只有一张图片。
             */
//            boolean isCameraImage = data.getBooleanExtra(ImageSelector.IS_CAMERA_IMAGE, false);
        }
    }

    public void onPhotoDataCallback(List<String> images) {

    }
    public void onMenuItemClick(int position){

    }
//    /**
//     * 相册选择成功后的回调
//     */
//    private OnPhotoCallback onPhotoCallback;
//    public void setOnPhotoCallback(OnPhotoCallback onPhotoCallback){
//        this.onPhotoCallback = onPhotoCallback;
//    }
//
//    public interface OnPhotoCallback {
//        void photoCallback(List<String> images);
//    }
}
