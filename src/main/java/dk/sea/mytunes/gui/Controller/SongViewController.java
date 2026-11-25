package dk.sea.mytunes.gui.Controller;

import dk.sea.mytunes.be.Song;
import dk.sea.mytunes.bll.MusicPlayer;
import dk.sea.mytunes.gui.Model.SongModel;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SongViewController implements Initializable {
    public Label lblHeading;
    public TextField txtTitle;
    public TextField txtArtist;
    public TextField txtCategory;
    public TextField txtTime;
    public TextField txtFilePath;

    private SongModel songModel;
    private MusicPlayer musicPlayer;
    private boolean isAddMode;

    public SongViewController() {
        songModel = new SongModel();
        musicPlayer = new MusicPlayer();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblHeading.setText(isAddMode ? "Add Song" : "Edit Song");
    }

    public void onChooseClick(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.showOpenDialog(new Stage());
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3"));
        System.out.println(fileChooser.getInitialFileName());;
    }

    public void onCancelClick(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    public void onSaveClick(ActionEvent actionEvent) {
        if (validateFields()) {
//            songModel.createSong(
//                    new Song(txtTitle.getText(),
//                            txtArtist.getText(),
//                            txtCategory.getText(),
//                            txtTime.getText(),
//                            txtFilePath.getText())
//            );
        }
    }

    public void setIsAddMode(boolean bool) {
        isAddMode = bool;
    }

    private boolean validateFields() {
        return !txtTitle.getText().isEmpty() &&
                !txtArtist.getText().isEmpty() &&
                !txtCategory.getText().isEmpty() &&
                !txtTime.getText().isEmpty() &&
                !txtFilePath.getText().isEmpty();
    }
}
