package dk.sea.mytunes.be;

public class PlaylistSong {

    private Playlist playlist;
    private Song song;
    private int position;

    public PlaylistSong() {}

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public PlaylistSong(Playlist playlist, Song song, int position) {
        this.playlist = playlist;
        this.song = song;
        this.position = position;
    }
}
