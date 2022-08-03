package fxml.group;

import fxml.ControllerContext;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class ChatController {
    private static String userID;
    private static String groupID;
    private static boolean admin;
    private static boolean owner;

    public static void setUserID(String userID) {
        ChatController.userID = userID;
    }
    public static void setGroupID(String groupID) {
        ChatController.groupID = groupID;
    }
    public static void setAdmin(boolean admin) {
        ChatController.admin = admin;
    }
    public static void setOwner(boolean owner) {
        ChatController.owner = owner;
    }

    private static final FXMLLoader fxmlLoader = new FXMLLoader(ControllerContext.class.getResource("chat.fxml"));
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

    public static ChatController getController() {
        return fxmlLoader.getController();
    }

    //-----------------------------------------------------------------------------------------------------------------



    public boolean init_groupMessages(String groupID) {
        try {
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
