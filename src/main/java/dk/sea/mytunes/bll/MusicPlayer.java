package dk.sea.mytunes.bll;

import dk.sea.mytunes.be.Song;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

public class MusicPlayer {

    private MediaPlayer mediaPlayer;
    private boolean autoPlay = true;
    private Consumer<Song> songChanged;
    private List<Song> playlistList;
    private int currentSongIndex;

    public void playSong(Song song) {
        stopSong();
        try {
            File songFilePath = new File(song.getFilePath());
            Media mediaSong = new Media(songFilePath.toURI().toString());
            mediaPlayer = new MediaPlayer(mediaSong);
            mediaPlayer.setOnEndOfMedia(() -> {
                if(autoPlay) {
                    playNextSong();
                }
            });
            mediaPlayer.play();
            if (songChanged != null) {
                songChanged.accept(song);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void playSongFromPlaylist(List<Song> playlist, int index) {
        if (playlist != null && !playlist.isEmpty() && index <= playlist.size() - 1 && index > -1) {
            playlistList = playlist;
            this.currentSongIndex = index;
            playSong(playlist.get(index));
        }
    }

    public void playNextSong() {
        if(playlistList == null) {return;}
        if (currentSongIndex < playlistList.size() - 1) {
            currentSongIndex++;
            playSong(playlistList.get(currentSongIndex));
        }
        else if (currentSongIndex == playlistList.size() - 1) {
            currentSongIndex = 0;
            playSong(playlistList.get(currentSongIndex));
        }
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public Song getCurrentSong() {
        if (playlistList != null && currentSongIndex >= 0 && currentSongIndex < playlistList.size()) {
            return playlistList.get(currentSongIndex);
        }
        return null;
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void resume() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    public void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    public double getVolume() {
        if (mediaPlayer != null) {
            return mediaPlayer.getVolume();
        }
        return 0;
    }

    public double getCurrentTime() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentTime().toSeconds();
        }
        return 0;
    }

    public void setTime(double seconds) {
        if (mediaPlayer != null) {
            mediaPlayer.seek(Duration.seconds(seconds));
        }
    }

    public void playPrevious() {
        if (currentSongIndex > 0) {
            currentSongIndex--;
            playSong(playlistList.get(currentSongIndex));
        }
        else if (currentSongIndex == 0) {
            currentSongIndex = playlistList.size() - 1;
            playSong(playlistList.get(currentSongIndex));
        }
    }

    public void getTotalTime(String filePath, Consumer<Duration> callback) {
        File songFilePath = new File(filePath);
        Media mediaSong = new Media(songFilePath.toURI().toString());
        MediaPlayer tempMedia = new MediaPlayer(mediaSong);

        if (!songFilePath.exists() || !songFilePath.isFile()) {
            System.out.println("File does not exist");
            return;
        }

        tempMedia.setOnReady(() -> {
            callback.accept(tempMedia.getTotalDuration());
            tempMedia.dispose();
        });

        tempMedia.setOnError(() -> {
            System.out.println("Error loading song: " + tempMedia.getError());
            tempMedia.dispose();
        });
    }


    private void stopSong() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }

    public void setOnSongChange(Consumer<Song> callback) {
        this.songChanged = callback;
    }
}
