package application.model;

import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;

public class SoundManager {
    private static MediaPlayer backgroundMusic;

    public static void playBackgroundMusic(String path) {
        Media media = new Media(SoundManager.class.getResource(path).toExternalForm());
        backgroundMusic = new MediaPlayer(media);
        backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE); // boucle
        backgroundMusic.setVolume(0.5); // volume de 0.0 à 1.0
        backgroundMusic.play();
    }

    public static void stopBackgroundMusic() {
        if (backgroundMusic != null) backgroundMusic.stop();
    }

    public static void setVolume(double volume) {
        if (backgroundMusic != null) backgroundMusic.setVolume(volume);
    }
    public static void playClickSound(String path, double volume) {
        Media clickSound = new Media(SoundManager.class.getResource(path).toExternalForm());
        MediaPlayer clickPlayer = new MediaPlayer(clickSound);
        clickPlayer.setVolume(volume);
        clickPlayer.play();
    }
}
