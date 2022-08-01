package fxml;

import entity.Message;
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

public class ForwardMessage {

    private static final Stage stage = new Stage();
    static {
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Forward");
        stage.setResizable(false);
        Image icon = new Image("icon.png");
        stage.getIcons().add(icon);
    }
    private static final FXMLLoader fxmlLoader = new FXMLLoader(ControllerContext.class.getResource("forward.fxml"));
    private static Scene scene;
    static {
        try {
            scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String text;
    public static void setText(String text) {
        ForwardMessage.text = text;
    }
    private static String senderID;
    public static void setSenderID(String senderID) {
        ForwardMessage.senderID = senderID;
    }
    private static String userID;
    public static void setUserID(String userID) {
        ForwardMessage.userID = userID;
    }


    @FXML
    private TextField idField;
    @FXML
    private Button forwardButton;
    @FXML
    private Label idErrLabel;


    public static void display(String text, String senderID, String userID) {
        setText(text);
        setSenderID(senderID);
        setUserID(userID);
        stage.showAndWait();
    }

    public void forward(ActionEvent event) throws SQLException {
        String receiverID = idField.getText().trim();
        if (!receiverID.isEmpty()) {
            if (Database.user_exists(receiverID)) {
                String forwardText = "forwarded from @" + senderID + "\n" + text;
                if (Database.add_message(new Message(forwardText, userID, receiverID)) > 0) {
                    stage.close();
                }
            }
            else idErrLabel.setText("User doesn't exist.");
        }
        else idErrLabel.setText("Field shouldn't be empty.");
    }
}
