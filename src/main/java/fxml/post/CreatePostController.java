package fxml.post;

import entity.Post;
import entity.Time;
import fxml.ControllerContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import jdbc.Database;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CreatePostController implements Initializable {
    private static String userID;
    private String PicURL;

    public static void setUserID(String userID) {
        CreatePostController.userID = userID;
    }

    private static final FXMLLoader fxmlLoader = new FXMLLoader(ControllerContext.class.getResource("CreatePost.fxml"));
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

    public static CreatePostController getController() {
        return fxmlLoader.getController();
    }

    //-----------------------------------------------------------------------------------------------------------------
    @FXML
    private ChoiceBox<String> choiceBoxField;
    @FXML
    private CheckBox check_AD_Post;
    @FXML
    private Button ReturnButton;
    @FXML
    private Button PostingButton;
    @FXML
    private Button PicURLButton;
    @FXML
    private TextArea LocationTxtArea1;
    @FXML
    private TextArea pictureTxtArea11;
    @FXML
    private TextArea CaptionTxtArea;

    private String[] Fields = {"1.sports", "2.entertainment", "3.nature", "4.educational", "5.fashion", "6.political", "7.music", "8.movie", "9.economics"};


    // private static void Update_Location(String post_id){
    //      String new_location = post_location();
    //      while (true){
    //          try {
    //             if(Database.Update_location(new_location,post_id)>0){
    //                 return;
    //             }
    //          }catch (Exception e){
    //             System.out.println("invalid command!");
    //           }
    //   }

    @FXML
    private void create_post ()  {
        String caption = CaptionTxtArea.getText();
        String posterID = userID;
        String postID = null;
        try {
            postID = userID+ Database.get_postNum(userID);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        boolean ad = check_AD_Post.isSelected();
        String PictureURL= PicURL;
        if(PictureURL.isEmpty()){
            AlertBox.display("Error","URL is not correct!",true);
        }
        String time=post_time();
        String field = choiceBoxField.getValue();
        String Location = LocationTxtArea1.getText();

        try {
            if(safe_caption(caption) && safe_ad() && safe_postID() && safe_posterID() && (!PictureURL.isEmpty())){
                if (Database.add_post(new Post( postID, posterID, ad,
                        PictureURL,caption,time,field,Location)) > 0){
                    ControllerContext.change_scene(HomePageController.getScene());
                    AlertBox.display("Success","Posted Successfully!",false);
                }else AlertBox.display("Error","Something went wrong! try again.",true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //getting caption
    private  boolean safe_caption (String caption){
       if (caption.isEmpty()){
            AlertBox.display("Error","caption can't be empty!",true);
            return false;
        }else {
            return true;
        }

    }

    private  boolean safe_postID() throws SQLException {
        if(Database.Update_postNum(Database.user_loggedIn(), Database.get_postNum(userID))<=0){
            AlertBox.display("Error","Something went wrong!",true);return false;
        }else {
            return true;
        }
    }

    private  boolean safe_posterID()  {
        if(userID.isEmpty()){
            AlertBox.display("Error","No body is logged in!",true);return false;
        }else {
            return true;
        }
    }

    private  boolean safe_ad() throws SQLException {
        if(Database.check_ads(userID)){
            return true;
        }else {
            AlertBox.display("Error","this is not a business account!",true);
            return false;
        }

    }

    @FXML
    private  String safe_picture (){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(ControllerContext.getMainStage());
        pictureTxtArea11.setText(file.toURI().toString());
        PicURL = file.toURI().toString();
        return file.toURI().toString();
    }

    private  String post_time (){
        return (new Time()).toString();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBoxField.getItems().addAll(Fields);
        choiceBoxField.setValue("1.sports");
    }

    @FXML
    public void return_MyPosts(ActionEvent actionEvent) {
        ControllerContext.change_scene(MyPostsController.getScene());
    }
}
