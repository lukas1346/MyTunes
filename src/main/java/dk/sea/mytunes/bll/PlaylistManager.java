
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

    public PlaylistManager() throws IOException {
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

    public List<Song> getSongsInPlaylist(Playlist playlist) throws SQLException, IOException {
        playlist.setSongs(playlistDAO.getSongsInPlaylist(playlist.getPlaylistId()));
        return playlist.getSongs();
    }

    public void addSongToPlaylist(Playlist playlist, Song song) throws SQLException, IOException {
        playlistDAO.addSongToPlaylist(playlist.getPlaylistId(), song.getSongId());
        playlist.getSongs().add(song);
    }

    public void removeSongFromPlaylist(Playlist playlist, Song song) throws SQLException, IOException {
        playlistDAO.removeSongFromPlaylist(playlist.getPlaylistId(), song.getSongId());
        playlist.getSongs().removeIf(s -> s.getSongId() == song.getSongId());
    }

    public void updateSongPosition(Playlist playlist, Song song, boolean moveUp) throws SQLException, IOException {
        int originalPosition = playlist.getSongs().indexOf(song) + 1;
        if (originalPosition == 1 && moveUp) return;
        if (originalPosition == playlist.getSongs().size() && !moveUp) return;
        int positionToSwap = moveUp ? originalPosition - 1 : originalPosition + 1;
        playlistDAO.swapSongs(playlist, originalPosition, positionToSwap, moveUp);
    }
}
