package fxml;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import jdbc.Database;

import java.io.IOException;
import java.sql.SQLException;

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
    private TextField idField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;
    @FXML
    private Label idErrLabel;
    @FXML
    private Label pwErrLabel;
    @FXML
    private Button forgotButton;


    public void login(ActionEvent event) throws SQLException {
        idErrLabel.setText("");
        pwErrLabel.setText("");

        if (Database.number_of_users() == 0) {
            idErrLabel.setText("No user has registered yet.");
            return;
        }

        String id = idField.getText();
        String password = passwordField.getText();
        if (correct_id(id) && correct_password(id, password)) {
            idField.setText("");
            passwordField.setText("");
            MainMenuController.setUserID(id);
            ControllerContext.change_scene(MainMenuController.SCENE_NUM);
        }
    }

    private boolean correct_id(String id) throws SQLException {
        if (Database.user_exists(id)) {
            if(Database.Update_logged_in_yes(id)>0){
                return true;
            }
            else {
                idErrLabel.setText("could not update index!");
                idField.setText("");
            }
        }
        else {
            idErrLabel.setText("user doesn't exist.");
            idField.setText("");
        }

        return false;
    }

    private boolean correct_password(String id, String password) throws SQLException {
        if (Database.check_password(id, password)) return true;
        else {
            pwErrLabel.setText("wrong password!");
            passwordField.setText("");
        }
        return false;
    }

    public void go_to_register(ActionEvent event) {
        ControllerContext.change_scene(RegisterController.SCENE_NUM);
    }

    public void go_to_restorePassword(ActionEvent event) {
        String id = idField.getText();
        if (!id.isEmpty()) {
            RestorePasswordController.getRestorePasswordController().setUserID(id);
            ControllerContext.change_scene(RestorePasswordController.SCENE_NUM);
        }
        else idErrLabel.setText("id can't be empty.");
    }
}
