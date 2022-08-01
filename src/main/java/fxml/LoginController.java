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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginController {
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

    public static LoginController getController() {
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
            ControllerContext.change_scene(MainMenuController.getScene());
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
        ControllerContext.change_scene(RegisterController.getScene());
    }

    public void go_to_restorePassword(ActionEvent event) {
        String id = idField.getText();
        if (!id.isEmpty()) {
            RestorePasswordController.setUserID(id);
            ControllerContext.change_scene(RestorePasswordController.getScene());
        }
        else idErrLabel.setText("id can't be empty.");
    }

    public static class RestorePasswordController {
        private static String userID;

        public static void setUserID(String userID) {
            RestorePasswordController.userID = userID;
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

        public static RestorePasswordController getController() {
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
            ControllerContext.change_scene(LoginController.getScene());
        }
    }
}
