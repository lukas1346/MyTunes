
package dk.sea.mytunes.bll;

import dk.sea.mytunes.be.Playlist;
import dk.sea.mytunes.be.PlaylistSong;
import dk.sea.mytunes.dal.IPlaylistDataAccess;
import dk.sea.mytunes.dal.PlaylistDAO;

import java.util.List;

public class PlaylistManager {

    private final IPlaylistDataAccess playlistDAO;

    public PlaylistManager() {
        playlistDAO = new PlaylistDAO();
    }

    public List<Playlist> getAllPlaylists() {
        return playlistDAO.getAllPlaylists();
    }

    public Playlist createPlaylist(Playlist playlist) {
        return playlistDAO.createPlaylist(playlist);
    }

    public void updatePlaylist(Playlist playlist) {
        playlistDAO.updatePlaylist(playlist);
    }

    public void deletePlaylist(Playlist playlist) {
        playlistDAO.deletePlaylist(playlist);
    }

    public List<PlaylistSong> getSongsInPlaylist(int playlistId) {
        return playlistDAO.getSongsInPlaylist(playlistId);
    }

    public void addSongToPlaylist(int playlistId, int songId) {
        playlistDAO.addSongToPlaylist(playlistId, songId);
    }

    public void removeSongFromPlaylist(int playlistId, int songId) {
        playlistDAO.removeSongFromPlaylist(playlistId, songId);
    }

    public void updateSongPosition(int playlistId, int songId, int position) {
        playlistDAO.updateSongPosition(playlistId, songId, position);
    }
}
