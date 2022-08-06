package fxml.group;

import fxml.ControllerContext;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jdbc.Database;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupMembers {

    private static final Stage stage = new Stage();
    static {
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Members");
        stage.setResizable(false);
        Image icon = new Image("icon.png");
        stage.getIcons().add(icon);
        stage.setOnCloseRequest(windowEvent -> getController().vBox.getChildren().clear());
    }
    private static final FXMLLoader fxmlLoader = new FXMLLoader(ControllerContext.class.getResource("group-members.fxml"));
    private static Scene scene;
    static {
        try {
            scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GroupMembers getController() {
        return fxmlLoader.getController();
    }


    @FXML
    private VBox vBox;

    public void display(String groupID) throws SQLException {
        vBox.setAlignment(Pos.TOP_CENTER);

        vBox.getChildren().add(new Label("Admins:"));
        ResultSet adminsTableSet = Database.get_adminsTable(groupID);
        while (adminsTableSet.next()) {
            vBox.getChildren().add(new Label("@" + adminsTableSet.getString(1)));
        }

        vBox.getChildren().add(new Label("\nmembers:"));
        ResultSet membersTableSet = Database.get_membersTable(groupID);
        while (membersTableSet.next()) {
            vBox.getChildren().add(new Label("@" + membersTableSet.getString(1)));
        }

        stage.showAndWait();
    }
}
