package dk.sea.mytunes.gui.Controller;

import dk.sea.mytunes.be.Playlist;
import dk.sea.mytunes.be.Song;
import dk.sea.mytunes.gui.ErrorDisplay;
import dk.sea.mytunes.gui.Main;
import dk.sea.mytunes.gui.Model.PlaylistModel;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
                try {
                    if (selectedPlaylist == newValue) {
                        loadPlaylistSongs(selectedPlaylist);
                    } else {
                        selectedPlaylist = newValue;
                    }
                } catch (Exception e) {
                    ErrorDisplay.displayError(e);
                }
            }
        });

        lstSongsInPlaylists.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (selectedSongInPlaylist == newValue) {
                playSelectedPlaylistSong(selectedSongInPlaylist);
            } else {
                selectedSongInPlaylist = newValue;
            }
        }));

        tblSongs.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                if (selectedSong == newValue) {
                    playSelectedSong(selectedSong);
                } else {
                    selectedSong = newValue;
                }
            }
        });
    }

    public void onNewPlaylistClick(ActionEvent actionEvent) throws Exception {
        Main.loadPlaylistView(true);
    }
    public void onEditPlaylistClick(ActionEvent actionEvent) throws Exception {
        Main.loadPlaylistView(false);
    }

    public void onDeletePlaylistClick(ActionEvent actionEvent) {
    }

    public void onMoveDownSongClick(ActionEvent actionEvent) {
    }

    public void onMoveUpSongClick(ActionEvent actionEvent) {
    }

    public void onDeleteSongPlaylist(ActionEvent actionEvent) {
    }

    public void onNewSongClick(ActionEvent actionEvent) throws Exception {
        Main.loadSongView(true);
    }

    public void onEditSongClick(ActionEvent actionEvent) throws Exception {
        Main.loadSongView(false);
    }

    public void onDeleteSongClick(ActionEvent actionEvent) {

    }

    public void onPreviousSongClick(ActionEvent actionEvent) {
    }

    public void onPlayPauseClick(ActionEvent actionEvent) {
    }

    public void onNextSongClick(ActionEvent actionEvent) {
    }

    public void OnFilterClick(ActionEvent actionEvent) {
    }

    private void loadPlaylistSongs(Playlist playlist) {

    }

    private void playSelectedPlaylistSong(Song song) {

    }

    private void playSelectedSong(Song song) {

    }
}
