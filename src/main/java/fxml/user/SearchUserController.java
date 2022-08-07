package fxml.user;

import entity.User;
import fxml.ControllerContext;
import fxml.MainMenuController;
import fxml.message.SendMessage2Controller;
import fxml.post.ShoPostsUserController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jdbc.Database;

import java.io.IOException;
import java.sql.SQLException;

public class SearchUserController {

    private static String showedUserID;
    private static String watcherUserID;
    private static boolean isFollowed;

    public static void setShowedUserID(String showedUserID) {
        SearchUserController.showedUserID = showedUserID;
    }

    public static void setWatcherUserID(String watcherUserID) {
        SearchUserController.watcherUserID = watcherUserID;
    }

    public static void setIsFollowed(boolean isFollowed) {
        SearchUserController.isFollowed = isFollowed;
    }

    private static final FXMLLoader fxmlLoader = new FXMLLoader(ControllerContext.class.getResource("search-user.fxml"));
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

    public static SearchUserController getController() {
        return fxmlLoader.getController();
    }

    //-----------------------------------------------------------------------------------------------------------------

    @FXML
    private TextField idField;
    @FXML
    private Button searchButton;
    @FXML
    private Label idErrLabel;
    @FXML
    private ImageView imageView;
    @FXML
    private Label label;
    @FXML
    private Button followButton;
    @FXML
    private Button postsButton;
    @FXML
    private Button sendButton;
    @FXML
    private Button mainMenuButton;


    public void search_user(ActionEvent event) throws SQLException {
        String showedUserID = idField.getText().trim();
        if (!showedUserID.isEmpty()) {
            if (Database.user_exists(showedUserID)) {
                    setShowedUserID(showedUserID);
                    setIsFollowed(Database.isFollowed(watcherUserID, showedUserID));
                    idField.setText("");
                    idErrLabel.setText("");

                    User user = Database.get_user_by_id(showedUserID);
                    if (!user.getProfilePicture().isEmpty()) {
                        Image image = new Image(user.getProfilePicture());
                        imageView.setImage(image);
                    }
                    label.setText(user.toString());
                    if (isFollowed) followButton.setText("Unfollow");
                    else followButton.setText("Follow");
                    followButton.setVisible(true);
                    postsButton.setVisible(true);
                    sendButton.setVisible(true);
            }
            else idErrLabel.setText("User doesn't exist.");
        }
        else idErrLabel.setText("Please fill the field.");
    }

    public void update_follow(ActionEvent event) throws SQLException {
        if (isFollowed) unfollow();
        else follow();
    }

    private void follow() throws SQLException {
        if (Database.follow(watcherUserID, showedUserID)) followButton.setText("Unfollow");
    }

    private void unfollow() throws SQLException {
        if (Database.unfollow(watcherUserID, showedUserID)) followButton.setText("Follow");
    }

    @FXML
    public void show_posts() {
        ShoPostsUserController.setUserID(watcherUserID);
        if( ShoPostsUserController.getController().init_posts(showedUserID)){
            watcherUserID=null;
            ControllerContext.change_scene(ShoPostsUserController.getScene());
        }

    }

    public void send_message(ActionEvent event) {
        SendMessage2Controller.display(watcherUserID, showedUserID);
    }

    public void go_to_mainMenu(ActionEvent event) {
        idField.setText("");
        imageView.setImage(null);
        postsButton.setVisible(false);
        sendButton.setVisible(false);
        followButton.setVisible(false);
        idErrLabel.setText("");
        label.setText(null);
        setWatcherUserID(null);
        setShowedUserID(null);
        ControllerContext.change_scene(MainMenuController.getScene());
    }



}
