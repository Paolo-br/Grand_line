package application;
import application.controller.MenuController;
import application.model.SoundManager;
import application.view.MenuView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main_menu.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root,1000,700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Grand Line");
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        primaryStage.show();
        SoundManager.playBackgroundMusic("/musiqueOnePiece.mp3");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
