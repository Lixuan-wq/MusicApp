package com.fs.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    //所有通过 Retrofit 发起的请求都会基于这个 URL
    private static final String BASE_URL = "https://open.bigmodel.cn/api/paas/v4/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    //设置基础 URL
                    .baseUrl(BASE_URL)
                    //添加 Gson 转换器，用于处理 JSON 数据的序列化和反序列化。
                    .addConverterFactory(GsonConverterFactory.create())
                    //构建并返回 Retrofit 实例
                    .build();
        }
        return retrofit;
    }
}
