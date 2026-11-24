package dk.sea.mytunes.dal;

import dk.sea.mytunes.be.Song;

import java.util.List;

public interface ISongDataAccess {
    List<Song> getAllSongs();

    Song createSong(Song song);

    List<Song> searchSongs(String query);

    void updateSong(Song song);

    void deleteSong(Song song);

    Song getSongById(Song song);
}