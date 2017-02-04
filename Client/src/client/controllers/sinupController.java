/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 *
 * @author khaledgamal
 */
public class sinupController implements Initializable {
    
    @FXML
    private Label label=new Label();
    @FXML
    private Text massage = new Text();
    @FXML
    private TextField userName = new TextField();
    @FXML
    private TextField userPassword = new TextField();
    @FXML
    private TextField firstName = new TextField();
    @FXML
    private TextField lastName = new TextField();
    
    @FXML
    ListView<ImageView> imglist = new ListView<ImageView>();
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        //validation here
        userName.setText("");
         userPassword.setText("");
         firstName.setText("");
         lastName.setText("");
         massage.setText("new palyer");
       System.out.println(Integer.toString(imglist.getSelectionModel().getSelectedIndex()));
        
        try {
//            Parent homePageParent = FXMLLoader.load(getClass().getResource("/resources/views/home.fxml"));
            Parent homePageParent = FXMLLoader.load(getClass().getResource("/resources/views/home.fxml"));
            Scene homeScene = new Scene(homePageParent);
            Stage homeStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            homeStage.hide();
            homeStage.setScene(homeScene);
            homeStage.show();
        } catch (IOException ex) {
            System.out.println("Error done with an exception");
//            Logger.getLogger(sinupController.class.getName()).log(Level.SEVERE, null, ex);    
            ex.printStackTrace();

        }
        
    }
    
    @FXML
//    private void selectUserPic(ActionEvent event){
//        FileChooser filechooser = new FileChooser();
//                filechooser.setTitle("Open Resource File");
//                filechooser.getInitialDirectory();
//                FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("PNG Images", "*.png");
//                FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter("Jpeg Images", "*.jpg");
//                filechooser.getExtensionFilters().addAll(jpgFilter , pngFilter);
//                File file = filechooser.showOpenDialog(new Stage());
//                if(file != null){
////                    write the rest code here 
//
//                    String path = file.getAbsolutePath();
//                    Image image = new Image(file.toURI().toString());
//                    userPic.setImage(image);
//                    
//                }
//    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
