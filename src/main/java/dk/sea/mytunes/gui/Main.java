package dk.sea.mytunes.gui;

import dk.sea.mytunes.be.Playlist;
import dk.sea.mytunes.be.Song;
import dk.sea.mytunes.gui.Controller.PlaylistViewController;
import dk.sea.mytunes.gui.Controller.SongViewController;
import dk.sea.mytunes.gui.Model.PlaylistModel;
import dk.sea.mytunes.gui.Model.SongModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("/dk/sea/mytunes/MainView.fxml")));
        stage.setTitle("MyTunes");
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.setMaximized(true);
        stage.show();
    }

    public static void loadConfirmationView() throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("/dk/sea/mytunes/ConfirmationView.fxml")));
        Stage stage = new Stage();
        stage.setTitle("MyTunes");
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.show();
    }
}