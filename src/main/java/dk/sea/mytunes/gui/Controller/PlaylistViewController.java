package dk.sea.mytunes.gui.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class PlaylistViewController implements Initializable {
    @FXML private Label lblHeading;
    @FXML private TextField txtName;

    // boolean to determine whether the user is adding a song or editing a song.
    private boolean isAddMode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblHeading.setText(isAddMode ? "Add Playlist" : "Edit Playlist");
    }

    public void onCancelClick(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    public void onSaveClick(ActionEvent actionEvent) {
    }

    public void setIsAddMode(boolean bool){
        isAddMode = bool;
    }
}
