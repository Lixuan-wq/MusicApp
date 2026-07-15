package com.fs.api;

import com.fs.model.Result;
import com.fs.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserApiSerivice {
    //http://www.softeem.top/user/login?username=xixi&password=123456
    @GET("/user/login")
    Call<Result<User>> login(@Query("username") String username,
                             @Query("password") String password);
    @POST("/user/reg")
    Call<Result> register(@Body User user);
}
