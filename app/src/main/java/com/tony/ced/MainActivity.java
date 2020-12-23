package com.tony.ced;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.tony.ced.adapter.MainAdapter;
import com.tony.ced.adapter.listener.OnListItemClickListener;
import com.tony.ced.bean.MainItem;
import com.tony.ced.ui.BaseFeaturesTestingActivity;
import com.tony.ced.ui.BitmapOptionActivity;
import com.tony.ced.ui.CameraTestingActivity;
import com.tony.ced.ui.FeatureTestingMatchingActivity;
import com.tony.ced.ui.GrayImageActivity;
import com.tony.ced.ui.ImageOptionActivity;
import com.tony.ced.ui.MapAndBitmapActivity;
import com.tony.ced.ui.MatOptionActivity;
import com.tony.ced.ui.MatPixelOptionActivity;
import com.tony.ced.utils.ActivityUtils;
import com.tony.ced.utils.ToastUtil;
import com.tony.ced.weight.ScrollSpeedLinearLayoutManger;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * OpenCV的应用
 * 一、卫星地图与电子地图拼接
 * 二、医学中图像噪声处理、对象检测
 * 三、安防监控领域安全与入侵检测、自动监视报警
 * 四、制造业与工业中的产品质量检测、摄像机锁定
 * 五、军事领域无人机飞行、无人驾驶与水下机器人
 * 六、农业领域自动采摘等
 * <p>
 * <p>
 * OpenCV中的核心功能模块：
 * 1.运动估算
 * 2.人脸识别
 * 3.姿势识别
 * 4.人机交互
 * 5.运动理解
 * 6.对象检测
 * 7.移动机器人
 * 8.分割与识别
 * 9.视频分析
 * 10.运动跟踪
 * 11.图像处理
 * 12.机器学习
 * 13.深度神经网络
 */


/**
 * 基本概念：亮度、对比度、饱和度、锐化、分辨率
 * <p>
 * 亮度：一般指图像的明暗程度，对于数字图像，如果灰度值再[0~255]之间，则约接近0亮度越低，约接近255则亮度越高。
 * 对比度：指的是最高与最低灰度级之间的灰度差。
 * 饱和度：指的是图像中颜色种类的多少，其对具有鲜艳颜色、颜色丰富的图像影响很大。
 * 锐化：图像锐化是指补偿图像轮廓，增强图像边缘及灰度跳变部分，使图像变得清晰。
 * 分辨率：图像分辨率指图像中存储的信息量，是每英寸图像内有多少个像素点。
 */
public class MainActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private MainAdapter adapter = null;
    private List<MainItem> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        initData();
        adapter = new MainAdapter(context, datas, (OnListItemClickListener<MainItem>) (data, position) -> {
            ActivityUtils.getInstance().startActivity(MainActivity.this, data.getClassName());
        });
        recyclerView = findViewById(R.id.recyclerView);
        initRecycleView();
        recyclerView.setAdapter(adapter);
        initLoadOpenCv();
    }

    /**
     * @description 初始化数据
     * @date: 2020/11/26 11:12
     * @author: wei.yang
     */
    private void initData() {
        datas.add(new MainItem("将任意图像转换为灰度图像", GrayImageActivity.class.getName()));
        datas.add(new MainItem("Bitmap和Mat", MapAndBitmapActivity.class.getName()));
        datas.add(new MainItem("操作像素", BitmapOptionActivity.class.getName()));
        datas.add(new MainItem("控制Mat绘制基本的几何形状", MatOptionActivity.class.getName()));
        datas.add(new MainItem("操作Mat像素", MatPixelOptionActivity.class.getName()));
        datas.add(new MainItem("操作Mat像素", ImageOptionActivity.class.getName()));
        datas.add(new MainItem("图像基本特征检测", BaseFeaturesTestingActivity.class.getName()));
        datas.add(new MainItem("图像特征检测与匹配", FeatureTestingMatchingActivity.class.getName()));
        datas.add(new MainItem("相机的使用", CameraTestingActivity.class.getName()));

    }

    /**
     * @description 初始化RecycleView
     * @date: 2020/10/26 14:49
     * @author: wei.yang
     */
    private void initRecycleView() {
        recyclerView.setLayoutManager(new ScrollSpeedLinearLayoutManger(context));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.bg_main_line));
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void initLoadOpenCv() {
        boolean success = OpenCVLoader.initDebug();
        if (success) {
            ToastUtil.show("初始化成功");
        } else {
            ToastUtil.show("初始化失败");
        }
    }


}
