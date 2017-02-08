/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controllers;

import client.ClientApp;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
    private TextField confirmPassword = new TextField();
    @FXML
    private TextField firstName = new TextField();
    @FXML
    private TextField lastName = new TextField();
    
    @FXML
    private ImageView userPic = new ImageView();
     private Stage primaryStage;
    @FXML ListView<ImageView> imglist = new ListView<>();
    @FXML private ImageView v1 = new ImageView();
     private ObservableList list = FXCollections.observableArrayList();
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        //validation here
        System.out.println("person"+imglist.getSelectionModel().getSelectedIndex()+".png");
        
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Registration error!");
        if( userName.getText().equals("") || 
            userPassword.getText().equals("") ||
            confirmPassword.getText().equals("") || 
            firstName.getText().equals("") ||
            lastName.getText().equals("") ){
            alert.setContentText("Please complete all your information!");
            alert.showAndWait();
            alert.showAndWait();
        }else if(!userPassword.getText().equals(confirmPassword.getText())){
            alert.setContentText("password doesn't match the confirmation!");
            alert.showAndWait();
        }else{
            primaryStage.hide();
            primaryStage.setScene(client.ClientApp.home);
            primaryStage.show();
            alert.setContentText("registration succeded");
        }
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        primaryStage = ClientApp.primaryStage;
//       list.add("person1.png");
//                
//        imglist.setItems(list);
      
    }    
    @FXML
    private void handleButton_back_Action(ActionEvent event) {
        //back button
        System.out.println("back");
    }
    
}
