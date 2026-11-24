package dk.sea.mytunes.dal;

import dk.sea.mytunes.be.Song;

import java.util.List;

public class SongDAO implements ISongDataAccess{
    @Override
    public List<Song> getAllSongs() {
        return List.of();
    }

    @Override
    public Song createSong(Song song) {
        return null;
    }

    @Override
    public List<Song> searchSongs(String query) {
        return List.of();
    }

    @Override
    public void updateSong(Song song) {

    }

    @Override
    public void deleteSong(Song song) {

    }

    @Override
    public Song getSongById(Song song) {
        return null;
    }
}
