package fxml.post;

import entity.Comment;
import entity.Like;
import entity.Time;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import fxml.ControllerContext;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;
import jdbc.Database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class CommentsOfPostsController {
    private static String userID;
    private final Image like = new Image("like.jpg");
    private final Image dislike = new Image("dislike.jpg");

    public static void setUserID(String userID) {
        CommentsOfPostsController.userID = userID;
    }

    private static final FXMLLoader fxmlLoader = new FXMLLoader(ControllerContext.class.getResource("CommentsOfPosts.fxml"));
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

    public static CommentsOfPostsController getController() {
        return fxmlLoader.getController();
    }

    //-----------------------------------------------------------------------------------------------------------------
    @FXML
    private Label PostID;

    @FXML
    private TextArea CAptionText;

    @FXML
    private Button ReturnButton;

    @FXML
    private VBox vBox;

    public boolean init_comments_posts(ArrayList <String> comments_OR_postID,ArrayList <String> comments_caption,ArrayList <String> comments_id,String id) {
        try {
            vBox.getChildren().clear();
            PostID.setText(id);

            for (int i = 0; i < comments_OR_postID.size(); i++) {
                if(comments_OR_postID.get(i).substring(0,id.length()).equals(id)){
                    Label commentlabel = new Label();
                    String Comment_id = comments_id.get(i);
                    commentlabel.setOnMouseClicked(mouseEvent -> {
                        create_comment(Comment_id+"#");
                        });
                    commentlabel.setOnMouseDragged(mouseEvent -> {
                        delete_comment(Comment_id);
                    });
                    //setting commentID

                    commentlabel.setText("id : "+Comment_id+"\n"+comments_caption.get(i));
                    vBox.getChildren().add(commentlabel);

                //Likes
                ImageView Like = new ImageView();
                if(Database.CheckForDuplicateLike(Comment_id+"#",userID)){
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
                        Delete_Like(Comment_id+"#"+userID);
                    }else if(Like.getImage()==dislike){
                        Like.setImage(like);
                        create_likes(Comment_id+"#",userID);
                    }
                });
                }
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();return false;
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

    void create_comment(String post_comment_id ){
    try {
        int a;
        if(post_comment_id.charAt(post_comment_id.length()-1)=='#'){
            a = Database.get_commentNum_from_comments(post_comment_id.substring(0,post_comment_id.length()-1))+1;
            Database.Update_commentNum_from_comments(post_comment_id.substring(0,post_comment_id.length()-1),a);
        }
        else if(post_comment_id.charAt(post_comment_id.length()-1)=='*'){
            a = Database.get_commentNum_from_posts(post_comment_id.substring(0,post_comment_id.length()-1))+1;
            Database.Update_commentNum_from_posts(post_comment_id.substring(0,post_comment_id.length()-1),a);
        }
    }catch (Exception e){
        e.printStackTrace();
    }

        String commentID = post_comment_id + userID;
        String time=comment_time();
        String caption = CAptionText.getText();

        if(caption.isEmpty()){
            AlertBox.display("Error","caption shouldn't be empty.",true);
        }else {
            try {
                if (Database.add_comment(new Comment(post_comment_id,commentID,userID,caption,time)) > 0){
                    AlertBox.display("success","created the comment",false);
                    vBox.getChildren().clear();
                    PostID.setText("");
                    CAptionText.setText("");
                    HomePageController.getController().Show_latest_10_post_prim();
                    HomePageController.setUserID(userID);
                    setUserID(null);
                    ControllerContext.change_scene(HomePageController.getScene());
                }else  AlertBox.display("Error","couldn't create the comment",true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    void delete_comment(String cap){
        try {
            if(Database.delete_comment(cap)){
                int index =0;
                for (int i = cap.length()-1 ; i >= 0 ; i--) {
                    if(cap.charAt(i)=='*' || cap.charAt(i)=='#' ){
                        index= i;break;
                    }
                }
                int a;
                String post_comment_id = cap.substring(0,index+1);
                if(post_comment_id.charAt(post_comment_id.length()-1)=='#'){
                    a = Database.get_commentNum_from_comments(post_comment_id.substring(0,post_comment_id.length()-1))-1;
                    Database.Update_commentNum_from_comments(post_comment_id.substring(0,post_comment_id.length()-1),a);
                }
                else if(post_comment_id.charAt(post_comment_id.length()-1)=='*'){
                    a = Database.get_commentNum_from_posts(post_comment_id.substring(0,post_comment_id.length()-1))-1;
                    Database.Update_commentNum_from_posts(post_comment_id.substring(0,post_comment_id.length()-1),a);
                }
                AlertBox.display("success","deleted the comment",false);
                vBox.getChildren().clear();
                PostID.setText("");
                CAptionText.setText("");
                HomePageController.setUserID(userID);
                HomePageController.getController().Show_latest_10_post_prim();
                ControllerContext.change_scene(HomePageController.getScene());
                return;
            }else {
                AlertBox.display("Error","couldn't delete the comment",true);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    void comment_on_post(MouseEvent event) {
        create_comment(PostID.getText()+"*");
    }

    public static String comment_time (){
        return (new Time()).toString();
    }

    @FXML
    void Return_homePage(ActionEvent event) {
        vBox.getChildren().clear();
        PostID.setText("");
        CAptionText.setText("");
        HomePageController.setUserID(userID);
        HomePageController.getController().Show_latest_10_post_prim();
        ControllerContext.change_scene(HomePageController.getScene());
    }

}
