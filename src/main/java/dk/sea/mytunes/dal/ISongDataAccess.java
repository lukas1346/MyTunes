package dk.sea.mytunes.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.sea.mytunes.be.Song;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface ISongDataAccess {
    List<Song> getAllSongs() throws IOException, SQLException;

    Song createSong(Song song) throws SQLException, IOException;

    void updateSong(Song song) throws SQLException, IOException;

    void deleteSong(Song song) throws IOException, SQLException;

    Song getSongById(Song song) throws IOException, SQLException;
}