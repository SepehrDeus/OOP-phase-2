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

public class KickMember {

    private static final Stage stage = new Stage();
    static {
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Kick member");
        stage.setResizable(false);
        Image icon = new Image("icon.png");
        stage.getIcons().add(icon);
    }
    private static final FXMLLoader fxmlLoader = new FXMLLoader(ControllerContext.class.getResource("kick-member.fxml"));
    private static Scene scene;
    static {
        try {
            scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static KickMember getController() {
        return fxmlLoader.getController();
    }


    private static String groupID;
    public static void setGroupID(String groupID) {
        KickMember.groupID = groupID;
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

    public void kick_member(ActionEvent event) throws SQLException {
        String memberID = idField.getText().trim();
        if (!memberID.isEmpty()) {
            if (Database.user_exists(memberID)) {
                if (Database.isJoined(memberID, groupID)) {
                    if (!Database.isAdmin(memberID, groupID)) {
                        if (Database.kick_member(memberID, groupID) > 1) {
                            idField.setText("");
                            stage.close();
                        }
                        else label.setText("Couldn't kick the user.");
                    }
                    else label.setText("User is admin.");
                }
                else label.setText("Member doesn't exist.");
            }
            else label.setText("User doesn't exist.");
        }
        else label.setText("Please fill the field.");
    }
}