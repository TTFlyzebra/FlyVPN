package com.flyzebra.network;


import com.flyzebra.flyui.bean.ThemeBean;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface Api {

    @GET("api/theme")
    Observable<List<ThemeBean>> doTheme(@Query("type") String type);

}