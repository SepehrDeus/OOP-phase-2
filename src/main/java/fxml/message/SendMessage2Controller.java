package fxml.message;

import entity.Message;
import fxml.ControllerContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jdbc.Database;

import java.io.IOException;
import java.sql.SQLException;

public class SendMessage2Controller {

    private static String senderID;
    private static String receiverID;

    public static void setSenderID(String senderID) {
        SendMessage2Controller.senderID = senderID;
    }

    public static void setReceiverID(String receiverID) {
        SendMessage2Controller.receiverID = receiverID;
    }


    private static final FXMLLoader fxmlLoader = new FXMLLoader(ControllerContext.class.getResource("send-message-2.fxml"));
    private static Parent root;
    static {
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static final Scene scene = new Scene(root);
    private static final Stage stage = new Stage();
    static {
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Send message");
        stage.setResizable(false);
        Image icon = new Image("icon.png");
        stage.getIcons().add(icon);
        stage.setScene(scene);
    }


    public static FXMLLoader getFxmlLoader() {
        return fxmlLoader;
    }

    public static Parent getRoot() {
        return root;
    }

    public static Scene getScene() {
        return scene;
    }

    public static SendMessage2Controller getController() {
        return fxmlLoader.getController();
    }

    //-----------------------------------------------------------------------------------------------------------------

    @FXML
    private TextArea textArea;
    @FXML
    private Label textErrLabel;
    @FXML
    private Button sendButton;

    public static void display(String senderID, String receiverID) {
        setSenderID(senderID);
        setReceiverID(receiverID);
        stage.showAndWait();
    }

    public void send_message(ActionEvent event) throws SQLException {
        String text = textArea.getText();
        if (!text.isEmpty()) {
            if (Database.add_message(new Message(text, senderID, receiverID)) > 0) {
                setSenderID(null);
                setReceiverID(null);
                textArea.setText("");
                stage.close();
            }
        }
        else textErrLabel.setText("Please fill the field.");
    }
}
