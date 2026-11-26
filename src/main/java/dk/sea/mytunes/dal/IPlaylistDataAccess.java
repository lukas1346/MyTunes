package dk.sea.mytunes.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.sea.mytunes.be.Playlist;
import dk.sea.mytunes.be.Song;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface IPlaylistDataAccess {

    List<Playlist> getAllPlaylists() throws IOException, SQLException;

    Playlist createPlaylist(Playlist playlist) throws IOException, SQLException;

    void updatePlaylist(Playlist playlist) throws IOException, SQLException;

    void deletePlaylist(Playlist playlist) throws IOException, SQLException;

    List<Song> getSongsInPlaylist(int playlistId) throws IOException, SQLException;

    void addSongToPlaylist(int playlistId, int songId) throws SQLException, IOException;

    void removeSongFromPlaylist(int playlistId, int songId) throws IOException, SQLException;

    void swapSongPosition(int playlistId, int originalPosition, int positionToSwap) throws IOException, SQLException;
}
