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
