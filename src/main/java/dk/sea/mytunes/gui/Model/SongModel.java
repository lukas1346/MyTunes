package dk.sea.mytunes.gui.Model;

import dk.sea.mytunes.be.Playlist;
import dk.sea.mytunes.be.Song;
import dk.sea.mytunes.bll.SongManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.io.IOException;
import java.sql.SQLException;

public class SongModel {
    private SongManager songManager;
    private ObservableList<Song> observableSongs;

    public SongModel() {
        songManager = new SongManager();
        observableSongs = FXCollections.observableArrayList();
        try {
            observableSongs.addAll(songManager.getAllSongs());
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ObservableList<Song> getObservableSongs() {
        return observableSongs;
    }

    public Song createSong(Song song) throws SQLException, IOException {
        Song newSong = songManager.createSong(song);
        if (newSong != null) {
            observableSongs.add(song);
        }
        return newSong;
    }

    public void updateSong(Song song) throws SQLException, IOException {
        songManager.updateSong(song);
        int index = observableSongs.indexOf(song);
        observableSongs.set(index, song);
    }

    public void deleteSong(Song song) throws SQLException, IOException {
        songManager.deleteSong(song);
        observableSongs.remove(song);
    }

    public FilteredList<Song> searchSongs(String query) throws SQLException, IOException {
        return (FilteredList<Song>) songManager.searchSongs(query);
    }
}
