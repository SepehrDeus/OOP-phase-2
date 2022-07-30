package fxml;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import jdbc.Database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RestorePasswordController {
    public static final int SCENE_NUM = 4;
    private String userID;

    public void setUserID(String userID) {
        this.userID = userID;
    }

    private static final FXMLLoader fxmlLoader = new FXMLLoader(ControllerContext.class.getResource("restore-password.fxml"));
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

    public static RestorePasswordController getRestorePasswordController() {
        return fxmlLoader.getController();
    }

    //-----------------------------------------------------------------------------------------------------------------

    @FXML
    private TextField petField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button restoreButton;
    @FXML
    private Label petErrLabel;
    @FXML
    private Label pwErrLabel;
    @FXML
    private Button cancelButton;

    public void restore(ActionEvent event) throws SQLException {
        petErrLabel.setText("");
        pwErrLabel.setText("");

        String pet = petField.getText();
        String newPass = passwordField.getText();

        if (safe_pet(pet) && safe_password(newPass)) {
            if (Database.update_password(userID, newPass) > 0) {
                go_to_login(event);
            }
        }
    }

    private boolean safe_pet(String pet) throws SQLException {
        if(!Database.check_pet(userID).equals(pet)){
            petErrLabel.setText("Wrong answer.");
            petField.setText("");
            return false;
        }

        return true;
    }

    private boolean safe_password(String password) {
        if (password.isEmpty()) {
            pwErrLabel.setText("password can't be empty.");
            passwordField.setText("");
            return false;
        }

        if (password.length() < 8) {
            pwErrLabel.setText("password must have at least 8 characters.");
            passwordField.setText("");
            return false;
        }

        Pattern pattern1 = Pattern.compile("[A-Z]");
        Pattern pattern2 = Pattern.compile("[a-z]");
        Pattern pattern3 = Pattern.compile("[0-9]");
        Matcher matcher1 = pattern1.matcher(password);
        Matcher matcher2 = pattern2.matcher(password);
        Matcher matcher3 = pattern3.matcher(password);
        if (!matcher1.find() || !matcher2.find() || !matcher3.find()) {
            pwErrLabel.setText("password must consist small and capital letters and numbers.");
            passwordField.setText("");
            return false;
        }

        return true;
    }

    public void go_to_login(ActionEvent event) {
        setUserID(null);
        ControllerContext.change_scene(LoginController.SCENE_NUM);
    }
}
