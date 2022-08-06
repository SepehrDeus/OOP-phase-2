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
    private  boolean ADposts = false;
    private boolean Explorer = false;
    private boolean ShowPosts = false;

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
    private Button ADPostsButton;

    @FXML
    private Button ShowPostsButton;

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
    void go_to_My_Posts () {
        MyPostsController.setUserID(userID);
        MyPostsButton.setEffect(null);
        UserSuggestionButton.setEffect(null);
        ExploreButton.setEffect(null);
        ShowPostsButton.setEffect(null);
        HomePageController.setUserID(null);
        ControllerContext.change_scene(MyPostsController.getScene());
    }

    @FXML
     void Show_latest_10_post () {
        MyPostsButton.setEffect(null);
        UserSuggestionButton.setEffect(null);
        ExploreButton.setEffect(new Shadow());
        ADPostsButton.setEffect(null);
        ShowPostsButton.setEffect(null);
        vBox.getChildren().clear();

            if(Explorer){
                Show_latest_10_post_prim();return;
            }
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
                    CommentsOfPostsController.setButtonPressed("ExploreButton");

                    if(CommentsOfPostsController.getController().init_comments_posts(comments_OR_postID,comments_caption,comments_id, id)){
                        setUserID(null);
                        ExploreButton.setEffect(null);

                        ControllerContext.change_scene(CommentsOfPostsController.getScene());
                    }
                });

                if(Database.Update_post_view(id,resultSet_posts.getInt("viewsNum"))>0){

                }
                counter++;
            }
            Explorer = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void Show_latest_10_post_prim () {
        MyPostsButton.setEffect(null);
        UserSuggestionButton.setEffect(null);
        ExploreButton.setEffect(new Shadow());
        ADPostsButton.setEffect(null);
        ShowPostsButton.setEffect(null);
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
                //Caption
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
                comment.setMaxSize(700,-1);
                vBox.getChildren().add(comment);
                comment.setOnMouseClicked(mouseEvent -> {
                    CommentsOfPostsController.setUserID(userID);
                    ExploreButton.setEffect(null);
                    if(CommentsOfPostsController.getController().init_comments_posts(comments_OR_postID,comments_caption,comments_id, id)){
                        setUserID(null);
                        CommentsOfPostsController.setButtonPressed("ExploreButton");
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
     void Show_user_suggestion () {
        MyPostsButton.setEffect(null);
        UserSuggestionButton.setEffect(new Shadow());
        ExploreButton.setEffect(null);
        ADPostsButton.setEffect(null);
        ShowPostsButton.setEffect(null);

    }
    @FXML
     void Return_main_menu() {
        MainMenuController.setUserID(userID);
        MyPostsButton.setEffect(null);
        ADPostsButton.setEffect(null);
        UserSuggestionButton.setEffect(null);
        ExploreButton.setEffect(null);
        ShowPostsButton.setEffect(null);
        ADposts =false;
        Explorer = false;
        ShowPosts = false;
        vBox.getChildren().clear();
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

    //there is three ways for scoring for an AD post:
    //having more LikesNum and ViewsNum.
    //having the most liked field by the user(the similarity wanted between posts)
    //having been liked by user's followers and followings (because the following and followers probably will have the same taste in fields of a post as the taste of the user)
    @FXML
    void AD_posts() {
        vBox.getChildren().clear();
        MyPostsButton.setEffect(null);
        UserSuggestionButton.setEffect(null);
        ExploreButton.setEffect(null);
        ADPostsButton.setEffect(new Shadow());
        ShowPostsButton.setEffect(null);
        try {
            String UserID = userID;
            ResultSet resultSetLikes = Database.getLikes();
            ArrayList <String> postIDs_likedByUSer = new ArrayList<>();
            while (resultSetLikes.next()){
                if(resultSetLikes.getString(1).contains("*") && !resultSetLikes.getString(1).contains("#") && resultSetLikes.getString("Liker_ID").equals(userID)){
                    postIDs_likedByUSer.add(resultSetLikes.getString(1).substring(0,resultSetLikes.getString(1).length()-1));
                }
            }
            //giving scores based on popularity in userLiked AD-posts
            int [] fields_likes = new int[9];
            ResultSet resultSet_AD_posts = Database.get_ADposts();
            //considering ids and scores for ADposts
            ArrayList <String> ids_AD_posts = new ArrayList<>();
            ArrayList <Double> Scores = new ArrayList<>();
            ArrayList <String > pictureID = new ArrayList<>();
            ArrayList <String> posterID = new ArrayList<>();
            ArrayList <Integer> viewsNum = new ArrayList<>();
            ArrayList <String> caption = new ArrayList<>();
            while (resultSet_AD_posts.next()){
                //The first way of scoring : (next two lines)
                ids_AD_posts.add(resultSet_AD_posts.getString("id"));
                pictureID.add(resultSet_AD_posts.getString("pictureid"));
                posterID.add(resultSet_AD_posts.getString("posterid"));
                viewsNum.add(resultSet_AD_posts.getInt("viewsNum"));
                caption.add(resultSet_AD_posts.getString("caption"));
                Scores.add(Double.parseDouble(resultSet_AD_posts.getString("likesNum"))/10+Double.parseDouble(resultSet_AD_posts.getString("viewsNum"))/100);
                //                1.sports
                //                2.entertainment
                //                3.nature
                //                4.educational
                //                5.fashion
                //                6.political
                //                7.music
                //                8.movie
                //                9.economics

                for (String postID_liked : postIDs_likedByUSer) {
                    //checking for being AD between posts that are liked by user
                    if(postID_liked.equals(resultSet_AD_posts.getString("id"))){

                        switch (resultSet_AD_posts.getString("field")){
                            case "1.sports": fields_likes[0]+=1;break;
                            case "2.entertainment": fields_likes[1]+=1;break;
                            case "3.nature": fields_likes[2]+=1;break;
                            case "4.educational": fields_likes[3]+=1;break;
                            case "5.fashion": fields_likes[4]+=1;break;
                            case "6.political": fields_likes[5]+=1;break;
                            case "7.music": fields_likes[6]+=1;break;
                            case "8.movie": fields_likes[7]+=1;break;
                            case "9.economics": fields_likes[8]+=1;break;
                        }

                    }
                }
            }
            //second way of scoring :
            resultSet_AD_posts.beforeFirst();
            int counter=0;
            while (resultSet_AD_posts.next()){
                switch (resultSet_AD_posts.getString("field")){
                    case "1.sports": Scores.set(counter,Scores.get(counter)+(double)10*fields_likes[0]/postIDs_likedByUSer.size());break;
                    case "2.entertainment": Scores.set(counter,Scores.get(counter)+(double)10*fields_likes[1]/postIDs_likedByUSer.size());break;
                    case "3.nature": Scores.set(counter,Scores.get(counter)+(double)10*fields_likes[2]/postIDs_likedByUSer.size());break;
                    case "4.educational": Scores.set(counter,Scores.get(counter)+(double)10*fields_likes[3]/postIDs_likedByUSer.size());break;
                    case "5.fashion": Scores.set(counter,Scores.get(counter)+(double)10*fields_likes[4]/postIDs_likedByUSer.size());break;
                    case "6.political": Scores.set(counter,Scores.get(counter)+(double)10*fields_likes[5]/postIDs_likedByUSer.size());break;
                    case "7.music": Scores.set(counter,Scores.get(counter)+(double)10*fields_likes[6]/postIDs_likedByUSer.size());break;
                    case "8.movie": Scores.set(counter,Scores.get(counter)+(double)10*fields_likes[7]/postIDs_likedByUSer.size());break;
                    case "9.economics": Scores.set(counter,Scores.get(counter)+(double)10*fields_likes[8]/postIDs_likedByUSer.size());break;
                }
                counter++;
            }



            resultSet_AD_posts.beforeFirst();
            ResultSet Followers = Database.get_followersTable(userID);
            ResultSet Followings = Database.get_followingTable(userID);
            ArrayList <String> followings = new ArrayList<>();
            ArrayList <String> followers = new ArrayList<>();
            while (Followings.next()){
                followings.add(Followings.getString(1));
            }
            while (Followers.next()){
                followers.add(Followers.getString(1));
            }
            resultSetLikes.beforeFirst();
            ArrayList <String> postIDs_likedBy_following_followers = new ArrayList<>();
            //the third way of scoring
            while (resultSetLikes.next()){
                for (String follower : followers) {
                    if(resultSetLikes.getString("Liker_ID").equals(follower)){
                        postIDs_likedBy_following_followers.add(resultSetLikes.getString(1).substring(0,resultSetLikes.getString(1).length()-1));
                    }
                }
                for (String following : followings) {
                    if(resultSetLikes.getString("Liker_ID").equals(following)){
                        postIDs_likedBy_following_followers.add(resultSetLikes.getString(1).substring(0,resultSetLikes.getString(1).length()-1));
                    }
                }
            }
            counter=0;
            while (resultSet_AD_posts.next()){
                for (String s : postIDs_likedBy_following_followers) {
                    if(resultSet_AD_posts.getString("id").equals(s)){
                        Scores.set(counter,Scores.get(counter)+0.01);
                    }
                }
                counter++;
            }

            String BlankString ;
            Double BlankInt;
            int Blankint;
            for (int i = 0; i < Scores.size(); i++) {
                for (int i1 = 0; i1 < Scores.size()-1; i1++) {
                    if(Scores.get(i1)<Scores.get(i1+1)){
                        BlankInt = Scores.get(i1);
                        Scores.set(i1,Scores.get(i1+1))  ;
                        Scores.set(i1+1,BlankInt)  ;
                        Blankint = viewsNum.get(i1);
                        viewsNum.set(i1,viewsNum.get(i1+1))  ;
                        viewsNum.set(i1+1,Blankint)  ;
                        BlankString = ids_AD_posts.get(i1);
                        ids_AD_posts.set(i1,ids_AD_posts.get(i1+1))  ;
                        ids_AD_posts.set(i1+1,BlankString)  ;
                        BlankString = pictureID.get(i1);
                        pictureID.set(i1,pictureID.get(i1+1));
                        pictureID.set(i1+1,BlankString);
                        BlankString = posterID.get(i1);
                        posterID.set(i1,posterID.get(i1+1));
                        posterID.set(i1+1,BlankString);
                        BlankString = caption.get(i1);
                        caption.set(i1,caption.get(i1+1));
                        caption.set(i1+1,BlankString);
                    }
                }
            }

            for (int i = 0; i < ids_AD_posts.size() && i<5; i++) {
                ImageView imageView = new ImageView(pictureID.get(i));
                imageView.setFitHeight(200);
                imageView.setPreserveRatio(true);
                vBox.getChildren().add(imageView);

                Label id_poster = new Label(posterID.get(i)+"\n Caption : "+caption.get(i)+"\n------------------------------");
                id_poster.setStyle("../css/ButtonFirstStyle.css");
                id_poster.setId("shiny-orange");
                id_poster.setMaxSize(700,-1);
                vBox.getChildren().add(id_poster);

                if(!ADposts){
                    if(Database.Update_post_view(ids_AD_posts.get(i),viewsNum.get(i))>0){

                    }
                }
            }
            if(!ADposts){
                AlertBox.display("listen","you have viewed all this five posts",false);
                ADposts = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    void Show_posts() {
        vBox.getChildren().clear();
        MyPostsButton.setEffect(null);
        UserSuggestionButton.setEffect(null);
        ExploreButton.setEffect(null);
        ADPostsButton.setEffect(null);
        ShowPostsButton.setEffect(new Shadow());
        try {
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
            if(!ShowPosts){
                AlertBox.display("listen","you have viewed all this ten posts",false);
            }

            ResultSet resultSet = Database.show_posts();
            ResultSet Followings = Database.get_followingTable(userID);
            ResultSet Followers = Database.get_followersTable(userID);

            ArrayList <String> followings = new ArrayList<>();
            ArrayList <String> followers = new ArrayList<>();
            while (Followings.next()){
                followings.add(Followings.getString(1));
            }
            while (Followers.next()){
                followers.add(Followers.getString(1));
            }

            while (resultSet.next() && counter<10){

                if(resultSet.getString("posterid").equalsIgnoreCase(userID)){
                    //post
                    ImageView imageView = new ImageView(resultSet.getString("pictureid"));
                    imageView.setFitHeight(200);
                    imageView.setPreserveRatio(true);
                    vBox.getChildren().add(imageView);

                    String id = resultSet.getString("id");

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
                    Label caption = new Label("Caption : "+resultSet.getString("caption"));
                    vBox.getChildren().add(caption);
                    // comment
                    Label comment = new Label("No comments yet.");
                    caption.setStyle("../css/ButtonFirstStyle.css");
                    caption.setId("shiny-orange");
                    for (int i = 0; i < comments_OR_postID.size(); i++) {
                        if(comments_OR_postID.get(i).startsWith(id)){
                            comment.setText(comments_caption.get(i));break;
                        }
                    }
                    vBox.getChildren().add(comment);
                    comment.setOnMouseClicked(mouseEvent -> {
                        CommentsOfPostsController.setUserID(userID);
                        CommentsOfPostsController.setButtonPressed("ShowPostsButton");
                        comment.setStyle("../css/ButtonFirstStyle.css");
                        comment.setId("shiny-orange");
                        if(CommentsOfPostsController.getController().init_comments_posts(comments_OR_postID,comments_caption,comments_id, id)){
                            setUserID(null);
                            ShowPostsButton.setEffect(null);
                            ControllerContext.change_scene(CommentsOfPostsController.getScene());
                        }
                    });

                    counter++;
                }
                for (String following : followings) {
                    if(following.equals((resultSet.getString("posterid")))){
                        //post
                        ImageView imageView = new ImageView(resultSet.getString("pictureid"));
                        imageView.setFitHeight(200);
                        imageView.setPreserveRatio(true);
                        vBox.getChildren().add(imageView);

                        String id = resultSet.getString("id");

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
                        Label caption = new Label("Caption : "+resultSet.getString("caption"));
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
                            CommentsOfPostsController.setButtonPressed("ShowPostsButton");
                            if(CommentsOfPostsController.getController().init_comments_posts(comments_OR_postID,comments_caption,comments_id, id)){
                                setUserID(null);
                                ShowPostsButton.setEffect(null);

                                ControllerContext.change_scene(CommentsOfPostsController.getScene());
                            }
                        });
                        counter++;
                    }
                }
                for (String follower : followers) {
                    if(follower.equals((resultSet.getString("posterid")))){
                        //post
                        ImageView imageView = new ImageView(resultSet.getString("pictureid"));
                        imageView.setFitHeight(200);
                        imageView.setPreserveRatio(true);
                        vBox.getChildren().add(imageView);

                        String id = resultSet.getString("id");

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
                        Label caption = new Label("Caption : "+resultSet.getString("caption"));
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
                            ShowPostsButton.setEffect(null);
                            if(CommentsOfPostsController.getController().init_comments_posts(comments_OR_postID,comments_caption,comments_id, id)){
                                setUserID(null);

                                CommentsOfPostsController.setButtonPressed("ShowPostsButton");
                                ControllerContext.change_scene(CommentsOfPostsController.getScene());
                            }
                        });

                        counter++;
                    }
                }
                if(!ShowPosts){
                    if(Database.Update_post_view(resultSet.getString("id"),resultSet.getInt("viewsNum"))>0) {
                    }
                }
            }
            ShowPosts = true;
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
