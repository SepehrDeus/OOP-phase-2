package fxml.group;

import fxml.ControllerContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jdbc.Database;

import java.io.IOException;
import java.sql.SQLException;

public class AddAdmin {

    private static final Stage stage = new Stage();
    static {
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Add admin");
        stage.setResizable(false);
        Image icon = new Image("icon.png");
        stage.getIcons().add(icon);
    }
    private static final FXMLLoader fxmlLoader = new FXMLLoader(ControllerContext.class.getResource("add-admin.fxml"));
    private static Scene scene;
    static {
        try {
            scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static AddAdmin getController() {
        return fxmlLoader.getController();
    }


    private static String groupID;
    public static void setGroupID(String groupID) {
        AddAdmin.groupID = groupID;
    }

    @FXML
    private TextField idField;
    @FXML
    private Label label;
    @FXML
    private Button addButton;


    public static void display(String groupID) {
        setGroupID(groupID);
        stage.showAndWait();
    }

    public void add_admin(ActionEvent event) throws SQLException {
        String userID = idField.getText();
        if (!userID.isEmpty()) {
            if (Database.user_exists(userID)) {
                if (Database.isJoined(userID, groupID)) {
                    if (!Database.isAdmin(userID, groupID)) {
                        if (Database.add_admin(userID, groupID) > 0) {
                            idField.setText("");
                            stage.close();
                        }
                        else label.setText("Adding admin failed.");
                    }
                    else label.setText("User is already admin.");
                }
                else label.setText("Member doesn't exist.");
            }
            else label.setText("User doesn't exist.");
        }
        else label.setText("Please fill the field.");
    }
}
