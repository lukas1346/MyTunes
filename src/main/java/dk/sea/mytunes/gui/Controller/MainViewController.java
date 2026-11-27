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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @FXML
    private ImageView imgPlayPause;
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
    private boolean isUpdatingSlider = false;
    private boolean filtering = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        songModel = new SongModel();
        try {
            playlistModel = new PlaylistModel();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        musicModel = new MusicModel();

        musicModel.setOnSongChange(song -> {
            lblCurrentSongTitle.setText(song.getTitle());
            lblCurrentArtist.setText(song.getArtist());
        });

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
            if (!isUpdatingSlider && musicModel.getCurrentSong() != null) {
                long totalTime = musicModel.getCurrentSong().getDuration();
                long currentTime = Math.round(
                        (newValue.doubleValue() / 1000) * totalTime
                );
                musicModel.setTime(currentTime);
                lblCurrentSongTime.setText(formatDuration(currentTime));
            }
        });
        sliderVolume.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            musicModel.setVolume((double) newValue);
        });

        tblPlaylists.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            selectedPlaylist = newValue;
            if (selectedPlaylist != null) {
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
        lstSongsInPlaylists.setItems(playlistModel.getObservableListSongsInPlaylist());
        tblSongs.setItems(songModel.getObservableSongs());
        musicModel.setAllSongsList(tblSongs.getItems());
        lblSongAmount.setText(tblSongs.getItems().size() + " Songs");
    }

    @FXML
    private void onNewPlaylistClick(ActionEvent actionEvent) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/dk/sea/mytunes/PlaylistView.fxml"));
        Parent root = loader.load();
        PlaylistViewController controller = loader.getController();
        controller.setContext(true, playlistModel, null);
        Stage stage = new Stage();
        stage.setTitle("MyTunes");
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    private void onEditPlaylistClick(ActionEvent actionEvent) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/dk/sea/mytunes/PlaylistView.fxml"));
        Parent root = loader.load();
        PlaylistViewController controller = loader.getController();
        controller.setContext(false, playlistModel, selectedPlaylist);
        Stage stage = new Stage();
        stage.setTitle("MyTunes");
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    private void onDeletePlaylistClick(ActionEvent actionEvent) {
        if (selectedPlaylist != null) {
            try {
                if (showConfirmation(selectedPlaylist.getName())) {
                    playlistModel.deletePlaylist(selectedPlaylist);
                }
            } catch (SQLException | IOException e) {
                ErrorDisplay.displayError(e);
            }
        }
    }

    @FXML
    private void onMoveDownSongClick(ActionEvent actionEvent) {
        if (selectedPlaylist == null || selectedSong == null) {
            return;
        }
        Song songToMove = selectedSong;
        try {
            playlistModel.moveSongPosition(selectedPlaylist, songToMove, false);
            highlightSelectedSong(songToMove);
        } catch (SQLException | IOException e) {
            ErrorDisplay.displayError(e);
        }
    }

    @FXML
    private void onMoveUpSongClick(ActionEvent actionEvent) {
        if (selectedPlaylist == null || selectedSong == null) {
            return;
        }
        Song songToMove = selectedSong;
        try {
            playlistModel.moveSongPosition(selectedPlaylist, selectedSong, true);
            highlightSelectedSong(songToMove);
        } catch (SQLException | IOException e) {
            ErrorDisplay.displayError(e);
        }
    }

    private void highlightSelectedSong(Song songToMove){
        int newIndex = lstSongsInPlaylists.getItems().indexOf(songToMove);
        lstSongsInPlaylists.getSelectionModel().clearSelection();
        lstSongsInPlaylists.getSelectionModel().select(newIndex);
    }

    @FXML
    private void onDeleteSongPlaylist(ActionEvent actionEvent) {
        if (selectedPlaylist == null || selectedSong == null) {
            return;
        }
        try {
            if (showConfirmation(selectedSong.getTitle())) {
                playlistModel.removeSongFromPlaylist(selectedPlaylist, selectedSong);
            }
        } catch (SQLException | IOException e) {
            ErrorDisplay.displayError(e);
        }
    }

    @FXML
    private void onNewSongClick(ActionEvent actionEvent) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/dk/sea/mytunes/SongView.fxml"));
        Parent root = loader.load();
        SongViewController controller = loader.getController();
        controller.setContext(true, null, songModel);
        Stage stage = new Stage();
        stage.setTitle("MyTunes");
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    private void onEditSongClick(ActionEvent actionEvent) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/dk/sea/mytunes/SongView.fxml"));
        Parent root = loader.load();
        SongViewController controller = loader.getController();
        controller.setContext(false, selectedSong, songModel);
        Stage stage = new Stage();
        stage.setTitle("MyTunes");
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    private void onDeleteSongClick(ActionEvent actionEvent) {
        if (selectedSong != null) {
            try {
                if (showConfirmation(selectedSong.getTitle()))
                    songModel.deleteSong(selectedSong);
            } catch (SQLException | IOException e) {
                ErrorDisplay.displayError(e);
            }
        }
    }

    @FXML
    private void onPreviousSongClick(ActionEvent actionEvent) {
        musicModel.playPrevious();
    }

    @FXML
    private void onPlayPauseClick(ActionEvent actionEvent) {
        if (musicModel.isPlaying()) {
            imgPlayPause.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/play.png"))));
            musicModel.pause();
        } else if (musicModel.getCurrentSong() != null && musicModel.getCurrentSong().equals(selectedSong)) {
            musicModel.resume();
            imgPlayPause.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/pause.png"))));
        } else if (selectedSongInPlaylist != null) {
            playSelectedPlaylistSong(selectedSongInPlaylist);
            imgPlayPause.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/pause.png"))));
        } else if (selectedSong != null) {
            playSelectedSong(selectedSong);
            imgPlayPause.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/pause.png"))));
        }
    }

    @FXML
    private void onNextSongClick(ActionEvent actionEvent) {
        musicModel.playNextSong();
    }

    @FXML
    private void OnFilterClick(ActionEvent actionEvent) throws SQLException, IOException {
        if(!filtering) {
            songModel.searchSong(txtFilter.getText());
            btnFilter.setText("Clear");
            filtering = true;
        }
        else {
            songModel.searchSong("");
            btnFilter.setText("Filter");
            txtFilter.clear();
            filtering = false;
        }
    }

    private void updatePlaylistInfo(Playlist playlist) {
        lblCurrentPlaylistTitle.setText(playlist.getName());
    }

    private void loadPlaylistSongs(Playlist playlist) {
        try {
            lstSongsInPlaylists.setItems(playlistModel.getSongsInPlaylist(playlist));
        } catch (SQLException | IOException e) {
            ErrorDisplay.displayError(e);
        }
        updatePlaylistInfo(playlist);
    }

    private void playSelectedPlaylistSong(Song song) {
        if (song == null) {
            return;
        }

        int index = lstSongsInPlaylists.getSelectionModel().getSelectedIndex();

        if (index != -1) {
            musicModel.playSongFromPlaylist(lstSongsInPlaylists.getItems(), index);
        }
    }

    private void startSliderTime() {
        timeline = new Timeline(
                new KeyFrame(Duration.millis(100), event -> {
                    if (musicModel.isPlaying()) {
                        long totalTime = musicModel.getCurrentSong().getDuration();
                        long currentTime = musicModel.getCurrentTime();
                        isUpdatingSlider = true;
                        sliderTime.setValue(
                                ((double) currentTime / totalTime) * 1000
                        );
                        isUpdatingSlider = false;
                        lblCurrentSongTime.setText(formatDuration(currentTime));
                        lblTotalSongLength.setText(formatDuration(totalTime));
                    }
                })
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public String formatDuration(long milliseconds) {
        long minutes = milliseconds / 60000;
        long seconds = (milliseconds % 60000) / 1000;

        return String.format("%02d:%02d", minutes, seconds);
    }

    private void playSelectedSong(Song song) {
        if (song == null) {return;}
        musicModel.playSong(song);
    }

    public void onAddSongToPlaylistClick(ActionEvent actionEvent) {
        if (selectedPlaylist == null || selectedSong == null) {
            return;
        }
        try {
            playlistModel.addSongToPlaylist(selectedPlaylist, selectedSong);
        } catch (SQLException | IOException e) {
            ErrorDisplay.displayError(e);
        }
    }

    private boolean showConfirmation(String itemToDelete) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/dk/sea/mytunes/ConfirmationView.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        ConfirmationViewController controller = loader.getController();
        controller.setContext(itemToDelete);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("MyTunes");
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.showAndWait();
        return controller.getIsConfirm();
    }
}
