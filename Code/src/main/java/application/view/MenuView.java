package application.view;

import application.controller.MapSelectionController;
import application.controller.MenuController;
import application.controller.PersonnalisationController;
import application.controller.PopupCombatController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuView {


    public static void showPersonnalisationWindow(MenuController menuController) {
        try {
            FXMLLoader loader = new FXMLLoader(MenuView.class.getResource("/personnalisation.fxml"));
            Parent root = loader.load();
            PersonnalisationController controller = loader.getController();
            controller.setControleurB(menuController);
            Stage stage = new Stage();
            stage.setTitle("Personnalisation du personnage");
            stage.setScene(new Scene(root, 400, 700));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void showPlayWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(MenuView.class.getResource("/interface.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Grand Line");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showMapSelectionWindow(MenuController menuController) {
        try {
            FXMLLoader loader = new FXMLLoader(MenuView.class.getResource("/map_selection.fxml"));
            Parent root = loader.load();
            MapSelectionController controller = loader.getController();
            controller.setControleurB(menuController);
            Stage stage = new Stage();
            stage.setTitle("Sélection de la carte");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

