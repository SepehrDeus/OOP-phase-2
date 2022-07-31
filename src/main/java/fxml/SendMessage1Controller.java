package fxml;

import entity.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import jdbc.Database;

import java.io.IOException;
import java.sql.SQLException;

public class SendMessage1Controller {
    private static String userID;

    public static void setUserID(String userID) {
        SendMessage1Controller.userID = userID;
    }


    private static final FXMLLoader fxmlLoader = new FXMLLoader(ControllerContext.class.getResource("send-message-1.fxml"));
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

    public static SendMessage1Controller getController() {
        return fxmlLoader.getController();
    }

    //-----------------------------------------------------------------------------------------------------------------

    @FXML
    private TextField receiverField;
    @FXML
    private Label rcvrErrLabel;
    @FXML
    private TextArea textArea;
    @FXML
    private Label txtErrLabel;
    @FXML
    private Label endLabel;
    @FXML
    private Button sendButton;
    @FXML
    private Button returnButton;

    public void send_message(ActionEvent event) throws SQLException {
        rcvrErrLabel.setText("");
        txtErrLabel.setText("");

        String receiverID = receiverField.getText();
        String text = textArea.getText();

        if (check_id(receiverID) && check_text(text)) {
            if (Database.add_message(new Message(text, userID, receiverID)) > 0) {
                endLabel.setText("Message sent successfully.");
                receiverField.setText("");
                textArea.setText("");
            }
            else endLabel.setText("Sending message failed.");
        }
    }

    private boolean check_id(String receiverID) throws SQLException {
        if (receiverID.isEmpty()) {
            rcvrErrLabel.setText("Please fill the field.");
            return false;
        }
        else if (!Database.user_exists(receiverID)) {
            rcvrErrLabel.setText("User doesn't exist.");
            return false;
        }

        return true;
    }

    private boolean check_text(String text) {
        if (text.isEmpty()) {
            txtErrLabel.setText("Please fill the field.");
            return false;
        }

        return true;
    }

    public void go_to_messages(ActionEvent event) {
        setUserID(null);
        ControllerContext.change_scene(MessagesController.getScene());
    }
}
