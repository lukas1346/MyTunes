package dk.sea.mytunes.dal;

import dk.sea.mytunes.be.Song;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SongDAO implements ISongDataAccess{
    @Override
    public List<Song> getAllSongs() throws IOException, SQLException {
        String sql = "SELECT * FROM dbo.Songs";
        List<Song> songs = new ArrayList<>();
        try (Connection connection = new DBConnector().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                songs.add(createSongFromResult(resultSet));
            }
        }
        songs.sort(Comparator.comparing(Song::getTitle));
        return songs;
    }

    @Override
    public Song createSong(Song song) throws SQLException, IOException {
        String sql = "INSERT INTO dbo.Songs (Title, Artist, Category, Duration, FilePath) VALUES(?, ?, ?, ?, ?)";
        Song createdSong = null;
        try(Connection connection = new DBConnector().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, song.getTitle());
            preparedStatement.setString(2, song.getArtist());
            preparedStatement.setString(3, song.getCategory());
            preparedStatement.setDouble(4, song.getDuration());
            preparedStatement.setString(5, song.getFilePath());

            preparedStatement.executeUpdate();

            try(ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if(resultSet.next()) {
                    createdSong = new Song(resultSet.getInt(1),
                            song.getTitle(),
                            song.getArtist(),
                            song.getCategory(),
                            song.getDuration(),
                            song.getFilePath());
                }
            } catch (SQLException e) {
                throw new RuntimeException("Could not retrieve generated movie id.");
            }
        }
        return createdSong;
    }

    @Override
    public List<Song> searchSongs(String query) throws IOException, SQLException {
        if(!query.isEmpty()) {
            List<Song> songsFound = new ArrayList<>();
            String sql = "SELECT * FROM dbo.Songs WHERE Title LIKE ? OR Artist LIKE ? ORDER BY Title";
            try (Connection connection = new DBConnector().getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, "%" + query + "%");
                preparedStatement.setString(2, "%" + query + "%");

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        songsFound.add(createSongFromResult(resultSet));
                    }
                }
            }
            return songsFound;
        }
        else {
            return getAllSongs();
        }
    }

    @Override
    public void updateSong(Song song) throws SQLException, IOException {
        String sql = "UPDATE dbo.Songs SET Title = ?, Artist = ?, Category = ?, Duration = ?, FilePath = ? WHERE SongId = ?";
        try(Connection connection = new DBConnector().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, song.getTitle());
            preparedStatement.setString(2, song.getArtist());
            preparedStatement.setString(3, song.getCategory());
            preparedStatement.setDouble(4, song.getDuration());
            preparedStatement.setString(5, song.getFilePath());
            preparedStatement.setInt(6, song.getId());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void deleteSong(Song song) throws IOException, SQLException {
        String sql = "DELETE FROM dbo.Songs WHERE SongId = ?";
        try (Connection connection = new DBConnector().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, song.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public Song getSongById(Song song) throws IOException, SQLException {
        String sql = "SELECT * FROM dbo.Songs WHERE SongId = ?";
        Song songById = null;
        try (Connection connection = new DBConnector().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, song.getId());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    songById = createSongFromResult(resultSet);
                }
            }
        }
        return songById;
    }

    private Song createSongFromResult(ResultSet resultSet) throws SQLException {
        return new Song(
                resultSet.getInt("SongId"),
                resultSet.getString("Title"),
                resultSet.getString("Artist"),
                resultSet.getString("Category"),
                resultSet.getInt("Duration"),
                resultSet.getString("FilePath")
        );
    }
}
