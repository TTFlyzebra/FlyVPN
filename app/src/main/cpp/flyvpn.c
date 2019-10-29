#include <jni.h>
#include "tun_dev.h"


JNIEXPORT jint JNICALL
Java_com_flyzebra_flyvpn_MainActivity_openTunDev(JNIEnv *env, jobject thiz) {
    return open_dev();
}

JNIEXPORT void JNICALL
Java_com_flyzebra_flyvpn_MainActivity_closeTunDev(JNIEnv *env, jobject thiz, jint fd) {
    close_dev(fd);
}