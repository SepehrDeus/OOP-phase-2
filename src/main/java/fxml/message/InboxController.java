package fxml.message;

import fxml.ControllerContext;
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
import java.sql.SQLException;

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


    public void search_message(ActionEvent event) throws SQLException {
        String searchText = searchField.getText();
        if (!searchText.isEmpty()) {
            vBox.getChildren().clear();

            ResultSet resultSet = Database.received_messages(userID);
            while (resultSet.next()) {
                if (resultSet.getString("text").contains(searchText)) {
                    String senderID = resultSet.getString("senderID");
                    String text = resultSet.getString("text");
                    String time = resultSet.getString("time");
                    int messageID = resultSet.getInt("id");


                    String message = "from @" + senderID + ":\n" +
                            text + "\n" +
                            time + "\n" +
                            "@" + messageID + "\n";

                    Label messageLabel = new Label(message);
                    messageLabel.setMaxSize(850,-1);
                    messageLabel.setStyle("../css/Buttons.css");
                    messageLabel.setId("shiny-orange");

                    // forward
                    messageLabel.setOnMouseClicked(mouseEvent -> ForwardMessage.display(text, senderID, userID));
                    // reply
                    messageLabel.setOnMouseDragged(mouseEvent -> ReplyMessage.display(messageID, senderID, userID));

                    vBox.getChildren().add(messageLabel);
                }
            }
        }
        else init_messages(userID);
    }

    public boolean init_messages(String userID) {
        try {
            vBox.getChildren().clear();

            ResultSet resultSet = Database.received_messages(userID);
            while (resultSet.next()) {
                String senderID = resultSet.getString("senderID");
                String text = resultSet.getString("text");
                String time = resultSet.getString("time");
                int messageID = resultSet.getInt("id");

                String message = "from @" + senderID + ":\n" +
                        text + "\n" +
                        time + "\n" +
                        "@" + messageID + "\n";

                Label messageLabel = new Label(message);
                messageLabel.setMaxSize(850,-1);
                messageLabel.setStyle("../css/Buttons.css");
                messageLabel.setId("shiny-orange");

                // forward
                messageLabel.setOnMouseClicked(mouseEvent -> ForwardMessage.display(text, senderID, userID));
                // reply
                messageLabel.setOnMouseDragged(mouseEvent -> ReplyMessage.display(messageID, senderID, userID));

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
