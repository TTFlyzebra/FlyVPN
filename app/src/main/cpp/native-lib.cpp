#include <jni.h>
#include <string>
#include <android/log.h>


#include <unistd.h>
#include <fcntl.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <syslog.h>
#include <errno.h>
#include <sys/ioctl.h>
#include <sys/socket.h>
#include <linux/if.h>
#include <netinet/in.h>
#include <linux/if_tun.h>

#define TAG "flyvpn-jni"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__)
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__)

#define OTUNSETIFF     (('T'<< 8) | 202)

extern "C" JNIEXPORT jstring JNICALL
Java_com_flyzebra_flyvpn_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    LOGD("log by flycnzebra");
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_flyzebra_flyvpn_MainActivity_openTunDev(JNIEnv *env, jobject thiz) {
    struct ifreq ifr;
    int fd;
    int ret;
    LOGD("start open /dev/tun....");
    if ((fd = open("/dev/tun", O_RDWR)) < 0)  goto failed;
    LOGD("ioctl fd=%d",fd);
    LOGD("start ioctl TUNSETIFF....");
    memset(&ifr, 0, sizeof(ifr));
    ifr.ifr_flags =  IFF_TUN | IFF_NO_PI;
    strncpy(ifr.ifr_name, "/dev/tun", IFNAMSIZ);
    ret = ioctl(fd, TUNSETIFF, (void *) &ifr);
    LOGD("ioctl ret=%d",ret);
    if(ret<0) goto failed;
    LOGD("open dev tun success");
    return fd;
    failed:
    LOGD("open dev tun failed");
    close(fd);
    return -1;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_flyzebra_flyvpn_MainActivity_closeTunDev(JNIEnv *env, jobject thiz, jint fd) {
    LOGD("close dev tun fd=%d",fd);
    close(fd);
}