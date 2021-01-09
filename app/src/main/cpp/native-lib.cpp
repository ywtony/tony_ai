#include <jni.h>
#include <android/log.h>
#include <android/bitmap.h>
#include <string>
#include <opencv2/opencv.hpp>
#include <opencv2/core.hpp>

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


///**
// * 将Bitmap转换为矩阵
// * @param env  环境
// * @param obj_bitmap bitmap
// * @param matrix 矩阵
// * @return 返回是否转换成功
// */
//bool bitmapToMatrix(JNIEnv *env, jobject obj_bitmap, Mat &matrix) {
//    void *bitmapPixels;//保存图片像素数据
//    AndroidBitmapInfo bitmapInfo;
//    ASSERT_FALSE(AndroidBitmap_getInfo(env, obj_bitmap, &bitmapInfo) > 0);//获取图像参数
//    ASSERT_FALSE(bitmapInfo.format == ANDROID_BITMAP_FORMAT_RGBA_8888 ||
//                 bitmapInfo.format == ANDROID_BITMAP_FORMAT_RGB_565);//仅支持ARGB 888 和RGB565被支持
//    ASSERT_FALSE(AndroidBitmap_lockPixels(env, obj_bitmap, &bitmapPixels) >= 0);//获取图像像素数据并锁定内存
//    ASSERT_FALSE(bitmapPixels);
//    if (bitmapInfo.format == ANDROID_BITMAP_FORMAT_RGBA_8888) {
//        Mat tmp(bitmapInfo.height, bitmapInfo.width, CV_8UC4, bitmapPixels);
//        tmp.copyTo(matrix);
//    } else {
//        Mat tmp(bitmapInfo.height, bitmapInfo.width, CV_8UC2, bitmapPixels);
//        cvtColor(tmp, matrix, COLOR_BGR5652RGB);
//    }
//    //转换RGB为BGR
//    cvtColor(matrix, matrix, COLOR_RGB2BGR);
//    AndroidBitmap_unlockPixels(env, obj_bitmap);
//    return true;
//}
//
///**
// * 将矩阵转换为Bitmap
// * @param env 环境
// * @param matrix  目标Mat
// * @param obj_bitmap  目标bitmap
// * @return 返回是否成功
// */
//bool matrixToBitmap(JNIEnv *env, Mat &matrix, jobject obj_bitmap) {
//    void *bitmapPixels;//存储图片像素数据
//    AndroidBitmapInfo bitmapInfo;
//    //获取图像参数
//    ASSERT_FALSE(AndroidBitmap_getInfo(env, obj_bitmap, &bitmapInfo) >= 0);
//    //限制仅可以使用RGB_8888和RGB_565的数据格式
//    ASSERT_FALSE(bitmapInfo.format == ANDROID_BITMAP_FORMAT_RGBA_8888 ||
//                 bitmapInfo.format == ANDROID_BITMAP_FORMAT_RGB_565);
//    //必须是宽高相同的二维矩阵
//    ASSERT_FALSE(matrix.dims == 2 && bitmapInfo.height == (uint32_t) matrix.rows &&
//                 bitmapInfo.width == (uint32_t) matrix.cols)
//    ASSERT_FALSE(matrix.type() == CV_8UC1 || matrix.type() == CV_8UC3 || matrix.type() == CV_8UC4)
//    //获取像素并锁定这块内存
//    ASSERT_FALSE(AndroidBitmap_lockPixels(env, obj_bitmap, &bitmapPixels) >= 0)
//    ASSERT_FALSE(bitmapPixels)
//    if (bitmapInfo.format == ANDROID_BITMAP_FORMAT_RGBA_8888) {
//        Mat tmp(bitmapInfo.height, bitmapInfo.width, CV_8UC4, bitmapPixels);
//        switch (matrix.type()) {
//            case CV_8UC1:
//                cvtColor(matrix, tmp, COLOR_GRAY2RGBA);
//                break;
//            case CV_8UC3:
//                cvtColor(matrix, tmp, COLOR_RGB2RGBA);
//                break;
//            case CV_8UC4:
//                matrix.copyTo(tmp);
//                break;
//            default:
//                AndroidBitmap_unlockPixels(env, obj_bitmap);
//                return false;
//
//        }
//    } else {
//        Mat tmp(bitmapInfo.height, bitmapInfo.width, CV_8UC2, bitmapPixels);
//        switch (matrix.type()) {
//            case CV_8UC1:
//                cvtColor(matrix, tmp, COLOR_GRAY2BGR565);
//                break;
//            case CV_8UC3:
//                cvtColor(matrix, tmp, COLOR_RGB2BGR565);
//                break;
//            case CV_8UC4:
//                cvtColor(matrix, tmp, COLOR_RGBA2BGR565);
//                break;
//        }
//    }
//    AndroidBitmap_unlockPixels(env, obj_bitmap);
//    return true;
//}
void BitmapToMat2(JNIEnv *env, jobject& bitmap, Mat& mat, jboolean needUnPremultiplyAlpha) {
    AndroidBitmapInfo info;
    void *pixels = 0;
    Mat &dst = mat;

    try {
        LOGD("nBitmapToMat");
        CV_Assert(AndroidBitmap_getInfo(env, bitmap, &info) >= 0);
        CV_Assert(info.format == ANDROID_BITMAP_FORMAT_RGBA_8888 ||
                  info.format == ANDROID_BITMAP_FORMAT_RGB_565);
        CV_Assert(AndroidBitmap_lockPixels(env, bitmap, &pixels) >= 0);
        CV_Assert(pixels);
        dst.create(info.height, info.width, CV_8UC4);
        if (info.format == ANDROID_BITMAP_FORMAT_RGBA_8888) {
            LOGD("nBitmapToMat: RGBA_8888 -> CV_8UC4");
            Mat tmp(info.height, info.width, CV_8UC4, pixels);
            if (needUnPremultiplyAlpha) cvtColor(tmp, dst, COLOR_mRGBA2RGBA);
            else tmp.copyTo(dst);
        } else {
            // info.format == ANDROID_BITMAP_FORMAT_RGB_565
            LOGD("nBitmapToMat: RGB_565 -> CV_8UC4");
            Mat tmp(info.height, info.width, CV_8UC2, pixels);
            cvtColor(tmp, dst, COLOR_BGR5652RGBA);
        }
        AndroidBitmap_unlockPixels(env, bitmap);
        return;
    } catch (const cv::Exception &e) {
        AndroidBitmap_unlockPixels(env, bitmap);
        LOGE("nBitmapToMat catched cv::Exception: %s", e.what());
        jclass je = env->FindClass("org/opencv/core/CvException");
        if (!je) je = env->FindClass("java/lang/Exception");
        env->ThrowNew(je, e.what());
        return;
    } catch (...) {
        AndroidBitmap_unlockPixels(env, bitmap);
        LOGE("nBitmapToMat catched unknown exception (...)");
        jclass je = env->FindClass("java/lang/Exception");
        env->ThrowNew(je, "Unknown exception in JNI code {nBitmapToMat}");
        return;
    }
}

