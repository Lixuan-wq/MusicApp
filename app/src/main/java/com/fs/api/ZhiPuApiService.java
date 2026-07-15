package com.fs.api;

import com.fs.model.ChatRequest;
import com.fs.model.ChatResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ZhiPuApiService {
    // 路径必须加上 v4/
    @POST("v4/chat/completions")
    Call<ChatResponse> createChatCompletion(
            @Header("Authorization") String authHeader,
            @Body ChatRequest request
    );
}
