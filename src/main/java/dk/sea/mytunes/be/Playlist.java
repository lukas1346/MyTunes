package dk.sea.mytunes.be;

import java.util.ArrayList;
import java.util.List;

public class Playlist {

    private int playlistId;
    private String name;
    private List<Song> songs;

    public Playlist() {
        this.songs = new ArrayList<>();
    }

    public Playlist(int playlistId, String name) {
        this.playlistId = playlistId;
        this.name = name;
        this.songs = new ArrayList<>();
    }

    public Playlist(String name) {
        this.name = name;
        this.songs = new ArrayList<>();
    }

    public int getId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public long calculatePlaylistDuration() {
        long milliseconds = 0;

        for (Song song : songs) {
            milliseconds += song.getDuration();
        }

        return milliseconds;
    }
}
