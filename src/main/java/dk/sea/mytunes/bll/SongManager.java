
package dk.sea.mytunes.bll;

import dk.sea.mytunes.be.Song;
import dk.sea.mytunes.bll.util.SongSearcher;
import dk.sea.mytunes.dal.ISongDataAccess;
import dk.sea.mytunes.dal.SongDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class SongManager {

    private SongSearcher songSearcher = new SongSearcher();
    private final ISongDataAccess songDAO;

    public SongManager() throws IOException {
        songDAO = new SongDAO();
    }

    public List<Song> getAllSongs() throws SQLException, IOException {
        return songDAO.getAllSongs();
    }

    public Song createSong(Song song) throws SQLException, IOException {
        return songDAO.createSong(song);
    }

    public List<Song> searchSongs(String query) throws IOException, SQLException {
        List<Song> songs = getAllSongs();
        return songSearcher.search(songs, query);
    }

    public void updateSong(Song song) throws SQLException, IOException {
        songDAO.updateSong(song);
    }

    public void deleteSong(Song song) throws SQLException, IOException {
        songDAO.deleteSong(song);
    }

    public Song getSongById(Song song) throws SQLException, IOException {
        return songDAO.getSongById(song);
    }
}
