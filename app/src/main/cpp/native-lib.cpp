#include <jni.h>
#include <stdio.h>
#include <android/log.h>
#include <android/bitmap.h>
#include <string>
#include <opencv2/opencv.hpp>
#include <opencv2/core.hpp>
#include "tony/tonyutil.hpp"

#define ASSERT(status, ret) if (!(status)) {return ret;}
#define ASSERT_FALSE(status) ASSERT(status, false)

#define LOGE(...) ((void)__android_log_print(ANDROID_LOG_ERROR, "error", __VA_ARGS__));
#define LOGD(...) ((void)__android_log_print(ANDROID_LOG_DEBUG, "debug", __VA_ARGS__));


using namespace std;
using namespace cv;
extern "C" JNIEXPORT jstring JNICALL
Java_com_tony_ced_local_NativeUtils_getOpenCVVersion(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}




extern "C"
JNIEXPORT void JNICALL
Java_com_tony_ced_local_NativeUtils_getGrayImage(JNIEnv *env, jobject thiz, jstring path,
                                                 jobject output) {
    try {
        //读取图片
        Mat mat = imread(getStringStr(env, path));
        Mat dst;
        //将图像转换为灰度图像
        cvtColor(mat, dst, COLOR_BGR2GRAY);
        matToBitmap(env, dst, output);
    } catch (...) {

    }


}
extern "C" JNIEXPORT void JNICALL
Java_com_tony_ced_local_NativeUtils_getComicImage(JNIEnv *env, jobject /* this */,
                                                  jobject input, jobject output) {
//    Mat result;
//    bool ret = bitmapToMatrix(env,input,result);
//    //将图像转化为灰度图
////    cvtColor(result,result,COLOR_BGR2RGBA);
//
//    matrixToBitmap(env,result,output);
    Mat src;
    //将bitmap转换为mat对象
    bitmapToMat(env, input, src);
    Mat dst;
    blur(src, dst, Size2i(10, 10));
    //转换为java矩阵并更新
    //将mat对象转换为bitmap对象
    matToBitmap(env, dst, output);
    format(src, Formatter::FMT_PYTHON);
}

extern "C" JNIEXPORT void JNICALL
Java_com_tony_ced_local_NativeUtils_pixelNegation(JNIEnv *env, jobject /* this */,
                                                  jobject input, jobject output) {
    Mat src;
    //将Bitmap转换为Mat对象
    bitmapToMat(env, input, src);
    Mat result(src.size(), src.type());
//    bitwise_not(src, result);//用api执行像素取反
    //像素取反
    int channels = src.channels();//获取通道数
    for (int row = 0; row < src.cols; row++) {
        for (int col = 0; col < src.rows; col++) {
            if (channels == 1) {//对一维色彩图像进行取反
                int gray = src.at<uchar>(row, col);
                result.at<uchar>(row, col) = 255 - gray;
            } else if (channels == 3) {
                int b = src.at<Vec4i>(row, col)[0];
                int g = src.at<Vec4i>(row, col)[1];
                int r = src.at<Vec4i>(row, col)[2];
                result.at<Vec4i>(row, col)[0] = 255 - b;
                result.at<Vec4i>(row, col)[1] = 255 - g;
                result.at<Vec4i>(row, col)[2] = 255 - r;
            }
        }
    }
    //释放src资源
    src.release();
    //将mat转换为Bitmap
    matToBitmap(env, result, output);
    result.release();

}



