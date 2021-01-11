#include <jni.h>
#include <stdio.h>
#include <android/log.h>
#include <android/bitmap.h>
#include <string>
#include <opencv2/opencv.hpp>
#include <opencv2/core.hpp>

using namespace std;
using namespace cv;

/**
 * 将bitmap对象转换为mat对象
 * @param env
 * @param bitmap
 * @param mat
 * @param needUnPremultiplyAlpha
 */
void bitmapToMat2(JNIEnv *env, jobject &bitmap, Mat &mat, jboolean needUnPremultiplyAlpha);

/**
 * 将bitmap对象转换为mat对象
 * @param env
 * @param bitmap 输入的bitmap对象
 * @param mat  输出的mat对象
 */
void bitmapToMat(JNIEnv *env, jobject &bitmap, Mat &mat);

/**
 * 将mat转换为bitmap对象
 * @param env
 * @param mat
 * @param bitmap
 * @param needPremultiplyAlpha
 */
void matToBitmap2
        (JNIEnv *env, Mat &mat, jobject &bitmap, jboolean needPremultiplyAlpha);

/**
 * 将mat转换为bitmap
 * @param env
 * @param mat mat对象
 * @param bitmap  bitmap对象
 */
void matToBitmap(JNIEnv *env, Mat &mat, jobject &bitmap);

/**
 * 将jstring字符串转换为C++平台的字符串
 * @param env jni环境对象
 * @param jstring1  jstring字符串
 * @return void
 */
string getStringStr(JNIEnv *env, jstring jstring1);