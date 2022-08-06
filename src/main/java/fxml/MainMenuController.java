package fxml;

import fxml.message.MessagesController;
import fxml.post.AlertBox;
import fxml.post.CommentsOfPostsController;
import fxml.post.HomePageController;
import fxml.post.MyPostsController;
import fxml.user.EditController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import jdbc.Database;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainMenuController {
    private static String userID;

    public static void setUserID(String userID) {
        MainMenuController.userID = userID;
    }


    private static final FXMLLoader fxmlLoader = new FXMLLoader(ControllerContext.class.getResource("main-menu.fxml"));
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

    public static MainMenuController getController() {
        return fxmlLoader.getController();
    }

    //-----------------------------------------------------------------------------------------------------------------

    @FXML
    private Button HomePageButton;
    @FXML
    private Button messagesButton;
    @FXML
    private Button groupsButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button editButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button profileButton;

    public void go_to_messages(ActionEvent event) {
        MessagesController.setUserID(userID);
        ControllerContext.change_scene(MessagesController.getScene());
    }

    public void go_to_edit(ActionEvent event) {
        EditController.setUserID(userID);
        ControllerContext.change_scene(EditController.getScene());
    }

    public void go_to_login(ActionEvent event) {
        String id = null;
        try {
            id= Database.user_loggedIn();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            Database.Update_logged_in_no(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setUserID(null);
        ControllerContext.change_scene(LoginController.getScene());
    }

    public void go_to_homepage (ActionEvent event) {
        HomePageController.setUserID(userID);
        ControllerContext.change_scene(HomePageController.getScene());
    }

   /* void Show_posts_user (String ID){

        try {
            ResultSet resultSet = Database.show_posts();
            ResultSet resultSet_posts = Database.SHOW_LATEST_posts_10();
            ResultSet resultSet_comments = Database.get_AllComments();
            ArrayList<String> comments_OR_postID = new ArrayList<>();
            ArrayList <String> comments_caption = new ArrayList<>();
            ArrayList <String> comments_id = new ArrayList<>();

            while (resultSet_comments.next()){
                comments_OR_postID.add(resultSet_comments.getString("postORcommentID"));
                comments_caption.add(resultSet_comments.getString("comment_caption"));
                comments_id.add(resultSet_comments.getString("id"));
            }

            int counter = 0;
            AlertBox.display("listen","you have viewed all this ten posts",false);

            while (resultSet_posts.next() ){
                if(resultSet.getString("posterid").equals(ID)){
                    //post
                    ImageView imageView = new ImageView(resultSet_posts.getString("pictureid"));
                    imageView.setFitHeight(200);
                    imageView.setPreserveRatio(true);
                    vBox.getChildren().add(imageView);

                    String id = resultSet_posts.getString("id");

                    //like
                    ImageView Like = new ImageView();
                    if(Database.CheckForDuplicateLike(id+"*",userID)){
                        Like.setImage(like);
                    }else {
                        Like.setImage(dislike);
                    }
                    Like.setFitHeight(50);
                    Like.setPreserveRatio(true);
                    vBox.getChildren().add(Like);
                    Like.setOnMouseClicked(mouseEvent -> {
                        if(Like.getImage()==like){
                            Like.setImage(dislike);
                            Delete_Like(id+"*"+userID);
                        }else if(Like.getImage()==dislike){
                            Like.setImage(like);
                            create_likes(id+"*",userID);
                        }
                    });
                    Label caption = new Label("Caption : "+resultSet_posts.getString("caption"));
                    vBox.getChildren().add(caption);
                    // comment
                    Label comment = new Label("No comments yet.");
                    for (int i = 0; i < comments_OR_postID.size(); i++) {
                        if(comments_OR_postID.get(i).startsWith(id)){
                            comment.setText(comments_caption.get(i));break;
                        }
                    }
                    vBox.getChildren().add(comment);
                    comment.setOnMouseClicked(mouseEvent -> {
                        CommentsOfPostsController.setUserID(userID);
                        if(CommentsOfPostsController.getController().init_comments_posts(comments_OR_postID,comments_caption,comments_id, id)){
                            setUserID(null);
                            ExploreButton.setEffect(null);
                            CommentsOfPostsController.setButtonPressed("ExploreButton");
                            ControllerContext.change_scene(CommentsOfPostsController.getScene());
                        }
                    });

                    if(Database.Update_post_view(id,resultSet_posts.getInt("viewsNum"))>0){

                    }
                    counter++;
                }
            }
            Explorer = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

}
