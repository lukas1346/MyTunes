package dk.sea.mytunes.dal;


import dk.sea.mytunes.be.Playlist;
import dk.sea.mytunes.be.PlaylistSong;

import java.util.List;

public interface IPlaylistDataAccess {

    List<Playlist> getAllPlaylists();

    Playlist createPlaylist(Playlist playlist);

    void updatePlaylist(Playlist playlist);

    void deletePlaylist(Playlist playlist);

    List<PlaylistSong> getSongsInPlaylist(int playlistId);

    void addSongToPlaylist(int playlistId, int songId);

    void removeSongFromPlaylist(int playlistId, int songId);

    void updateSongPosition(int playlistId, int songId, int position);
}
