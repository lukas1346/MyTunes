package dk.sea.mytunes.dal;

import dk.sea.mytunes.be.Playlist;
import dk.sea.mytunes.be.PlaylistSong;

import java.util.List;

public class PlaylistDAO implements IPlaylistDataAccess {
    @Override
    public List<Playlist> getAllPlaylists() {
        return List.of();
    }

    @Override
    public Playlist createPlaylist(Playlist playlist) {
        return null;
    }

    @Override
    public void updatePlaylist(Playlist playlist) {

    }

    @Override
    public void deletePlaylist(Playlist playlist) {

    }

    @Override
    public List<PlaylistSong> getSongsInPlaylist(int playlistId) {
        return List.of();
    }

    @Override
    public void addSongToPlaylist(int playlistId, int songId) {

    }

    @Override
    public void removeSongFromPlaylist(int playlistId, int songId) {

    }

    @Override
    public void updateSongPosition(int playlistId, int songId, int position) {

    }
}
