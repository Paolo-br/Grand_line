package application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class PopupCombatController {

    @FXML private Button btnOui;
    @FXML private Button btnNon;
    @FXML private ImageView bossImage;

    private Runnable onAccept;

    public void initialize() {
        Image imOui = new Image("oui.png");
        ImageView ouiView = new ImageView(imOui);
        ouiView.setFitWidth(180);
        ouiView.setFitHeight(60);
        btnOui.setGraphic(ouiView);
        btnOui.setText("");
        btnOui.setStyle("-fx-background-color: transparent;");
        btnOui.setOnMousePressed(e-> {
            ouiView.setScaleX(0.95);
            ouiView.setScaleY(0.95);
        });
        btnOui.setOnMouseReleased(e-> {
            ouiView.setScaleX(1.0);
            ouiView.setScaleY(1.0);
        });

        Image imNon = new Image("non.png");
        ImageView nonView = new ImageView(imNon);
        nonView.setFitWidth(180);
        nonView.setFitHeight(60);
        btnNon.setGraphic(nonView);
        btnNon.setText("");
        btnNon.setStyle("-fx-background-color: transparent;");
        btnNon.setOnMousePressed(e-> {
            nonView.setScaleX(0.95);
            nonView.setScaleY(0.95);
        });
        btnNon.setOnMouseReleased(e-> {
            nonView.setScaleX(1.0);
            nonView.setScaleY(1.0);
        });

        btnOui.setOnAction(e -> {
            if (onAccept != null) onAccept.run();
            closeWindow();
        });
        btnNon.setOnAction(e -> closeWindow());
    }

    private void closeWindow() {
        Stage stage = (Stage) btnOui.getScene().getWindow();
        stage.close();
    }

    public void setOnAccept(Runnable onAccept) {
        this.onAccept = onAccept;
    }

    public void setBossImage(String imagePath) {
        bossImage.setImage(new javafx.scene.image.Image(imagePath));
    }
}
