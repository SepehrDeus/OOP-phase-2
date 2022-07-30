package fxml;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import jdbc.Database;

import java.io.IOException;
import java.sql.SQLException;

public class MainMenuController {
    public static final int SCENE_NUM = 2;
    private static String userID;

    public static void setUserID(String userID) {
        MainMenuController.userID = userID;
    }


    private static final FXMLLoader fxmlLoader = new FXMLLoader(ControllerContext.class.getResource("main-menu.fxml"));
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

    public static LoginController getLoginController() {
        return fxmlLoader.getController();
    }

    //-----------------------------------------------------------------------------------------------------------------

    @FXML
    private Button postsButton;
    @FXML
    private Button messagesButton;
    @FXML
    private Button groupsButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button editButton;
    @FXML
    private Button logoutButton;

    public void go_to_edit(ActionEvent event) {

    }

    public void go_to_login(ActionEvent event) {
        String id = null;
        try {
            id= Database.user_loggedIn();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            Database.Update_logged_in_no(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setUserID(null);
        ControllerContext.change_scene(LoginController.SCENE_NUM);
    }
}
