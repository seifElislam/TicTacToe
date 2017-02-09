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
    private Stage primaryStage;
    
    @FXML protected void handleSignInButtonAction(ActionEvent event) {
        if(ClientApp.session == null){
            ClientApp.session = new Session("127.0.0.1", 5555);
        }
        ClientApp.session.openConnection();
        if(ClientApp.session.connected){
            if(ClientApp.session.loginToServer(txtf_userName.getText(), txtf_password.getText())){
                primaryStage.hide();
                primaryStage.setScene(client.ClientApp.home);
                primaryStage.show();
                ClientApp.homeController.bindPlayersTable();
                ClientApp.homeController.playerInfo();
               
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
        primaryStage.setScene(client.ClientApp.signUp);
        primaryStage.show();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
            primaryStage = ClientApp.primaryStage;
        
    }    
    public void terminateConnectino(){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Connection lost");
        alert.setHeaderText("Server disconnected!");
        alert.setContentText("Opps! you've lost the connection with server, try reconnecting later");
        alert.showAndWait();
    }
}
