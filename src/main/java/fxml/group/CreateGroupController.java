package fxml.group;

import entity.Group;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateGroupController {

    private static String userID;

    public static void setUserID(String userID) {
        CreateGroupController.userID = userID;
    }


    private static final FXMLLoader fxmlLoader = new FXMLLoader(ControllerContext.class.getResource("create-group.fxml"));
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

    public static CreateGroupController getController() {
        return fxmlLoader.getController();
    }

    //-----------------------------------------------------------------------------------------------------------------

    @FXML
    private ImageView imageView;
    @FXML
    private Button browseButton;
    @FXML
    private TextField imageField;
    @FXML
    private TextField idField;
    @FXML
    private Label idErrLabel;
    @FXML
    private TextField nameField;
    @FXML
    private Label nameErrLabel;
    @FXML
    private TextArea bioArea;
    @FXML
    private Button createButton;
    @FXML
    private Button returnButton;
    @FXML
    private Label endLabel;


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

    public void create_group(ActionEvent event) throws SQLException {
        idErrLabel.setText("");
        nameErrLabel.setText("");

        String groupID = idField.getText();
        String groupName = nameField.getText();
        String biography = bioArea.getText();
        String groupPicture = imageField.getText();

        if (safe_id(groupID) && safe_name(groupName) &&
                Database.add_group(new Group(groupID, groupName, userID, biography, groupPicture)) > 0 &&
                Database.join_group(userID, groupID) > 1 &&
                Database.add_admin(userID, groupID) > 0) {
            idField.setText("");
            nameField.setText("");
            bioArea.setText("");
            imageView.setImage(null);
            imageField.setText("");

            endLabel.setText("Registered successfully.");
        }
        else endLabel.setText("Something went wrong!\nPlease try again.");
    }

    private boolean safe_id(String id) throws SQLException {
        if (id.isEmpty()) {
            idErrLabel.setText("id can't be empty.");
            return false;
        }

        Pattern pattern = Pattern.compile("[^\\w_]");
        Matcher matcher = pattern.matcher(id);
        if (matcher.find()) {
            idErrLabel.setText("Invalid character.");
            return false;
        }

        if (Database.group_exists(id)) {
            idErrLabel.setText("This id already exist.");
            return false;
        }

        return true;
    }

    public void go_to_groups(ActionEvent event) throws SQLException {
        if (GroupsController.getController().init_groups(userID)) {
            GroupsController.setUserID(userID);
            setUserID(null);
            ControllerContext.change_scene(GroupsController.getScene());
        }
    }

    private boolean safe_name(String name) {
        if (!name.isEmpty()) {
            return true;
        }
        else {
            nameErrLabel.setText("Please fill the field.");
            return false;
        }
    }
}
