/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controllers;

import client.network.Session;
import assets.*;
import client.ClientApp;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author seif
 */
public class FXMLClientController implements Initializable {
    
    @FXML private Text actiontarget;
    @FXML private TextField txtf_password;
    @FXML private TextField txtf_userName;
    private Scene homeScene;
    private Scene signupScene;
    private Stage primaryStage;
    
    @FXML protected void handleSignInButtonAction(ActionEvent event) {
        Session session = new Session("127.0.0.1", 5555);
        session.openConnection();
        if(session.connected){
            if(session.loginToServer(txtf_userName.getText(), txtf_password.getText())){
//                primaryStage.hide();
                primaryStage.setScene(homeScene);
                primaryStage.show();
            }else{
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("TicTacToe");
                alert.setHeaderText("Login failure");
                alert.setContentText("Invalid username or password!");
                alert.showAndWait();
            }
        }else{
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("TicTacToe");
            alert.setHeaderText("Connection failure");
            alert.setContentText("Cannot establish connection with server!");
            alert.showAndWait();
        }
    }
    @FXML protected void handleSignUpButtonAction(ActionEvent event) {
//        primaryStage.hide();
        primaryStage.setScene(signupScene);
        primaryStage.show();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try{
            homeScene = new Scene(FXMLLoader.load(getClass().getResource("/resources/views/home.fxml")));
            signupScene = new Scene(FXMLLoader.load(getClass().getResource("/resources/views/sinup.fxml")));
            primaryStage = ClientApp.primaryStage;
        }catch(IOException ioex){
            
        }
    }    
    
}
