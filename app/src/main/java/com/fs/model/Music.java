package com.fs.model;

public class Music implements java.io.Serializable {
    private Long id;
    private String name;
    private String path;
    private Long duration;
    private Long size;
    private String ablum;
    private String ablumImg;
    private String singer;
    private String style;
    private String lrc;
    private String lyric;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getAblum() {
        return ablum;
    }

    public void setAblum(String ablum) {
        this.ablum = ablum;
    }

    public String getAblumImg() {
        return ablumImg;
    }

    public void setAblumImg(String ablumImg) {
        this.ablumImg = ablumImg;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getLrc() {
        return lrc;
    }

    public void setLrc(String lrc) {
        this.lrc = lrc;
    }

    // 新增 lyric 的 getter 和 setter
    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    @Override
    public String toString() {
        return "Music{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                ", ablum='" + ablum + '\'' +
                ", ablumImg='" + ablumImg + '\'' +
                ", singer='" + singer + '\'' +
                ", style='" + style + '\'' +
                ", lrc='" + lrc + '\'' +
                ", lyric='" + lyric + '\'' +
                '}';
    }
}
