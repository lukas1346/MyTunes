
package dk.sea.mytunes.bll;

import dk.sea.mytunes.be.Song;
import dk.sea.mytunes.dal.ISongDataAccess;
import dk.sea.mytunes.dal.SongDAO;

import java.util.List;

public class SongManager {

    private final ISongDataAccess songDAO;

    public SongManager() {
        songDAO = new SongDAO();
    }

    public List<Song> getAllSongs() {
        return songDAO.getAllSongs();
    }

    public Song createSong(Song song) {
        return songDAO.createSong(song);
    }

    public List<Song> searchSongs(String query) {
        return songDAO.searchSongs(query);
    }

    public void updateSong(Song song) {
        songDAO.updateSong(song);
    }

    public void deleteSong(Song song) {
        songDAO.deleteSong(song);
    }

    public Song getSongById(Song song) {
        return songDAO.getSongById(song);
    }
}
