package com.example.playfigros;

public class Song {
    private String name;
    private String author;
    private int cover ;      //from R class
    private int wallpaper;  //from R class
    private int song;      //from R class


    public Song(String name, String author, int cover, int wallpaper, int song) {
        this.name = name;
        this.author = author;
        this.cover = cover;
        this.wallpaper = wallpaper;
        this.song = song;
    }

    public int getSong() {
        return song;
    }

    public void setSong(int song) {
        this.song = song;
    }

    public int getWallpaper() {
        return wallpaper;
    }

    public void setWallpaper(int wallpaper) {
        this.wallpaper = wallpaper;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getCover() {
        return cover;
    }

    public void setCover(int cover) {
        this.cover = cover;
    }
}
