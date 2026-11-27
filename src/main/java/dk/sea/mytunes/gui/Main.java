package dk.sea.mytunes.gui;

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
}