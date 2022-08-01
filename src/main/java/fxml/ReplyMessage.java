package fxml;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ReplyMessage {

    private static final Stage stage = new Stage();
    static {
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Reply");
        stage.setResizable(false);
        Image icon = new Image("icon.png");
        stage.getIcons().add(icon);
    }
    private static final FXMLLoader fxmlLoader = new FXMLLoader(ControllerContext.class.getResource("reply.fxml"));
    private static Scene scene;
    static {
        try {
            scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static int messageID;
    public static void setMessageID(int messageID) {
        ReplyMessage.messageID = messageID;
    }
    private static String senderID;
    public static void setSenderID(String senderID) {
        ReplyMessage.senderID = senderID;
    }
    private static String userID;
    public static void setUserID(String userID) {
        ReplyMessage.userID = userID;
    }


    @FXML
    private TextField idField;
    @FXML
    private Label idErrLabel;
    @FXML
    private TextArea replyArea;
    @FXML
    private Label replyErrLabel;
    @FXML
    private Button replyButton;

    public static void display(int messageID, String senderID, String userID) {
        stage.showAndWait();
        setMessageID(messageID);
        setSenderID(senderID);
        setUserID(userID);
    }

    public void reply(ActionEvent event) {

    }
}
