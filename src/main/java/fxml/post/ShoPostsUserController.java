package fxml.post;

import entity.Like;
import fxml.user.SearchUserController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import fxml.ControllerContext;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import jdbc.Database;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ShoPostsUserController {
    private static String userID;
    private final Image like = new Image("like.jpg");
    private final Image dislike = new Image("dislike.jpg");
    private boolean initialized = false;


    private static final FXMLLoader fxmlLoader = new FXMLLoader(ControllerContext.class.getResource("show_post_user.fxml"));
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

    public static ShoPostsUserController getController() {
        return fxmlLoader.getController();
    }
    //-----------------------------------------------------------------------------------------------------------------
    @FXML
    private Button ReturnButton;

    @FXML
    private VBox vBox;

    public static void setUserID(String userID) {
        ShoPostsUserController.userID = userID;
    }

    public boolean init_posts (String ID_wanted){
        vBox.getChildren().clear();
        try {
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


            if(!initialized){
                AlertBox.display("listen","you have viewed all this posts",false);
            }

            while (resultSet_posts.next() ){
                if( resultSet_posts.getString("posterid").equals(ID_wanted)){
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
                    caption.setStyle("../css/ButtonFirstStyle.css");
                    caption.setId("shiny-orange");
                    vBox.getChildren().add(caption);
                    // comment
                    Label comment = new Label("No comments yet.");
                    comment.setStyle("../css/ButtonFirstStyle.css");
                    comment.setId("shiny-orange");
                    for (int i = 0; i < comments_OR_postID.size(); i++) {
                        if(comments_OR_postID.get(i).startsWith(id)){
                            comment.setText(comments_caption.get(i));break;
                        }
                    }
                    vBox.getChildren().add(comment);
                    comment.setOnMouseClicked(mouseEvent -> {
                        CommentsOfPostsController.setUserID(userID);
                        CommentsOfPostsController.setUserId_searchUser(ID_wanted);
                        CommentsOfPostsController.setButtonPressed("ExploreButton");

                        if(CommentsOfPostsController.getController().init_comments_posts(comments_OR_postID,comments_caption,comments_id, id)){
                            CommentsOfPostsController.setButtonPressed("UserPosts");
                            CommentsOfPostsController.setUserID(userID);
                            setUserID(null);
                            ControllerContext.change_scene(CommentsOfPostsController.getScene());
                        }
                    });
                    if(!initialized){
                        if(Database.Update_post_view(id,resultSet_posts.getInt("viewsNum"))>0){

                        }
                    }
                }
            }
            initialized = true;
            return true;
        } catch (SQLException e) {
            e.printStackTrace();return  false;
        }
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

    @FXML
    void Return(ActionEvent event) {
        vBox.getChildren().clear();
        initialized = false;
        SearchUserController.setWatcherUserID(userID);
        userID = null;
        ControllerContext.change_scene(SearchUserController.getScene());
    }
}
