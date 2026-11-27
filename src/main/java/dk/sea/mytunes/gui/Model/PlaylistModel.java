package dk.sea.mytunes.gui.Model;

import dk.sea.mytunes.be.Playlist;
import dk.sea.mytunes.be.Song;
import dk.sea.mytunes.bll.PlaylistManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;

public class PlaylistModel {
    private ObservableList<Playlist> observablePlaylists;
    private ObservableList<Song> observableListSongsInPlaylist;
    PlaylistManager playlistManager;

    public PlaylistModel() throws IOException {
        playlistManager = new PlaylistManager();
        observablePlaylists = FXCollections.observableArrayList();
        observableListSongsInPlaylist = FXCollections.observableArrayList();
        try {
            observablePlaylists.addAll(playlistManager.getAllPlaylists());
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ObservableList<Playlist> getObservablePlaylists() {
        return observablePlaylists;
    }

    public ObservableList<Song> getObservableListSongsInPlaylist() {
        return observableListSongsInPlaylist;
    }

    public Playlist createPlaylist(Playlist playlist) throws SQLException, IOException {
        Playlist newPlaylist = playlistManager.createPlaylist(playlist);
        if (newPlaylist != null) {
            observablePlaylists.add(newPlaylist);
        }
        return newPlaylist;
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

    public void addSongToPlaylist(Playlist playlist, Song song) throws SQLException, IOException {
        if (!playlistManager.songAlreadyInPlaylist(playlist, song)) {
            playlistManager.addSongToPlaylist(playlist, song);
            observableListSongsInPlaylist.add(song);
        }
    }

    public ObservableList<Song> getSongsInPlaylist(Playlist selectedPlaylist) throws SQLException, IOException {
        observableListSongsInPlaylist.setAll(playlistManager.getSongsInPlaylist(selectedPlaylist));
        return observableListSongsInPlaylist;
    }

    public void removeSongFromPlaylist(Playlist playlist, Song song) throws SQLException, IOException {
        playlistManager.removeSongFromPlaylist(playlist, song);
        observableListSongsInPlaylist.remove(song);
    }

    public void moveSongPosition(Playlist playlist, Song song, boolean up) throws SQLException, IOException {
        int index = observableListSongsInPlaylist.indexOf(song);
        if (index < 0) return;
        int indexToSwapWith = up ? index - 1 : index + 1;
        if (indexToSwapWith < 0 || indexToSwapWith >= observableListSongsInPlaylist.size()) {
            return;
        }
        playlistManager.moveSong(playlist, song, up);
        Collections.swap(playlist.getSongs(), index, indexToSwapWith);
        Collections.swap(observableListSongsInPlaylist, index, indexToSwapWith);
    }
}
