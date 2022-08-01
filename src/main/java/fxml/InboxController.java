package fxml;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import jdbc.Database;

import java.io.IOException;
import java.sql.ResultSet;

public class InboxController {
    private static String userID;

    public static void setUserID(String userID) {
        InboxController.userID = userID;
    }


    private static final FXMLLoader fxmlLoader = new FXMLLoader(ControllerContext.class.getResource("inbox.fxml"));
    private static BorderPane root;
    static {
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static Scene scene = new Scene(root);


    public static FXMLLoader getFxmlLoader() {
        return fxmlLoader;
    }

    public static Parent getRoot() {
        return root;
    }

    public static Scene getScene() {
        return scene;
    }

    public static InboxController getController() {
        return fxmlLoader.getController();
    }

    //-----------------------------------------------------------------------------------------------------------------

    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private VBox vBox;
    @FXML
    private Button returnButton;


    public void search_message(ActionEvent event) {
        String searchText = searchField.getText();
        if (!searchText.isEmpty()) {

        }
    }

    public boolean set_messages(String userID) {
        try {
            vBox.getChildren().clear();

            ResultSet resultSet = Database.received_messages(userID);
            while (resultSet.next()) {
                String message = "from @" + resultSet.getString("senderID") + ":\n" +
                        resultSet.getString("text") + "\n" +
                        resultSet.getString("time") + "\n" +
                        "@" + resultSet.getInt("id") + "\n";
                Label messageLabel = new Label(message);
                messageLabel.setMaxSize(900,-1);
                vBox.getChildren().add(messageLabel);
            }

            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void go_to_messages(ActionEvent event) {
        setUserID(null);
        vBox.getChildren().clear();
        ControllerContext.change_scene(MessagesController.getScene());
    }
}
