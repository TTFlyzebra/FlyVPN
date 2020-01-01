package com.flyzebra.network;


import com.flyzebra.flyui.bean.ThemeBean;

import java.util.List;

import rx.Subscriber;

public interface ApiAction {
    void doTheme(String type, Subscriber<List<ThemeBean>> subscriber);
}