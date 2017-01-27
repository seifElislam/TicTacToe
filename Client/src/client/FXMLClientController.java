/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

/**
 *
 * @author seif
 */
public class FXMLClientController implements Initializable {
    
    @FXML private Text actiontarget;
    
    @FXML protected void handleSignInButtonAction(ActionEvent event) {
        actiontarget.setText("Sign in button pressed");
    }
     @FXML protected void handleSignUpButtonAction(ActionEvent event) {
        actiontarget.setText("Sign up button pressed");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
