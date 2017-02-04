/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controllers;

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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    
    @FXML protected void handleSignInButtonAction(ActionEvent event) {

        actiontarget.setText("Sign in button pressed");
        txtf_userName.setText("");
        txtf_password.setText("");
    
    

        try {
            Parent signInParent = FXMLLoader.load(getClass().getResource("/resources/views/home.fxml"));
            Scene signInScene = new Scene(signInParent);
            Stage signInStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            signInStage.hide();
            signInStage.setScene(signInScene);
            signInStage.show();
        } catch (IOException ex) {
            System.out.println("Error done with an exception");
            ex.printStackTrace();

        }
    }
     @FXML protected void handleSignUpButtonAction(ActionEvent event) {
          actiontarget.setText("Sign up button pressed");
        txtf_userName.setText("");
        txtf_password.setText("");
        try {
		
            Parent signUpParent = FXMLLoader.load(getClass().getResource("/resources/views/sinup.fxml"));
            Scene signUpScene = new Scene(signUpParent);
            Stage signUpStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            signUpStage.hide();
            signUpStage.setScene(signUpScene);
            signUpStage.show();
        } catch (IOException ex) {
            System.out.println("Error done with an exception");
            ex.printStackTrace();

        }

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
