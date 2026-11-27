package dk.sea.mytunes.bll.util;

import dk.sea.mytunes.be.Song;

import java.util.ArrayList;
import java.util.List;

public class SongSearcher {
    public List<Song> search(List<Song> searchBase, String query) {
        List<Song> searchResult = new ArrayList<>();
        for (Song song : searchBase) {
            if (compareToSongTitle(query, song) || compareSongToArtist(query, song)) {
                searchResult.add(song);
            }
        }
        return searchResult;
    }

    private boolean compareSongToArtist(String query, Song song) {
        return song.getArtist().toLowerCase().contains(query.toLowerCase());
    }

    private boolean compareToSongTitle(String query, Song song) {
        return song.getTitle().toLowerCase().contains(query.toLowerCase());
    }
}
