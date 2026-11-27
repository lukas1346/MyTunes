package dk.sea.mytunes.gui.Controller;

import dk.sea.mytunes.be.Song;
import dk.sea.mytunes.bll.util.FileSaver;
import dk.sea.mytunes.gui.Model.SongModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.function.Consumer;

public class SongViewController {
    @FXML private Label lblHeading;
    @FXML private TextField txtTitle;
    @FXML private TextField txtArtist;
    @FXML private TextField txtCategory;
    @FXML private TextField txtTime;
    @FXML private TextField txtFilePath;

    private boolean isAddMode;
    private Song selectedSong;
    private SongModel songModel;
    private long songDurationInMilliseconds;
    private File pendingAudioFile;

    public void onChooseClick(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Audio File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3"));
        File dataDir = new File("data");
        if (dataDir.exists()) {
            fileChooser.setInitialDirectory(dataDir);
        }

        Window window = ((Node) actionEvent.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(window);
        if (selectedFile != null) {
            pendingAudioFile = selectedFile;
            String relativePath = "data/" + selectedFile.getName();
            txtFilePath.setText(relativePath);
            txtTime.setText("Loading...");
            FileSaver.getDurationInMilliseconds(selectedFile, milliseconds -> {
                songDurationInMilliseconds = milliseconds;
                txtTime.setText(formatMilliseconds(milliseconds));
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
        String filePath = "";

        if (pendingAudioFile != null){
            filePath = FileSaver.copyFileToDataDir(pendingAudioFile);
        }
        if (isAddMode) {
            if (songModel.createSong(new Song(title, artist, category, songDurationInMilliseconds, filePath)) != null) {
                closeWindow(actionEvent);
            }
        } else {
            selectedSong.setTitle(title);
            selectedSong.setArtist(artist);
            selectedSong.setCategory(category);
            selectedSong.setDuration(songDurationInMilliseconds);
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

        if (isAddMode) {
            lblHeading.setText("New Song");
        } else {
            lblHeading.setText("Edit Song");
            txtTitle.setText(selectedSong.getTitle());
            txtArtist.setText(selectedSong.getArtist());
            txtCategory.setText(selectedSong.getCategory());
            txtFilePath.setText(selectedSong.getFilePath());
            this.songDurationInMilliseconds = selectedSong.getDuration();
            txtTime.setText(formatMilliseconds(songDurationInMilliseconds));
        }
    }
    private String formatMilliseconds(long milliseconds) {
        long minutes = milliseconds / 60000;
        long seconds = (milliseconds % 60000) / 1000;
        long millis = milliseconds % 1000;

        return String.format("%d:%02d.%03d", minutes, seconds, millis);
    }
}