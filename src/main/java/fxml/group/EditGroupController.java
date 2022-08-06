package fxml.group;

import fxml.ControllerContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import jdbc.Database;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class EditGroupController {

    private static String groupID;

    public static void setGroupID(String groupID) {
        EditGroupController.groupID = groupID;
    }

    private static final FXMLLoader fxmlLoader = new FXMLLoader(ControllerContext.class.getResource("edit-group.fxml"));
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

    public static EditGroupController getController() {
        return fxmlLoader.getController();
    }

    //-----------------------------------------------------------------------------------------------------------------

    @FXML
    private ImageView imageView;
    @FXML
    private TextField imageField;
    @FXML
    private Button browseButton;
    @FXML
    private Button imageChangeButton;
    @FXML
    private Label imageErrLabel;
    @FXML
    private TextField nameField;
    @FXML
    private Button nameChangeButton;
    @FXML
    private Label nameErrLabel;
    @FXML
    private TextArea bioArea;
    @FXML
    private Label bioErrLabel;
    @FXML
    private Button bioChangeButton;
    @FXML
    private Button returnButton;


    public void browse_image(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg")
        );
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            String profilePicture = file.toURI().toString();
            Image image = new Image(profilePicture);

            imageField.setText(profilePicture);
            imageView.setImage(image);
        }
    }

    public void change_groupName(ActionEvent event) throws SQLException {
        String newName = nameField.getText();
        if (!newName.isEmpty()) {
            if (Database.update_groupName(groupID, newName) > 0) {
                nameField.setText("");
                nameErrLabel.setText("Updated successfully.");
            }
            else nameErrLabel.setText("Updating failed.");
        }
        else nameErrLabel.setText("Please fill the field.");
    }

    public void change_groupBiography(ActionEvent event) throws SQLException {
        String newBio = bioArea.getText();
        if (!newBio.isEmpty()) {
            if (Database.update_groupBiography(groupID, newBio) > 0) {
                bioErrLabel.setText("Updated successfully.");
                bioArea.setText("");
            }
            else bioErrLabel.setText("Updating failed.");
        }
        else bioErrLabel.setText("Please fill the field.");
    }

    public void change_groupPicture(ActionEvent event) throws SQLException {
        String newPicture = imageField.getText();
        if (!newPicture.isEmpty()) {
            if (Database.update_groupPicture(groupID, newPicture) > 0) {
                imageView.setImage(null);
                imageField.setText("");
                imageErrLabel.setText("Updated successfully.");
            }
            else imageErrLabel.setText("Updating failed.");
        }
        else imageErrLabel.setText("Please fill the field.");
    }

    public void go_to_chats(ActionEvent event) {
        setGroupID(null);
        nameField.setText("");
        nameErrLabel.setText("");
        bioArea.setText("");
        bioErrLabel.setText("");
        imageView.setImage(null);
        imageField.setText("");
        ControllerContext.change_scene(ChatController.getScene());
    }
}
