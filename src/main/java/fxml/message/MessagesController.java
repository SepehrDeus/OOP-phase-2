package fxml.message;

import fxml.ControllerContext;
import fxml.MainMenuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;
import java.sql.SQLException;

public class MessagesController {
    private static String userID;

    public static void setUserID(String userID) {
        MessagesController.userID = userID;
    }

    private static final FXMLLoader fxmlLoader = new FXMLLoader(ControllerContext.class.getResource("messages.fxml"));
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

    public static MessagesController getController() {
        return fxmlLoader.getController();
    }

    //-----------------------------------------------------------------------------------------------------------------

    @FXML
    private Button sendButton;
    @FXML
    private Button inboxButton;
    @FXML
    private Button sentButton;


    public void go_to_send(ActionEvent event) {
        SendMessage1Controller.setUserID(userID);
        ControllerContext.change_scene(SendMessage1Controller.getScene());
    }

    public void go_to_inbox(ActionEvent event) throws SQLException {
        if (InboxController.getController().init_messages(userID)) {
            InboxController.setUserID(userID);
            ControllerContext.change_scene(InboxController.getScene());
        }
    }

    public void go_to_sent(ActionEvent event) {
        if (SentController.getController().init_messages(userID)) {
            SentController.setUserID(userID);
            ControllerContext.change_scene(SentController.getScene());
        }
    }

    public void go_to_mainMenu(ActionEvent event) {
        setUserID(null);
        ControllerContext.change_scene(MainMenuController.getScene());
    }
}
