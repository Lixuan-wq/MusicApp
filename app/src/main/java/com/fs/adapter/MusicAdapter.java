package com.fs.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fs.activity.PlayerActivity;
import com.fs.model.Music;
import com.fs.R;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {

    private List<Music> musicList;
    // 1. 新增：定义列表项点击接口
    private OnItemClickListener listener;

    // 2. 新增：定义点击事件接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    // 3. 新增：外部设置点击监听的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public MusicAdapter(List<Music> musicList) {
        this.musicList = musicList;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.music_item, parent, false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        Music music = musicList.get(position);
        holder.tvId.setText(String.valueOf(music.getId()));
        holder.tvName.setText(music.getName());
        holder.tvSinger.setText(music.getSinger());
        holder.tvAlbum.setText(music.getAblum());
        //使用glide加载网络图片
        Glide.with(holder.itemView.getContext())
                .load(music.getAblumImg())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.ivAlbumImg);


        holder.btnPlay.setOnClickListener(v -> {
            // 处理播放按钮点击事件,跳转到播放页面
            Intent intent=new Intent(holder.itemView.getContext(), PlayerActivity.class);
            intent.putExtra("music",music);
            holder.itemView.getContext().startActivity(intent);
        });

        // 4. 新增：给列表项绑定点击事件，跳转到ChatActivity
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(v, position);
            }
        });
    }

    // 获取数据源大小
    @Override
    public int getItemCount() {
        return musicList == null ? 0 : musicList.size();
    }

    // 定义ViewHolder
    static class MusicViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvName, tvSinger, tvAlbum;
        ImageView ivAlbumImg;
        ImageButton btnPlay;

        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tv_id);
            tvName = itemView.findViewById(R.id.tv_name);
            tvSinger = itemView.findViewById(R.id.tv_singer);
            tvAlbum = itemView.findViewById(R.id.tv_ablum);
            ivAlbumImg = itemView.findViewById(R.id.iv_ablumImg);
            btnPlay = itemView.findViewById(R.id.btn_play);
        }
    }
}
