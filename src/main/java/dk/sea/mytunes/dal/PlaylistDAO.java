package dk.sea.mytunes.dal;

import dk.sea.mytunes.be.Playlist;
import dk.sea.mytunes.be.Song;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlaylistDAO implements IPlaylistDataAccess {
    @Override
    public List<Playlist> getAllPlaylists() throws IOException, SQLException {
        String sql = "SELECT PlaylistId, Name FROM dbo.Playlists";
        List<Playlist> playlists = new ArrayList<>();
        try (Connection connection = new DBConnector().getConnection()) {
            Statement statement = connection.createStatement();
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
        try(Connection connection = new DBConnector().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, playlist.getName());

            preparedStatement.executeUpdate();


            try(ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
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
        try(Connection connection = new DBConnector().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, playlist.getName());
            preparedStatement.setInt(2, playlist.getPlaylistId());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void deletePlaylist(Playlist playlist) throws IOException, SQLException {
        String sql = "DELETE FROM dbo.Playlists WHERE PlaylistId = ?";
        try (Connection connection = new DBConnector().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, playlist.getPlaylistId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<Song> getSongsInPlaylist(int playlistId) throws IOException, SQLException {
        String sql =  """
            SELECT * FROM Songs s
            INNER JOIN PlaylistSongs p ON s.SongId = p.SongId
            WHERE PlaylistId = ?
            ORDER BY position
            """;
        List<Song> playlistSongs = new ArrayList<>();
        try (Connection connection = new DBConnector().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, playlistId);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    playlistSongs.add(createSongFromResult(resultSet));
                }
            }
        }
        return playlistSongs;
    }

    @Override
    public void addSongToPlaylist(int playlistId, int songId) throws SQLException, IOException {
        int position = getLastPosition(playlistId)+1;

        String sql = "INSERT INTO PlaylistSongs (playlistId, songId, position) VALUES (?, ?, ?)";

        try (Connection connection = new DBConnector().getConnection()) {
             PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, playlistId);
            preparedStatement.setInt(2, songId);
            preparedStatement.setInt(3, position);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void removeSongFromPlaylist(int playlistId, int songId) throws IOException, SQLException {
        String sql = "DELETE FROM PlaylistSongs WHERE playlistId = ? AND songId = ?";

        try (Connection connection = new DBConnector().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, playlistId);
            preparedStatement.setInt(2, songId);
            preparedStatement.executeUpdate();
        }
        reorderPlaylistSongs(playlistId);
    }

    @Override
    public void updateSongPosition(int playlistId, int songId, boolean up) throws IOException, SQLException {
        String sql = """
                 SET NOCOUNT ON; -- Stops execution from returning messages
                 SET XACT_ABORT ON; -- If something goes wrong in the middle of the execution it rolls back
                
                 BEGIN TRANSACTION;
                 
                 DECLARE @SelectedPosition INT;
                 DECLARE @PlaylistId INT = ?;
                 DECLARE @Up BIT = ?;
                 DECLARE @LastPosition INT = ?;
                 DECLARE @SwapPosition INT;
                 
                 SELECT @SelectedPosition = Position
                 FROM PlaylistSongs
                 WHERE PlaylistId = @PlaylistId
                 AND SongId = ?
                 
                 
                 IF @SelectedPosition IS NOT NULL
                 BEGIN
                     IF @Up = 1 AND @SelectedPosition > 1
                     BEGIN
                         SET @SwapPosition = @SelectedPosition - 1;
                     END
                     ELSE IF @Up = 0 AND @SelectedPosition < @LastPosition
                     BEGIN
                         SET @SwapPosition = @SelectedPosition + 1;
                     END
                     IF @SwapPosition IS NOT NULL
                     BEGIN
                         UPDATE PlaylistSongs
                         SET Position = @LastPosition + 1
                         WHERE PlaylistId = @PlaylistId
                         AND Position = @SelectedPosition;

                         UPDATE PlaylistSongs
                         SET Position = @SelectedPosition
                         WHERE PlaylistId = @PlaylistId
                         AND Position = @SwapPosition;

                         UPDATE PlaylistSongs
                         SET Position = @SwapPosition
                         WHERE PlaylistId = @PlaylistId
                         AND Position = @LastPosition + 1
                     END
                 END
                 COMMIT TRANSACTION;
                 """;

            try(Connection connection = new DBConnector().getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setInt(1, playlistId);
                preparedStatement.setBoolean(2, up);
                preparedStatement.setInt(3, getLastPosition(playlistId));
                preparedStatement.setInt(4, songId);
                preparedStatement.execute();
            }
        }

    private void reorderPlaylistSongs(int playlistId) throws IOException, SQLException {
        String sql = """
                        SET NOCOUNT ON; -- Stops execution from returning messages
                        SET XACT_ABORT ON; -- If something goes wrong in the middle of the execution it rolls back
                
                        DECLARE @PlaylistId INT = ?; -- declares a variable

                        BEGIN TRANSACTION; -- all the CRUD should be in one uncommited block
                
                        UPDATE PlaylistSongs
                        SET Position = Position + 1000000 -- Avoids unique constraints
                        WHERE PlaylistId = @PlaylistId;
                
                        WITH OrderedSongs AS (
                            SELECT
                                SongId,
                                -- ROW_NUMBER() orders rows into 1, 2 ,3, ...
                                -- Over just asks in what order the rows should be numbered by
                                ROW_NUMBER() over (
                                    ORDER BY Position
                                    ) AS NewPosition
                            FROM PlaylistSongs
                            WHERE PlaylistId = @PlaylistId
                        ) -- Ordered songs creates a new column called NewPosition that has all the new numbered rows
                        UPDATE ps -- updates the PlaylistSongs table
                        SET Position = os.NewPosition -- changes the position in PlaylistSongs to the new ordered positions
                        FROM PlaylistSongs ps
                        INNER JOIN OrderedSongs os
                        ON ps.SongId = os.SongId -- tells how the rows should match from os and ps
                        WHERE ps.PlaylistId = @PlaylistId;
                
                        COMMIT TRANSACTION;
                """;

        try(Connection connection = new DBConnector().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, playlistId);
            preparedStatement.execute();
        }
    }

    private int getLastPosition(int playlistId) throws SQLException, IOException {
        String sql = "SELECT MAX(Position) FROM PlaylistSongs WHERE PlaylistId = ?";
        try (Connection connection = new DBConnector().getConnection()) {
             PreparedStatement preparedStatement = connection.prepareStatement(sql);

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