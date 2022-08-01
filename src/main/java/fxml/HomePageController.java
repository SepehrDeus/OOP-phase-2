package fxml;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class HomePageController {
    private static String userID;

    public static void setUserID(String userID) {
        HomePageController.userID = userID;
    }

    private static final FXMLLoader fxmlLoader = new FXMLLoader(ControllerContext.class.getResource("HomePage.fxml"));
    private static Parent root;
    static {
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static final Scene scene = new Scene(root);


    public static FXMLLoader getFxmlLoader() {
        return fxmlLoader;
    }

    public static Parent getRoot() {
        return root;
    }

    public static Scene getScene() {
        return scene;
    }

    public static HomePageController getController() {
        return fxmlLoader.getController();
    }

    public void go_to_My_Posts(ActionEvent actionEvent) {
    }

    public void Show_latest_10_post(ActionEvent actionEvent) {
    }

    public void Show_user_suggestion(ActionEvent actionEvent) {
    }

    public void Retun_main_menu(ActionEvent actionEvent) {
    }


    //-----------------------------------------------------------------------------------------------------------------


}
