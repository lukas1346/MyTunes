package dk.sea.mytunes.bll.util;

import dk.sea.mytunes.be.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SongSearcher {
    public List<Song> search(List<Song> searchBase, String query) {
        List<Song> searchResult = new ArrayList<>();

        for (Song song : searchBase) {
            if (compareToMovieTitle(query, song) || compareMovieToArtist(query, song)) {
                searchResult.add(song);
            }
        }

        return searchResult;
    }

    private boolean compareMovieToArtist(String query, Song song) {
        return song.getArtist().toLowerCase().contains(query.toLowerCase());
    }

    private boolean compareToMovieTitle(String query, Song movie) {
        return movie.getTitle().toLowerCase().contains(query.toLowerCase());
    }
}
