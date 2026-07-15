package com.fs.activity;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fs.R;
import com.fs.adapter.MusicAdapter;
import com.fs.api.MusicApiService;
import com.fs.api.RetrofitClient;
import com.fs.model.Music;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusicListActivity extends AppCompatActivity {

    //原有控件
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private MusicAdapter adapter;

    //完整全部歌曲原始数据（用来搜索还原全列表）
    private List<Music> musicAllList = new ArrayList<>();
    //当前页面展示的列表（搜索筛选后的列表）
    private List<Music> musicShowList = new ArrayList<>();

    //音量控件
    private AudioManager audioManager;
    private SeekBar seek_vol;
    private ImageButton iv_vol_up, iv_vol_down;

    //搜索控件
    private SearchView search_music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        //绑定原有控件
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        Button btnChat = findViewById(R.id.btn_chat);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MusicAdapter(musicShowList);
        recyclerView.setAdapter(adapter);

        //列表点击跳转AI聊天页
        adapter.setOnItemClickListener((view, position) -> {
            Intent intent = new Intent(MusicListActivity.this, ChatActivity.class);
            startActivity(intent);
        });

        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadData();
            swipeRefreshLayout.setRefreshing(false);
        });

        //顶部按钮跳转聊天
        btnChat.setOnClickListener(v -> {
            startActivity(new Intent(MusicListActivity.this, ChatActivity.class));
        });


        // ========== 音量调节初始化 ==========
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        seek_vol = findViewById(R.id.seek_vol);
        iv_vol_up = findViewById(R.id.iv_vol_up);
        iv_vol_down = findViewById(R.id.iv_vol_down);

        //读取系统当前媒体音量
        int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int nowVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        seek_vol.setMax(maxVol);
        seek_vol.setProgress(nowVol);

        //拖动滑块改音量
        seek_vol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        //音量+
        iv_vol_up.setOnClickListener(v -> {
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, 0);
            seek_vol.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        });
        //音量-
        iv_vol_down.setOnClickListener(v -> {
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, 0);
            seek_vol.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        });


        // ========== 搜索框监听（匹配歌名name、歌手singer） ==========
        search_music = findViewById(R.id.search_music);
        search_music.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String keyword) {
                searchFilter(keyword.trim().toLowerCase());
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String keyword) {
                searchFilter(keyword.trim().toLowerCase());
                return true;
            }
        });


        //首次加载歌曲
        loadData();
    }

    /**网络加载所有歌曲，存入总集合 musicAllList*/
    private void loadData() {
        MusicApiService apiService = RetrofitClient.getClient().create(MusicApiService.class);
        Call<List<Music>> call = apiService.musicListAll();
        call.enqueue(new Callback<List<Music>>() {
            @Override
            public void onResponse(Call<List<Music>> call, Response<List<Music>> response) {
                Log.i("MusicListActivity", "onResponse: " + response);
                if (response.isSuccessful() && response.body() != null) {
                    List<Music> list = response.body();
                    if (list != null && list.size() > 0) {
                        //保存完整原始数据
                        musicAllList.clear();
                        musicAllList.addAll(list);

                        //默认展示全部歌曲
                        musicShowList.clear();
                        musicShowList.addAll(musicAllList);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(MusicListActivity.this, "数据加载失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Music>> call, Throwable t) {
                Log.e("MusicListActivity", "onFailure: ", t);
                Toast.makeText(MusicListActivity.this, "网络请求失败~！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 搜索过滤：匹配歌名 name / 歌手 singer
     * 和你适配器 Music实体类方法完全对应：getName()、getSinger()
     */
    private void searchFilter(String keyword) {
        musicShowList.clear();

        //搜索框清空，恢复全部歌曲
        if (keyword.isEmpty()) {
            musicShowList.addAll(musicAllList);
        } else {
            for (Music music : musicAllList) {
                String songName = music.getName().toLowerCase();
                String singerName = music.getSinger().toLowerCase();
                //歌名包含关键词 或者 歌手包含关键词，就筛选出来
                if (songName.contains(keyword) || singerName.contains(keyword)) {
                    musicShowList.add(music);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

}
