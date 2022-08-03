package fxml.group;

import fxml.ControllerContext;
import fxml.MainMenuController;
import fxml.message.MessagesController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import jdbc.Database;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupsController {

    private static String userID;

    public static void setUserID(String userID) {
        GroupsController.userID = userID;
    }


    private static final FXMLLoader fxmlLoader = new FXMLLoader(ControllerContext.class.getResource("groups.fxml"));
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

    public static GroupsController getController() {
        return fxmlLoader.getController();
    }

    //-----------------------------------------------------------------------------------------------------------------

    @FXML
    private Menu menu;
    @FXML
    private MenuItem refreshItem;
    @FXML
    private MenuItem createItem;
    @FXML
    private MenuItem mainMenuItem;
    @FXML
    private VBox groupsVBox;


    public void refresh(ActionEvent event) throws SQLException {
        init_groups(userID);
    }

    public boolean init_groups(String userID) throws SQLException {
        try {
            groupsVBox.getChildren().clear();

            ResultSet resultSet = Database.joined_groups(userID);
            while (resultSet.next()) {
                String groupID = resultSet.getString(1);

                Label groupLabel = new Label(groupID);
                groupLabel.setMaxSize(600, -1);

                groupLabel.setOnMouseClicked(mouseEvent -> {
                    try {
                        ChatController.setGroupID(groupID);
                        ChatController.setUserID(userID);
                        ChatController.setAdmin(Database.isAdmin(userID, groupID));
                        ChatController.setOwner(Database.isOwner(userID, groupID));
                        if (ChatController.getController().init_groupMessages(groupID) &&
                                ChatController.getController().init_menuBar(userID, groupID))
                            ControllerContext.change_scene(ChatController.getScene());
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                groupsVBox.getChildren().add(groupLabel);
            }

            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void go_to_createGroup(ActionEvent event) {
        CreateGroupController.setUserID(userID);
        ControllerContext.change_scene(CreateGroupController.getScene());
    }

    public void go_to_mainMenu(ActionEvent event) {
        setUserID(null);
        ControllerContext.change_scene(MainMenuController.getScene());
    }
}
