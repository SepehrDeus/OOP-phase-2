package fxml.post;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import fxml.ControllerContext;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import jdbc.Database;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        private TextField PostORCommentID;

        @FXML
        VBox vBox ;

        @FXML
        void Return_myPosts(ActionEvent event) {
            setUserID(null);
            vBox.getChildren().clear();
            ControllerContext.change_scene(MyPostsController.getScene());
        }

    @FXML
    void show_likes(ActionEvent event) {
        if(event.getSource() == PostToggle){
            vBox.getChildren().clear();
            String id = PostORCommentID.getText();
            if(id.isEmpty()){
                AlertBox.display("Error","The id field should be filled.",true);
            }else {
                try {
                    if(Database.check_existence_post(id)){
                        ResultSet LikerIDs = Database.SHOW_LIKES_post(id);
                        ResultSet temp;
                        while (LikerIDs.next()){
                            temp = Database.get_usernameANDpictureurl(LikerIDs.getString("Liker_ID"));
                            while (temp.next()){
                                System.out.println(temp.getString(2));
                                ImageView imageView = new ImageView(temp.getString(2));
                                imageView.setFitHeight(100);
                                imageView.setFitWidth(100);
                                //dont know how to set max size

                                vBox.getChildren().add(imageView);
                                Label message = new Label(temp.getString(1) +
                                        " Liked the post.\n"+"\n");
                                message.setMaxSize(900,-1);
                                vBox.getChildren().add(message);
                            }
                            }

                        if(!LikerIDs.next()){
                            Label label =new Label("No more Likes.");
                            label.setMaxSize(900,-1);
                            vBox.getChildren().add(label);
                        }
                    }else {
                        AlertBox.display("Error","No posts Found!",true);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }else if(event.getSource() == CommentToggle){
            vBox.getChildren().clear();
            String id = PostORCommentID.getText();
            if(id.isEmpty()){
                AlertBox.display("Error","The id field shoud be filled.",true);
            }else {
                try {
                    if(Database.check_existence_comment(id)){
                        ResultSet LikerIDs = Database.SHOW_LIKES_comment(id);
                        ResultSet temp;
                        while (LikerIDs.next()){
                            temp = Database.get_usernameANDpictureurl(LikerIDs.getString("Liker_ID"));
                            while (temp.next()){
                                ImageView imageView = new ImageView(temp.getString(2));
                                imageView.setFitHeight(100);
                                imageView.setFitWidth(100);
                                //dont know how to set max size

                                vBox.getChildren().add(imageView);
                                Label message = new Label(temp.getString(1) +
                                        " Liked the comment.\n"+"\n");
                                message.setMaxSize(900,-1);
                                vBox.getChildren().add(message);
                            }
                        }
                        if(!LikerIDs.next()){
                            Label label =new Label("No more Likes.");
                            label.setMaxSize(900,-1);
                            vBox.getChildren().add(label);
                        }
                        return ;
                    }else {
                        AlertBox.display("Error","No posts Found!",true);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
