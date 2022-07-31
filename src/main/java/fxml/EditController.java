package fxml;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jdbc.Database;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditController {
    public static final int SCENE_NUM = 3;
    private static String userID;

    public static void setUserID(String userID) {
        EditController.userID = userID;
    }


    private static final FXMLLoader fxmlLoader = new FXMLLoader(ControllerContext.class.getResource("edit.fxml"));
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

    public static EditController getEditController() {
        return fxmlLoader.getController();
    }

    //-----------------------------------------------------------------------------------------------------------------

    @FXML
    private TextField usernameField;
    @FXML
    private Button usernameButton;
    @FXML
    private Label usernameLabel;
    @FXML
    private TextField passwordField;
    @FXML
    private Button passwordButton;
    @FXML
    private Label passwordLabel;
    @FXML
    private TextArea biographyArea;
    @FXML
    private Button biographyButton;
    @FXML
    private Label biographyLabel;
    @FXML
    private TextField emailField;
    @FXML
    private Button emailButton;
    @FXML
    private Label emailLabel;
    @FXML
    private TextField websiteField;
    @FXML
    private Button websiteButton;
    @FXML
    private Label websiteLabel;
    @FXML
    private ImageView imageView;
    @FXML
    private TextField imageField;
    @FXML
    private Button browseButton;
    @FXML
    private Button imageChangeButton;
    @FXML
    private Label imageLabel;
    @FXML
    private Button mainMenuButton;


    public void change_username(ActionEvent event) throws SQLException {
        String username = usernameField.getText();
        if (!username.isEmpty()) {
            if (Database.update_username(userID, username) > 0) {
                usernameLabel.setText("Username changed successfully.");
                usernameField.setText("");
            }
        }
        else usernameLabel.setText("username can't be empty.");
    }

    public void change_password(ActionEvent event) throws SQLException {
        String password = passwordField.getText();
        if (safe_password(password) && Database.update_password(userID, password)>0) {
            passwordLabel.setText("Password changed successfully.");
            passwordField.setText("");
        }
    }

    private boolean safe_password(String password) {
        if (password.isEmpty()) {
            passwordLabel.setText("password can't be empty.");
            passwordField.setText("");
            return false;
        }

        if (password.length() < 8) {
            passwordLabel.setText("password must have at least 8 characters.");
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
            passwordLabel.setText("password must consist small and capital letters and numbers.");
            passwordField.setText("");
            return false;
        }

        return true;
    }

    public void change_biography(ActionEvent event) throws SQLException {
        String biography = biographyArea.getText();
        if (Database.update_biography(userID, biography) > 0) {
            biographyLabel.setText("Changed successfully.");
            biographyArea.setText("");
        }
    }

    public void change_email(ActionEvent event) throws SQLException {
        String email = emailField.getText();
        if (email.isEmpty()) emailLabel.setText("Field shouldn't be empty.");
        else if (Database.update_email(userID, email) > 0) {
            emailLabel.setText("Changed successfully.");
            emailField.setText("");
        }
    }

    public void change_website(ActionEvent event) throws SQLException {
        String website = websiteField.getText();
        if (website.isEmpty()) websiteLabel.setText("Field shouldn't be empty.");
        else if (Database.update_website(userID, website) > 0) {
            websiteLabel.setText("Changed successfully.");
            websiteField.setText("");
        }
    }

    public void change_profilePicture(ActionEvent event) throws SQLException {
        String profilePicture = imageField.getText();
        if (profilePicture.isEmpty()) imageLabel.setText("Field shouldn't be empty.");
        else if (Database.update_profilePicture(userID, profilePicture) > 0) {
            imageLabel.setText("Changed successfully.");
            imageField.setText("");
        }
    }

    public void browse_image(ActionEvent event) {
        Stage stage = new Stage();
        stage.setTitle("Choose image");
        stage.initModality(Modality.APPLICATION_MODAL);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose image");
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            String profilePicture = file.toURI().toString();
            Image image = new Image(profilePicture);

            imageField.setText(profilePicture);
            imageView.setImage(image);
        }
    }

    public void go_to_mainMenu(ActionEvent event) {
        usernameField.setText("");
        usernameLabel.setText("");
        passwordField.setText("");
        passwordLabel.setText("");
        biographyArea.setText("");
        biographyLabel.setText("");
        emailField.setText("");
        emailLabel.setText("");
        websiteField.setText("");
        websiteLabel.setText("");
        imageView.setImage(null);
        imageField.setText("");
        setUserID(null);
        ControllerContext.change_scene(MainMenuController.SCENE_NUM);
    }
}
