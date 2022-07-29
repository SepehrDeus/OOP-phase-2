package fxml;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class LoginController {
    public static final int SCENE_NUM = 0;

    private static final FXMLLoader fxmlLoader = new FXMLLoader(ControllerContext.class.getResource("login.fxml"));
    private static Parent root;
    static {
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static final Scene scene = new Scene(root);
//    private static final String css = ControllerContext.class.getResource("air.css").toExternalForm();
//    static {
//        scene.getStylesheets().addAll(ControllerContext.getTheme(),css);
//    }








    public static FXMLLoader getFxmlLoader() {
        return fxmlLoader;
    }

    public static Parent getRoot() {
        return root;
    }

    public static Scene getScene() {
        return scene;
    }

    public static LoginController getLoginController() {
        return fxmlLoader.getController();
    }
}
