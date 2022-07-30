package fxml;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import jdbc.Database;

import java.io.IOException;

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





    public void change_username(ActionEvent event) {
        if (!username.isEmpty()) {
            if (Database.update_username(id, username)>0) {
                System.out.println("Username changed successfully.");
                return;
            }
        }
        else System.err.println("username can't be empty.");
    }
}
