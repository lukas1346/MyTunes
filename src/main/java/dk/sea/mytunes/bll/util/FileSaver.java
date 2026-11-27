package dk.sea.mytunes.bll.util;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.function.Consumer;

public class FileSaver {
    private static final String DATA_DIR = "data";

    public static String copyFileToDataDir(File selectedFile) throws IOException {
        Path targetDir = Paths.get(DATA_DIR);
        Files.createDirectories(targetDir);

        Path selectedPath = targetDir.resolve(selectedFile.getName());
        if (!selectedFile.toPath().toAbsolutePath().equals(selectedPath.toAbsolutePath())) {
            Files.copy(selectedFile.toPath(), selectedPath, StandardCopyOption.REPLACE_EXISTING);
        }
        return DATA_DIR+"/"+selectedFile.getName();
    }

    public static void getDurationInMilliseconds(File file, Consumer<Long> onDurationReady) {
        Media media = new Media(file.toURI().toString());
        MediaPlayer tempPlayer = new MediaPlayer(media);

        tempPlayer.setOnReady(() -> {
            long milliseconds = Math.round(media.getDuration().toMillis());
            onDurationReady.accept(milliseconds);
            tempPlayer.dispose();
        });

        tempPlayer.setOnError(() -> {
            System.out.println("Error loading audio: " + tempPlayer.getError());
            tempPlayer.dispose();
        });
    }
}
