package fxml.post;

import fxml.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;

public class MyPostsController {
    private static String userID;

    public static void setUserID(String userID) {
        MyPostsController.userID = userID;
    }

    private static final FXMLLoader fxmlLoader = new FXMLLoader(ControllerContext.class.getResource("MyPosts.fxml"));
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

    public static MyPostsController getController() {
        return fxmlLoader.getController();
    }



    //-----------------------------------------------------------------------------------------------------------------

    @FXML
    Button CreatePostButton ;

    @FXML
    Button ShowCommentsButton ;

    @FXML
    Button ShowLikesButton ;

    @FXML
    Button ShowStatsButton;

    @FXML
    Button DeletePostButton ;

    @FXML
    Button UpdatePostButton ;

    @FXML
    Button ReturnHomePageButton ;

    public void go_to_create_post(ActionEvent actionEvent) {
        CreatePostController.setUserID(userID);
        ControllerContext.change_scene(CreatePostController.getScene());
    }

    public void go_to_show_comments(ActionEvent actionEvent) {
        ControllerContext.change_scene(ShowCommentsController.getScene());
    }

    public void go_to_show_likes(ActionEvent actionEvent) {
        ControllerContext.change_scene(ShowLikesController.getScene());
    }

    public void go_to_show_stats(ActionEvent actionEvent) {
        ShowStatsController.setUserID(userID);
        ControllerContext.change_scene(ShowStatsController.getScene());
    }

    public void go_to_update_caption(ActionEvent actionEvent) {
        UpdatePostController.setUserID(userID);
        ControllerContext.change_scene(UpdatePostController.getScene());
    }

    public void Return_HomePage(ActionEvent actionEvent) {
        ControllerContext.change_scene(HomePageController.getScene());
    }

    public void go_to_delete_post(ActionEvent actionEvent) {
        DeletepostController.setUserID(userID);
        if(DeletepostController.getController().init_deletePost()){
            ControllerContext.change_scene(DeletepostController.getScene());
        }
    }
}
