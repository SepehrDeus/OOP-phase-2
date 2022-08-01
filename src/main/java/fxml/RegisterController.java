package fxml;

import entity.User;
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

public class RegisterController {


    private static final FXMLLoader fxmlLoader = new FXMLLoader(ControllerContext.class.getResource("register.fxml"));
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

    public static RegisterController getController() {
        return fxmlLoader.getController();
    }

    //-----------------------------------------------------------------------------------------------------------------

    @FXML
    private TextField idField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label idErrLabel;
    @FXML
    private Label unErrLabel;
    @FXML
    private Label pwErrLabel;
    @FXML
    private CheckBox businessBox;
    @FXML
    private TextArea biographyArea;
    @FXML
    private TextField emailField;
    @FXML
    private TextField websiteField;
    @FXML
    private Button registerButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label endLabel;
    @FXML
    private TextField petField;
    @FXML
    private Label petErrLabel;


    public void register(ActionEvent event) throws SQLException {
        idErrLabel.setText("");
        unErrLabel.setText("");
        pwErrLabel.setText("");
        petErrLabel.setText("");
        endLabel.setText("");
        String id = idField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        boolean business = businessBox.isSelected();
        String biography = biographyArea.getText();
        String email = emailField.getText();
        String website = websiteField.getText();
        String pet = petField.getText();

        if (safe_id(id) && safe_username(username) && safe_password(password) && safe_pet(pet)) {
            if (Database.add_user(new User(id, username, password, business, biography, email, website, pet)) > 0) {
                idField.setText("");
                usernameField.setText("");
                passwordField.setText("");
                businessBox.setSelected(false);
                biographyArea.setText("");
                emailField.setText("");
                websiteField.setText("");
                petField.setText("");
                endLabel.setText("Registered successfully.");
            }
            else endLabel.setText("Something went wrong!\nPlease try again.");
        }
    }

    private boolean safe_id(String id) throws SQLException {
        if (id.isEmpty()){
            idErrLabel.setText("id can't be empty.");
            idField.setText("");
            return false;
        }

        Pattern pattern = Pattern.compile("[^\\w_]");
        Matcher matcher = pattern.matcher(id);
        if (matcher.find()) {
            idErrLabel.setText("Invalid character.");
            idField.setText("");
            return false;
        }

        if (Database.user_exists(id)) {
            idErrLabel.setText("This id already exist.");
            idField.setText("");
            return false;
        }

        return true;
    }

    private boolean safe_username(String username) {
        if (username.isEmpty()) {
            unErrLabel.setText("username can't be empty.");
            usernameField.setText("");
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

    private boolean safe_pet(String pet) {
        if (pet.isEmpty()) {
            petErrLabel.setText("pet can't be empty.");
            petField.setText("");
            return false;
        }

        return true;
    }

    public void go_to_login(ActionEvent event) {
        ControllerContext.change_scene(LoginController.getScene());
    }
}
