package dk.sea.mytunes.gui.Model;

import dk.sea.mytunes.be.Song;
import dk.sea.mytunes.bll.SongManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class SongModel {
    private SongManager songManager;
    private ObservableList<Song> observableSongs;

    public SongModel() throws IOException {
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

    public void searchSong(String query) throws SQLException, IOException {
        List<Song> searchResults = songManager.searchSongs(query);
        observableSongs.clear();
        observableSongs.addAll(searchResults);
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
}
