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
    private Playlist selectedPlaylist;
    private boolean isAddMode;

    public PlaylistViewController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (isAddMode) {
            lblHeading.setText("Add Song");
        } else {
            lblHeading.setText("Edit Song");
            txtName.setText(selectedPlaylist != null ? selectedPlaylist.getName() : "");
        }
    }

    public void onCancelClick(ActionEvent actionEvent) {
        closeWindow(actionEvent);
    }

    public void onSaveClick(ActionEvent actionEvent) throws SQLException, IOException {
        if (validateFields()) {
            if (isAddMode) {
                Playlist playlist = new Playlist(txtName.getText());
                if (playlistModel.createPlaylist(playlist) != null) {
                    closeWindow(actionEvent);
                }
            } else {
                selectedPlaylist.setName(txtName.getText());
                playlistModel.updatePlaylist(selectedPlaylist);
                closeWindow(actionEvent);
            }
        }
    }

    private boolean validateFields() {
        return !txtName.getText().isEmpty();
    }

    private void closeWindow(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    public void setContext(boolean isAddMode, PlaylistModel playlistModel, Playlist selectedPlaylist) {
        this.isAddMode = isAddMode;
        this.playlistModel = playlistModel;
        this.selectedPlaylist = selectedPlaylist;

        if (isAddMode) {
            lblHeading.setText("New Playlist");
            txtName.clear();
        } else {
            lblHeading.setText("Edit Playlist");
            txtName.setText(selectedPlaylist != null ? selectedPlaylist.getName() : "");
        }
    }
}
