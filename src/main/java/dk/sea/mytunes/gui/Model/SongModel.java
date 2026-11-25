package dk.sea.mytunes.gui.Model;

import dk.sea.mytunes.be.Song;
import dk.sea.mytunes.bll.SongManager;

import java.io.IOException;
import java.sql.SQLException;

public class SongModel {
    private SongManager songManager;

    public SongModel() {
        songManager = new SongManager();
    }

    public Song createSong(Song song) {
        try {
            return songManager.createSong(song);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
