package dk.sea.mytunes.gui.Model;

import dk.sea.mytunes.be.Song;
import dk.sea.mytunes.bll.MusicPlayer;

import java.util.List;
import java.util.function.Consumer;

public class MusicModel {
    private final MusicPlayer musicPlayer;

    public MusicModel() {
        this.musicPlayer = new MusicPlayer();
    }

    public void playSong(Song song) {
        musicPlayer.playSong(song);
    }

    public void playSongFromPlaylist(List<Song> playlist, int index) {
        musicPlayer.playSongFromPlaylist(playlist, index);
    }

    public void playNextSong() {
        musicPlayer.playNextSong();
    }

    public void playPrevious() {
        musicPlayer.playPrevious();
    }

    public void pause() {
        musicPlayer.pause();
    }

    public void resume() {
        musicPlayer.resume();
    }

    public void setVolume(double volume) {
        musicPlayer.setVolume(volume);
    }

    public long getCurrentTime() {
        return musicPlayer.getCurrentTime();
    }

    public void setTime(long milliseconds) {
        musicPlayer.setTime(milliseconds);
    }

    public void setAllSongsList(List<Song> songs) {
        musicPlayer.setAllSongsList(songs);
    }

    public Song getCurrentSong() {
        return musicPlayer.getCurrentSong();
    }

    public boolean isPlaying() {
        return musicPlayer.isPlaying();
    }

    public void setOnSongChange(Consumer<Song> callback) {
        musicPlayer.setOnSongChange(callback);
    }

}
