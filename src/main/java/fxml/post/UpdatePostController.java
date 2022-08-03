package fxml.post;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import fxml.ControllerContext;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import jdbc.Database;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdatePostController {
    private static String userID;

    public static void setUserID(String userID) {
        UpdatePostController.userID = userID;
    }

    private static final FXMLLoader fxmlLoader = new FXMLLoader(ControllerContext.class.getResource("UpdatePost.fxml"));
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

    public static UpdatePostController getController() {
        return fxmlLoader.getController();
    }

    //-----------------------------------------------------------------------------------------------------------------

    @FXML
    private TextArea CaptionArea;

    @FXML
    private TextArea LocationArea;

    @FXML
    private TextField PostIDText;

    @FXML
    private Button ReturnButton;

    @FXML
    private Button UpdateButton;

    @FXML
    private VBox vBox;

    @FXML
    void Return(ActionEvent event) {
        CaptionArea.setText("");
        LocationArea.setText("");
        PostIDText.setText("");
        setUserID(null);
        vBox.getChildren().clear();
        ControllerContext.change_scene(MyPostsController.getScene());
    }

    @FXML
    void Update(ActionEvent event) {
        if(CaptionArea.getText().isEmpty() ){
            AlertBox.display("Error","caption can't be empty.",true);
        }else {
            if(PostIDText.getText().isEmpty() ){
                AlertBox.display("Error","no post is selected!",true);
            }else {
                try {
                    if(Database.Update_Caption(CaptionArea.getText(),PostIDText.getText())>0 && Database.Update_location(LocationArea.getText(),PostIDText.getText())>0){
                        AlertBox.display("success","Updated the post successfully.",false);
                        setUserID(null);
                        CaptionArea.setText("");
                        LocationArea.setText("");
                        PostIDText.setText("");
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
    }

    public boolean init_deletePost(){
        try {
            ResultSet resultSet = Database.get_URl(userID);
            while (resultSet.next()){
                ImageView imageView = new ImageView(resultSet.getString("pictureid"));
                imageView.setFitHeight(300);
                imageView.setPreserveRatio(true);
                Label postID = new Label(resultSet.getString("id"));
                imageView.setOnMouseClicked(mouseEvent -> {
                    try {
                        PostIDText.setText(postID.getText());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
                imageView.setOnMouseEntered(mouseEvent -> imageView.setEffect(new DropShadow()));
                imageView.setOnMouseExited(mouseEvent -> imageView.setEffect(null));
                vBox.getChildren().add(imageView);
                vBox.getChildren().add(postID);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
