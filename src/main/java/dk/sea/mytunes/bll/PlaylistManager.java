
package dk.sea.mytunes.bll;

import dk.sea.mytunes.be.Playlist;
import dk.sea.mytunes.be.Song;
import dk.sea.mytunes.dal.IPlaylistDataAccess;
import dk.sea.mytunes.dal.PlaylistDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class PlaylistManager {

    private final IPlaylistDataAccess playlistDAO;

    public PlaylistManager() {
        playlistDAO = new PlaylistDAO();
    }

    public List<Playlist> getAllPlaylists() throws SQLException, IOException {
        return playlistDAO.getAllPlaylists();
    }

    public Playlist createPlaylist(Playlist playlist) throws SQLException, IOException {
        return playlistDAO.createPlaylist(playlist);
    }

    public void updatePlaylist(Playlist playlist) throws SQLException, IOException {
        playlistDAO.updatePlaylist(playlist);
    }

    public void deletePlaylist(Playlist playlist) throws SQLException, IOException {
        playlistDAO.deletePlaylist(playlist);
    }

    public List<Song> getSongsInPlaylist(int playlistId) throws SQLException, IOException {
        return playlistDAO.getSongsInPlaylist(playlistId);
    }

    public void addSongToPlaylist(Playlist playlist, Song song) throws SQLException, IOException {
        playlistDAO.addSongToPlaylist(playlist.getPlaylistId(), song.getSongId());
        playlist.getSongs().add(song);
    }

    public void removeSongFromPlaylist(int playlistId, int songId) throws SQLException, IOException {
        playlistDAO.removeSongFromPlaylist(playlistId, songId);
    }

    public void updateSongPosition(int playlistId, int songId, boolean up) throws SQLException, IOException {
        playlistDAO.updateSongPosition(playlistId, songId, up);
    }
}