void BitmapToMat(JNIEnv *env, jobject& bitmap, Mat& mat) {
    BitmapToMat2(env, bitmap, mat, false);
}

void MatToBitmap2
        (JNIEnv *env, Mat& mat, jobject& bitmap, jboolean needPremultiplyAlpha) {
    AndroidBitmapInfo info;
    void *pixels = 0;
    Mat &src = mat;

    try {
        LOGD("nMatToBitmap");
        CV_Assert(AndroidBitmap_getInfo(env, bitmap, &info) >= 0);
        CV_Assert(info.format == ANDROID_BITMAP_FORMAT_RGBA_8888 ||
                  info.format == ANDROID_BITMAP_FORMAT_RGB_565);
        CV_Assert(src.dims == 2 && info.height == (uint32_t) src.rows &&
                  info.width == (uint32_t) src.cols);
        CV_Assert(src.type() == CV_8UC1 || src.type() == CV_8UC3 || src.type() == CV_8UC4);
        CV_Assert(AndroidBitmap_lockPixels(env, bitmap, &pixels) >= 0);
        CV_Assert(pixels);
        if (info.format == ANDROID_BITMAP_FORMAT_RGBA_8888) {
            Mat tmp(info.height, info.width, CV_8UC4, pixels);
            if (src.type() == CV_8UC1) {
                LOGD("nMatToBitmap: CV_8UC1 -> RGBA_8888");
                cvtColor(src, tmp, COLOR_GRAY2RGBA);
            } else if (src.type() == CV_8UC3) {
                LOGD("nMatToBitmap: CV_8UC3 -> RGBA_8888");
                cvtColor(src, tmp, COLOR_RGB2RGBA);
            } else if (src.type() == CV_8UC4) {
                LOGD("nMatToBitmap: CV_8UC4 -> RGBA_8888");
                if (needPremultiplyAlpha)
                    cvtColor(src, tmp, COLOR_RGBA2mRGBA);
                else
                    src.copyTo(tmp);
            }
        } else {
            // info.format == ANDROID_BITMAP_FORMAT_RGB_565
            Mat tmp(info.height, info.width, CV_8UC2, pixels);
            if (src.type() == CV_8UC1) {
                LOGD("nMatToBitmap: CV_8UC1 -> RGB_565");
                cvtColor(src, tmp, COLOR_GRAY2BGR565);
            } else if (src.type() == CV_8UC3) {
                LOGD("nMatToBitmap: CV_8UC3 -> RGB_565");
                cvtColor(src, tmp, COLOR_RGB2BGR565);
            } else if (src.type() == CV_8UC4) {
                LOGD("nMatToBitmap: CV_8UC4 -> RGB_565");
                cvtColor(src, tmp, COLOR_RGBA2BGR565);
            }
        }
        AndroidBitmap_unlockPixels(env, bitmap);
        return;
    } catch (const cv::Exception &e) {
        AndroidBitmap_unlockPixels(env, bitmap);
        LOGE("nMatToBitmap catched cv::Exception: %s", e.what());
        jclass je = env->FindClass("org/opencv/core/CvException");
        if (!je) je = env->FindClass("java/lang/Exception");
        env->ThrowNew(je, e.what());
        return;
    } catch (...) {
        AndroidBitmap_unlockPixels(env, bitmap);
        LOGE("nMatToBitmap catched unknown exception (...)");
        jclass je = env->FindClass("java/lang/Exception");
        env->ThrowNew(je, "Unknown exception in JNI code {nMatToBitmap}");
        return;
    }
}

void MatToBitmap(JNIEnv *env, Mat& mat, jobject& bitmap) {
    MatToBitmap2(env, mat, bitmap, false);
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
    BitmapToMat(env, input, src);
    Mat dst;
    blur(src, dst, Size2i(10, 10));
//转换为java矩阵并更新
//将mat对象转换为bitmap对象
    MatToBitmap(env, dst, output);
}



