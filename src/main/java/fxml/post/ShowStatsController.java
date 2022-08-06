package fxml.post;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

public class ShowStatsController {
    private static String userID;

    public static void setUserID(String userID) {
        ShowStatsController.userID = userID;
    }

    private static final FXMLLoader fxmlLoader = new FXMLLoader(ControllerContext.class.getResource("ShowStats.fxml"));
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

    public static ShowStatsController getController() {
        return fxmlLoader.getController();
    }



    //-----------------------------------------------------------------------------------------------------------------
    @FXML
    private TextField PostIDText;

    @FXML
    private TextField LikesNum;

    @FXML
    private Button ReturnButton;

    @FXML
    private Button ShowStatsButton;

    @FXML
    private TextField ViewsNum;

    @FXML
    private VBox vBox;

    @FXML
    void Return(ActionEvent event) {
        LikesNum.setText("");
        ViewsNum.setText("");
        PostIDText.setText("");
        setUserID(null);
        vBox.getChildren().clear();
        ControllerContext.change_scene(MyPostsController.getScene());
    }

    @FXML
    void Show_Stats(ActionEvent event) {
        if(PostIDText.getText().isEmpty()){
            AlertBox.display("Error","No post is selected!",true);
        }else {
            try {
                if(Database.check_existence_post(PostIDText.getText())){
                    ResultSet resultSet = Database.get_likeANDview_num(PostIDText.getText());
                    if (resultSet.next()){
                        LikesNum.setText(Integer.toString(resultSet.getInt("likesNum")));
                        ViewsNum.setText(Integer.toString(resultSet.getInt("viewsNum")));
                    }
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
                if(resultSet.getInt("ad")==1){
                    ImageView imageView = new ImageView(resultSet.getString("pictureid"));
                    imageView.setFitHeight(300);
                    imageView.setPreserveRatio(true);
                    Label caption = new Label("Caption : "+resultSet.getString("caption"));
                    caption.setStyle("../css/ButtonFirstStyle.css");
                    caption.setId("shiny-orange");
                    Label postID = new Label(resultSet.getString("id"));
                    postID.setStyle("../css/ButtonFirstStyle.css");
                    postID.setId("shiny-orange");
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
                    vBox.getChildren().add(caption);
                }
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
