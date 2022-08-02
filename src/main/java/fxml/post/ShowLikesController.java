package fxml.post;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import fxml.ControllerContext;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class ShowLikesController {
    private static String userID;

    public static void setUserID(String userID) {
        ShowLikesController.userID = userID;
    }

    private static final FXMLLoader fxmlLoader = new FXMLLoader(ControllerContext.class.getResource("ShowLikes.fxml"));
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

    public static ShowLikesController getController() {
        return fxmlLoader.getController();
    }



    //-----------------------------------------------------------------------------------------------------------------

         @FXML
        private Button RetunButton;
        @FXML
        private ToggleButton CommentToggle;

        @FXML
        private ToggleGroup IDs;

        @FXML
        private ToggleButton PostToggle;

        @FXML
        void post_comment_toggle(ActionEvent event) {
            if(event.getSource() == PostToggle){

            }else if(event.getSource() == CommentToggle){

            }
        }

        @FXML
        void Return_myPosts(ActionEvent event) {

        }

    }
