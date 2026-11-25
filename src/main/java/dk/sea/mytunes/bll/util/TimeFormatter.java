package dk.sea.mytunes.bll.util;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.function.Consumer;

public class TimeFormatter {
    public static void getDurationInSeconds(File file, Consumer<Integer> onDurationReady) {
        Media media = new Media(file.toURI().toString());
        MediaPlayer tempPlayer = new MediaPlayer(media);

        tempPlayer.setOnReady(() -> {
            int seconds = (int) Math.round(media.getDuration().toSeconds());
            onDurationReady.accept(seconds);
            tempPlayer.dispose();
        });
    }

    public static String formatSeconds(int totalSeconds) {
        if (totalSeconds < 0) {
            totalSeconds = 0;
        }
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        }
        return String.format("%d:%02d", minutes, seconds);
    }

}
