package fxml;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jdbc.Database;

import java.sql.SQLException;

public class ControllerContext extends Application {
    private static Stage mainStage;

    @Override
    public void start(Stage stage) {
        mainStage = stage;
        mainStage.setResizable(false);
        Image icon = new Image("icon.png");
        mainStage.getIcons().add(icon);
        mainStage.setTitle("oopgram");
        mainStage.setScene(LoginController.getScene());
        mainStage.show();
        mainStage.setOnCloseRequest(
                event -> {
                    event.consume();
                    logout();
                    exit();
                }
        );
    }

    public static void run() {
        launch();
    }

    private static void exit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("exit");
        alert.setHeaderText("You're about to close the program.");
        alert.setContentText("Are you sure?");
        if (alert.showAndWait().get()== ButtonType.OK) mainStage.close();
    }

    private static void logout() {
        String id = null;
        try {
            id = Database.user_loggedIn();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            Database.Update_logged_in_no(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void change_scene(Scene scene) {
        mainStage.setScene(scene);
    }
}