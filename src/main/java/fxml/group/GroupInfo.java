package fxml.group;

import entity.Group;
import fxml.ControllerContext;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jdbc.Database;

import java.io.IOException;
import java.sql.SQLException;

public class GroupInfo {

    private static final Stage stage = new Stage();
    static {
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Info");
        stage.setResizable(false);
        Image icon = new Image("icon.png");
        stage.getIcons().add(icon);
    }
    private static final FXMLLoader fxmlLoader = new FXMLLoader(ControllerContext.class.getResource("group-info.fxml"));
    private static Scene scene;
    static {
        try {
            scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GroupInfo getController() {
        return fxmlLoader.getController();
    }


    @FXML
    private ImageView imageView;
    @FXML
    private Label idLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label ownerLabel;
    @FXML
    private Label bioLabel;

    public void display(String groupID) throws SQLException {
        Group group = Database.get_group(groupID);

        Image image = new Image(group.getGroupPicture());
        imageView.setImage(image);
        idLabel.setText("groupID: " + group.getId());
        nameLabel.setText("group name: " + group.getGroupName());
        ownerLabel.setText("group ownerID: " + group.getOwnerID());
        bioLabel.setText("biography: \n" + group.getBiography());

        stage.showAndWait();
    }
}
