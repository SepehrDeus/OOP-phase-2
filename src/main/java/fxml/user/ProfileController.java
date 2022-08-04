package fxml.user;

import entity.User;
import fxml.ControllerContext;
import fxml.MainMenuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jdbc.Database;

import java.io.IOException;
import java.sql.SQLException;

public class ProfileController {

    private static String userID;

    public static void setUserID(String userID) {
        ProfileController.userID = userID;
    }


    private static final FXMLLoader fxmlLoader = new FXMLLoader(ControllerContext.class.getResource("profile.fxml"));
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

    public static ProfileController getController() {
        return fxmlLoader.getController();
    }

    //-----------------------------------------------------------------------------------------------------------------

    @FXML
    private ImageView imageView;
    @FXML
    private Label label;
    @FXML
    private Button returnButton;
    @FXML
    private Button deleteAccount;

    public boolean init_profile(String userID) {
        try {
            User user = Database.get_user_by_id(userID);
            label.setText(user.toString());
            if (!user.getProfilePicture().isEmpty()) {
                Image image = new Image(user.getProfilePicture());
                imageView.setImage(image);
            }
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void go_to_mainMenu(ActionEvent event) {
        setUserID(null);
        label.setText("");
        imageView.setImage(null);
        ControllerContext.change_scene(MainMenuController.getScene());
    }

    public void delete_account(ActionEvent event) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete account");
        alert.setHeaderText("You're about to delete this account.");
        alert.setContentText("Are you sure?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            Database.delete_user(userID);
            go_to_mainMenu(event);
            MainMenuController.getController().go_to_login(event);
        }
    }
}
