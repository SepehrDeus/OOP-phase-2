package fxml;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SentController implements Initializable {
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void go_to_messages(ActionEvent event) {
        setUserID(null);
        ControllerContext.change_scene(MessagesController.getScene());
    }
}
