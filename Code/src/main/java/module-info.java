module application {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires java.desktop;
    requires javafx.media;

    opens application to javafx.fxml;
    opens application.controller to javafx.fxml;

    exports application;
}