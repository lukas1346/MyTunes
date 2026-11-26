package dk.sea.mytunes.gui.Controller;

import dk.sea.mytunes.be.Playlist;
import dk.sea.mytunes.be.Song;
import dk.sea.mytunes.gui.ErrorDisplay;
import dk.sea.mytunes.gui.Main;
import dk.sea.mytunes.gui.Model.MusicModel;
import dk.sea.mytunes.gui.Model.PlaylistModel;
import dk.sea.mytunes.gui.Model.SongModel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @FXML
    private Label lblCurrentSongTime;
    @FXML
    private Label lblTotalSongLength;
    @FXML
    private Slider sliderTime;
    @FXML
    private Slider sliderVolume;
    @FXML
    private Label lblCurrentSongTitle;
    @FXML
    private Label lblCurrentArtist;
    @FXML
    private TextField txtFilter;
    @FXML
    private Button btnFilter;

    @FXML
    private ListView<Song> lstSongsInPlaylists;
    @FXML
    private Label lblCurrentPlaylistTitle;
    @FXML
    private TableView<Playlist> tblPlaylists;
    @FXML
    private TableColumn<Playlist, String> colPlaylistName;
    @FXML
    private TableColumn<Playlist, Integer> colPlaylistSongs;
    @FXML
    private TableColumn<Playlist, String> colPlaylistTime;
    @FXML
    private TableView<Song> tblSongs;
    @FXML
    private TableColumn<Song, String> colSongName;
    @FXML
    private TableColumn<Song, String> colSongs;
    @FXML
    private TableColumn<Song, String> colCategory;
    @FXML
    private TableColumn<Song, String> colSongTime;
    @FXML
    private Label lblSongAmount;

    private Song selectedSong;
    private Playlist selectedPlaylist;
    private Song selectedSongInPlaylist;

    private SongModel songModel;
    private PlaylistModel playlistModel;
    private MusicModel musicModel;

    private Timeline timeline;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        songModel = new SongModel();
        playlistModel = new PlaylistModel();
        musicModel = new MusicModel();

        colPlaylistName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPlaylistSongs.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getSongs().size()).asObject()
        );
        colPlaylistTime.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().calculatePlaylistDuration())
        );

        colSongName.setCellValueFactory(new PropertyValueFactory<>("title"));
        colSongs.setCellValueFactory(new PropertyValueFactory<>("artist"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colSongTime.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().formatDuration())
        );


        sliderTime.valueProperty().addListener((observableValue, oldValue, newValue) -> {

        });
        sliderVolume.valueProperty().addListener((observableValue, oldValue, newValue) -> {

        });

        tblPlaylists.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                loadPlaylistSongs(newValue);
            }
        });

        lstSongsInPlaylists.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                selectedSongInPlaylist = newValue;
                selectedSong = newValue;
                tblSongs.getSelectionModel().clearSelection();
            }
        }));

        tblSongs.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                selectedSong = newValue;
                selectedSongInPlaylist = null;
                lstSongsInPlaylists.getSelectionModel().clearSelection();
            }
        });

        startSliderTime();
        tblPlaylists.setItems(playlistModel.getObservablePlaylists());
        tblSongs.setItems(songModel.getObservableSongs());
        lblSongAmount.setText(tblSongs.getItems().size() + " Songs");
    }

    @FXML
    private void onNewPlaylistClick(ActionEvent actionEvent) throws Exception {
        Main.loadPlaylistView(true, playlistModel, null);
    }

    @FXML
    private void onEditPlaylistClick(ActionEvent actionEvent) throws Exception {
        Main.loadPlaylistView(false, playlistModel, selectedPlaylist);
    }

    @FXML
    private void onDeletePlaylistClick(ActionEvent actionEvent) {
        if (selectedPlaylist != null) {
            try {
                playlistModel.deletePlaylist(selectedPlaylist);
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    private void onMoveDownSongClick(ActionEvent actionEvent) {

    }

    @FXML
    private void onMoveUpSongClick(ActionEvent actionEvent) {

    }

    @FXML
    private void onDeleteSongPlaylist(ActionEvent actionEvent) {

    }

    @FXML
    private void onNewSongClick(ActionEvent actionEvent) throws Exception {
        Main.loadSongView(true, null, songModel);
    }

    @FXML
    private void onEditSongClick(ActionEvent actionEvent) throws Exception {
        Main.loadSongView(false, selectedSong, songModel);
    }

    @FXML
    private void onDeleteSongClick(ActionEvent actionEvent) {
        if (selectedSong != null) {
            try {
                songModel.deleteSong(selectedSong);
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    private void onPreviousSongClick(ActionEvent actionEvent) {

    }

    @FXML
    private void onPlayPauseClick(ActionEvent actionEvent) {

        if (musicModel.isPlaying() && selectedSong == musicModel.getCurrentSong()) {
            musicModel.pause();
        } else if (selectedSongInPlaylist != null) {
            playSelectedPlaylistSong(selectedSongInPlaylist);
        } else if (selectedSong != null) {
            playSelectedSong(selectedSong);
        } else {
            musicModel.resume();
        }
    }

    @FXML
    private void onNextSongClick(ActionEvent actionEvent) {

    }

    @FXML
    private void OnFilterClick(ActionEvent actionEvent) {

    }

    private void updatePlaylistInfo(Playlist playlist) {
        lblCurrentPlaylistTitle.setText(playlist.getName());
    }

    private void loadPlaylistSongs(Playlist playlist) {
        try {
            lstSongsInPlaylists.setItems(playlistModel.getSongsInPlaylist(playlist));
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
        updatePlaylistInfo(playlist);
    }

    private void playSelectedPlaylistSong(Song song) {
        if (song == null) {
            return;
        }

        int index = lstSongsInPlaylists.getSelectionModel().getSelectedIndex();

        if (index != -1) {
            musicModel.playSongFromPlaylist(
                    lstSongsInPlaylists.getItems(),
                    index
            );

            lblCurrentSongTitle.setText(song.getTitle());
            lblCurrentArtist.setText(song.getArtist());
        }
    }

    private void startSliderTime() {
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    if (musicModel.isPlaying()) {
                        double totalTime = musicModel.getCurrentSong().getDuration();
                        double currentTime = musicModel.getCurrentTime();
                        sliderTime.setValue((currentTime / totalTime) * 1000);
                        lblCurrentSongTime.setText(formatDuration(currentTime));
                        lblTotalSongLength.setText(formatDuration(totalTime));
                    }
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public String formatDuration(double duration) {
        int minutes = (int) duration / 60;
        int seconds = (int) duration % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void playSelectedSong(Song song) {
        if (song == null) {
            return;
        }

        musicModel.playSong(song);

        lblCurrentSongTitle.setText(song.getTitle());
        lblCurrentArtist.setText(song.getArtist());
    }
}
