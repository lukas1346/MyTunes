package dk.sea.mytunes.gui.Controller;

import dk.sea.mytunes.be.Song;
import dk.sea.mytunes.bll.MusicPlayer;
import dk.sea.mytunes.bll.util.TimeFormatter;
import dk.sea.mytunes.gui.Model.SongModel;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SongViewController {
    public Label lblHeading;
    public TextField txtTitle;
    public TextField txtArtist;
    public TextField txtCategory;
    public TextField txtTime;
    public TextField txtFilePath;

    private boolean isAddMode;
    private Song selectedSong;
    private SongModel songModel;
    private int songDurationInSeconds;

    public void onChooseClick(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Audio File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3"));

        Window window = ((Node) actionEvent.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(window);
        if (selectedFile != null) {
            txtFilePath.setText(selectedFile.getAbsolutePath());
            TimeFormatter.getDurationInSeconds(selectedFile, seconds -> {
                songDurationInSeconds = seconds;
                txtTime.setText(TimeFormatter.formatSeconds(seconds));
            });
        }
    }

    public void onCancelClick(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    public void onSaveClick(ActionEvent actionEvent) throws SQLException, IOException {
        if (!validateFields()) {
            return;
        }
        String title = txtTitle.getText().trim();
        String artist = txtArtist.getText().trim();
        String category = txtCategory.getText().trim();
        String filePath = txtFilePath.getText().trim();
        if (isAddMode) {
            if (songModel.createSong(new Song(title, artist, category, songDurationInSeconds, filePath)) != null) {
                closeWindow(actionEvent);
            }
        } else {
            selectedSong.setTitle(title);
            selectedSong.setArtist(artist);
            selectedSong.setCategory(category);
            selectedSong.setDuration(songDurationInSeconds);
            selectedSong.setFilePath(filePath);
            songModel.updateSong(selectedSong);
            closeWindow(actionEvent);
        }

    }

    private boolean validateFields() {
        return !txtTitle.getText().isEmpty() &&
                !txtArtist.getText().isEmpty() &&
                !txtCategory.getText().isEmpty() &&
                !txtTime.getText().isEmpty() &&
                !txtFilePath.getText().isEmpty();
    }

    private void closeWindow(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    public void setContext(boolean isAddMode, Song selectedSong, SongModel songModel) {
        this.isAddMode = isAddMode;
        this.selectedSong = selectedSong;
        this.songModel = songModel;

        if (isAddMode){
            lblHeading.setText("New Song");
        } else {
            lblHeading.setText("Edit Song");
            txtTitle.setText(selectedSong.getTitle());
            txtArtist.setText(selectedSong.getArtist());
            txtCategory.setText(selectedSong.getCategory());
            txtFilePath.setText(selectedSong.getFilePath());
            this.songDurationInSeconds = selectedSong.getDuration();
            txtTime.setText(TimeFormatter.formatSeconds(songDurationInSeconds));
        }
    }
}
