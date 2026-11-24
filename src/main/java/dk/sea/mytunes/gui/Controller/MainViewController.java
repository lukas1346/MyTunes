package dk.sea.mytunes.gui.Controller;

import dk.sea.mytunes.gui.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class MainViewController {
    @FXML private Label lblCurrentSongTitle;
    @FXML private Label lblCurrentArtist;
    @FXML private TextField txtFilter;
    @FXML private Button btnFilter;
    @FXML private TableView tblPlaylists;
    @FXML private Label lblCurrentPlaylistTitle;
    @FXML private TableView tblPlaylistSongs;;
    @FXML private TableView tblSongs;
    @FXML private Label lblPlaylistLength;

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
}
