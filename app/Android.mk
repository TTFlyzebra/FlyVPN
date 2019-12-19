LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := optional
LOCAL_STATIC_JAVA_LIBRARIES :=  gson-2.8.5 \
								android-support-v4 \
								android-support-v7-appcompat \

LOCAL_SRC_FILES := $(call all-java-files-under, $(LOCAL_PATH)/src/main/java) \
                   $(call all-Iaidl-files-under, $(LOCAL_PATH)/src/main/aidl) \

LOCAL_RESOURCE_DIR = $(LOCAL_PATH)/src/main/res \
					 frameworks/support/v7/appcompat/res \

LOCAL_AAPT_FLAGS :=  --auto-add-overlay \
					 --extra-packages android.support.v7.appcompat \

LOCAL_MANIFEST_FILE := src/main/AndroidManifest.xml

LOCAL_OVERRIDES_PACKAGES := mpApp
#LOCAL_PRIVILEGED_MODULE := true
LOCAL_PACKAGE_NAME := NewMpApp
LOCAL_CERTIFICATE := platform
LOCAL_PROGUARD_ENABLED := disabled

include $(BUILD_PACKAGE)

include $(CLEAR_VARS)
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES :=  gson-2.8.5:libs/gson-2.8.5.jar

include $(BUILD_MULTI_PREBUILT)