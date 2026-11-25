package dk.sea.mytunes.gui.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SongViewController implements Initializable {
    public Label lblHeading;
    public HBox txtTitle;
    public TextField txtArtist;
    public TextField txtCategory;
    public TextField txtTime;
    public TextField txtFilePath;

    private boolean isAddMode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblHeading.setText(isAddMode ? "Add Song" : "Edit Song");
    }

    public void onChooseClick(ActionEvent actionEvent) {
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
