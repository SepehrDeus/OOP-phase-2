package fxml;

import fxml.group.GroupsController;
import fxml.message.MessagesController;
import fxml.post.HomePageController;
import fxml.post.MyPostsController;
import fxml.user.EditController;
import fxml.user.ProfileController;
import fxml.user.SearchUserController;
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

    public static MainMenuController getController() {
        return fxmlLoader.getController();
    }

    //-----------------------------------------------------------------------------------------------------------------

    @FXML
    private Button HomePageButton;
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
    @FXML
    private Button profileButton;

    public void go_to_messages(ActionEvent event) {
        MessagesController.setUserID(userID);
        ControllerContext.change_scene(MessagesController.getScene());
    }

    public void go_to_edit(ActionEvent event) {
        EditController.setUserID(userID);
        ControllerContext.change_scene(EditController.getScene());
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
        ControllerContext.change_scene(LoginController.getScene());
    }

    public void go_to_homepage (ActionEvent event) {
        MyPostsController.setUserID(userID);
        HomePageController.setUserID(userID);
        ControllerContext.change_scene(MyPostsController.getScene());
    }

    public void go_to_groups(ActionEvent event) throws SQLException {
        if (GroupsController.getController().init_groups(userID)) {
            GroupsController.setUserID(userID);
            ControllerContext.change_scene(GroupsController.getScene());
        }
    }

    public void go_to_profile(ActionEvent event) {
        if (ProfileController.getController().init_profile(userID)) {
            ProfileController.setUserID(userID);
            ControllerContext.change_scene(ProfileController.getScene());
        }
    }

    public void go_to_searchUser(ActionEvent event) {
        SearchUserController.setWatcherUserID(userID);
        ControllerContext.change_scene(SearchUserController.getScene());
    }
}
