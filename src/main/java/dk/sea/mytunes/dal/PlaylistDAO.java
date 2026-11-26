package dk.sea.mytunes.dal;

import dk.sea.mytunes.be.Playlist;
import dk.sea.mytunes.be.Song;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlaylistDAO implements IPlaylistDataAccess {
    DBConnector dbConnector = new DBConnector();

    public PlaylistDAO() throws IOException {
    }

    @Override
    public List<Playlist> getAllPlaylists() throws IOException, SQLException {
        String sql = "SELECT PlaylistId, Name FROM dbo.Playlists";
        List<Playlist> playlists = new ArrayList<>();
        try (Connection connection = dbConnector.getConnection();
             Statement statement = connection.createStatement();) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                playlists.add(createPlaylistFromResult(resultSet));
            }
        }
        playlists.sort(Comparator.comparing(Playlist::getName));
        return playlists;
    }

    @Override
    public Playlist createPlaylist(Playlist playlist) throws IOException, SQLException {
        String sql = "INSERT INTO dbo.Playlists (Name) VALUES(?)";
        Playlist createdPlaylist = null;
        try (Connection connection = dbConnector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, playlist.getName());

            preparedStatement.executeUpdate();

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    createdPlaylist = new Playlist(resultSet.getInt(1), playlist.getName());
                }
            }
        }
        return createdPlaylist;
    }

    @Override
    public void updatePlaylist(Playlist playlist) throws IOException, SQLException {
        String sql = "UPDATE dbo.Playlists SET Name = ? WHERE PlaylistId = ?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, playlist.getName());
            preparedStatement.setInt(2, playlist.getId());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void deletePlaylist(Playlist playlist) throws IOException, SQLException {
        String sql = "DELETE FROM dbo.Playlists WHERE PlaylistId = ?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, playlist.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<Song> getSongsInPlaylist(int playlistId) throws IOException, SQLException {
        String sql = """
                SELECT * FROM Songs s
                INNER JOIN PlaylistSongs p ON s.SongId = p.SongId
                WHERE PlaylistId = ?
                ORDER BY position
                """;
        List<Song> playlistSongs = new ArrayList<>();
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, playlistId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    playlistSongs.add(createSongFromResult(resultSet));
                }
            }
        }
        return playlistSongs;
    }

    @Override
    public void addSongToPlaylist(int playlistId, int songId) throws SQLException, IOException {

        String sql = "INSERT INTO PlaylistSongs (playlistId, songId, position) VALUES (?, ?, ?)";

        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            int position = getLastPosition(connection, playlistId) + 1;

            preparedStatement.setInt(1, playlistId);
            preparedStatement.setInt(2, songId);
            preparedStatement.setInt(3, position);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void removeSongFromPlaylist(int playlistId, int songId) throws IOException, SQLException {
        String sql = "DELETE FROM PlaylistSongs WHERE playlistId = ? AND songId = ?";

        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, playlistId);
            preparedStatement.setInt(2, songId);
            preparedStatement.executeUpdate();
            reorderPlaylistSongs(connection, playlistId);
        }
    }

    @Override
    public void swapSongPosition(int playlistId, int originalPosition, int positionToSwapWith) throws IOException, SQLException {
        String sql = "UPDATE dbo.PlaylistSongs SET Position = ? WHERE PlaylistId = ? AND toPosition = ?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);

            int tempPosition = getLastPosition(connection, playlistId);
            updatePosition(ps, playlistId, originalPosition, tempPosition);
            updatePosition(ps, playlistId, positionToSwapWith, originalPosition);
            updatePosition(ps, playlistId, tempPosition, positionToSwapWith);

            connection.commit();
        }
    }

    private void updatePosition(PreparedStatement ps, int playlistId, int oldPosition, int newPosition) throws SQLException {
        ps.setInt(1, newPosition);
        ps.setInt(2, playlistId);
        ps.setInt(3, oldPosition);
    }

    private void reorderPlaylistSongs(Connection connection, int playlistId) throws IOException, SQLException {
        List<Song> songs = getSongsInPlaylist(playlistId);
        String sql = "UPDATE dbo.PlaylistSongs SET Position = ? WHERE PlaylistId = ? AND SongId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < songs.size(); i++) {
                preparedStatement.setInt(1, i + 1);
                preparedStatement.setInt(2, playlistId);
                preparedStatement.setInt(3, songs.get(i).getId());
                preparedStatement.executeUpdate();
            }
        }
    }

    private int getLastPosition(Connection connection, int playlistId) throws SQLException, IOException {
        String sql = "SELECT MAX(Position) FROM PlaylistSongs WHERE PlaylistId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {

            preparedStatement.setInt(1, playlistId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        return 0;
    }

    private Playlist createPlaylistFromResult(ResultSet resultSet) throws SQLException {
        return new Playlist(
                resultSet.getInt("PlaylistId"),
                resultSet.getString("Name")
        );
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