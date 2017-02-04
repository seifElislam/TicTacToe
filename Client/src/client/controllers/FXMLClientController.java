/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

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
    }
     @FXML protected void handleSignUpButtonAction(ActionEvent event) {
        actiontarget.setText("Sign up button pressed");
        txtf_userName.setText("");
        txtf_password.setText("");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
