package application.model;

import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;

// cette classe permet de mettre une musique d'ambiance et des sons pour les boutons

public class SoundManager {
    private static MediaPlayer backgroundMusic;

    // pour jouer une musique
    public static void playBackgroundMusic(String path) {
        stopBackgroundMusic();
        Media media = new Media(SoundManager.class.getResource(path).toExternalForm());
        backgroundMusic = new MediaPlayer(media);
        backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE); // boucle
        backgroundMusic.setVolume(0.15); // volume de 0.0 à 1.0
        backgroundMusic.play();
    }

    //pour stopper la musique
    public static void stopBackgroundMusic() {
        if (backgroundMusic != null) backgroundMusic.stop();
    }

    //pour faire un son
    public static void playClickSound(String path, double volume) {
        Media clickSound = new Media(SoundManager.class.getResource(path).toExternalForm());
        MediaPlayer clickPlayer = new MediaPlayer(clickSound);
        clickPlayer.setVolume(volume);
        clickPlayer.play();
    }
}
