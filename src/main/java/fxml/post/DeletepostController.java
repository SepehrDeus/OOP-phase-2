package fxml.post;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import fxml.ControllerContext;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import jdbc.Database;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeletepostController {
    private static String userID;
    private String PicURL;

    public static void setUserID(String userID) {
        DeletepostController.userID = userID;
    }

    private static final FXMLLoader fxmlLoader = new FXMLLoader(ControllerContext.class.getResource("Deletepost.fxml"));
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

    public static DeletepostController getController() {
       return fxmlLoader.getController();
    }



    //-----------------------------------------------------------------------------------------------------------------


    @FXML
    private Button DeleteButton;

    @FXML
    private Button ReturnMyposts;

    @FXML
    private VBox vBox;

    @FXML
    void Delete_post(ActionEvent event) {
        if( PicURL == null || PicURL.isEmpty() ){
            AlertBox.display("Error","no post is selected",true);
        }else {
            try {
                if(Database.delete_Post(PicURL)>0){
                    AlertBox.display("success","Deleted the post successfully.",false);
                    setUserID(null);
                    vBox.getChildren().clear();
                    ControllerContext.change_scene(MyPostsController.getScene());
                    return;
                }else {
                    AlertBox.display("Error","no post exists with that ID!",true);return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean init_deletePost(){
        try {
            ResultSet resultSet = Database.get_URl(userID);
            while (resultSet.next()){
                ImageView imageView = new ImageView(resultSet.getString("pictureid"));
                imageView.setFitHeight(400);
                imageView.setPreserveRatio(true);

                imageView.setOnMouseClicked(mouseEvent -> {
                    try {
                        imageView.setEffect(new DropShadow());
                        PicURL = resultSet.getString("id");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
                vBox.getChildren().add(imageView);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    void Retutn_my_posts(ActionEvent event) {
        setUserID(null);
        vBox.getChildren().clear();
        ControllerContext.change_scene(MyPostsController.getScene());
    }
}
