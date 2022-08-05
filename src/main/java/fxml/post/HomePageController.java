package fxml.post;

import entity.Like;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import fxml.ControllerContext;
import fxml.MainMenuController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.Shadow;
import jdbc.Database;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HomePageController {
    private static String userID;
    private final Image like = new Image("like.jpg");
    private final Image dislike = new Image("dislike.jpg");

    public static void setUserID(String userID) {
        HomePageController.userID = userID;
    }

    private static final FXMLLoader fxmlLoader = new FXMLLoader(ControllerContext.class.getResource("HomePage.fxml"));
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

    public static HomePageController getController() {
        return fxmlLoader.getController();
    }

    //-----------------------------------------------------------------------------------------------------------------

    @FXML
    private Button ExploreButton;

    @FXML
    private Button MyPostsButton;

    @FXML
    private Button ReturnButton;

    @FXML
    private Button UserSuggestionButton;

    @FXML
    private VBox vBox;


    @FXML
    void go_to_My_Posts (ActionEvent actionEvent) {

        MyPostsController.setUserID(userID);
        MyPostsButton.setEffect(null);
        UserSuggestionButton.setEffect(null);
        ExploreButton.setEffect(null);
        HomePageController.setUserID(null);
        ControllerContext.change_scene(MyPostsController.getScene());
    }

    @FXML
     void Show_latest_10_post (ActionEvent actionEvent) {
        MyPostsButton.setEffect(null);
        UserSuggestionButton.setEffect(null);
        ExploreButton.setEffect(new Shadow());
        vBox.getChildren().clear();


        try {
            ResultSet resultSet_posts = Database.SHOW_LATEST_posts_10();
            ResultSet resultSet_comments = Database.get_AllComments();
            ArrayList <String> comments_OR_postID = new ArrayList<>();
            ArrayList <String> comments_caption = new ArrayList<>();
            ArrayList <String> comments_id = new ArrayList<>();

            while (resultSet_comments.next()){
                comments_OR_postID.add(resultSet_comments.getString("postORcommentID"));
                comments_caption.add(resultSet_comments.getString("comment_caption"));
                comments_id.add(resultSet_comments.getString("id"));
            }

            int counter = 0;
            AlertBox.display("listen","you have viewed all this ten posts",false);

            while (resultSet_posts.next() && counter<10){
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
                        ControllerContext.change_scene(CommentsOfPostsController.getScene());
                    }
                });

                if(Database.Update_post_view(id,resultSet_posts.getInt("viewsNum"))>0){

                }
                counter++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void Show_latest_10_post_prim () {
        MyPostsButton.setEffect(null);
        UserSuggestionButton.setEffect(null);
        ExploreButton.setEffect(new Shadow());
        vBox.getChildren().clear();

        try {
            ResultSet resultSet_posts = Database.SHOW_LATEST_posts_10();
            ResultSet resultSet_comments = Database.get_AllComments();
            ArrayList <String> comments_OR_postID = new ArrayList<>();
            ArrayList <String> comments_caption = new ArrayList<>();
            ArrayList <String> comments_id = new ArrayList<>();

            while (resultSet_comments.next()){
                comments_OR_postID.add(resultSet_comments.getString("postORcommentID"));
                comments_caption.add(resultSet_comments.getString("comment_caption"));
                comments_id.add(resultSet_comments.getString("id"));
            }

            int counter = 0;

            while (resultSet_posts.next() && counter<10){
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
                        ControllerContext.change_scene(CommentsOfPostsController.getScene());
                    }
                });

                counter++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
     void Show_user_suggestion (ActionEvent actionEvent) {
        MyPostsButton.setEffect(null);
        UserSuggestionButton.setEffect(new Shadow());
        ExploreButton.setEffect(null);

    }
    @FXML
     void Return_main_menu(ActionEvent actionEvent) {
        MainMenuController.setUserID(userID);
        MyPostsButton.setEffect(null);
        UserSuggestionButton.setEffect(null);
        ExploreButton.setEffect(null);
        ControllerContext.change_scene(MainMenuController.getScene());
    }

     void create_likes (String post_commentID, String likerID)  {
        try {
            int a;
            if(post_commentID.charAt(post_commentID.length()-1)=='#'){
                a = Database.get_likesNum_from_comments(post_commentID.substring(0,post_commentID.length()-1))+1;
                Database.Update_likesNum_into_comments(a,post_commentID.substring(0,post_commentID.length()-1));
            }else if(post_commentID.charAt(post_commentID.length()-1)=='*'){
                a = Database.get_likesNum_from_posts(post_commentID.substring(0,post_commentID.length()-1))+1;
                Database.Update_likesNum_into_posts(a,post_commentID.substring(0,post_commentID.length()-1));
            }
            String LikeID = post_commentID + likerID;
            if (Database.add_likes(new Like(post_commentID,LikeID,likerID)) > 0){

            }else AlertBox.display("Error","Couldn't update.",true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void Delete_Like (String cap){
        try {
            if(Database.delete_like(cap)){
                int index =0;
                for (int i = cap.length()-1 ; i >= 0 ; i--) {
                    if(cap.charAt(i)=='*' || cap.charAt(i)=='#' ){
                        index= i;break;
                    }
                }
                int a;
                String post_comment_id = cap.substring(0,index+1);
                if(post_comment_id.charAt(post_comment_id.length()-1)=='#'){
                    a = Database.get_likesNum_from_comments(post_comment_id.substring(0,post_comment_id.length()-1))-1;
                    Database.Update_likesNum_into_comments(a,post_comment_id.substring(0,post_comment_id.length()-1));
                }else if(post_comment_id.charAt(post_comment_id.length()-1)=='*'){
                    a = Database.get_likesNum_from_posts(post_comment_id.substring(0,post_comment_id.length()-1))-1;
                    Database.Update_likesNum_into_posts(a,post_comment_id.substring(0,post_comment_id.length()-1));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
