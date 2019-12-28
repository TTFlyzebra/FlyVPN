#include <jni.h>
#include "tun_dev.h"


JNIEXPORT jint JNICALL
Java_com_flyzebra_flyvpn_TestActivity_openTunDev__(JNIEnv *env, jobject thiz) {
    return open_dev();
}

JNIEXPORT void JNICALL
Java_com_flyzebra_flyvpn_TestActivity_closeTunDev__I(JNIEnv *env, jobject thiz, jint fd) {
    close_dev(fd);
}