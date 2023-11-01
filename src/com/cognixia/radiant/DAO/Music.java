package com.cognixia.radiant.DAO;

public class Music {
    private int music_id;
    private String title;
    private String artist_name;
    private int length_sec;

    public Music(int music_id, String title, String artist_name, int length_sec){
        this.music_id = music_id;
        this.title = title;
        this.artist_name = artist_name;
        this.length_sec = length_sec;
    }

    @Override
    public String toString() {

        int minutes = length_sec/60;
        int remainingSeconds = length_sec - minutes*60;

        return "Song|ID:" + music_id + ", Title: " + title + ", Artist: " + artist_name + ", Duration: " + minutes + ":" + remainingSeconds + "|";
    }

    public int getMusic_id() {
        return music_id;
    }
    public void setMusic_id(int music_id) {
        this.music_id = music_id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getArtist_name() {
        return artist_name;
    }
    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }
    public int getLength_sec() {
        return length_sec;
    }
    public void setLength_sec(int length_sec) {
        this.length_sec = length_sec;
    }

}
