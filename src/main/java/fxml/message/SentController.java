package fxml.message;

import fxml.ControllerContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import jdbc.Database;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SentController {
    private static String userID;

    public static void setUserID(String userID) {
        SentController.userID = userID;
    }


    private static final FXMLLoader fxmlLoader = new FXMLLoader(ControllerContext.class.getResource("sent.fxml"));
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

    public static SentController getController() {
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


    public void search_message(ActionEvent event) throws SQLException {
        String searchText = searchField.getText();
        if (!searchText.isEmpty()) {
            vBox.getChildren().clear();

            ResultSet resultSet = Database.sent_messages(userID);
            while (resultSet.next()) {
                if (resultSet.getString("text").contains(searchText)) {
                    String receiverID = resultSet.getString("receiverID");
                    String text = resultSet.getString("text");
                    String time = resultSet.getString("time");
                    int messageID = resultSet.getInt("id");


                    String message = "from @" + userID + ":\n" +
                            text + "\n" +
                            time + "\n" +
                            "@" + messageID + "\n";

                    Label messageLabel = new Label(message);
                    messageLabel.setMaxSize(900,-1);

                    // forward
                    messageLabel.setOnMouseClicked(mouseEvent -> ForwardMessage.display(text, userID, userID));
                    // reply
                    messageLabel.setOnMouseDragged(mouseEvent -> ReplyMessage.display(messageID, userID, userID));

                    vBox.getChildren().add(messageLabel);
                }
            }
        }
        else init_messages(userID);
    }

    public boolean init_messages(String userID) {
        try {
            vBox.getChildren().clear();

            ResultSet resultSet = Database.sent_messages(userID);
            while (resultSet.next()) {
                String receiverID = resultSet.getString("receiverID");
                String text = resultSet.getString("text");
                String time = resultSet.getString("time");
                int messageID = resultSet.getInt("id");

                String message = "to @" + receiverID + ":\n" +
                        text + "\n" +
                        time + "\n" +
                        "@" + messageID + "\n";

                Label messageLabel = new Label(message);
                messageLabel.setMaxSize(900,-1);

                // forward
                messageLabel.setOnMouseClicked(mouseEvent -> ForwardMessage.display(text, userID, userID));
                // reply
                messageLabel.setOnMouseDragged(mouseEvent -> ReplyMessage.display(messageID, userID, userID));

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
