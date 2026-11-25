package dk.sea.mytunes.gui.Model;

import dk.sea.mytunes.be.Playlist;
import dk.sea.mytunes.be.Song;
import dk.sea.mytunes.bll.PlaylistManager;
import dk.sea.mytunes.bll.SongManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;

public class PlaylistModel {
    private ObservableList<Playlist> observablePlaylists;
    private ObservableList<Song> observableSongsInPlaylist;
    PlaylistManager playlistManager;

    public PlaylistModel() {
        playlistManager = new PlaylistManager();
        observablePlaylists = FXCollections.observableArrayList();
        observableSongsInPlaylist = FXCollections.observableArrayList();
        try {
            observablePlaylists.addAll(playlistManager.getAllPlaylists());
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ObservableList<Playlist> getObservablePlaylists() {
        return observablePlaylists;
    }

    public boolean createPlaylist(Playlist playlist) throws SQLException, IOException {
        Playlist newPlaylist = playlistManager.createPlaylist(playlist);
        if (newPlaylist != null) {
            observablePlaylists.add(newPlaylist);
            return true;
        }
        return false;
    }

    public void updatePlaylist(Playlist playlist) throws SQLException, IOException {
        playlistManager.updatePlaylist(playlist);
        int index = observablePlaylists.indexOf(playlist);
        observablePlaylists.set(index, playlist);
    }

    public void deletePlaylist(Playlist playlist) throws SQLException, IOException {
        playlistManager.deletePlaylist(playlist);
        observablePlaylists.remove(playlist);
    }

    public ObservableList<Song> getSongsInPlaylist(Playlist selectedPlaylist) throws SQLException, IOException {
        return FXCollections.observableArrayList(playlistManager.getSongsInPlaylist(selectedPlaylist.getPlaylistId()));
    }


}
