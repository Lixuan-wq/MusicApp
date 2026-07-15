package com.fs.api;

import com.fs.model.Music;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
public interface MusicApiService {
    @GET("/music/listAll")
    Call<List<Music>> musicListAll();
}
