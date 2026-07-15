package com.fs.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.Rotate;
import com.fs.R;
import com.fs.api.MusicApiService;
import com.fs.api.RetrofitClient;
import com.fs.model.Music;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayerActivity extends AppCompatActivity {
    private Music music;
    private TextView songName, singerName;
    private ImageView diskImage;
    private TextView lyricPrev, lyricCurrent, lyricNext;
    private SeekBar musicProgress;
    private TextView currentTime, totalTime;
    private ImageButton prevBtn, playPauseBtn, nextBtn;

    private ObjectAnimator animator;
    private MediaPlayer player;
    private int currentPlaying;
    private LinkedList<Music> playList = new LinkedList<>();
    private boolean isInitialized = false;

    // 歌词相关
    private List<LyricLine> lyricList = new ArrayList<>();
    private int currentLyricIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        music = (Music) getIntent().getSerializableExtra("music");
        if (music == null) {
            Toast.makeText(this, "音乐数据有误", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        init();
        preparePlayList(music);

        // 定时更新进度和歌词
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateTimer();
                updateLyric();
            }
        }, 0, 300);
    }

    private void init() {
        songName = findViewById(R.id.tv_song_name);
        singerName = findViewById(R.id.tv_song_singer);
        diskImage = findViewById(R.id.iv_disk);
        lyricPrev = findViewById(R.id.tv_lyric_previous);
        lyricCurrent = findViewById(R.id.tv_lyric_current);
        lyricNext = findViewById(R.id.tv_lyric_next);
        musicProgress = findViewById(R.id.sb_progress);
        currentTime = findViewById(R.id.tv_progress_current);
        totalTime = findViewById(R.id.tv_progress_total);
        prevBtn = findViewById(R.id.btn_prev);
        playPauseBtn = findViewById(R.id.btn_play_pause);
        nextBtn = findViewById(R.id.btn_next);

        View.OnClickListener onClick = new OnClickControl();
        prevBtn.setOnClickListener(onClick);
        playPauseBtn.setOnClickListener(onClick);
        nextBtn.setOnClickListener(onClick);

        // 唱片旋转动画
        animator = ObjectAnimator.ofFloat(diskImage, "rotation", 0, 360F);
        animator.setDuration(5000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);

        player = new MediaPlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
        if (animator != null) {
            animator.cancel();
        }
    }

    private void preparePlayList(Music currentMusic) {
        MusicApiService apiService = RetrofitClient.getClient().create(MusicApiService.class);
        Call<List<Music>> call = apiService.musicListAll();
        call.enqueue(new Callback<List<Music>>() {
            @Override
            public void onResponse(Call<List<Music>> call, Response<List<Music>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    playList.clear();
                    playList.addAll(response.body());
                    currentPlaying = getMusicIndex(playList, currentMusic.getId());
                    prepareMedia();
                }
            }

            @Override
            public void onFailure(Call<List<Music>> call, Throwable t) {
                Toast.makeText(PlayerActivity.this, "加载播放列表失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void prepareMedia() {
        Music currentMusic = playList.get(currentPlaying);
        songName.setText(currentMusic.getName());
        singerName.setText(currentMusic.getSinger() + "  " + currentMusic.getAblum());

        // 封面转正
        Glide.with(this)
                .load(currentMusic.getAblumImg())
                .transform(new CircleCrop(), new Rotate(180))
                .placeholder(R.mipmap.logo)
                .error(R.mipmap.logo)
                .into(diskImage);

        // ========== 歌词：后端有就用后端，没有就用写死测试歌词 ==========
        String lyricContent = currentMusic.getLyric();
        if (lyricContent == null || lyricContent.trim().isEmpty()) {
            // 写死 LRC 测试歌词（自动同步）
            lyricContent =
                    "[00:00.00]蔡旻佑 - 我可以\n" +
                            "[00:03.00]词：蔡旻佑/林唯\n" +
                            "[00:06.00]曲：蔡旻佑\n" +
                            "[00:25.00]寄没有地址的信\n" +
                            "[00:30.00]这样的情绪有种距离\n" +
                            "[00:35.00]你放着谁的歌曲\n" +
                            "[00:40.00]是怎样的心情\n" +
                            "[00:55.00]雨下得好安静\n" +
                            "[01:00.00]是不是你偷偷在哭泣\n" +
                            "[01:20.00]我可以陪你去看星星\n" +
                            "[01:25.00]不用再多说明\n" +
                            "[01:30.00]我就要和你在一起\n" +
                            "[01:40.00]我不想又再一次和你分离\n" +
                            "[01:45.00]我多么想每一次的美丽\n" +
                            "[01:50.00]是因为你";
        }

        parseLyric(lyricContent);
        resetLyricView();
        // =====================================================

        if (isInitialized) {
            player.stop();
            player.reset();
        }

        String musicUrl = currentMusic.getPath();
        try {
            player.setDataSource(musicUrl);
            player.setOnPreparedListener(mp -> {
                int dur = mp.getDuration();
                musicProgress.setMax(dur);
                totalTime.setText(formatTime(dur));
                mp.start();
                isInitialized = true;
                animator.start();
                playPauseBtn.setImageResource(android.R.drawable.ic_media_pause);
            });
            player.setOnErrorListener((mp, what, extra) -> true);
            player.setOnCompletionListener(mp -> {
                currentPlaying = (currentPlaying + 1) % playList.size();
                prepareMedia();
            });
            player.prepareAsync();
        } catch (Exception e) {
            Toast.makeText(this, "加载音乐失败", Toast.LENGTH_SHORT).show();
            isInitialized = false;
        }

        musicProgress.setOnSeekBarChangeListener(new OnSeekBarChangeControl());
    }


    // 更新播放/暂停按钮图标
    private void updatePlayPauseIcon(boolean isPlaying) {
        if (isPlaying) {
            playPauseBtn.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            playPauseBtn.setImageResource(android.R.drawable.ic_media_play);
        }
    }

    // 更新播放进度与时间
    private void updateTimer() {
        runOnUiThread(() -> {
            if (player != null && isInitialized) {
                int pos = player.getCurrentPosition();
                musicProgress.setProgress(pos);
                currentTime.setText(formatTime(pos));
            }
        });
    }

    // 歌词同步更新
    private void updateLyric() {
        if (lyricList.isEmpty() || player == null || !isInitialized) return;

        int pos = player.getCurrentPosition();
        int idx = findLyricIndex(pos);

        if (idx != currentLyricIndex) {
            currentLyricIndex = idx;
            runOnUiThread(this::refreshLyricView);
        }
    }

    private int findLyricIndex(int pos) {
        for (int i = 0; i < lyricList.size(); i++) {
            LyricLine line = lyricList.get(i);
            if (pos >= line.time && (i == lyricList.size() - 1 || pos < lyricList.get(i + 1).time)) {
                return i;
            }
        }
        return -1;
    }

    private void refreshLyricView() {
        if (currentLyricIndex < 0) {
            resetLyricView();
            return;
        }
        lyricPrev.setText(currentLyricIndex > 0 ? lyricList.get(currentLyricIndex - 1).text : "");
        lyricCurrent.setText(lyricList.get(currentLyricIndex).text);
        lyricNext.setText(currentLyricIndex < lyricList.size() - 1 ? lyricList.get(currentLyricIndex + 1).text : "");

        lyricCurrent.setTextColor(0xFF00BFFF);
        lyricPrev.setTextColor(0xFF888888);
        lyricNext.setTextColor(0xFF888888);
    }

    private void resetLyricView() {
        lyricPrev.setText("");
        lyricCurrent.setText("暂无歌词");
        lyricNext.setText("");
        lyricCurrent.setTextColor(0xFF888888);
    }

    // 解析LRC歌词
    private void parseLyric(String lrcText) {
        lyricList.clear();
        currentLyricIndex = -1;
        if (lrcText == null || lrcText.trim().isEmpty()) return;

        String[] lines = lrcText.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (!line.startsWith("[")) continue;

            try {
                int endBracket = line.indexOf("]");
                String timeStr = line.substring(1, endBracket);
                String text = line.substring(endBracket + 1).trim();

                String[] minSec = timeStr.split(":");
                int min = Integer.parseInt(minSec[0]);
                double sec = Double.parseDouble(minSec[1]);
                int timeMs = (int) (min * 60 * 1000 + sec * 1000);

                lyricList.add(new LyricLine(timeMs, text));
            } catch (Exception ignored) {}
        }
    }

    // 毫秒转 分:秒 格式
    private String formatTime(int ms) {
        int s = ms / 1000;
        int m = s / 60;
        s %= 60;
        return String.format("%02d:%02d", m, s);
    }

    // 按钮点击事件
    private class OnClickControl implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_prev:
                    if (isInitialized) {
                        // 循环播放：第一首切到最后一首
                        if (currentPlaying <= 0) {
                            currentPlaying = playList.size() - 1;
                        } else {
                            currentPlaying--;
                        }
                        prepareMedia();
                        Toast.makeText(PlayerActivity.this, "上一首", Toast.LENGTH_SHORT).show();
                        updatePlayPauseIcon(true);
                    }
                    break;

                case R.id.btn_play_pause:
                    if (!isInitialized) {
                        prepareMedia();
                        Toast.makeText(PlayerActivity.this, "开始播放", Toast.LENGTH_SHORT).show();
                        updatePlayPauseIcon(true);
                    } else if (player.isPlaying()) {
                        player.pause();
                        animator.pause();
                        Toast.makeText(PlayerActivity.this, "已暂停", Toast.LENGTH_SHORT).show();
                        updatePlayPauseIcon(false);
                    } else {
                        player.start();
                        animator.resume();
                        Toast.makeText(PlayerActivity.this, "继续播放", Toast.LENGTH_SHORT).show();
                        updatePlayPauseIcon(true);
                    }
                    break;

                case R.id.btn_next:
                    if (isInitialized) {
                        currentPlaying = (currentPlaying + 1) % playList.size();
                        prepareMedia();
                        Toast.makeText(PlayerActivity.this, "下一首", Toast.LENGTH_SHORT).show();
                        updatePlayPauseIcon(true);
                    }
                    break;
            }
        }
    }

    // 进度条拖动监听
    private class OnSeekBarChangeControl implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser && isInitialized) {
                player.seekTo(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            if (player.isPlaying()) {
                player.pause();
                animator.pause();
            }
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (isInitialized) {
                player.start();
                animator.resume();
            }
        }
    }

    // 根据歌曲ID获取索引
    private Integer getMusicIndex(List<Music> musicList, Long id) {
        for (int i = 0; i < musicList.size(); i++) {
            if (musicList.get(i).getId().equals(id)) {
                return i;
            }
        }
        return 0;
    }

    // 歌词实体类
    private static class LyricLine {
        int time;
        String text;
        LyricLine(int time, String text) {
            this.time = time;
            this.text = text;
        }
    }
}
