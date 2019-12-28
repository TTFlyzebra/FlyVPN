//
// Created by flyzebra on 19-10-29.
//

#include <jni.h>
#include <android/log.h>
#define TAG "flyvpn-jni"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__)
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__)

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
#include <strings.h>
#include <arpa/inet.h>
#include "tun_dev.h"

int open_dev(){
    struct ifreq ifr;
    int fd;
    int ret;
    LOGD("start open /dev/tun");
    fd = open("/dev/tun", O_RDWR);
    LOGD("open /dev/tun fd=%d",fd);
    if(fd<0) goto failed;
    LOGD("start ioctl TUNSETIFF");
    memset(&ifr, 0, sizeof(ifr));
    ifr.ifr_flags =  IFF_TUN | IFF_NO_PI;
    ret = ioctl(fd, TUNSETIFF, (void *) &ifr);
    LOGD("ioctl ret=%d",ret);
    if(ret<0) goto failed;
    LOGD("resetNetworkLink tun success");

    setIP(ifr.ifr_name);

    return fd;
    failed:
    LOGD("resetNetworkLink tun failed");
    close(fd);
}

int close_dev(int fd){
    int ret = close(fd);
    LOGD("close tun %d",ret);
}

int setIP(char *dev){
    struct ifreq ifr;
    struct sockaddr_in addr;
    int sockfd, err = -1;

    bzero(&addr, sizeof(addr));
    addr.sin_family = AF_INET;
    inet_pton(AF_INET, "10.0.0.1", &addr.sin_addr);

    bzero(&ifr, sizeof(ifr));
    strcpy(ifr.ifr_name, dev);
    bcopy(&addr, &ifr.ifr_addr, sizeof(addr));
    sockfd = socket(AF_INET, SOCK_DGRAM, 0);
    if (sockfd < 0) {
        perror("socket");
        return -1;
    }

    // ifconfig tun0 10.0.0.1 #设定ip地址
    if ((err = ioctl(sockfd, SIOCSIFADDR, (void *)&ifr)) < 0) {
        perror("ioctl SIOSIFADDR");
        goto done;
    }

    /* 获得接口的标志 */
    if ((err = ioctl(sockfd, SIOCGIFFLAGS, (void *)&ifr)) < 0) {
        perror("ioctl SIOCGIFADDR");
        goto done;
    }

    /* 设置接口的标志 */
    ifr.ifr_flags |= IFF_UP;
    // ifup tap0 #启动设备
    if ((err = ioctl(sockfd, SIOCSIFFLAGS, (void *)&ifr)) < 0) {
        perror("ioctl SIOCSIFFLAGS");
        goto done;
    }

    inet_pton(AF_INET, "255.255.255.252", &addr.sin_addr);
    bcopy(&addr, &ifr.ifr_netmask, sizeof(addr));
    // ifconfig tun0 10.0.0.1/24 #设定子网掩码
    if ((err = ioctl(sockfd, SIOCSIFNETMASK, (void *) &ifr)) < 0) {
        perror("ioctl SIOCSIFNETMASK");
        goto done;
    }

    done:
    close(sockfd);
    return err;
}