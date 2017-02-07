/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controllers;

import client.ClientApp;
import client.network.Session;
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
    ListView<ImageView> imglist = new ListView<ImageView>();
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        //validation here
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
            if(ClientApp.session == null){
                ClientApp.session = new Session("127.0.0.1", 5555);
            }
            ClientApp.session.openConnection();
            if(ClientApp.session.connected){
                boolean regResult = ClientApp.session.signUpToServer(firstName.getText(), lastName.getText(), userName.getText(), userPassword.getText(), "person1.png");
                if(regResult){
                    alert.setContentText("Congratulations! you've registered successfully!\nYou will be redirected to login page");
                    alert.showAndWait();
                    primaryStage.hide();
                    primaryStage.setScene(client.ClientApp.signIn);
                    primaryStage.show();
                }else{
                    alert.setContentText("Registration failed! username already existed!");
                    alert.showAndWait();
                }
            }else{
                alert.setContentText("Cannot establish connection with server");
                alert.showAndWait();
            }
            ClientApp.session.closeConnection();
        }
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        primaryStage = ClientApp.primaryStage;
    }    
    
}
