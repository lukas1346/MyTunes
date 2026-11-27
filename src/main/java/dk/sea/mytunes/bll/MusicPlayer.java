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
    private List<Song> allSongsList;
    private int currentSongIndex;
    private int currentAllSongsIndex;
    private Song currentSong;

    public void playSong(Song song) {
        playlistList = null;
        if (allSongsList != null) {
            currentAllSongsIndex = allSongsList.indexOf(song);
        }
        loadAndPlaySong(song);
    }

    private void loadAndPlaySong(Song song) {
        stopSong();
        try {
            currentSong = song;
            File songFilePath = new File(song.getFilePath());
            Media mediaSong = new Media(songFilePath.toURI().toString());
            mediaPlayer = new MediaPlayer(mediaSong);
            mediaPlayer.setOnEndOfMedia(() -> {
                if (autoPlay) {
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
            loadAndPlaySong(playlist.get(index));
        }
    }

    public void playNextSong() {
        if (playlistList != null) {

            currentSongIndex++;

            if (currentSongIndex >= playlistList.size()) {
                currentSongIndex = 0;
            }

            loadAndPlaySong(playlistList.get(currentSongIndex));

        } else if (allSongsList != null && !allSongsList.isEmpty()) {

            currentAllSongsIndex++;

            if (currentAllSongsIndex >= allSongsList.size()) {
                currentAllSongsIndex = 0;
            }

            playSong(allSongsList.get(currentAllSongsIndex));
        }
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public Song getCurrentSong() {
        return currentSong;
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

    public long getCurrentTime() {
        if (mediaPlayer != null) {
            return Math.round(mediaPlayer.getCurrentTime().toMillis());
        }
        return 0;
    }

    public void setTime(long milliseconds) {
        if (mediaPlayer != null) {
            mediaPlayer.seek(Duration.millis(milliseconds));
        }
    }

    public void playPrevious() {
        if (playlistList != null && !playlistList.isEmpty()) {

            if (currentSongIndex > 0) {
                currentSongIndex--;
                loadAndPlaySong(playlistList.get(currentSongIndex));
            } else if (currentSongIndex == 0) {
                currentSongIndex = playlistList.size() - 1;
                loadAndPlaySong(playlistList.get(currentSongIndex));
            }
        }
        else if (allSongsList != null && !allSongsList.isEmpty()) {
            if (currentAllSongsIndex > 0) {
                currentAllSongsIndex--;
                loadAndPlaySong(allSongsList.get(currentAllSongsIndex));
            } else if (currentAllSongsIndex == 0) {
                currentAllSongsIndex = allSongsList.size() - 1;
                loadAndPlaySong(allSongsList.get(currentAllSongsIndex));
            }
        }
    }

    public void getTotalTime(String filePath, Consumer<Long> callback) {
        File songFilePath = new File(filePath);
        if (!songFilePath.exists() || !songFilePath.isFile()) {
            System.out.println("File does not exist");
            return;
        }

        Media mediaSong = new Media(songFilePath.toURI().toString());
        MediaPlayer tempMedia = new MediaPlayer(mediaSong);

        tempMedia.setOnReady(() -> {
            long totalMilliseconds =
                    Math.round(tempMedia.getTotalDuration().toMillis());

            callback.accept(totalMilliseconds);
            tempMedia.dispose();
        });

        tempMedia.setOnError(() -> {
            System.out.println("Error loading song: " + tempMedia.getError());
            tempMedia.dispose();
        });
    }

    public void setAllSongsList(List<Song> songs) {
        this.allSongsList = songs;
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
