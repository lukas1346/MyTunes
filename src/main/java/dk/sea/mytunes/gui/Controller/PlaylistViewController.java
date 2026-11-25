package dk.sea.mytunes.gui.Controller;

import dk.sea.mytunes.be.Playlist;
import dk.sea.mytunes.gui.Model.PlaylistModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PlaylistViewController implements Initializable {
    @FXML
    private Label lblHeading;
    @FXML
    private TextField txtName;

    private PlaylistModel playlistModel;

    // boolean to determine whether the user is adding a song or editing a song.
    private boolean isAddMode;

    public PlaylistViewController() {
        playlistModel = new PlaylistModel();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblHeading.setText(isAddMode ? "Add Playlist" : "Edit Playlist");
    }

    public void onCancelClick(ActionEvent actionEvent) {
        closeWindow(actionEvent);
    }

    public void onSaveClick(ActionEvent actionEvent) throws SQLException, IOException {
        if (validateFields()) {
            Playlist playlist = new Playlist(txtName.getText());
            if (isAddMode) {
                if (playlistModel.createPlaylist(playlist)) {
                    closeWindow(actionEvent);
                }
            } else {
                playlistModel.updatePlaylist(playlist);
                closeWindow(actionEvent);
            }
        }
    }

    public void setIsAddMode(boolean bool) {
        isAddMode = bool;
    }

    private boolean validateFields() {
        return !txtName.getText().isEmpty();
    }

    private void closeWindow(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }
}
